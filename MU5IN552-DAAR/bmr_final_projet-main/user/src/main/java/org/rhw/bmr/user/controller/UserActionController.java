package org.rhw.bmr.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.common.convention.result.Results;
import org.rhw.bmr.user.remote.BookSearchRemoteService;

import org.rhw.bmr.user.remote.dto.req.*;

import org.rhw.bmr.user.remote.dto.resp.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserActionController {

    BookSearchRemoteService bookSearchRemoteService = new BookSearchRemoteService() {};

    @GetMapping("/api/bmr/user/v1/bookSearch_page")
    public Result<IPage<BookSearchRespDTO>> pageBookSearch(BookSearchReqDTO requestParam){

        return bookSearchRemoteService.bookSearch(requestParam);

    }

    @GetMapping("/api/bmr/user/v1/bookSearch_by_word")
    public Result<IPage<BookSearchByWordRespDTO>> pageBookSearchByWord(BookSearchByWordReqDTO requestParam){

        return bookSearchRemoteService.bookSearchByWord(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/bookSearch_by_regexp")
    public Result<IPage<BookSearchByRegespRespDTO>> pageBookSearchByRegexp(BookSearchByRegexpReqDTO requestParam){
        return bookSearchRemoteService.bookSearchByRegexp(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/readBook")
    public Result<ReadBookRespDTO> readBook(ReadBookReqDTO requestParam){

        bookSearchRemoteService.recordUserPreference(requestParam);

        return bookSearchRemoteService.readBook(requestParam);
    }

    @PostMapping("/api/bmr/user/v1/bookmark")
    public Result<Void> bookmark(BookmarkReqDTO requestParam){
        bookSearchRemoteService.bookmark(requestParam);
        return Results.success();
    }

    @PostMapping("/api/bmr/user/v1/bookmark/delete")
    public Result<Void> bookmarkDelete(BookmarkReqDTO requestParam){
        bookSearchRemoteService.deleteBookmark(requestParam);
        return Results.success();
    }

    @GetMapping("/api/bmr/user/v1/bookmark/search")
    public Result<IPage<BookmarkSearchRespDTO>> bookmarkSearch(BookmarkSearchReqDTO requestParam){
        return bookSearchRemoteService.bookmarkSearch(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/recommend")
    public Result<List<BookDO>> RecommendBook(RecommendBookReqDTO requestParam){
        return bookSearchRemoteService.recommendBooksForUser(requestParam);
    }


    @GetMapping("/api/bmr/user/v1/bookmark/textInternalsearchBykmp")
    public Result<String[]> textInternalsearchBykmp(TextInternalSearchByKMPReqDTO requestParam){
        return bookSearchRemoteService.TextInternalSearchByKMP(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/bookmark/textInternalsearchByEgrep")
    public Result<String[]> textInternalsearchByEgrep(TextInternalSearchByEgreplikeReqDTO requestParam){
        return bookSearchRemoteService.TextInternalSearchByEgreplike(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/bookmark/textInternalsearchBykmp/long")
    public Result<long[]> textInternalsearchBykmpLong(TextInternalSearchByKMPReqDTO requestParam){
        return bookSearchRemoteService.TextInternalSearchByKMPLong(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/bookmark/textInternalsearchByEgrep/long")
    public Result<long[]> textInternalsearchByEgrepLong(TextInternalSearchByEgreplikeReqDTO requestParam){
        return bookSearchRemoteService.TextInternalSearchByEgreplikeLong(requestParam);
    }



}
