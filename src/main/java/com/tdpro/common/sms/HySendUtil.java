package com.tdpro.common.sms;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
public class HySendUtil {


    /**
     * 无参构造
     * */
    private HySendUtil() {
    }

    /**
     * zyer send
     *
     * @return
     * @throws IOException
     */
    public static boolean sendMsm(String apiId, String apiKey, String[] mobiles, String content, String httpUrl) throws IOException {
        StringBuilder sub = new StringBuilder();
        BufferedReader br;
        URL url;
        HttpURLConnection con;
        String line = null;
        try {
            // 设置发送内容的编码方式
            String sendContent = URLEncoder.encode((content).replaceAll("<br/>", " "), "UTF-8");// 发送内容
            String mobile = "";
            for (int j = 0; j < mobiles.length; j++) {
                mobile += mobiles[j];
                if (j < mobiles.length - 1) {
                    mobile += ",";
                }
            }
            url = new URL(httpUrl+ "?method=Submit" + "&account=" + apiId + "&password=" + apiKey + "&mobile=" + mobile + "&content=" + sendContent+"&format=json");
            con = (HttpURLConnection) url.openConnection();
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            line = br.readLine();
            br.close();
        } catch (IOException e) {
            log.error("",e);
        }
        return readJson(line);
    }

    public static boolean readJson(String line){
        if(null != line && StringUtils.hasText(line)){
            JSONObject jsonObject = new JSONObject(line);
            int code    = jsonObject.getInt("code");
            String msg  = jsonObject.getString("msg");
            if(code == 2){
                return true;
            }
            log.error("短信返回状态为：" + code);
            log.error("短信返回信息提示：" + msg);
        }
        return false;
    }
}
