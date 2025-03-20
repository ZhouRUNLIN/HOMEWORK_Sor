package org.rhw.bmr.project.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDeleteReqDTO {

    private String gid;

    private String username;

    private String bookId;

}
