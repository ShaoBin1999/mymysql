package com.bsren.mymysql.common;

import com.bsren.mymysql.storage.BufferPool;
import com.bsren.mymysql.storage.LogFile;

import java.util.concurrent.atomic.AtomicReference;

public class Database {

    private static final AtomicReference<Database> _instance = new AtomicReference<>(new Database());
    private final Catalog _catalog;
    private final BufferPool _bufferpool;

    private final static String LOGFILENAME = "log";
    private final LogFile _logfile;
}
