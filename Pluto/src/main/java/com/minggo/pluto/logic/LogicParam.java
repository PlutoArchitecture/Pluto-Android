package com.minggo.pluto.logic;

/**
 * Created by minggo on 2017/2/22.
 */

public abstract class LogicParam {

    public final class ParamName{
        public static final String PN = "pn";
        public static final String PS = "ps";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
    }

    public final class ParamWhat{
        public static final int LOGIN = 10000;
        public static final int REGIST = 10001;
    }

}
