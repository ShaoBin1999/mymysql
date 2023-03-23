package com.bsren.mymysql.lock;

import com.bsren.mymysql.transaction.TransactionId;

public class PageLock {

    public static final int SHARE = 0;

    public static final int EXCLUSIVE = 1;

    private int type;

    private TransactionId transactionId;

    public PageLock(int type, TransactionId transactionId) {
        this.type = type;
        this.transactionId = transactionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "PageLock{" +
                "type=" + type +
                ", transactionId=" + transactionId +
                '}';
    }
}