package com.frzcd.ftpproducer;

import com.frzcd.ftpproducer.dao.FTPFileDao;
import com.frzcd.ftpproducer.dao.FTPFileDaoImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FtpFileDaoImplTest {
    FTPFileDao ftpFileDao = new FTPFileDaoImpl("src/main/resources/test/testDao.txt");

    @Test
    void containsTestTrue() {
        assertTrue(ftpFileDao.contains("filename"));
    }

    @Test
    void containsTestFalse() {
        assertFalse(ftpFileDao.contains("no such string"));
    }
}
