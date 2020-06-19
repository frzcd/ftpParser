package com.frzcd.ftpproducer.parser;

import com.frzcd.ftpproducer.dao.FTPFileDao;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FtpParser {
    private FTPFileDao dao;
    private KafkaTemplate<String, Bytes> kafkaTemplateBytes;
    private FTPClient ftp;

    @Autowired
    public FtpParser(KafkaTemplate<String, Bytes> kafkaTemplateBytes,
                     FTPClient ftp,
                     FTPFileDao dao,
                     @Value("${ftp.startingDirectory}")String startingWorkingDirectory) throws IOException {
        this.kafkaTemplateBytes = kafkaTemplateBytes;
        this.ftp = ftp;
        this.dao = dao;
        ftp.changeWorkingDirectory(startingWorkingDirectory);

        start();

    }

    public void start() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                scanDirectory(ftp.printWorkingDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //Scans current directory. calls proceedFile() for files, calls scanDirectory() recursively for directories
    private void scanDirectory(String directoryToScan) throws IOException {
        ftp.changeWorkingDirectory(directoryToScan);
        System.out.println("Scanning directory: " + ftp.printWorkingDirectory());
        FTPFile[] files = ftp.listFiles();

        if (files.length > 0) {
            for (FTPFile file : files) {
                if (file.isFile()) {

                    processFile(file, ftp);

                } else {
                    System.out.println(ftp.printWorkingDirectory() + "/" +  file.getName());
                    scanDirectory(ftp.printWorkingDirectory() + "/" +  file.getName());
                }
            }
        }
    }

    //Checks isn't file in dao. Sends FTPFile to kafka as Bytes. Copies filInfo to dao.
    private void processFile(FTPFile file, FTPClient ftp) throws IOException {
        String fileFullName = ftp.printWorkingDirectory() + "/" + file.getName();
        String fileInfo = fileFullName + " " + file.getTimestamp().getTimeInMillis();

        if (dao.contains(fileInfo)) {
            return;
        }

        System.out.println("sending file " + fileFullName);
        System.out.println("File size: " + file.getSize());

        InputStream is = ftp.retrieveFileStream(fileFullName);
        if (is == null) {
            System.out.println("could not retrieve file: " + fileFullName);
            return;
        }
        Bytes data = readBytes(is);
        System.out.println("Bytes size: " + data.get().length);

        ftp.completePendingCommand();

        ListenableFuture<SendResult<String, Bytes>> future =
                kafkaTemplateBytes.send("test", fileFullName, data);

        dao.add(fileInfo + "\n");
    }

    //converts InputStream to Bytes
    private Bytes readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return new Bytes(buffer.toByteArray());
    }
}
