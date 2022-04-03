package com.zjw.pdf_epub_provider.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubWriter;

import java.io.*;
import java.util.*;

import static com.zjw.pdf_epub_provider.utils.HtmlUtil.updateHtml;
import static com.zjw.pdf_epub_provider.utils.TxtUtil.updateTxt;

/**
 * Epub操作工具类
 *
 * @author
 */
@Slf4j
public class EpubUtil {

    /**
     * 根据html集合的路径创建单个epub文件
     *
     * @param filePath   存放html文件集合的路径
     * @param targetFile 用于存放epub文件的路径
     */
    public static void toEpub(String filePath, String targetFile) {
        try {
            String[] bookTitle = PdfUtil.getBookTitle();
            int[] pageNumber = PdfUtil.getPageNumber();
            int strindex = PdfUtil.getStrindex();

            File file = new File(filePath);
            File[] files = file.listFiles();
            Book book = new Book();
            ArrayList<String> fileNameArr = new ArrayList<String>();
            ArrayList<Integer> middle = new ArrayList<Integer>();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".html")) {
                    middle.add(Integer.parseInt(files[i].getName().substring(0, files[i].getName().lastIndexOf("."))));
                }
            }
            Collections.sort(middle);
            for (Integer integer : middle) {
                fileNameArr.add(filePath + "/" + integer.toString() + ".html");
            }

            for (String s : fileNameArr) {
                //去除Html文件里面的水印
                updateHtml(s);

            }

           /* // Set the title
            book.getMetadata().addTitle("Epublib test book 1");

            // Add an Author
            book.getMetadata().addAuthor(new Author("Joe", "Tester"));

            // Add Chapter 1
            book.addSection("Introduction", new Resource(Simple1.class.getResourceAsStream("/book1/chapter1.html"), "chapter1.html"));

            // Add css file
            book.getResources().add(new Resource(Simple1.class.getResourceAsStream("/book1/book1.css"), "book1.css"));

            // Add Chapter 2
            TOCReference chapter2 = book.addSection("Second Chapter", new Resource(Simple1.class.getResourceAsStream("/book1/chapter2.html"), "chapter2.html"));

            // Add image used by Chapter 2
            book.getResources().add(new Resource(Simple1.class.getResourceAsStream("/book1/flowers_320x240.jpg"), "flowers.jpg"));

            // Add Chapter2, Section 1
            book.addSection(chapter2, "Chapter 2, section 1", new Resource(Simple1.class.getResourceAsStream("/book1/chapter2_1.html"), "chapter2_1.html"));

            // Add Chapter 3
            book.addSection("Conclusion", new Resource(Simple1.class.getResourceAsStream("/book1/chapter3.html"), "chapter3.html"));

            // Create EpubWriter
            EpubWriter epubWriter = new EpubWriter();

            // Write the Book as Epub
            epubWriter.write(book, new FileOutputStream("test1_book1.epub"));*/


            // Create EpubWriter
            /*EpubWriter epubWriter = new EpubWriter();*/

            /*for(int i=0;i<strindex;i++){//自己写的
                System.out.println(bookTitle[i]);
                System.out.println("pageNum :"+pageNumber[i]);
            }

            for (int i = 0, j = 0; i < fileNameArr.size(); i++, j++) {



                String fileName = fileNameArr.get(i);
                String pageNum = "Page:" + i;
                String pageName = fileName.substring(fileName.lastIndexOf("\\"));
                Resource rs = new Resource(new FileInputStream(fileName), pageName);
                TOCReference chapter2 = book.addSection(pageNum, rs);
                book.addSection(chapter2, pageNum + "_" + j, rs);
            }
*/
            EpubWriter epubWriter = new EpubWriter();
            for (int i = 0; i < fileNameArr.size(); i++) {
                String fileName = fileNameArr.get(i);
                //System.out.println(fileName);
                String pageNum = "Page:" + i;
                String pageName = fileName.substring(fileName.lastIndexOf("/")+1);
                //System.out.println(pageName);
                int flag=0,first=-1;
                for(int j=0;j<strindex;j++) {
                    if(pageNumber[j]==i){
                        flag++;
                        if(first==-1) {
                            first=j;
                        }
                    }
                }
                if(flag==0){
                    book.addSection(pageNum + "_" + i, new Resource(new FileInputStream(
                            fileName), pageName));
                }
                else if(flag==1){
                    book.addSection(bookTitle[first], new Resource(new FileInputStream(
                            fileName), pageName));
                }
                else{
                    Resource rs = new Resource(new FileInputStream(fileName), pageName);
                    TOCReference chapter=book.addSection(bookTitle[first], rs);
                    for(int j=1;j<flag;j++){
                        book.addSection(chapter, bookTitle[first+j], rs);
                    }
                }

            }

            // Write the Book as Epub
            epubWriter.write(book, new FileOutputStream(targetFile));
            log.info("epub生成完毕...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
