package com.zjw.pdf_epub_provider.dao;

import com.zjw.pdf_epub_provider.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer> {
    public User findByUsername(String name);
}
