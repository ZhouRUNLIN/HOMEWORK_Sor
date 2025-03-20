package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.BaseDO;


@Data
@TableName("t_books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDO extends BaseDO {

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

    @TableField(value = "es_sync_flag")
    private Integer esSyncFlag;

    private String img;
}
