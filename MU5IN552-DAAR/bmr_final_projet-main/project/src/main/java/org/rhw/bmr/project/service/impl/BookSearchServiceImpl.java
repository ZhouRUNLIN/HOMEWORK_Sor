package org.rhw.bmr.project.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dto.req.BookSearchByIdReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchByRegexpReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchByWordReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByIdRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByRegespRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByWordRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.service.BookSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookSearchServiceImpl extends ServiceImpl<BookMapper, BookDO> implements BookSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Override
    public IPage<BookSearchRespDTO> pageBookSearchPage(BookSearchReqDTO requestParam) {

        int currentPage = requestParam.getPageNo() == null ? 1 : requestParam.getPageNo();
        int pageSize = requestParam.getPageSize() == null ? 10 : requestParam.getPageSize();

        Page<BookDO> page = new Page<>(currentPage, pageSize);

        // 构造查询条件
        LambdaQueryWrapper<BookDO> queryWrapper = Wrappers.lambdaQuery(BookDO.class);
        if (requestParam.getTitle() != null && !requestParam.getTitle().isEmpty()) {
            queryWrapper.eq(BookDO::getTitle, requestParam.getTitle());
        }
        if (requestParam.getAuthor() != null && !requestParam.getAuthor().isEmpty()) {
            queryWrapper.eq(BookDO::getAuthor, requestParam.getAuthor());
        }
        if (requestParam.getCategory() != null && !requestParam.getCategory().isEmpty()) {
            queryWrapper.eq(BookDO::getCategory, requestParam.getCategory());
        }
        if (requestParam.getLanguage() != null && !requestParam.getLanguage().isEmpty()) {
            queryWrapper.eq(BookDO::getLanguage, requestParam.getLanguage());
        }

        queryWrapper.orderByDesc(BookDO::getClickCount);


        IPage<BookDO> bookSearchDOPage = baseMapper.selectPage(page, queryWrapper);

        IPage<BookSearchRespDTO> bookSearchRespDTOPage = bookSearchDOPage.convert(bookSearchDO ->
                BookSearchRespDTO.builder()
                        .id(String.valueOf(bookSearchDO.getId()))
                        .refId(bookSearchDO.getRefId())
                        .title(bookSearchDO.getTitle())
                        .storagePath(bookSearchDO.getStoragePath())
                        .author(bookSearchDO.getAuthor())
                        .category(bookSearchDO.getCategory())
                        .description(bookSearchDO.getDescription())
                        .language(bookSearchDO.getLanguage())
                        .clickCount(bookSearchDO.getClickCount())
                        .sortedOrder(bookSearchDO.getSortedOrder())
                        .img(bookSearchDO.getImg())
                        .build()
        );

        return bookSearchRespDTOPage;
    }


    @Override
    public IPage<BookSearchByWordRespDTO> pageBookSearchByWord(BookSearchByWordReqDTO requestParam) {

        int currentPage = requestParam.getPageNo() == null ? 1 : requestParam.getPageNo();
        int pageSz = requestParam.getPageSize() == null ? 10 : requestParam.getPageSize();


        try {
            int pageNo = currentPage;
            int pageSize = pageSz;
            int from = pageNo * pageSize - pageSize;

            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index("bmr_books")
                    .query(q -> q
                            .matchPhrase(m -> m
                                    .field("content")
                                    .query(requestParam.getWord())
                            )
                    )
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("clickCount")
                                    .order(SortOrder.Desc)
                            )
                    )
                    .source(s -> s
                            .filter(f -> f
                                    .includes("id","title", "author", "refId", "category", "language", "clickCount")
                            )
                    )
                    .from(from)
                    .size(pageSize)
            );

            SearchResponse<BookSearchByWordRespDTO> response = elasticsearchClient.search(
                    searchRequest, BookSearchByWordRespDTO.class
            );

            List<BookSearchByWordRespDTO> records = new ArrayList<>();
            for (Hit<BookSearchByWordRespDTO> hit : response.hits().hits()) {
                records.add(hit.source());
            }

            for (BookSearchByWordRespDTO record : records ){
                record.setId(String.valueOf(record.getId()));
            }


            long total = response.hits().total() != null ? response.hits().total().value() : 0;

            IPage<BookSearchByWordRespDTO> page = new Page<>(pageNo, pageSize, total);
            page.setRecords(records);
            return page;

        } catch (Exception e) {
            log.error("Elasticsearch Query failed", e);
            return new Page<>();
        }
    }


    @Override
    public IPage<BookSearchByRegespRespDTO> pageBookSearchByRegexp(BookSearchByRegexpReqDTO requestParam) {

        int currentPage = requestParam.getPageNo() == null ? 1 : requestParam.getPageNo();
        int pageSz = requestParam.getPageSize() == null ? 10 : requestParam.getPageSize();


        try {
            int pageNo = currentPage;
            int pageSize = pageSz;
            int from = pageNo * pageSize - pageSize;

            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index("bmr_books")
                    .query(q -> q
                            .regexp(r -> r
                                    .field("content")
                                    .value(requestParam.getRegularExpr())
                            )
                    )
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("clickCount")
                                    .order(SortOrder.Desc)
                            )
                    )
                    .source(s -> s
                            .filter(f -> f
                                    .includes("id","title", "author", "refId", "category", "language", "clickCount")
                            )
                    )
                    .from(from)
                    .size(pageSize)
            );


            SearchResponse<BookSearchByRegespRespDTO> response = elasticsearchClient.search(
                    searchRequest, BookSearchByRegespRespDTO.class
            );


            List<BookSearchByRegespRespDTO> records = new ArrayList<>();
            for (Hit<BookSearchByRegespRespDTO> hit : response.hits().hits()) {
                records.add(hit.source());
            }

            for (BookSearchByRegespRespDTO record : records ){
                record.setId(String.valueOf(record.getId()));
            }


            long total = response.hits().total() != null ? response.hits().total().value() : 0;

            IPage<BookSearchByRegespRespDTO> page = new Page<>(pageNo, pageSize, total);
            page.setRecords(records);
            return page;

        } catch (Exception e) {
            log.error("Elasticsearch Query failed", e);
            return new Page<>();
        }
    }

    @Override
    public BookSearchByIdRespDTO pageBookSearchById(BookSearchByIdReqDTO requestParam) {

        String bookid = requestParam.getBookId();

        BookDO bookDO = baseMapper.selectById(bookid);

        BookSearchByIdRespDTO bookSearchByIdRespDTO = new BookSearchByIdRespDTO();

        if (bookDO != null) {
            bookSearchByIdRespDTO.setId(String.valueOf(bookDO.getId()));
            bookSearchByIdRespDTO.setRefId(bookDO.getRefId());
            bookSearchByIdRespDTO.setTitle(bookDO.getTitle());
            bookSearchByIdRespDTO.setStoragePath(bookDO.getStoragePath());
            bookSearchByIdRespDTO.setAuthor(bookDO.getAuthor());
            bookSearchByIdRespDTO.setCategory(bookDO.getCategory());
            bookSearchByIdRespDTO.setDescription(bookDO.getDescription());
            bookSearchByIdRespDTO.setImg(bookDO.getImg());
            bookSearchByIdRespDTO.setLanguage(bookDO.getLanguage());
            bookSearchByIdRespDTO.setClickCount(bookDO.getClickCount());

        }


        return bookSearchByIdRespDTO;
    }


}
