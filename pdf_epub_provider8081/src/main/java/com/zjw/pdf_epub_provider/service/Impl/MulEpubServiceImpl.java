package com.zjw.pdf_epub_provider.service.Impl;

import com.zjw.pdf_epub_provider.dao.UserDao;
import com.zjw.pdf_epub_provider.entity.PathTotal;
import com.zjw.pdf_epub_provider.entity.User;
import com.zjw.pdf_epub_provider.service.MulEpubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 *  作者： Zzr
 *  <br>
 *  创建时间：2022/3/13 15:14
 */
@Slf4j
@Service
public class MulEpubServiceImpl implements MulEpubService {
    @Autowired
    SingleEpubServiceImpl singleEpubService;

    @Autowired
    UserDao userDao;

    @Autowired
    PathTotal pdfTotal;


    @Override
    public String MergePdfToEpub(MultipartFile[] files, String username) throws IOException {
        //获取用户信息
        User user = userDao.findByUsername(username);
        /*// pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();*/
        // 多个PDF文件逐个转换
        String epub_single = "";
        for (int i = 0; i < files.length; i++) {
            if (i != files.length-1) epub_single += singleEpubService.pdfToEpub_Single(files[i], username) + "\n";
            else epub_single += singleEpubService.pdfToEpub_Single(files[i], username);
        }
        /*// 合成文件
        for (MultipartFile file : files) {
            mergePdf.addSource(file.getInputStream());
        }*/
        //获取用户专属的pdf文件夹，若为新用户则创建文件夹
        File userFile = new File(pdfTotal.getPdfPath() + "/" + user.getUsername());
        if(!userFile.exists()){
            userFile.mkdir();
        }
        /*// 设置合并生成pdf文件
        String path = pdfTotal.getPdfPath() + "\\" + user.getUsername() + "\\" + files[0].getOriginalFilename();
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        mergePdf.setDestinationFileName(path);
        // 合并pdf
        mergePdf.mergeDocuments();
        String epub_single = singleEpubService.pdfToEpub_Single(file, username);*/
        return epub_single;

    }


}
