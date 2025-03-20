package org.rhw.bmr.project.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.service.BookSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookSyncServiceImpl extends ServiceImpl<BookMapper, BookDO> implements BookSyncService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Override
    public List<BookDO> getUnsyncedBooks(int limit) {

        LambdaQueryWrapper<BookDO> last = Wrappers.lambdaQuery(BookDO.class)
                .and(wrapper -> wrapper
                        .eq(BookDO::getEsSyncFlag, 0)
                        .or()
                        .isNull(BookDO::getEsSyncFlag)
                )
                .last("LIMIT " + limit);

        return baseMapper.selectList(last);
    }

    @Override
    public void updateSyncFlag(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        ids.forEach(Long::valueOf);

        LambdaUpdateWrapper<BookDO> wrapper = Wrappers.lambdaUpdate(BookDO.class)
                .in(BookDO::getId, ids)
                .set(BookDO::getEsSyncFlag, 1);


        baseMapper.update(null, wrapper);

    }


    @Override
    public void syncBooksToElasticsearch() {
        try {
            List<BookDO> unsyncedBooks = getUnsyncedBooks(100);

            while (!unsyncedBooks.isEmpty()) {
                BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

                for (BookDO bookSyncDO : unsyncedBooks) {
                    String fileContent = readFileContent(bookSyncDO.getStoragePath());

                    Map<String, Object> document = new HashMap<>();
                    document.put("id", bookSyncDO.getId());
                    document.put("refId", bookSyncDO.getRefId());
                    document.put("title", bookSyncDO.getTitle());
                    document.put("author", bookSyncDO.getAuthor());
                    document.put("category", bookSyncDO.getCategory());
                    document.put("language", bookSyncDO.getLanguage());
                    document.put("content", fileContent);
                    document.put("clickCount", bookSyncDO.getClickCount());

                    bulkRequestBuilder.operations(op -> op
                            .index(idx -> idx
                                    .index("bmr_books")
                                    .id(String.valueOf(bookSyncDO.getId()))
                                    .document(document)
                            ));
                }

                BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequestBuilder.build());
                if (bulkResponse.errors()) {
                    log.warn("Some data failed to synchronize: {}", bulkResponse.items().stream()
                            .filter(item -> item.error() != null)
                            .map(item -> "ID: " + item.id() + ", Reason: " + item.error().reason())
                            .collect(Collectors.joining(", ")));
                }

                List<String> syncedIds = unsyncedBooks.stream().map(BookDO::getId).collect(Collectors.toList());
                updateSyncFlag(syncedIds);

                log.info("Successfully synchronized {} entries.", syncedIds.size());

                unsyncedBooks = getUnsyncedBooks(100);
            }

            log.info("Data synchronization complete！");
        } catch (IOException e) {
            log.error("File read failed", e);
        } catch (Exception e) {
            log.error("Failed to synchronize data to Elasticsearch", e);
        }
    }

    private String readFileContent(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        try {
            if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
                return readFromUrl(filePath);
            } else {
                return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Unable to read content: {}", filePath, e);
            return null;
        }
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
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
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
}