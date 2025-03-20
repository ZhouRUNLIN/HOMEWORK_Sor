package org.rhw.bmr.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.*;
import org.rhw.bmr.project.dto.resp.*;
import org.rhw.bmr.project.service.BookSearchService;
import org.rhw.bmr.project.service.ReadBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookSearchController {


    private final BookSearchService bookSearchService;

    @GetMapping("/api/bmr/project/v1/bookSearch_page")
    public Result<IPage<BookSearchRespDTO>> pageBookSearch(BookSearchReqDTO requestParam){
        return Results.success(bookSearchService.pageBookSearchPage(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookSearch_by_word")
    public Result<IPage<BookSearchByWordRespDTO>> pageBookSearchByWord(BookSearchByWordReqDTO requestParam){
        return Results.success(bookSearchService.pageBookSearchByWord(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookSearch_by_regexp")
    public Result<IPage<BookSearchByRegespRespDTO>> pageBookSearchByRegexp(BookSearchByRegexpReqDTO requestParam){
        return Results.success(bookSearchService.pageBookSearchByRegexp(requestParam));
    }

    @GetMapping("/api/bmr/project/v1/bookSearch_by_id")
    public Result<BookSearchByIdRespDTO> pageBookSearchByID(BookSearchByIdReqDTO requestParam){
        return Results.success(bookSearchService.pageBookSearchById(requestParam));
    }



}
