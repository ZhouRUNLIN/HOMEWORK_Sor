package org.rhw.bmr.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.common.constant.RedisCacheConstant;
import org.rhw.bmr.user.dao.entity.UserDO;
import org.rhw.bmr.user.dao.mapper.UserMapper;
import org.rhw.bmr.user.dto.req.BmrSaveGroupReqDTO;
import org.rhw.bmr.user.dto.req.UserLoginReqDTO;
import org.rhw.bmr.user.dto.req.UserRegisterReqDTO;
import org.rhw.bmr.user.dto.req.UserUpdateReqDTO;
import org.rhw.bmr.user.dto.resp.UserLoginRespDTO;
import org.rhw.bmr.user.dto.resp.UserRespDTO;
import org.rhw.bmr.user.service.UserService;
import org.rhw.bmr.user.common.convention.exception.ClientException;
import org.rhw.bmr.user.common.enums.UserErrorCodeEnum;
import org.rhw.bmr.user.service.GroupService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.rhw.bmr.user.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static org.rhw.bmr.user.common.enums.UserErrorCodeEnum.USER_LOGIN_REPEAT;
import static org.rhw.bmr.user.common.enums.UserErrorCodeEnum.USER_NOT_ONLINE;


@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final RRateLimiter userRegisterRateLimiter;
    private final StringRedisTemplate redisTemplate;
    private final GroupService groupService;

    @Override
    public UserRespDTO getUserByUsername(String username) {

        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                                                    .eq(UserDO::getUsername, username);

        UserDO userDO = baseMapper.selectOne(queryWrapper);

        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean availableUsername(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {

        if (!userRegisterRateLimiter.tryAcquire(1)) {
            throw new ClientException(UserErrorCodeEnum.USER_REGISTER_RATE_ERROR);
        }

        if (!availableUsername(requestParam.getUsername())) {
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }

        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY+requestParam.getUsername());

        try {
            if (lock.tryLock()) {
                try{
                    int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                    if (inserted < 1){
                        throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                    }
                    userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());

                    groupService.saveGroup(new BmrSaveGroupReqDTO(requestParam.getUsername(),"default grouping"));
                    return;
                }catch (DuplicateKeyException e){
                    throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
                }
            }
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        } finally {
           lock.unlock();
        }
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {

        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO user = baseMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        if (redisTemplate.hasKey(USER_LOGIN_KEY+requestParam.getUsername())){

             redisTemplate.expire(USER_LOGIN_KEY+requestParam.getUsername(), 0, TimeUnit.SECONDS);
        }



        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(USER_LOGIN_KEY+requestParam.getUsername(), uuid, JSON.toJSONString(user));
        redisTemplate.expire(USER_LOGIN_KEY+requestParam.getUsername(), 6L, TimeUnit.HOURS);

        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {

        return redisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token) != null;

    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public void logout(String username, String token) {
        if (checkLogin(username, token)){
            redisTemplate.delete(USER_LOGIN_KEY+username);
        }else{
            throw new ClientException(USER_NOT_ONLINE);
        }
    }
}
