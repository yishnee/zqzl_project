package com.zjw.pdf_epub_provider.utils;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Pdf操作工具类
 *
 * @author yishnee
 */
@Slf4j
public class PdfToTxtUtil {
    /**
     * 用于按页拆分pdf为一系列pdf集合
     *
     * @param pdfFilePath    需要转换的pdf文件的路径
     * @param targetFilePath 用于保存分离出来的pdf文件集合的路径
     */
    public static void splitPdfs(String pdfFilePath, String targetFilePath) {
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile(pdfFilePath);
        //拆分为多个PDF文档
        doc.split(targetFilePath + "/{0}.pdf", 0);
    }

    /**
     * 将pdf文件集合转换为Txt文件集合
     *
     * @param pdfFilePath    需要转换的pdf文件集合的路径
     * @param targetFilePath 保存的txt文件集合的路径
     * @throws FileNotFoundException 可能会抛出的异常（找不到文件）
     */
    public static void toTxt(String pdfFilePath, String targetFilePath) throws IOException {
        log.info("将pdf文件集合转换为Txt文件集合...");
        File file = new File(pdfFilePath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(files[i].getAbsolutePath());


            //创建StringBuilder实例
            StringBuilder sb = new StringBuilder();

            PdfPageBase page;
            //遍历PDF页面，获取每个页面的文本并添加到StringBuilder对象
            for (int j = 0; j < pdf.getPages().getCount(); j++) {
                page = pdf.getPages().get(j);
                sb.append(page.extractText(true));

                /**
                 * 获取文件内的图片并且保存到html的文件夹中
                 */
                int index=0;
                if (page.extractImages()!=null){
                    for (BufferedImage image : page.extractImages()) {
                        //指定输出文件路径及名称
                        //File output = new File("D:\\data\\test\\" + String.format("Image_%d_%d.png", i,index++));
                        File output = new File(targetFilePath+"\\" + String.format("Image_%d_%d.png", i,index));
                        //将图片保存为PNG格式文件
                        ImageIO.write(image, "PNG", output);
                        index=index+1;
                    }
                }
            }
            File TxtFile = new File(targetFilePath + "\\" + files[i].getName().substring(0, files[i].getName().indexOf(".")) + ".txt");
            if (!TxtFile.exists()) {
                try {
                    TxtFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writer;
            try {
                //将StringBuilder对象中的文本写入到文本文件
                writer = new FileWriter(TxtFile);
                writer.write(sb.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdf.close();
        }
    }

}
