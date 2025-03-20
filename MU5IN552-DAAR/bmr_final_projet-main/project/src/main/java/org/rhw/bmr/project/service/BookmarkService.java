package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookmarkDO;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.req.BookmarkReqDTO;
import org.rhw.bmr.project.dto.req.BookmarkSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.dto.resp.BookmarkSearchRespDTO;

public interface BookmarkService extends IService<BookmarkDO> {


    void bookmark(BookmarkReqDTO requestParam);

    void deleteBookmark(BookmarkReqDTO requestParam);

    IPage<BookmarkSearchRespDTO> bookmarkSearch(BookmarkSearchReqDTO requestParam);


}
