package org.rhw.bmr.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.*;
import org.rhw.bmr.project.dto.resp.BookID2refIDRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.dto.resp.BookmarkSearchRespDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;
import org.rhw.bmr.project.service.BookmarkService;
import org.rhw.bmr.project.service.ReadBookService;
import org.rhw.bmr.project.service.TextInternalSearchService;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserActionController {

    @Autowired
    private ReadBookService readBookService;
    @Autowired
    private UserPreferenceService userPreferenceService;
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private TextInternalSearchService textInternalSearchService;

    @GetMapping("/api/bmr/project/v1/readBook")
    public Result<ReadBookRespDTO> readBook(ReadBookReqDTO requestParam){

        userPreferenceService.recordUserPreference(requestParam);

        return Results.success(readBookService.readBook(requestParam));
    }




    @PostMapping("/api/bmr/project/v1/bookmark")
    public Result<Void> bookmark(@RequestBody BookmarkReqDTO requestParam){
        userPreferenceService.recordUserPreference(requestParam);
        bookmarkService.bookmark(requestParam);
        return Results.success();
    }

    @PostMapping("/api/bmr/project/v1/bookmark/delete")
    public Result<Void> bookmarkDelete(@RequestBody BookmarkReqDTO requestParam){
        bookmarkService.deleteBookmark(requestParam);
        return Results.success();
    }

    @GetMapping("/api/bmr/project/v1/bookmark/search")
    public Result<IPage<BookmarkSearchRespDTO>> bookmarkSearch(BookmarkSearchReqDTO requestParam){
        return Results.success(bookmarkService.bookmarkSearch(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookmark/textInternalsearchBykmp")
    public Result<String[]> textInternalsearchBykmp(TextInternalSearchByKMPReqDTO requestParam){
        return Results.success(textInternalSearchService.TextInternalSearchByKMP(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookmark/textInternalsearchByEgrep")
    public Result<String[]> textInternalsearchByEgrep(TextInternalSearchByEgreplikeReqDTO requestParam){
        return Results.success(textInternalSearchService.TextInternalSearchByEgreplike(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookmark/textInternalsearchBykmp/long")
    public Result<long[]> textInternalsearchBykmpLong(TextInternalSearchByKMPReqDTO requestParam){
        return Results.success(textInternalSearchService.TextInternalSearchByKMPLong(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookmark/textInternalsearchByEgrep/long")
    public Result<long[]> textInternalsearchByEgrepLong(TextInternalSearchByEgreplikeReqDTO requestParam){
        return Results.success(textInternalSearchService.TextInternalSearchByEgreplikeLong(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookID2refID")
    public Result<BookID2refIDRespDTO> bookID2refID(BookID2refIDReqDTO requestParam){
        return Results.success(readBookService.bookID2refID(requestParam));
    }


}
