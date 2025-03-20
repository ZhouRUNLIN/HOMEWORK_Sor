package org.rhw.bmr.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dto.req.BookID2refIDReqDTO;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.dto.resp.BookID2refIDRespDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;
import org.rhw.bmr.project.service.ReadBookService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ReadBookImpl extends ServiceImpl<BookMapper, BookDO> implements ReadBookService {

    @Override
    public ReadBookRespDTO readBook(ReadBookReqDTO requestParam) {

        BookDO bookDO = baseMapper.selectById(requestParam.getBookId());

        if (bookDO != null) {

            String URL = bookDO.getStoragePath();
            String[] contents = new String[0];

            if (URL != null && !URL.trim().isEmpty()) {
                try {
                    String text;
                    if (URL.startsWith("http://") || URL.startsWith("https://")) {
                        text = readFromUrl(URL);
                    } else {
                        text = readFromLocalFile(URL);
                    }

                    if (text != null) {
                        contents = text.split("\\r?\\n");
                    }
                } catch (IOException e) {
                    log.error("Failed to read the book content: {}");
                }
            }

            LambdaUpdateWrapper<BookDO> wrapper = Wrappers.lambdaUpdate(BookDO.class)
                    .eq(BookDO::getId, bookDO.getId())
                    .set(BookDO::getClickCount, bookDO.getClickCount() + 1);
            baseMapper.update(null, wrapper);

            return new ReadBookRespDTO(bookDO.getStoragePath(), bookDO.getImg(), contents);
        }

        return new ReadBookRespDTO();
    }


    private String readFromUrl(String urlPath) throws IOException {
        StringBuilder content = new StringBuilder();
        while (true) {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(false);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM
                    || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                urlPath = connection.getHeaderField("Location");
                continue;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } finally {
                connection.disconnect();
            }
            break;
        }
        return content.toString();
    }

    private String readFromLocalFile(String filePath) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    @Override
    public BookID2refIDRespDTO bookID2refID(BookID2refIDReqDTO requestParam) {

        String bookID = requestParam.getBookID();

        BookDO bookDO = baseMapper.selectById(bookID);

        return new BookID2refIDRespDTO(bookDO.getRefId().toString());
    }

}
