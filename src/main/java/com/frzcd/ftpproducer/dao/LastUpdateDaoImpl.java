package com.frzcd.ftpproducer.dao;

import com.frzcd.ftpproducer.exceptions.MyCannotCreateLastUpdateFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@Slf4j
@Repository
public class LastUpdateDaoImpl implements LastUpdateDao {

    String lastUpdateFileName;

    public LastUpdateDaoImpl(@Value("${lastUpdate-file}")String lastUpdateFileName) {
        this.lastUpdateFileName = lastUpdateFileName;

        if(!new File(lastUpdateFileName).exists()) {
            log.info("LastUpdateFile {} is not exists. Creating new File.", lastUpdateFileName);

            try {
                Files.createFile(Paths.get(lastUpdateFileName));
            } catch (IOException e) {
                e.printStackTrace();
                throw new MyCannotCreateLastUpdateFileException();
            }

        }
    }

    @Override
    public long getLastUpdateMillis() {
        long lastUpdate = getDefaultLastUpdateMillis();

        try {
            Scanner scanner = new Scanner(new FileInputStream(lastUpdateFileName));

            if (scanner.hasNext()) {
                lastUpdate = Long.parseLong(scanner.nextLine());
            } else {
                lastUpdate = getDefaultLastUpdateMillis();
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            log.error("Last update save file is not found");
            e.printStackTrace();
        }

        return lastUpdate;
    }

    @Override
    public void setLastUpdateMillis(long lastUpdateMillis) {

        try {
            FileWriter fileWriter = new FileWriter(lastUpdateFileName);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(String.valueOf(lastUpdateMillis));
            bufferedWriter.flush();
        } catch (IOException e) {
            log.error("date is not saved");
            e.printStackTrace();
        }
    }

    @Override
    public long getDefaultLastUpdateMillis() {
        return 633879479777L;
    }
}
