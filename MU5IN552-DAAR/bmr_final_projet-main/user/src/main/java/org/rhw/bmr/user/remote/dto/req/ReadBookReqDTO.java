package org.rhw.bmr.user.remote.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadBookReqDTO {

    private String username;

    private String bookId;

}
