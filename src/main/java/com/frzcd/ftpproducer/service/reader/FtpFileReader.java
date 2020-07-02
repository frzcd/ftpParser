package com.frzcd.ftpproducer.service.reader;

import com.frzcd.ftpproducer.dao.LastUpdateDao;
import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.domain.MyXmlFile;
import com.frzcd.ftpproducer.service.sender.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Service
public class FtpFileReader implements Reader {
    @Autowired
    LastUpdateDao lastUpdateDao;
    @Autowired
    Sender sender;

    public FtpFileReader() {
        log.info(FTP_FILE_READER_INFO_13);
    }

    public MyFile readFile(FTPFile file, FTPClient ftp) {
        String workingDirectory;

        try {
           workingDirectory = ftp.printWorkingDirectory();
        } catch (IOException e) {
            log.error("radFile method exception w printWorkingDir");

            return null;
        }

        String fileFullName = workingDirectory + "/" + file.getName();

        log.info(FTP_FILE_READER_INFO_14, fileFullName);

        List<MyXmlFile> xmlFileList = readFileArchive(file, ftp);

        System.out.println(xmlFileList.size());

        try {
            if (!ftp.completePendingCommand()) {
                log.error("PENDING COMAND RRRr");
            }
        } catch (IOException e) {
            log.error("Complete pending command exception");
        }

        log.info(FTP_FILE_READER_INFO_15, fileFullName);

        return new MyFile(fileFullName, String.valueOf(file.getTimestamp().getTimeInMillis()), xmlFileList);
    }

    public List<MyXmlFile> readFileArchive(FTPFile file, FTPClient ftp) {
        log.info("begin reading archive");
        String workingDirectory;

        List<MyXmlFile> xmlFileList = new ArrayList<>();

        try {
            workingDirectory = ftp.printWorkingDirectory();
        } catch (IOException e) {
            log.error("ARCH radFile method exception w printWorkingDir");

            return null;
        }

        log.warn("ARCH readArchive workingDir is {}", workingDirectory);

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
                    log.error("Not XML file, but expected XML");
                    continue;
                }

                byte[] byteStr = IOUtils.toByteArray(archiveInputStream);
                String data = new String(byteStr);

                MyXmlFile xmlFile = new MyXmlFile();
                xmlFile.setFileName(entryName);
                xmlFile.setData(data);

                xmlFileList.add(xmlFile);
            }

            System.out.println("done");

        } catch (Exception e) {
            log.warn("ARCH EX");
            e.printStackTrace();
        }

        log.info("{} xml files read from archive", xmlFileList.size());

        return xmlFileList;
    }

    //converts InputStream to Bytes
    private Bytes readBytes(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead = -1;
        byte[] data = new byte[1000000];

        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        } catch (IOException e) {
            log.error("Exception in readBytes method");
            return null;
        }

        return new Bytes(buffer.toByteArray());
    }

    private void createNewFile(File filename) {
        try {
            filename.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValidEntryName(String entryName) {
        if (entryName.contains("/")) {
            return entryName.split("/")[entryName.split("/").length - 1];
        }

        return entryName;
    }
}
