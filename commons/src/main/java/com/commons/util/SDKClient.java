package com.commons.util;

import com.bcloud.msg.http.HttpSender;

public class SDKClient {
    public static String uri = "http://222.73.117.158/msg/";//应用地址
    public static String account = "hnqc88";//账号
    public static String pswd = "Tch123456";//密码
    public static String mobiles = "";//手机号码，多个号码使用","分割
    public static String content = "";//短信内容
    public static boolean needstatus = true;//是否需要状态报告，需要true，不需要false
    public static String product = null;//产品ID
    public static String extno = null;//扩展码

    public static String batchSend(String mobiles, String content) throws Exception{
        return HttpSender.batchSend(uri, account, pswd, mobiles, content, needstatus, product, extno);
    }
}
