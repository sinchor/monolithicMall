package com.mall.controller;

import com.mall.config.I18nMessage;
import com.mall.pojo.bo.ShopcartBO;
import com.mall.utils.MallJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "购物车相关的接口")
@RestController
@RequestMapping("/shopcart")
public class ShopcartController {

    final HttpServletRequest httpServletRequest;
    final HttpServletResponse httpServletResponse;

    @Autowired
    public ShopcartController(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Operation(summary = "增加用户购物车数据")
    @PostMapping("/add")
    public MallJSONResult add(@RequestParam String userId, @RequestBody ShopcartBO shopcartBO) {
        if (StringUtils.isBlank(userId)) {
            return MallJSONResult.errorMsg(I18nMessage.message("shopcart.error.userId_null"));
        }

        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        return MallJSONResult.ok();
    }

    @Operation(summary = "删除用户购物车数据")
    @PostMapping("/del")
    public MallJSONResult del(@RequestParam String userId, @RequestParam String itemSpecId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return MallJSONResult.errorMsg(I18nMessage.message("shopcart.error.parameter_null"));
        }
        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品
        return MallJSONResult.ok();
    }
}
