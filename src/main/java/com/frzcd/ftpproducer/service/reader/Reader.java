package com.frzcd.ftpproducer.service.reader;

import com.frzcd.ftpproducer.domain.MyFile;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Map;

public interface Reader {
    public MyFile readFile(FTPFile file, FTPClient ftp) throws IOException;
}
