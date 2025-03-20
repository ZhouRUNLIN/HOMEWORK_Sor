package org.rhw.bmr.project.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.BaseDO;

@Data
@TableName("t_user_bookmark")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDO  extends BaseDO {

    private Long id;

    private String gid;

    private String username;

    @TableField("bookId")
    private String bookId;

}
