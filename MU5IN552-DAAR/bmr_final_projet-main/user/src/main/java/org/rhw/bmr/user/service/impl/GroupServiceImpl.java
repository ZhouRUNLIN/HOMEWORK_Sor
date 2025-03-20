package org.rhw.bmr.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import groovy.util.logging.Slf4j;
import org.rhw.bmr.user.common.biz.user.UserContext;
import org.rhw.bmr.user.dao.entity.GroupDO;
import org.rhw.bmr.user.dto.req.BmrDeleteGroupReqDTO;
import org.rhw.bmr.user.dto.req.BmrGroupUpdateDTO;
import org.rhw.bmr.user.dto.req.BmrListGroupReqDTO;
import org.rhw.bmr.user.dto.req.BmrSaveGroupReqDTO;
import org.rhw.bmr.user.remote.BookSearchRemoteService;
import org.rhw.bmr.user.toolkit.RandomGenerator;
import org.rhw.bmr.user.dao.mapper.GroupMapper;
import org.rhw.bmr.user.dto.resp.BmrGroupRespDTO;
import org.rhw.bmr.user.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    BookSearchRemoteService bookSearchRemoteService = new BookSearchRemoteService() {
    };

    @Override
    public void saveGroup(BmrSaveGroupReqDTO requestParam) {
        String gid;

        do{
            gid = RandomGenerator.generateRandomString();
        }while(!hasGid(requestParam.getGroupName(), gid));

        GroupDO groupDO = GroupDO.builder()
                .gid(RandomGenerator.generateRandomString())
                .sortOrder(0)
                .username(requestParam.getUsername())
                .name(requestParam.getGroupName())
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<BmrGroupRespDTO> listGroup(BmrListGroupReqDTO requestParam) {

        LambdaQueryWrapper<GroupDO> eq = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, requestParam.getUsername());

        List<GroupDO> groupDOList = baseMapper.selectList(eq);

        if (Objects.nonNull(groupDOList)) {
            return groupDOList.stream().map(each -> {
                BmrGroupRespDTO bmrGroupRespDTO = new BmrGroupRespDTO();
                BeanUtil.copyProperties(each, bmrGroupRespDTO);
                return bmrGroupRespDTO;
            }).toList();
        }

        return null;
    }

    @Override
    public void updateGroup(BmrGroupUpdateDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, requestParam.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, requestParam.getGid());

        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void deleteGroup(BmrDeleteGroupReqDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, requestParam.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, requestParam.getGid());

        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);
    }

    private boolean hasGid(String userName, String gid) {
        LambdaQueryWrapper<GroupDO> eq = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, userName);
        GroupDO hasGroupFlag = baseMapper.selectOne(eq);
        return hasGroupFlag == null;
    }
}
