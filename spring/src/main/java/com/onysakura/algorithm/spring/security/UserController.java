package com.onysakura.algorithm.spring.security;

import com.onysakura.algorithm.spring.common.result.Result;
import com.onysakura.algorithm.spring.security.model.UserModel;
import com.onysakura.algorithm.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.PUT)
    public Result<UserModel> register(@RequestBody UserModel userModel) throws AuthenticationException {
        UserModel register = userService.register(userModel);
        return Result.ok(register);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<UserModel> update(@RequestBody UserModel userModel) throws AuthenticationException {
        UserModel register = userService.save(userModel);
        return Result.ok(register);
    }

    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    public Result<UserModel> updateRole(@RequestBody UserModel userModel) throws AuthenticationException {
        userService.updateUserRole(userModel);
        return Result.ok();
    }
}
