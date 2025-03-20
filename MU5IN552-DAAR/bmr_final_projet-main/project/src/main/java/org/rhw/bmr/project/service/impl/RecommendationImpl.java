package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.common.algo.CustomPageRankAlgorithm;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.dto.req.RecommendBookReqDTO;
import org.rhw.bmr.project.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Collections;
import java.util.Comparator;

@Service
public class RecommendationImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO>
        implements RecommendationService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserPreferenceServiceImpl userPreferenceServiceImpl;

    @Override
    public List<BookDO> recommendBooksForUser(RecommendBookReqDTO requestParam) {

        int currentPage = requestParam.getPageNo() == null ? 1 : requestParam.getPageNo();
        int pageSize = requestParam.getPageSize() == null ? 10 : requestParam.getPageSize();

        String username = requestParam.getUsername();

        Map<Long, Double> bookPageRank = computeBookPageRank();

        List<UserPreferenceDO> preferences = getUserPreferences(username);

        Set<Long> preferredBookIds = getPreferredBookIds(preferences);

        List<BookDO> recommendedBooks = new ArrayList<>();
        if (!preferredBookIds.isEmpty()) {
            List<BookDO> filteredBooks = bookMapper.selectList(
                    Wrappers.lambdaQuery(BookDO.class).in(BookDO::getId, preferredBookIds)
            );
            if (filteredBooks != null && !filteredBooks.isEmpty()) {
                recommendedBooks.addAll(filteredBooks);
            }
        }

        recommendedBooks.sort((b1, b2) -> {
            double score1 = bookPageRank.getOrDefault(b1.getId(), 0.0);
            double score2 = bookPageRank.getOrDefault(b2.getId(), 0.0);
            return Double.compare(score2, score1);
        });

        List<BookDO> finalList = new ArrayList<>();
        int limit = Math.min(100, recommendedBooks.size());
        for (int i = 0; i < limit; i++) {
            finalList.add(recommendedBooks.get(i));
        }

        if (finalList.size() < 100) {
            int stillNeed = 100 - finalList.size();
            List<BookDO> randomBooks = queryRandomBooks(stillNeed);
            finalList.addAll(randomBooks);
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, finalList.size());

        if (startIndex >= finalList.size()) {
            return Collections.emptyList();
        }

        return finalList.subList(startIndex, endIndex);
    }



    private Map<Long, Double> computeBookPageRank() {
        Map<Long, List<Long>> bookAdjList = userPreferenceServiceImpl.getBookAdjacencyList();

        double dampingFactor = 0.85;
        int maxIterations = 100;
        double epsilon = 1.0e-6;

        return CustomPageRankAlgorithm.computePageRank(bookAdjList, dampingFactor, maxIterations, epsilon);
    }


    private List<UserPreferenceDO> getUserPreferences(String username) {
        return baseMapper.selectList(
                Wrappers.lambdaQuery(UserPreferenceDO.class)
                        .eq(UserPreferenceDO::getUsername, username)
                        .orderByDesc(UserPreferenceDO::getLikeCount)
                        .last("LIMIT 6")
        );

    }

    private Set<Long> getPreferredBookIds(List<UserPreferenceDO> preferences) {
        Set<Long> bookIds = new HashSet<Long>();
        for (int i = 0; i < preferences.size(); i++) {
            UserPreferenceDO pref = preferences.get(i);
            List<BookDO> books = queryBooksByAuthorAndCategory(pref.getAuthor(), pref.getCategory());
            for (int j = 0; j < books.size(); j++) {
                bookIds.add(Long.valueOf(books.get(j).getId()));
            }
        }
        return bookIds;
    }

    private List<BookDO> queryBooksByAuthorAndCategory(String author, String category) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookDO> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookDO>();

        if (author != null && !author.isEmpty()) {
            wrapper.or().eq(BookDO::getAuthor, author);
        }
        if (category != null && !category.isEmpty()) {
            wrapper.or().eq(BookDO::getCategory, category);
        }
        wrapper.orderByDesc(BookDO::getClickCount);
        wrapper.last("LIMIT 20");

        return bookMapper.selectList(wrapper);
    }

    private List<BookDO> queryRandomBooks(int stillNeed) {

        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookDO> wrapper =
                Wrappers.lambdaQuery(BookDO.class).last("ORDER BY RAND() LIMIT " + stillNeed);
        return bookMapper.selectList(wrapper);
    }
}
