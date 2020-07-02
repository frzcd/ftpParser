package com.frzcd.ftpproducer.service.reader;

import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.domain.MyXmlFile;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.List;

public interface Reader {
    public MyFile readFile(FTPFile file, FTPClient ftp) throws IOException;
    public List<MyXmlFile> readFileArchive(FTPFile file, FTPClient ftp);
}
