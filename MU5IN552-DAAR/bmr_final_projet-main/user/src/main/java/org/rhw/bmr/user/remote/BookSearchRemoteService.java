package org.rhw.bmr.user.remote;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.dto.req.BmrRecycleBinPageReqDTO;
import org.rhw.bmr.user.remote.dto.req.*;
import org.rhw.bmr.user.remote.dto.resp.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * remote call service
 */
public interface BookSearchRemoteService {

    default Result<IPage<BookSearchRespDTO>> bookSearch(BookSearchReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("title", requestParam.getTitle());
        requestMap.put("author", requestParam.getAuthor());
        requestMap.put("category", requestParam.getCategory());
        requestMap.put("language", requestParam.getLanguage());

        requestMap.put("pageNo", requestParam.getPageNo());
        requestMap.put("pageSize", requestParam.getPageSize());


        String resultPageStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookSearch_page",
                requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>(){});
    }

    default Result<IPage<BookSearchByWordRespDTO>> bookSearchByWord(BookSearchByWordReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("word", requestParam.getWord());

        requestMap.put("pageNo", requestParam.getPageNo());
        requestMap.put("pageSize", requestParam.getPageSize());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookSearch_by_word",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }


    default Result<IPage<BookSearchByRegespRespDTO>> bookSearchByRegexp(BookSearchByRegexpReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("regularExpr", requestParam.getRegularExpr());

        requestMap.put("pageNo", requestParam.getPageNo());
        requestMap.put("pageSize", requestParam.getPageSize());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookSearch_by_regexp",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }


    default Result<ReadBookRespDTO> readBook(ReadBookReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("username", requestParam.getUsername());

        requestMap.put("bookId", requestParam.getBookId());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/readBook",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }

    default void bookmark(BookmarkReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("gid", requestParam.getGid());
        requestMap.put("username", requestParam.getUsername());
        requestMap.put("bookId", requestParam.getBookId());

        String resultBodyStr = HttpUtil.post(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookmark",
                requestMap);
    }

    default void deleteBookmark(BookmarkReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("gid", requestParam.getGid());
        requestMap.put("username", requestParam.getUsername());
        requestMap.put("bookId", requestParam.getBookId());

        String resultBodyStr = HttpUtil.post(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookmark/delete",
                requestMap);
    }

    default Result<IPage<BookmarkSearchRespDTO>> bookmarkSearch(BookmarkSearchReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("gid", requestParam.getGid());
        requestMap.put("username", requestParam.getUsername());

        requestMap.put("pageNo", requestParam.getPageNo());
        requestMap.put("pageSize", requestParam.getPageSize());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookmark/search",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }


    default void recordUserPreference(ReadBookReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("username", requestParam.getUsername());

        requestMap.put("bookId", requestParam.getBookId());

        String resultBodyStr = HttpUtil.post(
                "http://127.0.0.1:8001/api/bmr/project/v1/recordUserPreference",
                requestMap);
    }

    default Result<List<BookDO>> recommendBooksForUser(RecommendBookReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("username", requestParam.getUsername());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/recommend",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }

    default Result<String[]> TextInternalSearchByKMP(TextInternalSearchByKMPReqDTO requestParam) {
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("URL", requestParam.getURL());
        requestMap.put("word", requestParam.getWord());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookmark/textInternalsearchBykmp",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }

    default Result<String[]> TextInternalSearchByEgreplike(TextInternalSearchByEgreplikeReqDTO requestParam) {

        Map<String, Object> requestMap = new HashMap<>();

        requestMap.put("URL", requestParam.getURL());
        requestMap.put("regular", requestParam.getRegular());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookmark/textInternalsearchByEgrep",
                requestMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});

    }


    default  Result<long[]> TextInternalSearchByKMPLong(TextInternalSearchByKMPReqDTO requestParam) {
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("URL", requestParam.getURL());
        requestMap.put("word", requestParam.getWord());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookmark/textInternalsearchBykmp/long",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }

    default  Result<long[]> TextInternalSearchByEgreplikeLong(TextInternalSearchByEgreplikeReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();

        requestMap.put("URL", requestParam.getURL());
        requestMap.put("regular", requestParam.getRegular());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookmark/textInternalsearchByEgrep/long",
                requestMap);
        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }

}
