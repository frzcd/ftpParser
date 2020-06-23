package com.frzcd.ftpproducer.utils;

public class LogMessages {

    public static final String FTP_SERVER_CONFIG_INFO_1 = "[FTP-I001] [FTP_SERVER_CONFIG] Trying to connect to FTP";
    public static final String FTP_SERVER_CONFIG_INFO_2 = "[FTP-I002] [FTP_SERVER_CONFIG] FTP Server reply: {}";
    public static final String FTP_SERVER_CONFIG_INFO_3 = "[FTP-I003] [FTP_SERVER_CONFIG] FTP server connection established";
    public static final String KAFKA_PRODUCER_CONFIG_INFO_4 = "[FTP-I004] [KAFKA_PRODUCER_CONFIG] KafkaServer connection established";
    public static final String FTP_FILE_DAO_IMPL_INFO_5 = "[FTP-I005] [FTP_FILE_DAO_IMPL] Checking save file: {}";
    public static final String FTP_FILE_DAO_IMPL_INFO_6 = "[FTP-I006] [FTP_FILE_DAO_IMPL] Save file {} created";
    public static final String FTP_FILE_DAO_IMPL_INFO_7 = "[FTP-I007] [FTP_FILE_DAO_IMPL] Files already proceeded: {}";
    public static final String FTP_FILE_DAO_IMPL_INFO_8 = "[FTP-I008] [FTP_FILE_DAO_IMPL] File info {} saved to save file {}";
    public static final String FTP_FILE_PARSER_INFO_9 = "[FTP-I009] [FTP_FILE_PARSER] Ftp file parser created";
    public static final String FTP_FILE_PARSER_INFO_10 = "[FTP-I010] [FTP_FILE_PARSER] Parsing new starting directory {}";
    public static final String FTP_FILE_PARSER_INFO_11 = "[FTP-I011] [FTP_FILE_PARSER] Scanning directory {}";
    public static final String FTP_FILE_PARSER_INFO_12 = "[FTP-I012] [FTP_FILE_PARSER] File {} already in dao";
    public static final String FTP_FILE_READER_INFO_13 = "[FTP-I013] [FTP_FILE_READER] Ftp file reader created";
    public static final String FTP_FILE_READER_INFO_14 = "[FTP-I014] [FTP_FILE_READER] Reading file {}";
    public static final String FTP_FILE_READER_INFO_15 = "[FTP-I015] [FTP_FILE_READER] File {} successfully read";
    public static final String FTP_FILE_SENDER_INFO_16 = "[FTP-I016] [FTP_FILE_SENDER] Ftp file sender created";
    public static final String FTP_FILE_SENDER_INFO_17 = "[FTP-I017] [FTP_FILE_SENDER] Trying to send file {}";
    public static final String FTP_FILE_SENDER_INFO_18 = "[FTP-I018] [FTP_FILE_SENDER] File {} successfully sent";
    public static final String FTP_FILE_PARSER_INFO_19 = "[FTP-I019] [FTP_PARSER] Scanning all starting directories completed";

    public static final String FTP_SERVER_CONFIG_ERROR_1 = "[FTP-E001] [FTP_SERVER_CONFIG] Ftp connection failed. Server reply code: {}. Shutting down application";
    public static final String FTP_SERVER_CONFIG_ERROR_2 = "[FTP-E002] [KAFKA_PRODUCER] Failed to connect to ftp server";
    public static final String KAFKA_SERVER_CONFIG_ERROR_3 = "[FTP-E003] [KAFKA_PRODUCER] Failed to connect to Kafka server. Shutting down application";
    public static final String FTP_FILE_DAO_IMPL_ERROR_4 = "[FTP-E004] [FTP_FILE_DAO_IMPL] Cannot create save file. Shutting down application";
    public static final String FTP_FILE_DAO_IMPL_ERROR_5 = "[FTP-E005] [FTP_FILE_DAO_IMPL] Cannot open save file {}. Shutting down application";
    public static final String FTP_FILE_DAO_IMPL_ERROR_6 = "[FTP-E006] [FTP_FILE_DAO_IMPL] Failed to save file info {} to file {}";
    public static final String FTP_FILE_READER_ERROR_7 = "[FTP-E007] [FTP_FILE_READER] Cannot retrieve file {}";
    public static final String FTP_FILE_SENDER_ERROR_8 = "[FTP-E008] [FTP_FILE_SENDER] Data sending error: {}";
    public static final String FTP_FILE_PARSER_ERROR_9 = "[FTP-E009] [FTP_FILE_PARSER] Cannot parse directory {}";





}
