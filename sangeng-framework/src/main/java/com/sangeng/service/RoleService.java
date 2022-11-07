package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.dto.ChangeRoleStatusDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.vo.PageVo;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-11-03 13:38:34
 */
public interface RoleService extends IService<Role> {

    PageVo getRoleList(Role role, Integer pageNum, Integer pageSize);

    List<String> selectRoleKeyByUserId(Long id);


    void changeStatus(ChangeRoleStatusDto roleStatusDto);
}
