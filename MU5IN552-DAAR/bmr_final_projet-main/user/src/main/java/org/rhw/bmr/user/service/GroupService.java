package org.rhw.bmr.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.user.dao.entity.GroupDO;
import org.rhw.bmr.user.dto.req.BmrDeleteGroupReqDTO;
import org.rhw.bmr.user.dto.req.BmrGroupUpdateDTO;
import org.rhw.bmr.user.dto.req.BmrListGroupReqDTO;
import org.rhw.bmr.user.dto.req.BmrSaveGroupReqDTO;
import org.rhw.bmr.user.dto.resp.BmrGroupRespDTO;

import java.util.List;


public interface GroupService extends IService<GroupDO> {


    void saveGroup(BmrSaveGroupReqDTO requestParam);

    List<BmrGroupRespDTO> listGroup(BmrListGroupReqDTO requestParam);

    void updateGroup(BmrGroupUpdateDTO requestParam);

    void deleteGroup(BmrDeleteGroupReqDTO requestParam);

}
