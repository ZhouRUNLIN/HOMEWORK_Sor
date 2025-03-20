package org.rhw.bmr.project.dto.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.dao.entity.BookDO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchByIdRespDTO extends Page<BookDO> {

    private String id;

    @TableField("ref_id")
    private Long refId;

    private String title;

    @TableField("storage_path")
    private String storagePath;

    private String author;

    private String category;

    private String description;

    private String language;

    private Long clickCount;

    private Long sortedOrder;

    private String img;

}
