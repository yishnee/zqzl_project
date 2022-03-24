package com.zjw.pdf_epub_provider.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 用于测试打印catalog.txt中的文件内容
 * 目前无用
 * @author yishnee
 * @version 1.0
 */
@Slf4j
public class readTxtFile {
    public static void readTxtFile(String filePath){
        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    System.out.println(lineTxt);
                }
                read.close();
            }else{
                //System.out.println("找不到指定的文件");
                log.error("readTxtFile: 找不到指定的文件");
            }
        } catch (Exception e) {
            //System.out.println("读取文件内容出错");
            log.error("readTxtFile: 读取文件内容出错");
            e.printStackTrace();
        }

    }

    public static void main(String argv[]){
        String filePath = "catalog.txt";
//      "res/";
        readTxtFile(filePath);
    }

}
