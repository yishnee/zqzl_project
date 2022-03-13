package com.zjw.pdf_epub_provider.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author yishnee
 * @version 1.0
 * txt的替换功能
 */
@Slf4j
public class TxtUtil {
    public static void updateTxt(String txtFilePath) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(txtFilePath));
            StringBuilder sb = new StringBuilder();
            String str;
            str = sb.toString();
            str = str.replaceAll("Evaluation Warning : The document was created with Spire.PDF for java.", "");
            str = str.replaceAll("Evaluation Warning : The document was created with Spire.PDF for Java.", "");
            File file = new File(txtFilePath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes());
            fos.close();
            in.close();
        } catch (FileNotFoundException e) {
            log.error("文件不存在，或者该文件无法被读取！");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
