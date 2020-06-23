package com.frzcd.ftpproducer.dao;

import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.exceptions.MyCannotCreateSaveFileException;
import com.frzcd.ftpproducer.exceptions.MyCannotOpenSaveFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Repository
public class FTPFileDaoImpl implements FTPFileDao {
    Set<String> proceedFiles;
    @Value("${save-file}")
    String saveFileName;

    public FTPFileDaoImpl(@Value("${save-file}")String saveFileName) {
        log.info(FTP_FILE_DAO_IMPL_INFO_5, saveFileName);
        File saveFile = new File(saveFileName);
        proceedFiles = new HashSet<>();

        if (!saveFile.exists()) {

            try {
                saveFile.createNewFile();
                log.info(FTP_FILE_DAO_IMPL_INFO_6, saveFileName);
            } catch (IOException e) {
                log.error(FTP_FILE_DAO_IMPL_ERROR_4);
                e.printStackTrace();
                throw new MyCannotCreateSaveFileException();
            }

        } else {

            try (Scanner scanner = new Scanner(new FileInputStream(saveFile))) {

                while (scanner.hasNext()) {
                    proceedFiles.add(scanner.nextLine().trim());
                }

            } catch (FileNotFoundException e) {
                log.info(FTP_FILE_DAO_IMPL_ERROR_5, saveFileName);
                e.printStackTrace();
                throw new MyCannotOpenSaveFileException();
            }
        }

        log.info(FTP_FILE_DAO_IMPL_INFO_7, proceedFiles.size());
    }

    @Override
    public boolean contains(String fileInfo) {

        return proceedFiles.contains(fileInfo);
    }

    @Override
    public boolean add(MyFile myFile) {
        saveToFile(myFile.getFileInfo());

        return proceedFiles.add(myFile.getFileInfo());
    }

    private void saveToFile(String fileInfo) {
        try {
            FileWriter fileWriter = new FileWriter(saveFileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(fileInfo + "\n");
            bufferedWriter.flush();

            log.info(FTP_FILE_DAO_IMPL_INFO_8, fileInfo, saveFileName);
        } catch (IOException e) {
            log.error(FTP_FILE_DAO_IMPL_ERROR_6, fileInfo, saveFileName);
            e.printStackTrace();
        }
    }
}
