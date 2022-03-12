package com.zjw.pdf_epub_provider.controller;

import com.zjw.pdf_epub_provider.service.SingleEpubService;
import com.zjw.pdf_epub_provider.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author yishnee
 * @version 1.0
 */
@RestController
@Slf4j
public class TestController {
    @Autowired
    SingleEpubService singleEpubService;
    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/load")
    public String httpUpload(MultipartFile pdfFile, String username){
        String msg="";
        try {
            msg=singleEpubService.pdfToEpub_Single(pdfFile,username);
        } catch (IOException e) {
            log.error("无法创建上传的文件！");
            msg="error";
            e.printStackTrace();
        }
        log.info("用户："+username+"成功上传了文件："+pdfFile.getOriginalFilename());
        return msg;
    }

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file){
        return saveFile(file);
    }

    private Object saveFile(MultipartFile file){
        if (file.isEmpty()){
            return "未选择文件";
        }
        String filename = file.getOriginalFilename(); //获取上传文件原来的名称
        //String filePath = "/Users/laoniu/temp/";
        //File temp = new File(filePath);
        //if (!temp.exists()){
        //    temp.mkdirs();
        //}
        //
        //File localFile = new File(filePath+filename);
        //file.transferTo(localFile); //把上传的文件保存至本地
        System.out.println(file.getOriginalFilename()+" 上传成功");

        return "ok";
    }


}
