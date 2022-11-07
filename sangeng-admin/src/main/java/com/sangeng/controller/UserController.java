package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult userList(User user,Integer pageNum,Integer pageSize){
        PageVo pageVo = userService.selectUserList(user,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        if(!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if(userService.checkUserNameUnique(addUserDto)){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(userService.checkPhoneNumberUnique(addUserDto)){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if(userService.checkEmailUnique(addUserDto)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        userService.addNewUser(addUserDto);
        return ResponseResult.okResult();
    }
}
