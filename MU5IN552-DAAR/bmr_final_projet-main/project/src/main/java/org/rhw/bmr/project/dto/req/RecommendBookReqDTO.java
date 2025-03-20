package org.rhw.bmr.project.dto.req;

import lombok.Data;

@Data
public class RecommendBookReqDTO {

    private String username;

    private Integer pageNo;
    private Integer pageSize;
}
