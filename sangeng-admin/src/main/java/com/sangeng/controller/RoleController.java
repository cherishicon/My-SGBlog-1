package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddRoleDto;
import com.sangeng.domain.dto.ChangeRoleStatusDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult roleList(Role role,Integer pageNum,Integer pageSize){
        PageVo pageVo = roleService.getRoleList(role,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeroleStatus(@RequestBody ChangeRoleStatusDto roleStatusDto){
        roleService.changeStatus(roleStatusDto);
        return ResponseResult.okResult();
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        roleService.insertRole(addRoleDto);
        return ResponseResult.okResult();
    }
}
