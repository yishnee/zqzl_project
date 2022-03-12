package com.zjw.pdf_epub_provider.dao;

import com.zjw.pdf_epub_provider.entity.EpubInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpubDao extends JpaRepository<EpubInfo,Integer> {
}
