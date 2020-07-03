package com.frzcd.ftpproducer.service.parser;

import com.frzcd.ftpproducer.config.properties.FtpProperties;
import com.frzcd.ftpproducer.dao.FTPFileDao;
import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.domain.MyXmlFile;
import com.frzcd.ftpproducer.service.lastUpdateChecker.LastUpdateChecker;
import com.frzcd.ftpproducer.service.reader.Reader;
import com.frzcd.ftpproducer.service.sender.Sender;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Service
public class FtpFileParser implements Parser {
    private final Sender sender;
    private final Reader reader;
    private final FTPClient ftp;
    private final LastUpdateChecker lastUpdateChecker;
    private final FTPFileDao dao;
    private final String[] directories;

    public FtpFileParser(Sender sender,
                         Reader reader,
                         FTPClient ftp,
                         FTPFileDao dao,
                         LastUpdateChecker lastUpdateChecker,
                         FtpProperties ftpProperties) {
        this.sender = sender;
        this.reader = reader;
        this.ftp = ftp;
        this.dao = dao;
        this.lastUpdateChecker = lastUpdateChecker;
        this.directories = ftpProperties.getDirectories();

        log.info(FTP_FILE_PARSER_INFO_9);
    }

    @SneakyThrows
    @Override
    public void run() {
        for (String startingDirectory : directories) {
            log.info(FTP_FILE_PARSER_INFO_10, startingDirectory);
            scanDirectory(startingDirectory);
        }
        log.info(FTP_FILE_PARSER_INFO_19);
    }

    private synchronized void scanDirectory(String directoryToScan) throws IOException {
        log.info(FTP_FILE_PARSER_INFO_11, directoryToScan);

        ftp.changeWorkingDirectory(directoryToScan);

        FTPFile[] files = ftp.listFiles();

        if (files.length > 0) {
            for (FTPFile file : files) {

                ftp.changeWorkingDirectory(directoryToScan);

                if (!lastUpdateChecker.isFtpFileUpdateTimeValid(file)) {
                    continue;
                }

                if (file.isFile()) {
                    String fileFullName = ftp.printWorkingDirectory() + "/" + file.getName();
                    String fileInfo = fileFullName + " " + file.getTimestamp().getTimeInMillis();

                    if (!checkIsFileAlreadyInDao(fileInfo)) {
                        MyFile incomeFile = reader.readFile(file, ftp);

                        if (incomeFile == null) {
                            log.error(FTP_FILE_PARSER_ERROR_19);
                            continue;
                        }

                        log.info(FTP_FILE_PARSER_INFO_24, incomeFile.getFileInfo(), incomeFile.getXmlFilesList().size());

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
