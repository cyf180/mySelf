package com.tdpro.controller;

import com.alibaba.fastjson.JSON;
import com.tdpro.common.QiniuSimpleUpload;
import com.tdpro.common.utils.Response;
import com.tdpro.common.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@RequestMapping("/api/imgUpload")
@RestController
@Api(tags = "上传图片接口")
@Slf4j
public class ImgUploadController {

    @Value("${qiniu.imgPath}")
    private String imgPath;

    protected Logger logger = LoggerFactory.getLogger(getClass());



    @PostMapping("/upload")
    @ApiOperation(value = "图片上传接口", response = Response.class)
    public Response upLoad(HttpServletRequest request){
        Response response = ResponseUtils.errorRes("upFail");
        Map map = new HashMap<>();

        //判断request是否有文件上传
        MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
        //获取multiRequest 中所有的文件名
        Iterator iter=multiRequest.getFileNames();
        List<String> url=new ArrayList<String>();
        try {
            List<MultipartFile> files=new ArrayList<MultipartFile>();
            while(iter.hasNext()) {
                map.put("message", "文件过大,上传失败！");
                map.put("error", 1);
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                //获取上传文件类型
                String fileName=file.getOriginalFilename();
                String name=fileName.substring(fileName.lastIndexOf(".")+1);
                String prefix= name.toLowerCase();
               if(prefix.equals("jpg")||prefix.equals("png")){
                   //图片小于200Kb
                   if((file.getSize()/1024f)>200){
                       response.setData(map);
                   }
               }else if(prefix.equals("mp4")){
                   //视频小于200M
                   if((file.getSize()/1048576f)>200){
                       response.setData(map);
                   }
               }
                files.add(file);
            }
           for (MultipartFile file:files){
                response=  upFile(file,url);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return response;
    }

    private Response  upFile(MultipartFile file, List<String> url) throws IOException{
        Response response = ResponseUtils.errorRes("图片上传失败");
        Map map = new HashMap<>();
        if(file!=null) {
            QiniuSimpleUpload qiniuSimpleUpload=new QiniuSimpleUpload();
            String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
            String upResult = qiniuSimpleUpload.upload(file.getBytes(), fileName);
            com.alibaba.fastjson.JSONObject formatUpResult = JSON.parseObject(upResult);
            if(!StringUtils.isEmpty(formatUpResult.get("key").toString())) {
                map.put("img",formatUpResult.get("key").toString());
                map.put("error", 0);
                String urlStr="/"+formatUpResult.get("key").toString();
                map.put("url",imgPath+urlStr);
                url.add(urlStr);
                map.put("urls",url);
                response = ResponseUtils.successRes(map);

            }
        }
        return response;
    }

    @PostMapping(value = "/uploadCerts")
    @ApiOperation(value = "证书上传接口", response = Response.class)
    @ResponseBody
    public Response uploadCerts(HttpServletRequest req,
                           MultipartHttpServletRequest multiReq) {

        Map result = new HashMap();
        result.put("error", 1);
        //获取multiRequest 中所有的文件名
        Iterator iter=multiReq.getFileNames();
            while(iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile oldFile=multiReq.getFile(iter.next().toString());
                String uploadFileName = Long.valueOf(System.currentTimeMillis()).toString()+"_"+oldFile.getOriginalFilename();
                // 获取上传文件的路径
                String uploadFilePath = oldFile.getOriginalFilename();
                log.info("uploadFlePath:" + uploadFilePath);
                FileOutputStream fos = null;
                FileInputStream fis = null;
                try {
                    String filePath="certs/" + uploadFileName;
                    //File file = new File(this.getClass().getResource("/").getPath()+"/" + filePath);
                    File file = new File(filePath);
                    File fileParent = file.getParentFile();
                    if(!fileParent.exists()){
                        fileParent.mkdirs();
                    }
                    fis = (FileInputStream) oldFile.getInputStream();
                    fos = new FileOutputStream(file);
                    byte[] temp = new byte[1024];
                    int i = fis.read(temp);
                    while (i != -1){
                        fos.write(temp,0,temp.length);
                        fos.flush();
                        i = fis.read(temp);
                    }
                    result.put("error", 0);
                    result.put("url",filePath);
                    result.put("msg","");
                    return ResponseUtils.successRes(result);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    return ResponseUtils.errorRes("上传失败");
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            log.error(e.getMessage());
                            return ResponseUtils.errorRes("上传失败");
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            log.error(e.getMessage());
                            return ResponseUtils.errorRes("上传失败");
                        }
                    }
                }
            }
        return ResponseUtils.errorRes("上传失败");
    }


}
