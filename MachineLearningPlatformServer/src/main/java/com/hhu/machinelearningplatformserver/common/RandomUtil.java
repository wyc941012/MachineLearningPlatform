package com.hhu.machinelearningplatformserver.common;

import java.util.Random;

/**
 * 生成随机字符串工具
 *
 * @author hayes, @create 2017-12-14 14:35
 **/
public class RandomUtil {

    /**
     * 任意长度字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
