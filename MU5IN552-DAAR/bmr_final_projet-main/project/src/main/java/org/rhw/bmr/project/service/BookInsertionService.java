package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookDO;

public interface BookInsertionService extends IService<BookDO> {

    void insertBook();

}
