package org.rhw.bmr.user.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class BmrRecycleBinPageReqDTO extends Page {
    private List<String> gidList;
}

