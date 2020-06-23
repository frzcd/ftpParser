package com.frzcd.ftpproducer.service.parser;

import com.frzcd.ftpproducer.dao.FTPFileDao;
import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.service.reader.Reader;
import com.frzcd.ftpproducer.service.sender.Sender;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Service
public class FtpFileParser implements Parser {
    private Sender sender;
    private Reader reader;
    private FTPClient ftp;

    private FTPFileDao dao;
    private String[] directories;

    public FtpFileParser(Sender sender,
                         Reader reader,
                         FTPClient ftp,
                         FTPFileDao dao,
                         @Value("${ftp.directories}")String[] directories) {
        this.sender = sender;
        this.reader = reader;
        this.ftp = ftp;
        this.dao = dao;
        this.directories = directories;

        log.info(FTP_FILE_PARSER_INFO_9);
    }

    @SneakyThrows
    @Override
    public void run() {
        for (String startingDirectory : directories) {
            log.info(FTP_FILE_PARSER_INFO_10, startingDirectory);

            try {
                scanDirectory(ftp.printWorkingDirectory());
            } catch (IOException e) {
                log.error(FTP_FILE_PARSER_ERROR_9, startingDirectory);
                e.printStackTrace();
            }
        }
        log.info(FTP_FILE_PARSER_INFO_19);
    }

    private void scanDirectory(String directoryToScan) throws IOException {
        log.info(FTP_FILE_PARSER_INFO_11, directoryToScan);
        ftp.changeWorkingDirectory(directoryToScan);

        FTPFile[] files = ftp.listFiles();

        if (files.length > 0) {
            for (FTPFile file : files) {

                if (file.isFile()) {
                    String fileFullName = ftp.printWorkingDirectory() + "/" + file.getName();
                    String fileInfo = fileFullName + " " + file.getTimestamp().getTimeInMillis();

                    if (!checkIsFileAlreadyInDao(fileInfo)) {
                        MyFile incomeFile = reader.readFile(file, ftp);

                        if (incomeFile == null) {
                            continue;
                        }

                        sender.send(incomeFile);
                        dao.add(incomeFile);
                    } else {
                        log.info(FTP_FILE_PARSER_INFO_12, fileInfo);
                    }

                } else {
                    scanDirectory(ftp.printWorkingDirectory() + "/" +  file.getName());
                }
            }
        }
    }

    private boolean checkIsFileAlreadyInDao(String fileInfo) {
        return dao.contains(fileInfo);
    }
}
