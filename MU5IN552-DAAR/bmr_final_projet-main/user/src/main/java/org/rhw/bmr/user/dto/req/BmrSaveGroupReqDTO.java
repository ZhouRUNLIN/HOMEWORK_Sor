package org.rhw.bmr.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BmrSaveGroupReqDTO {

    private String username;

    private String groupName;


}
