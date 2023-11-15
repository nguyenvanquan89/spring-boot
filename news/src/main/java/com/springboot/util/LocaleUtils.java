package com.springboot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class LocaleUtils {

    @Autowired
    private MessageSource messageSource;

    public String getMessageByKey(String key, Object[] obj) {
        HttpServletRequest request = getCurrentRequest();
        return messageSource.getMessage(key, obj, request.getLocale());
    }

    /**
     * get current request
     * @return current request
     */
    public HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

}
