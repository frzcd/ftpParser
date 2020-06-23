package com.frzcd.ftpproducer.dao;

import com.frzcd.ftpproducer.domain.MyFile;

public interface FTPFileDao {
    public boolean contains(String fileInfo);

    public boolean add(MyFile myFile);
}
