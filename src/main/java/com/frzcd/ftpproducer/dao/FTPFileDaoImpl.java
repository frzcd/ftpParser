package com.frzcd.ftpproducer.dao;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Repository
public class FTPFileDaoImpl implements FTPFileDao {
    Set<String> proceedFiles;
    @Value("${save-file}")
    String saveFileName;

    public FTPFileDaoImpl(@Value("${save-file}")String saveFileName) throws IOException {
        System.out.println(saveFileName);
        File saveFile = new File(saveFileName);
        proceedFiles = new HashSet<>();

        if (!saveFile.exists()) {
            saveFile.createNewFile();
        } else {
            Scanner scanner = new Scanner(new FileInputStream(saveFile));
            while (scanner.hasNext()) {
                proceedFiles.add(scanner.nextLine().trim());
            }
        }

        System.out.println("Set files: ");
        for (String string : proceedFiles) {
            System.out.println(string);
        }
        System.out.println("End of Set");
    }

    @Override
    public boolean contains(String string) {
        return proceedFiles.contains(string);
    }

    @Override
    public boolean add(String string) {
        saveToFile(string);
        return proceedFiles.add(string);
    }

    private void saveToFile(String string) {
        try {
            FileWriter fileWriter = new FileWriter(saveFileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(string);
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("IOException in DaoImpl");
        }
    }
}
