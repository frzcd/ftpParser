package com.frzcd.ftpproducer.dao;

public interface LastUpdateDao {

    public long getLastUpdateMillis();

    public void setLastUpdateMillis(long lastUpdateMillis);

    public long getDefaultLastUpdateMillis();
}
