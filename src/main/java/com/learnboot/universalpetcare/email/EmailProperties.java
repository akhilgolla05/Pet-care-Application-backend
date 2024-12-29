package com.learnboot.universalpetcare.email;

import lombok.Data;

@Data
public class EmailProperties {

    public static final String DEFAULT_HOST = "smtp.gmail.com";
    public static final int DEFAULT_PORT = 587;
    public static final String DEFAULT_SENDER = "gollaakhil1115@gmail.com";
    public static final String DEFAULT_USERNAME = "gollaakhil1115@gmail.com";
    //app-password
    public static final String DEFAULT_PASSWORD = "nnnn nnnn nnnn nnnn";
    public static final boolean DEFAULT_AUTH = true;
    public static final boolean DEFAULT_STARTTLS = true;
}
