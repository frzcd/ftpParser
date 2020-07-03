package com.frzcd.ftpproducer.dao;

import com.frzcd.ftpproducer.config.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Repository
public class LastUpdateDaoImpl implements LastUpdateDao {

    private final String lastUpdateFileName;

    @Autowired
    public LastUpdateDaoImpl(AppProperties appProperties) {
        this.lastUpdateFileName = appProperties.getLastUpdateFile();

        if(!new File(lastUpdateFileName).exists()) {
            log.info(LAST_UPDATE_DAO_IMPL_INFO_23, lastUpdateFileName);

            try {
                Files.createFile(Paths.get(lastUpdateFileName));
            } catch (IOException e) {
                log.error(LAST_UPDATE_DAO_IMPL_ERROR_16);
                e.printStackTrace();
                //throw new MyCannotCreateLastUpdateFileException();
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
            log.error(LAST_UPDATE_DAO_IMPL_ERROR_17);
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
            log.error(LAST_UPDATE_DAO_IMPL_ERROR_18);
            e.printStackTrace();
        }
    }

    @Override
    public long getDefaultLastUpdateMillis() {
        return 633879479777L;
    }
}
