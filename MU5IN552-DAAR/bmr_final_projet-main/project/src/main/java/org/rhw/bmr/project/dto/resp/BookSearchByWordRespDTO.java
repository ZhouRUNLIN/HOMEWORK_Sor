package org.rhw.bmr.project.dto.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchByWordRespDTO {

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

    private List<String> words;

    private String img;
}
