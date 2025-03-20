package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dto.req.BookmarkReqDTO;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;

public interface UserPreferenceService extends IService<UserPreferenceDO> {

    void recordUserPreference(ReadBookReqDTO requestParam);

    void recordUserPreference(BookmarkReqDTO requestParam);


}
