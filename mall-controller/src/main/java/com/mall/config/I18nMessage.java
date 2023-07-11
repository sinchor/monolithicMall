package com.mall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nMessage {

    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        I18nMessage.messageSource = messageSource;
    }
    public static String message(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return I18nMessage.messageSource.getMessage(code,null, locale);
    }
}
