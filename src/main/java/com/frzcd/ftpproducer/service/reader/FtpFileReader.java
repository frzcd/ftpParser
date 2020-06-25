package com.frzcd.ftpproducer.service.reader;

import com.frzcd.ftpproducer.domain.MyFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Service
public class FtpFileReader implements Reader {

    public FtpFileReader() {
        log.info(FTP_FILE_READER_INFO_13);
    }

    public MyFile readFile(FTPFile file, FTPClient ftp) throws IOException {
        String fileFullName = ftp.printWorkingDirectory() + "/" + file.getName();

        log.info(FTP_FILE_READER_INFO_14, fileFullName);

        InputStream is = ftp.retrieveFileStream(fileFullName);

        if (is == null) {
            log.error(FTP_FILE_READER_ERROR_7, fileFullName);
            return null;
        }

        Bytes data = readBytes(is);

        ftp.completePendingCommand();

        log.info(FTP_FILE_READER_INFO_15, fileFullName);

        return new MyFile(fileFullName, String.valueOf(file.getTimestamp().getTimeInMillis()), data);
    }

    //converts InputStream to Bytes
    private Bytes readBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1000000];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return new Bytes(buffer.toByteArray());
    }
}
