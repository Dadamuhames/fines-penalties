package com.uzumtech.finespenalties.utils;

import org.springframework.stereotype.Component;

@Component
public class PhoneUtils {

    public String maskPhone(String phone) {
        String phoneWithoutCode = phone.substring(3);

        return String.format("998%s", phoneWithoutCode.replaceAll(".(?=.{5})", "*"));
    }

}
