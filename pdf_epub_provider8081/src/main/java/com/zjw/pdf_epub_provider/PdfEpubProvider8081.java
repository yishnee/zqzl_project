package com.zjw.pdf_epub_provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@Slf4j
@SpringBootApplication
public class PdfEpubProvider8081 {

    public static void main(String[] args) {
        log.info("********* 服务启动！*********");
        SpringApplication.run(PdfEpubProvider8081.class, args);
    }

}
