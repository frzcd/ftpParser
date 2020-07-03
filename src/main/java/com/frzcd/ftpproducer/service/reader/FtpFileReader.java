package com.frzcd.ftpproducer.service.reader;

import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.domain.MyXmlFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Service
public class FtpFileReader implements Reader {

    public FtpFileReader() {
        log.info(FTP_FILE_READER_INFO_13);
    }

    public MyFile readFile(FTPFile file, FTPClient ftp) {
        String workingDirectory;

        try {
           workingDirectory = ftp.printWorkingDirectory();
        } catch (IOException e) {
            log.error(FTP_FILE_READER_ERROR_14);

            return null;
        }

        String fileFullName = workingDirectory + "/" + file.getName();

        log.info(FTP_FILE_READER_INFO_14, fileFullName);

        List<MyXmlFile> xmlFileList = readFileArchive(file, ftp);

        try {
            ftp.completePendingCommand();
        } catch (IOException e) {
            log.error(FTP_FILE_READER_ERROR_11);
        }

        log.info(FTP_FILE_READER_INFO_15, fileFullName);

        return new MyFile(fileFullName, String.valueOf(file.getTimestamp().getTimeInMillis()), xmlFileList);
    }

    public List<MyXmlFile> readFileArchive(FTPFile file, FTPClient ftp) {
        String workingDirectory;

        List<MyXmlFile> xmlFileList = new ArrayList<>();

        try {
            workingDirectory = ftp.printWorkingDirectory();
        } catch (IOException e) {
            log.error(FTP_FILE_READER_ERROR_15);

            return null;
        }

        String fileFullName = workingDirectory + "/" + file.getName();

        try (InputStream fileInputStream = ftp.retrieveFileStream(fileFullName);
             InputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             InputStream gzi = new GzipCompressorInputStream(bufferedInputStream);
             ArchiveInputStream archiveInputStream = new TarArchiveInputStream(gzi);
            ) {

            ArchiveEntry archiveEntry;

            while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {

                if (archiveEntry.isDirectory()) {
                    continue;
                }

                String entryName = getValidEntryName(archiveEntry.getName());

                if (!entryName.endsWith(".xml")) {
                    log.error(FTP_FILE_READER_ERROR_12);
                    continue;
                }


                byte[] byteStr = IOUtils.toByteArray(archiveInputStream);
                String data = new String(byteStr);

                MyXmlFile xmlFile = new MyXmlFile(entryName, data);

                xmlFileList.add(xmlFile);
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    System.out.println("interrupt");
//                    e.printStackTrace();
//                }
            }

        } catch (IOException e) {
            log.error(FTP_FILE_READER_ERROR_13);
            e.printStackTrace();
        }

        log.info(FTP_FILE_READER_INFO_22, xmlFileList.size());

        return xmlFileList;
    }

    public String getValidEntryName(String entryName) {
        if (entryName.contains("/")) {
            return entryName.split("/")[entryName.split("/").length - 1];
        }

        return entryName;
    }
}
