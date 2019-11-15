package com.tdpro.common.utils;

/**
 * @About 工具类
 * @Author jy
 * @Date 2018/4/5 10:15
 */
public class ToolUtil {
    private static final String salf="d%AdPxx^L#D1uRLJBcL3^RnU6dQAKSNG";
    public static String getRedisKey(String str){
        return Md5Tool.md5Hex(str+salf);
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {
        String result = "";
        if (code.length()>=num){
            //直接截取字符串
            return code.substring(code.length()-num);
        }
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code));
        return result;
    }
}
