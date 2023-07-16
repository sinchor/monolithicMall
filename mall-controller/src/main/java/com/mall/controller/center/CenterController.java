package com.mall.controller.center;

import com.mall.config.I18nMessage;
import com.mall.pojo.Users;
import com.mall.service.center.CenterUserService;
import com.mall.utils.MallJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户中心接口")
@RestController
@RequestMapping("/center")
public class CenterController {

    final CenterUserService centerUserService;

    @Autowired
    public CenterController(CenterUserService centerUserService) {
        this.centerUserService = centerUserService;
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/userInfo")
    public MallJSONResult getUserInfo(@RequestParam String userId) {

        Users users = centerUserService.queryUserInfo(userId);

        if (users == null) {
            return MallJSONResult.errorMsg(I18nMessage.message("center.user.error.cannot_find_user"));
        }
        return MallJSONResult.ok(users);
    }
}
