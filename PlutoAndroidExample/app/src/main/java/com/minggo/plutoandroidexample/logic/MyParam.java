package com.minggo.plutoandroidexample.logic;

import com.minggo.pluto.logic.LogicParam;

/**
 * Created by minggo on 2017/2/22.
 */

public class MyParam extends LogicParam {

    public static final String DOMAIN = "http://m8en.com:8877/";
    public static final String DOMAIN_UPLOAD = "https://language.m9en.com/";

    public final class LoginParam{
        public static final int WHAT = 10000;
        public static final String URL = DOMAIN+"charmword/loginUser.action";
        public static final String CACHEKEY = "user_info";
    }

    public final class ServerUrlParam{

        public static final int WHAT = 10001;
        public static final String URL = DOMAIN+"ApiBus/getURLCommon.action";
        public static final String CACHEKEY = "server_url";

    }

    public final class UploadParam{

        public static final int WHAT = 10002;
        public static final String URL = DOMAIN_UPLOAD+"languagerecognition/recognition.action";

    }
}
