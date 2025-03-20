package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dto.req.RecommendBookReqDTO;

import java.util.List;

public interface RecommendationService extends IService<UserPreferenceDO> {

    List<BookDO> recommendBooksForUser(RecommendBookReqDTO requestParam);

}
