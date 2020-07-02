package com.frzcd.ftpproducer;

import com.frzcd.ftpproducer.dao.LastUpdateDaoImpl;
import com.frzcd.ftpproducer.service.lastUpdateChecker.LastUpdateCheckerImpl;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;

@SpringBootTest
public class LastUpdateCheckerImplTest {

    @Autowired
    LastUpdateDaoImpl lastUpdateDaoImpl;

    @Autowired
    LastUpdateCheckerImpl lastUpdateChecker;

    @Test
    void isFtpFileUpdateTimeValidTest() {
        long millis = lastUpdateDaoImpl.getLastUpdateMillis();

        long before = millis - 100L;
        FTPFile fileBefore = new FTPFile();
        Calendar calendarBefore = Calendar.getInstance();
        calendarBefore.setTimeInMillis(before);
        fileBefore.setTimestamp(calendarBefore);

        assertFalse(lastUpdateChecker.isFtpFileUpdateTimeValid(fileBefore));

        long after = millis + 100L;
        FTPFile fileAfter = new FTPFile();
        Calendar calendarAfter = Calendar.getInstance();
        calendarAfter.setTimeInMillis(after);
        fileAfter.setTimestamp(calendarAfter);

        assertTrue(lastUpdateChecker.isFtpFileUpdateTimeValid(fileAfter));

        FTPFile fileEquals = new FTPFile();
        Calendar calenderEquals = Calendar.getInstance();
        calenderEquals.setTimeInMillis(millis);
        fileEquals.setTimestamp(calenderEquals);

        assertFalse(lastUpdateChecker.isFtpFileUpdateTimeValid(fileEquals));
    }
}
