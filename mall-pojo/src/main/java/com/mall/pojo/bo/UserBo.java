package com.mall.pojo.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.context.annotation.ComponentScan;

@Schema(description = "从客户端，由用户传入的数据封装在此entity中")
public class UserBo {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Schema(description = "用户二次确认的密码，注册时必须，用作登录对象时不必须", requiredMode = Schema.RequiredMode.AUTO)
    private String confirmPassword;

    public UserBo(String username, String password, String confirmPassword) {
        this.username = username.trim();
        this.password = password.trim();
        if (confirmPassword == null) {
            this.confirmPassword = null;
        } else {
            this.confirmPassword = confirmPassword.trim();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {

        this.confirmPassword = confirmPassword.trim();
    }
}
