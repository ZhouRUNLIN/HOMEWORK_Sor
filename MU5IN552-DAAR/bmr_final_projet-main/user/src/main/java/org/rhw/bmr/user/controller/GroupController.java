package org.rhw.bmr.user.controller;


import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.dto.req.*;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.common.convention.result.Results;
import org.rhw.bmr.user.dto.resp.BmrGroupRespDTO;
import org.rhw.bmr.user.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;


    @PostMapping("/api/bmr/user/v1/group")
    public Result<Void> save(BmrSaveGroupReqDTO requestParam) {
        groupService.saveGroup(requestParam);
        return Results.success();
    }


    @GetMapping("/api/bmr/user/v1/group")
    public Result<List<BmrGroupRespDTO>> listGroup(BmrListGroupReqDTO requestParam) {
        return Results.success(groupService.listGroup(requestParam));
    }


    @PutMapping("/api/bmr/user/v1/group")
    public Result<Void> updateGroup(@RequestBody BmrGroupUpdateDTO requestParam) {
        groupService.updateGroup(requestParam);
        return Results.success();
    }

    @DeleteMapping("/api/bmr/user/v1/group")
    public Result<Void> deleteGroup(BmrDeleteGroupReqDTO requestParam) {
        groupService.deleteGroup(requestParam);
        return Results.success();
    }


}
