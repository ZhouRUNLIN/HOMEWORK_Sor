package org.rhw.bmr.project.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookSearchByIdReqDTO {

    private String bookId;

}
