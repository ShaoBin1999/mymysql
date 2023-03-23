package com.bsren.mymysql.storage;

import com.bsren.mymysql.evict.EvictStrategy;
import com.bsren.mymysql.lock.LockManager;

import java.util.concurrent.ConcurrentHashMap;

public class BufferPool {
    /** Bytes per page, including header. */
    private static final int DEFAULT_PAGE_SIZE = 4096;
    private static int pageSize = DEFAULT_PAGE_SIZE;
    /** Default number of pages passed to the constructor. This is used by
     other classes. BufferPool should use the numPages argument to the
     constructor instead. */
    public static final int DEFAULT_PAGES = 50;
    private final int numPages;
    ConcurrentHashMap<PageId,Page> pageCache;
    public final LockManager lockManager;
    private EvictStrategy evict;

}
