package com.zjw.pdf_epub_provider.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作Html文件的工具类
 * @author Zzr
 */
@Slf4j
public class HtmlUtil {
    /**
     *  将单个Html标准化去除其中水印以及添加上自结束标签以便Epub转换可以识别
     *  @param htmlFilePath 需要转换Html的路径
     */
    public static void updateHtml(String htmlFilePath) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(htmlFilePath));
            StringBuilder sb = new StringBuilder();
            String str = "";
            //自结束标签缺失，给他补全
            while ((str = in.readLine()) != null) {
                if(str.equals("<meta charset=\"utf-8\"><title></title>")){
                    str = "<meta charset=\"utf-8\"/><title></title>";
                }
                sb.append(str);
            }

            str = sb.toString();

            Matcher m = Pattern.compile("width=\"(\\d*)\" height=\"(\\d*)\"").matcher(str);
            if (m.find()) {
                str = str.replaceAll(m.group(),"preserveAspectRatio=\"xMinYMin meet\"");
            }
            str = str.replaceAll("<svg","<svg viewbox=\"0 0 800 1130\"");
            str = str.replaceAll("Evaluation Warning : The document was created with Spire.PDF for java.","");
            str = str.replaceAll("Evaluation Warning : The document was created with Spire.PDF for Java.","");
            File file = new File(htmlFilePath);
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
