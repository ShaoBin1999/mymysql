package com.bsren.mymysql.lock;

import com.bsren.mymysql.exception.TransactionAbortedException;
import com.bsren.mymysql.storage.PageId;
import com.bsren.mymysql.transaction.TransactionId;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LockManager {
    private Map<PageId, Map<TransactionId, PageLock>> pageLockMap;

    public LockManager() {
        pageLockMap = new ConcurrentHashMap<>();
    }

    public synchronized boolean acquireLock(PageId pageId, TransactionId tid, int acquireType)
            throws TransactionAbortedException, InterruptedException {
        final String lockType = acquireType == 0 ? "read lock" : "write lock";
        final String threadName = Thread.currentThread().getName();

        Map<TransactionId, PageLock> lockMap = pageLockMap.get(pageId);
        if (lockMap == null || lockMap.size() == 0) {
            PageLock pageLock = new PageLock(acquireType, tid);
            lockMap = new ConcurrentHashMap<>();
            lockMap.put(tid, pageLock);
            pageLockMap.put(pageId, lockMap);
            return true;
        }

        PageLock lock = lockMap.get(tid);
        if (lock != null) {
            if (acquireType == PageLock.SHARE) {
                return true;
            }
            if (acquireType == PageLock.EXCLUSIVE) {
                if (lockMap.size() > 1) {
//                    System.out.println(threadName + ": the" + pageId + "have many read locks, " +
//                            "transaction " + tid + " require" + lockType + " fail");
                    throw new TransactionAbortedException();
                }
                if (lockMap.size() == 1 && lock.getType() == PageLock.EXCLUSIVE) {
//                    System.out.println(threadName + ": the" + pageId +
//                            "have write lock with the same tid, transaction " + tid + " require" + lockType + " success");
                    return true;
                }
                if (lockMap.size() == 1 && lock.getType() == PageLock.SHARE) {
                    lock.setType(PageLock.EXCLUSIVE);
                    lockMap.put(tid, lock);
                    pageLockMap.put(pageId, lockMap);
//                    System.out.println(threadName + ": the" + pageId + "have read lock with the same tid, transaction " +
//                            tid + " require" + lockType + " success and upgrade");
                    return true;
                }
            }
        }

        if (lock == null) {
            if (acquireType == PageLock.SHARE) {
                if (lockMap.size() > 1) {
                    PageLock pageLock = new PageLock(acquireType, tid);
                    lockMap.put(tid, pageLock);
                    pageLockMap.put(pageId, lockMap);
//                    System.out.println(threadName + ": the" + pageId + "have many read locks, transaction " + tid + " require" + lockType + " success");
                    return true;
                }
                PageLock l = null;
                for (PageLock value : lockMap.values()) {
                    l = value;
                }
                if (lockMap.size() == 1 && l.getType() == PageLock.SHARE) {
                    PageLock pageLock = new PageLock(acquireType, tid);
                    lockMap.put(tid, pageLock);
                    pageLockMap.put(pageId, lockMap);

//                    System.out.println(threadName + ": the" + pageId + "have one lock with different tid, transaction " + tid + " require" + lockType + " success");
                    return true;
                }
                if (lockMap.size() == 1 && l.getType() == PageLock.EXCLUSIVE) {
                    wait(50);
                    return false;
                }
            }
            if (acquireType == PageLock.EXCLUSIVE) {
                wait(10);
                return false;
            }
        }
        return true;
    }

    public synchronized void releaseLock(PageId pageId, TransactionId tid) {
        final String threadName = Thread.currentThread().getName();

        Map<TransactionId, PageLock> lockMap = pageLockMap.get(pageId);
        if (lockMap == null) {
            return;
        }
        if (tid == null) {
            return;
        }
        PageLock lock = lockMap.get(tid);
        if (lock == null) {
            return;
        }
        lockMap.remove(tid);
        if (lockMap.size() == 0) {
            pageLockMap.remove(pageId);
        }
        this.notifyAll();
    }

    public synchronized boolean isHoldLock(PageId pageId, TransactionId tid) {
        Map<TransactionId, PageLock> lockMap = pageLockMap.get(pageId);
        if (lockMap == null) {
            return false;
        }
        return lockMap.get(tid) != null;
    }


    public synchronized void completeTransaction(TransactionId tid) {
        Set<PageId> pageIds = pageLockMap.keySet();
        for (PageId pageId : pageIds) {
            releaseLock(pageId, tid);
        }
    }


}
