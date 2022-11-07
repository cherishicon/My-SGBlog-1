package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.vo.PageVo;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-10-31 11:11:13
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    PageVo selectUserList(User user, Integer pageNum, Integer pageSize);

    void addNewUser(AddUserDto addUserDto);

    boolean checkUserNameUnique(AddUserDto addUserDto);

    boolean checkPhoneNumberUnique(AddUserDto addUserDto);

    boolean checkEmailUnique(AddUserDto addUserDto);
}
