package org.rhw.bmr.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookDO;

import java.util.List;

public interface BookSyncService extends IService<BookDO> {
    List<BookDO> getUnsyncedBooks(int limit);

    void updateSyncFlag(List<String> ids);

    void syncBooksToElasticsearch();
}
