package com.mall.controller;

import com.mall.pojo.Users;
import com.mall.pojo.bo.UserBo;
import com.mall.service.UserService;
import com.mall.utils.CookieUtils;
import com.mall.utils.JSONUtil;
import com.mall.utils.MallJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Tag(name = "用户注册登录")
@RestController
@RequestMapping("/passport")
public class PassportController {

    private static final String cookieName = "user";

    final UserService userService;
    final MessageSource messageSource;

    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    @Autowired
    public PassportController(UserService userService, MessageSource messageSource,
                              HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Operation(summary = "判断用户名是否存在")
    @GetMapping("/usernameExists")
    public MallJSONResult usernameExists(@RequestParam String username) {
        if (StringUtils.isEmpty(username)) {
            return MallJSONResult.errorMsg(message("passport.error.usernameNull"));
        }
        boolean nameExists = userService.queryUserNameExists(username);
        if (nameExists) {
            return MallJSONResult.errorMsg(message("passport.error.usernameExists"));
        }
        return MallJSONResult.ok();
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public MallJSONResult register(@RequestBody UserBo userBo) {
        String username = userBo.getUsername();
        String password = userBo.getPassword();
        String confirmPassword = userBo.getConfirmPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return MallJSONResult.errorMsg(message("passport.error.un_psw_null"));
        }

        // 1. 查询用户名是否存在
        boolean nameExists = userService.queryUserNameExists(username);
        if (nameExists) {
            return MallJSONResult.errorMsg(message("passport.error.usernameExists"));
        }

        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return MallJSONResult.errorMsg(message("passport.error.shortPswLength"));
        }
        // 3. 判断两次密码是否一致
        if (! password.equals(confirmPassword)) {
            return MallJSONResult.errorMsg(message("passport.error.pswMismatched"));
        }

        // 4. 实现注册
        Users user = userService.createUser(userBo);

        updatePropertiesAndSetCookie(user);

        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return MallJSONResult.ok();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public MallJSONResult login(@RequestBody UserBo userBo) {
        String username = userBo.getUsername();
        String password = userBo.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return MallJSONResult.errorMsg(message("passport.error.un_psw_null"));
        }
        Users user = userService.queryUserForLogin(username, password);
        if (user == null) {
            return MallJSONResult.errorMsg(message("passport.error.un_psw_error"));
        }

        user = updatePropertiesAndSetCookie(user);

        return MallJSONResult.ok(user);
    }
    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public MallJSONResult logout(String userId) {
        CookieUtils.deleteCookie(httpServletRequest, httpServletResponse, cookieName);
        // TODO remove shopcart
        // TODO remove token
        return MallJSONResult.ok();
    }

    private Users updatePropertiesAndSetCookie(Users user) {

        Users result = setNullProperty(user);

        //set cookie
        String cookieString = JSONUtil.objectToJSONString(result);
        CookieUtils.setCookie(httpServletRequest, httpServletResponse, cookieName,
                cookieString,true);
        return result;
    }
    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    private String message(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code,null, locale);
    }
}
