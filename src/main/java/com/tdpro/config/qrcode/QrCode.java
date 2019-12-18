package com.tdpro.config.qrcode;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tdpro.common.QiniuSimpleUpload;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 获取微信二维码
 */
public class QrCode {
    private String appid;
    private String secret;
    WxMaProperties wxMaProperties=new WxMaProperties();
    WxMaService wxMaService = new WxMaServiceImpl();
    public QrCode(String appid, String secret){
        this.appid = appid;
        this.secret = secret;
    }

    /**
     * 获取带参二维码
     * @param scene
     * @param path
     * @return
     */
    public String getQrCodeLimit(String scene,String path,Integer width){
        File file = new File("");
        String qrCodePath = null;
        try {
            wxMaProperties.setAppid(this.appid);   //需要替换为实际商户appid
            wxMaProperties.setSecret(this.secret); //需要替换为实际商户Secret
            wxMaService.setWxMaConfig(wxMaProperties);
            file = wxMaService.getQrcodeService().createWxCodeLimit(scene,path,width,true,(WxMaCodeLineColor)null);
            qrCodePath = encryptToBase64(file.getPath());
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return qrCodePath;
    }

    public String getQrCodeCreate(String path,int width){
        File file = new File("");
        String qrCodePath = null;
        try {
            wxMaProperties.setAppid(this.appid);
            wxMaProperties.setSecret(this.secret);
            wxMaService.setWxMaConfig(wxMaProperties);
            file = wxMaService.getQrcodeService().createWxCode(path,width);
            qrCodePath = encryptToBase64(file.getPath());
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return qrCodePath;
    }

    public String getQrCode(String appid,String secret){
        File file = new File("");
        QiniuSimpleUpload qiniuSimpleUpload=new QiniuSimpleUpload();
        String qrCodePath = null;
        try {
            wxMaProperties.setAppid(appid);
            wxMaProperties.setSecret(secret);
            wxMaService.setWxMaConfig(wxMaProperties);
            file = wxMaService.getQrcodeService().createWxCode("pages/initPage/initPage");

            String imgName=System.currentTimeMillis()+".png";
            qrCodePath = upLoadQinNiu(file,imgName);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return qrCodePath;
    }

    public String encryptToBase64(String filePath) {
        FileInputStream in;
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String upLoadQinNiu(File file,String imgName){
        String qrCodePath = null;
        QiniuSimpleUpload qiniuSimpleUpload=new QiniuSimpleUpload();
        try {
            String upResult = qiniuSimpleUpload.upload(file, imgName);
            JSONObject formatUpResult = JSON.parseObject(upResult);
            if (!StringUtils.isEmpty(formatUpResult.get("key").toString())) {
                qrCodePath = "/" + formatUpResult.get("key").toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return qrCodePath;
    }
}
