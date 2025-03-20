package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.BaseDO;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@TableName("t_user_preference")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDO extends BaseDO {

    /**
     * Primary Key ID
     */
    private Long id;

    /**
     * user name
     */
    @TableField("user_name")
    private String username;

    /**
     * Favorite Authors
     */
    private String author;

    /**
     * Favorite Categories
     */
    private String category;

    /**
     * Number of user clicks/reads
     */
    private Integer likeCount;


}
