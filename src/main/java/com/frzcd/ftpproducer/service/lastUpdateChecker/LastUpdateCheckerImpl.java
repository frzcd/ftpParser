package com.frzcd.ftpproducer.service.lastUpdateChecker;

import com.frzcd.ftpproducer.dao.LastUpdateDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static com.frzcd.ftpproducer.utils.LogMessages.LAST_UPDATE_CHECKER_IMPL_21;

@Slf4j
@Service
public class LastUpdateCheckerImpl implements LastUpdateChecker {
    private final LastUpdateDao lastUpdateDao;
    private long lastUpdate;

    @Autowired
    public LastUpdateCheckerImpl(LastUpdateDao lastUpdateDao) {
        this.lastUpdateDao = lastUpdateDao;
        lastUpdate = lastUpdateDao.getLastUpdateMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastUpdate);
        log.info(LAST_UPDATE_CHECKER_IMPL_21, calendar.getTime());
    }

    public boolean isFtpFileUpdateTimeValid(FTPFile file) {
        return lastUpdate < file.getTimestamp().getTimeInMillis();
    }

    public void setNewLastUpdateDate(long timeMillis) {
        lastUpdate = timeMillis;
        lastUpdateDao.setLastUpdateMillis(timeMillis);
    }
}
