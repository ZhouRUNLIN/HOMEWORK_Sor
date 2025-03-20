package org.rhw.bmr.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.BookmarkDO;
import org.rhw.bmr.project.dto.req.BookmarkSearchReqDTO;

public interface BookmarkMapper extends BaseMapper<BookmarkDO> {

    IPage<BookDO> pageBookmark(BookmarkSearchReqDTO requestParam);

}
