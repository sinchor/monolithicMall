package com.mall.controller.center;

import com.mall.config.BaseConfig;
import com.mall.pojo.Users;
import com.mall.pojo.bo.center.CenterUserBO;
import com.mall.service.center.CenterUserService;
import com.mall.utils.CookieUtils;
import com.mall.utils.DateUtil;
import com.mall.utils.JSONUtil;
import com.mall.utils.MallJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Tag(name = "用户信息相关接口")
@RestController
@RequestMapping("/userInfo")
public class CenterUserController {

    private final CenterUserService centerUserService;

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public CenterUserController(CenterUserService centerUserService,
                                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.centerUserService = centerUserService;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Operation(summary = "更新用户信息")
    @PostMapping("/update")
    public MallJSONResult update(@RequestParam String userId, @Validated @RequestBody CenterUserBO centerUserBO) {
        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        setNullProperty(users);
        //set cookie
        String cookieString = JSONUtil.objectToJSONString(users);
        CookieUtils.setCookie(httpServletRequest, httpServletResponse, BaseConfig.cookieName,
                cookieString,true);
        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return MallJSONResult.ok(users);
    }

    @Operation(summary = "上传用户头像")
    @PostMapping("/uploadFace")
    public MallJSONResult uploadFace(@RequestParam String userId, MultipartFile multipartFile) {

        String imageUserFaceLocation="D:\\Programs\\apache-tomcat-10.1.8\\webapps\\mall\\images";
        String imageServerUrl="http://localhost:8080/mall/images";

        // 定义头像保存的地址
//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = imageUserFaceLocation;
        // 在路径上为每一个用户增加一个userid，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        // 开始文件上传
        if (multipartFile != null) {
            FileOutputStream fileOutputStream = null;
            try {
                // 获得文件上传的文件名称
                String fileName = multipartFile.getOriginalFilename();

                if (StringUtils.isNotBlank(fileName)) {

                    // 文件重命名  imooc-face.png -> ["imooc-face", "png"]
                    String fileNameArr[] = fileName.split("\\.");

                    // 获取文件的后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    if (!suffix.equalsIgnoreCase("png") &&
                            !suffix.equalsIgnoreCase("jpg") &&
                            !suffix.equalsIgnoreCase("jpeg") ) {
                        return MallJSONResult.errorMsg("图片格式不正确！");
                    }

                    // face-{userid}.png
                    // 文件名称重组 覆盖式上传，增量式：额外拼接当前时间
                    String newFileName = "face-" + userId + "." + suffix;

                    // 上传的头像最终保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    // 用于提供给web服务访问的地址
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        // 创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    // 文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = multipartFile.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            return MallJSONResult.errorMsg("文件不能为空！");
        }

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(httpServletRequest, httpServletResponse, "user",
                JSONUtil.objectToJSONString(userResult), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return MallJSONResult.ok();
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
}
