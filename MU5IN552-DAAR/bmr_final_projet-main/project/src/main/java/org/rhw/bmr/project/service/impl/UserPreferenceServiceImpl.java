package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.PostConstruct;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.UserPreferenceMapper;
import org.rhw.bmr.project.dto.req.BookmarkReqDTO;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserPreferenceServiceImpl extends ServiceImpl<UserPreferenceMapper, UserPreferenceDO>
        implements UserPreferenceService {

    @Autowired
    private BookMapper bookMapper;

    private Map<Long, List<Long>> adjacencyList;

    @PostConstruct
    public void init() {
        adjacencyList = new HashMap<Long, List<Long>>();
        buildInitialGraph();
    }

    private void buildInitialGraph() {
        List<BookDO> books = bookMapper.selectList(null);
        if (books == null || books.isEmpty()) {
            return;
        }

        for (BookDO book : books) {
            adjacencyList.putIfAbsent(Long.valueOf(book.getId()), new ArrayList<Long>());
        }

        int size = books.size();
        for (int i = 0; i < size; i++) {
            BookDO bookA = books.get(i);
            Long idA = Long.valueOf(bookA.getId());
            for (int j = i + 1; j < size; j++) {
                BookDO bookB = books.get(j);
                Long idB = Long.valueOf(bookB.getId());

                boolean sameAuthor = false;
                if (bookA.getAuthor() != null && bookB.getAuthor() != null) {
                    sameAuthor = bookA.getAuthor().equals(bookB.getAuthor());
                }
                boolean sameCategory = false;
                if (bookA.getCategory() != null && bookB.getCategory() != null) {
                    sameCategory = bookA.getCategory().equals(bookB.getCategory());
                }

                if (sameAuthor || sameCategory) {
                    adjacencyList.get(idA).add(idB);
                    adjacencyList.get(idB).add(idA);
                }
            }
        }
    }

    @Override
    public void recordUserPreference(ReadBookReqDTO requestParam) {
        String username = requestParam.getUsername();
        String bookId = requestParam.getBookId();

        if (username == null || bookId == null) {
            return;
        }

        LambdaQueryWrapper<BookDO> bookQuery = Wrappers.lambdaQuery(BookDO.class)
                .eq(BookDO::getId, bookId);
        BookDO bookDO = bookMapper.selectOne(bookQuery);
        if (bookDO == null) {
            return;
        }

        String author = bookDO.getAuthor();
        String category = bookDO.getCategory();
        if ((author == null || author.isEmpty()) &&
                (category == null || category.isEmpty())) {
            return;
        }

        LambdaQueryWrapper<UserPreferenceDO> wrapper = Wrappers.lambdaQuery(UserPreferenceDO.class)
                .eq(UserPreferenceDO::getUsername, username)
                .eq(author != null, UserPreferenceDO::getAuthor, author)
                .eq(category != null, UserPreferenceDO::getCategory, category);

        UserPreferenceDO existPref = getOne(wrapper);
        if (existPref == null) {

            UserPreferenceDO newPref = new UserPreferenceDO();
            newPref.setUsername(username);
            newPref.setAuthor(author);
            newPref.setCategory(category);
            newPref.setLikeCount(1);
            save(newPref);
        } else {

            LambdaUpdateWrapper<UserPreferenceDO> updateWrapper = Wrappers.lambdaUpdate(UserPreferenceDO.class)
                    .eq(UserPreferenceDO::getId, existPref.getId())
                    .eq(UserPreferenceDO::getUsername, username)
                    .set(UserPreferenceDO::getLikeCount, existPref.getLikeCount() + 1);
            update(updateWrapper);
        }

        updateBookGraph(bookDO);
    }

    @Override
    public void recordUserPreference(BookmarkReqDTO requestParam) {
        String username = requestParam.getUsername();
        String bookId = requestParam.getBookId();

//        log.error("bookmark: {}");

        if (username == null || bookId == null) {

            return;
        }

        LambdaQueryWrapper<BookDO> bookQuery = Wrappers.lambdaQuery(BookDO.class)
                .eq(BookDO::getId, bookId);
        BookDO bookDO = bookMapper.selectOne(bookQuery);
        if (bookDO == null) {
            return;
        }

        String author = bookDO.getAuthor();
        String category = bookDO.getCategory();
        if ((author == null || author.isEmpty()) &&
                (category == null || category.isEmpty())) {
            return;
        }

        LambdaQueryWrapper<UserPreferenceDO> wrapper = Wrappers.lambdaQuery(UserPreferenceDO.class)
                .eq(UserPreferenceDO::getUsername, username)
                .eq(author != null, UserPreferenceDO::getAuthor, author)
                .eq(category != null, UserPreferenceDO::getCategory, category);

        UserPreferenceDO existPref = getOne(wrapper);
        if (existPref == null) {

            UserPreferenceDO newPref = new UserPreferenceDO();
            newPref.setUsername(username);
            newPref.setAuthor(author);
            newPref.setCategory(category);
            newPref.setLikeCount(1);
            save(newPref);


        } else {

            LambdaUpdateWrapper<UserPreferenceDO> updateWrapper = Wrappers.lambdaUpdate(UserPreferenceDO.class)
                    .eq(UserPreferenceDO::getUsername, username)
                    .eq(UserPreferenceDO::getId, existPref.getId())
                    .set(UserPreferenceDO::getLikeCount, existPref.getLikeCount() + 1);
            update(updateWrapper);


        }


        updateBookGraph(bookDO);
    }


    private void updateBookGraph(BookDO readBook) {
        Long readBookId = Long.valueOf(readBook.getId());
        if (!adjacencyList.containsKey(readBookId)) {
            adjacencyList.put(readBookId, new ArrayList<Long>());
        }


        List<BookDO> similarBooks = bookMapper.selectList(
                Wrappers.lambdaQuery(BookDO.class)
                        .eq(BookDO::getAuthor, readBook.getAuthor())
                        .or()
                        .eq(BookDO::getCategory, readBook.getCategory())
        );

        for (BookDO book : similarBooks) {
            Long otherId = Long.valueOf(book.getId());
            if (!otherId.equals(readBookId)) {

                if (!adjacencyList.containsKey(otherId)) {
                    adjacencyList.put(otherId, new ArrayList<Long>());
                }

                if (!adjacencyList.get(readBookId).contains(otherId)) {
                    adjacencyList.get(readBookId).add(otherId);
                }

                if (!adjacencyList.get(otherId).contains(readBookId)) {
                    adjacencyList.get(otherId).add(readBookId);
                }
            }
        }
    }


    public Map<Long, List<Long>> getBookAdjacencyList() {
        return adjacencyList;
    }
}
