package com.mall.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 */
public class CommonUtil {
    public static String processErrorString(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage() + ",");
        }
        return stringBuilder.substring(0,stringBuilder.length() - 1).toString();
    }

    public static String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        Base64.Encoder encoder = Base64.getEncoder();
        String result = encoder.encodeToString(messageDigest.digest(str.getBytes("utf-8")));
        return result;
    }
}
