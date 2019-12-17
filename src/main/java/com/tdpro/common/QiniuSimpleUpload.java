package com.tdpro.common;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.IOException;

/**
 * 七牛简单上传模块
 * @author lijie
 * @email  lijie@6mi.com
 * @time   2016年11月10日 下午4:26:14
 */

public class QiniuSimpleUpload {
    private String accessKey="uOp69-ZMu6dmXC1mkKbwQ1RkCaywX3CI6-ko8YVf";
    private String secretKey="wf_SY30qvQ_2TXoDpircGDSWdma4HaUq3tkIWsK7";
    private String bucketname="zhutx";


    //密钥配置
    Auth auth = Auth.create(accessKey, secretKey);
    UploadManager uploadManager = new UploadManager();

    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    public String upload(byte[] date, String key) throws IOException{
        try {
            //调用put方法上传
            Response res = uploadManager.put(date, key, getUpToken());
            //打印返回的信息
            return res.bodyString();
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                return r.bodyString();
            } catch (QiniuException e1) {
                return "{hash:"+""+",key:"+""+"}";
            }
        }
    }

    public String upload(File file, String key) throws IOException{
        try {
            //调用put方法上传
            Response res = uploadManager.put(file, key, getUpToken());
            //打印返回的信息
            return res.bodyString();
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                return r.bodyString();
            } catch (QiniuException e1) {
                return "fail";
            }
        }
    }
    public Boolean delete(String name){
        Auth auth = Auth.create(this.accessKey, this.secretKey);
        BucketManager bucketMgr = new BucketManager(auth);
        //指定需要删除的文件，和文件所在的存储空间
        String bucketName = this.bucketname;
        try {
            bucketMgr.delete(bucketName, name);//当前为7.2.1；  7.2.2后才能传多个key ，即：第二个参数为数组 (String... deleteTargets)
            return true;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return false;
    }
}


