package com.zjw.pdf_epub_provider.entity;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @author yishnee
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PathTotal {
    private String EpubPath;
    private String HtmlPath;
    private String PdfPath;
    private String TxtPath;
}
