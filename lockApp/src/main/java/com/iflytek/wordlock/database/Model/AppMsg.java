package com.iflytek.wordlock.database.Model;

import java.util.Random;

/**
 * Created by 57628 on 2019/6/26.
 */

public class AppMsg {
    public static Integer id = 1;
    public static String ID = "1";
    public static final String DATABASE_NAME = "appmsg";
    public static final String DATABASE_TABLE = "isregister";

    private Integer isRegister;

    public AppMsg() {}

    public AppMsg(Integer isRegister) {this.isRegister = isRegister;}

    public Integer getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(Integer isRegister) {
        this.isRegister = isRegister;
    }

}
