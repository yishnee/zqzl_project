package com.zjw.pdf_epub_provider.service;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
public interface SingleEpubService {


    public String pdfToEpub_Single(MultipartFile uploadFile, String username) throws IOException;


}
