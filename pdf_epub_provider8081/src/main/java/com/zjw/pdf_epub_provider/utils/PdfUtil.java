package com.zjw.pdf_epub_provider.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Pdf操作工具类
 *
 * @author
 */
@Slf4j

public class PdfUtil {

    public static String[] getBookTitle() {
        return bookTitle;
    }

    public static int[] getPageNumber() {
        return pageNumber;
    }

    public static int getStrindex() {
        return strindex;
    }

    //目录索引
    public static int strindex = 0;
    //目录的标题
    public static String[] bookTitle = new String[10000];
    //目录的页码
    public static int[] pageNumber = new int[10000];

    /**
     * 用于按页拆分pdf为一系列pdf集合
     *
     * @param pdfFilePath    需要转换的pdf文件的路径
     * @param targetFilePath 用于保存分离出来的pdf文件集合的路径
     */
    public static void splitPdfs(String pdfFilePath, String targetFilePath) throws IOException {
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile(pdfFilePath);

        //TODO 文件读取配置

        //读取pdf相关信息
        //PDFReader.getPDFCatalog(pdfFilePath);

        // pdf详细信息
        //PDFReader.getPDFInformation(pdfFilePath);

        //目录文件打印
        PDFReader.getPDFOutline(pdfFilePath);

        //style 文件打印
        //PDFReader.getFontName(pdfFilePath, targetFilePath);


        // 读取目录和对应页码
        PdfReader reader = new PdfReader(pdfFilePath);
        log.info("获取标题和对应页码中...");
        List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);
        for (Iterator<HashMap<String, Object>> i = list.iterator(); i.hasNext(); ) {
            showBookmark(i.next());
        }
        strindex = 0;
        for (Iterator<HashMap<String, Object>> i = list.iterator(); i.hasNext(); ) {
            getPageNumbers(i.next());
        }

        log.info("分离原pdf页面中...");
        //拆分为多个PDF文档
        doc.split(targetFilePath + "/{0}.pdf", 0);


    }

    /**
     * 将pdf文件集合转换为Html文件集合
     *
     * @param pdfFilePath    需要转换的pdf文件集合的路径
     * @param targetFilePath 保存的html文件集合的路径
     * @throws FileNotFoundException 可能会抛出的异常（找不到文件）
     */
    public static void toHtmls(String pdfFilePath, String targetFilePath) throws IOException {
        log.info("pdf页面转化为html页面中...");
        File file = new File(pdfFilePath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(files[i].getAbsolutePath());
            //TODO
            //System.out.println("***2***"+pdf.getDocumentInformation().getTitle());
            pdf.getConvertOptions().setPdfToHtmlOptions(true, true);
            File outFile = new File(targetFilePath + "/" + files[i].getName().substring(0, files[i].getName().indexOf(".")) + ".html");
            OutputStream outputStream = new FileOutputStream(outFile);
            pdf.saveToStream(outputStream, FileFormat.HTML);
            pdf.close();

        }
    }


    /**
     * 获取标题作为目录
     * @param bookmark PDF路径
     */
    private static void showBookmark(HashMap<String, Object> bookmark) {

        //System.out.println (bookmark.get ( "Title" )) ;
        bookTitle[strindex++] = bookmark.get("Title").toString();
        @SuppressWarnings("unchecked")
        ArrayList<HashMap<String, Object>> kids = (ArrayList<HashMap<String, Object>>) bookmark.get("Kids");
        if (kids == null) {
            return;
        }
        for (Iterator<HashMap<String, Object>> i = kids.iterator(); i.hasNext(); ) {

            showBookmark(i.next());
        }
    }


    /**
     * 获取标题的对应页码
     * @param bookmark PDF路径
     */
    public static void getPageNumbers(HashMap<String, Object> bookmark) {
        if (bookmark == null) {
            return;
        }

        if ("GoTo".equals(bookmark.get("Action"))) {

            String page = (String) bookmark.get("Page");
            if (page != null) {

                page = page.trim();

                int idx = page.indexOf(' ');

                int pageNum;

                if (idx < 0) {

                    pageNum = Integer.parseInt(page);
                    //System.out.println ("pageNum :"+ pageNum) ;
                    pageNumber[strindex++] = pageNum-1;
                } else {

                    pageNum = Integer.parseInt(page.substring(0, idx));
                    //System.out.println ("pageNum:" +pageNum) ;
                    pageNumber[strindex++] = pageNum-1;
                }
            }
            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, Object>> kids = (ArrayList<HashMap<String, Object>>) bookmark.get("Kids");
            if (kids == null) {
                return;
            }
            for (Iterator<HashMap<String, Object>> i = kids.iterator(); i.hasNext(); ) {

                getPageNumbers(i.next());
            }

        }
    }


}
