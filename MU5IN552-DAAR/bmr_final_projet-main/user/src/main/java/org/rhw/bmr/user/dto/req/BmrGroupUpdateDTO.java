package org.rhw.bmr.user.dto.req;

import lombok.Data;

@Data
public class BmrGroupUpdateDTO {
    private String gid;
    private String name;    //分组名

    private String username;

}
