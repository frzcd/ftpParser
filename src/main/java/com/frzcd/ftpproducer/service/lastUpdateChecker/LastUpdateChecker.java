package com.frzcd.ftpproducer.service.lastUpdateChecker;

import org.apache.commons.net.ftp.FTPFile;

public interface LastUpdateChecker {
    public boolean isFtpFileUpdateTimeValid(FTPFile file);

    public void setNewLastUpdateDate(long timeMillis);
}
