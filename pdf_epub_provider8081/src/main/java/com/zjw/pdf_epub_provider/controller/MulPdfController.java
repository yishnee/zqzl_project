package com.zjw.pdf_epub_provider.controller;

import com.zjw.pdf_epub_provider.service.MulEpubService;
import com.zjw.pdf_epub_provider.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 *  作者： Zzr
 *  <br>
 *  创建时间：2022/3/13 15:10
 */
@Slf4j
@RestController
@RequestMapping("/PdfToEpub")
public class MulPdfController {
    @Autowired
    MulEpubService mulEpubService;

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/upload/many")
    public String httpUploads(@RequestParam("uploadFile") MultipartFile[] uploadFiles, String username){
        String msg;
        try {
            msg=mulEpubService.MergePdfToEpub(uploadFiles,username);
        } catch (IOException e) {
            log.error("无法创建上传的文件！");
            msg="error";
            e.printStackTrace();
            return msg;
        }
        log.info("用户："+username+"成功批量上传了"+uploadFiles.length+"个文件："+uploadFiles.toString());
        return msg;
    }



}
