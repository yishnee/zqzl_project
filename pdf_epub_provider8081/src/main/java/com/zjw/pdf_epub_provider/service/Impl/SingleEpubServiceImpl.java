package com.zjw.pdf_epub_provider.service.Impl;

import com.spire.pdf.PdfDocument;
import com.zjw.pdf_epub_provider.dao.EpubDao;
import com.zjw.pdf_epub_provider.dao.HtmlInfoDao;
import com.zjw.pdf_epub_provider.dao.PdfInfoDao;
import com.zjw.pdf_epub_provider.dao.UserDao;
import com.zjw.pdf_epub_provider.entity.*;
import com.zjw.pdf_epub_provider.service.SingleEpubService;
import com.zjw.pdf_epub_provider.utils.CommonUtils;
import com.zjw.pdf_epub_provider.utils.EpubUtil;
import com.zjw.pdf_epub_provider.utils.PdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *  用于：
 *  1、保存和分离上传的 Pdf 文件并转换为 Html 文件
 *  2、创建 Epub 文件
 *  3、删除过程中多余的 Pdf 和 Html 文件
 */
@Slf4j
@Service
public class SingleEpubServiceImpl implements SingleEpubService {

    @Resource
    UserDao userDao;

    @Resource
    PdfInfoDao pdfInfoDao;

    @Resource
    HtmlInfoDao htmlInfoDao;

    @Resource
    EpubDao epubDao;

    @Autowired
    PathTotal pathTotal;



    /**
     * 用于保存用户上传的pdf文件
     *
     * @param uploadFile 用户上传的文件
     * @param user       用户信息
     * @return 用于下面连贯操作的PdfInfo对象
     */
    public PdfInfo savePdf(MultipartFile uploadFile, User user) throws IOException {
        //获取用户使用次数
        //log.info("获取用户使用次数中...");
        Integer times = user.getUseTimes();
        times++;
        //获取用户专属的pdf文件夹，若为新用户则创建文件夹
        File savaSplitPathFile = new File(pathTotal.getPdfPath() + "/" + user.getUsername() + "/" + times);
        if (!savaSplitPathFile.exists()) {
            savaSplitPathFile.mkdirs();
        }
        //根据用户的使用次数建立相应的文件夹，并且更新用户的使用次数
        user.setUseTimes(times);

        //获取文件夹所在位置
        String savaSplitPath = savaSplitPathFile.getAbsolutePath();

        //将用户上传的pdf放入相应文件夹中
        log.info("用户上传的pdf放入相应文件夹中...");
        byte[] uploadFileBytes = uploadFile.getBytes();
        File upFile = new File(pathTotal.getPdfPath() + "/" + user.getUsername() + "/" + uploadFile.getOriginalFilename());
        if (!upFile.exists()) {
            upFile.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(upFile);
        outputStream.write(uploadFileBytes);
        PdfDocument pdfDocument = new PdfDocument(new FileInputStream(upFile));
        //将pdf信息保存到数据库中
        log.info("pdf信息保存到数据库中...");
        PdfInfo pdfInfo = pdfInfoDao.save(new PdfInfo()
                .setPdfName(upFile.getName())
                .setSize(upFile.length())
                .setSavePath(upFile.getAbsolutePath())
                .setPageNum(pdfDocument.getPages().getCount())
                .setUserId(user.getUserId())
                .setSaveTime(CommonUtils.dateFormatRightNow())
                .setUpload(true)
                .setSplitPath(savaSplitPath));
        outputStream.close();
        return pdfInfo;
    }


    /**
     * 用于分离pdf文件
     *
     * @param pdfInfo 保存的pdf相关信息
     */
    public void splitPdf(PdfInfo pdfInfo) throws IOException {
        //获取pdf文件所在位置
        String pdfFileName = pdfInfo.getSavePath();
        //获取分离后所需放的文件夹
        String pdfSplitFileName = pdfInfo.getSplitPath();
        //调用方法，实现分离
        PdfUtil.splitPdfs(pdfFileName, pdfSplitFileName);
    }

    /**
     * 用于将分离的pdf文件转换为Html文件
     *
     * @param pdfInfo 保存的pdf相关信息
     * @param user    用户的信息
     * @return 用于下面连贯操作转换为pdf的HtmlInfo对象
     */
    public HtmlInfo saveHtml(PdfInfo pdfInfo, User user) throws IOException {
        //获取用户使用次数
        Integer times = user.getUseTimes();
        //获取pdf分离之后的路径(需要转换的pdf文件集合的路径)
        String splitPdfPath = pdfInfo.getSplitPath();
        //获得HtmlTotal文件下该用户专属的Html路径，若不存在则创建
        File userFile = new File(pathTotal.getHtmlPath() + "/" + user.getUsername());
        if (!userFile.exists()) {
            userFile.mkdirs();
        }
        //根据使用次数创建文件夹，若不存在则创建一个新的文件夹
        String savaSplitPath = userFile.getAbsolutePath() + "/" + times;
        //本次转换的pdf分离后所保存的文件夹
        File savaSplitPathFile = new File(savaSplitPath);
        if (!savaSplitPathFile.exists()) {
            savaSplitPathFile.mkdirs();
        }
        //获取当前时间
        Calendar calendar = new GregorianCalendar();
        PdfUtil.toHtmls(splitPdfPath, savaSplitPath);
        //Todo 生成文本信息 可以关掉
        //PdfToTxtUtil.toTxt(splitPdfPath, savaSplitPath);

        HtmlInfo htmlInfo = htmlInfoDao.save(new HtmlInfo()
                .setSaveTime(CommonUtils.dateFormat(calendar))
                .setSavePath(savaSplitPath)
                .setUserId(user.getUserId()));
        log.info("用户：" + user.getUsername() + "的第" + user.getUseTimes() + "次文件的html创建成功!");
        return htmlInfo;
    }

    /**
     * 用于创建Epub
     *
     * @param pdfInfo  需要使用到的pdf信息
     * @param htmlInfo 需要使用到的html信息
     * @param user     用户信息
     */
    public String createEpub(PdfInfo pdfInfo, HtmlInfo htmlInfo, User user) {
        log.info("创建epub中...");
        //获取需要转换为Epub的html文件集合路径
        String htmlInfoSavePath = htmlInfo.getSavePath();
        //获取该用户下的专属Epub文件夹，若不存在则创建
        File userFile = new File(pathTotal.getEpubPath() + "/" + user.getUsername());
        if (!userFile.exists()) {
            userFile.mkdirs();
        }
        //获取最终Epub的文件名
        String EpubName = pdfInfo.getPdfName().substring(0, pdfInfo.getPdfName().lastIndexOf(".")) + ".epub";
        //创建本次所需要保存的文件夹
        File saveFile = new File(userFile.getAbsolutePath() + "/" + user.getUseTimes());
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        //获取此次需要存放Epub的位置
        String saveEpubPath = userFile.getAbsolutePath() + "/" + user.getUseTimes() + "/" + EpubName;
        //调用工具类生成Epub文件
        EpubUtil.toEpub(htmlInfoSavePath, saveEpubPath);
        //获取当前时间
        Calendar calendar = new GregorianCalendar();
        //获取文件大小
        File EpubFile = new File(saveEpubPath);
        long length = EpubFile.length();
        epubDao.save(new EpubInfo()
                .setEpubName(EpubName)
                .setSavePath(saveEpubPath)
                .setSaveTime(CommonUtils.dateFormat(calendar))
                .setUserId(user.getUserId())
                .setSize(length));
        return saveEpubPath;
    }

    @Override
    public String pdfToEpub_Single(MultipartFile uploadFile, String username) throws IOException {
        User user = userDao.findByUsername(username);
        PdfInfo info = savePdf(uploadFile, user);
        splitPdf(info);
        HtmlInfo htmlInfo = saveHtml(info, user);
        String epub = createEpub(info, htmlInfo, user);
        delAndMoveFile(htmlInfo, info);
        return epub;
    }

    public String pdfToEpub_Single(File uploadFile, String username) throws IOException {
        User user = userDao.findByUsername(username);
        Integer times = user.getUseTimes();
        times++;
        user.setLastTime(CommonUtils.dateFormatRightNow()).setUseTimes(times);
        String savaSplitPath = uploadFile.getParent() + "/" + times;
        File savaSplitPathFile = new File(savaSplitPath);
        if (!savaSplitPathFile.exists()) {
            savaSplitPathFile.mkdir();
        }
        PdfDocument pdfDocument = new PdfDocument(new FileInputStream(uploadFile));
        PdfInfo info = new PdfInfo()
                .setPdfName(uploadFile.getName())
                .setSize(uploadFile.length())
                .setSavePath(uploadFile.getAbsolutePath())
                .setPageNum(pdfDocument.getPages().getCount())
                .setUserId(user.getUserId())
                .setSaveTime(CommonUtils.dateFormatRightNow())
                .setUpload(true)
                .setSplitPath(savaSplitPath);
        pdfInfoDao.save(info);
        userDao.save(user);
        splitPdf(info);
        HtmlInfo htmlInfo = saveHtml(info, user);
        String epub = createEpub(info, htmlInfo, user);
        delAndMoveFile(htmlInfo, info);
        return epub;
    }


    /**
     * 用于删除分离出来的pdf、保存用户上传的pdf和删除转换过程中的html文件
     *
     * @param htmlInfo html信息
     * @param pdfInfo  pdf信息
     * @throws IOException
     */
    public void delAndMoveFile(HtmlInfo htmlInfo, PdfInfo pdfInfo) throws IOException {
        //首先获取pdf的分隔文件夹，删除下面的所有文件夹
        String splitPath = pdfInfo.getSplitPath();
        File oldFile = new File(splitPath);
        File[] files = oldFile.listFiles();
        //for (File file : files) {
        //    file.delete();
        //}
        //获取保存html的文件夹，删除下面的所有文件夹,与上面同理
        String savePath = htmlInfo.getSavePath();
        File file = new File(savePath);
        File[] files1 = file.listFiles();
        //for (File file1 : files1) {
        //    while (file1.exists()) {
        //        System.gc();
        //        file1.delete();
        //    }
        //}
        //得到用户上传的pdf，并且保存将分隔pdf的文件夹作为目标文件夹
        File pdfFile = new File(pdfInfo.getSavePath());

        byte[] buf = new byte[8192];
        int bytesRead;
        OutputStream outputStream = null;
        InputStream inputStream = new FileInputStream(pdfFile);
        outputStream = new FileOutputStream(splitPath + "/" + pdfInfo.getPdfName());
        while ((bytesRead = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        pdfFile.delete();

    }
}


