package com.zjw.pdf_epub_provider.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "epub")
public class EpubInfo implements Serializable {
    //epub文件名
    private String epubName;
    //epub文件大小
    private Long size;
    //epub文件保存时间
    private String saveTime;
    //epub文件保存路径
    @Id
    private String savePath;
    //与之关联的用户Id
    private Integer userId;
}
