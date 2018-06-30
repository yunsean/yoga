package com.yoga.imessager.rongcloud.util;

/**
 * Created on 2017/3/28
 **/

public class hexSHA1 {
    public static void main(String[] args) {
        String appSecret_nonce_timeStamp;
        appSecret_nonce_timeStamp = "k5ZkVaEjS46rE91100";
        String sign = CodeUtil.hexSHA1(appSecret_nonce_timeStamp);
        System.out.println(sign);
    }
}
