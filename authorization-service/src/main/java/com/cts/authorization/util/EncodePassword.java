package com.cts.authorization.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePassword {


    public static void main(String[] args) {
        registerStudent();
    }

    /**
     * Any user can access this API - No Authentication required
     * @return
     */

    public static void registerStudent() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("password1"));
    }

}
