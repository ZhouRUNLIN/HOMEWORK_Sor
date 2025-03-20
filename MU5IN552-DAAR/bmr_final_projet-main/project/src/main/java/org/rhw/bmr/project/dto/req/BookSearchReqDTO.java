package org.rhw.bmr.project.dto.req;

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
public class BookSearchReqDTO{

    private String title;

    private String author;

    private String category;

    private String language;

    private Integer pageNo;
    private Integer pageSize;

}
