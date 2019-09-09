package com.signaretech.seneachat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageTranslator {

    private static ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    MessageTranslator(ReloadableResourceBundleMessageSource messageSource) {
        MessageTranslator.messageSource = messageSource;
    }

    public static String getLocaleMessage(String msgKey) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgKey, null, locale);
    }
}
