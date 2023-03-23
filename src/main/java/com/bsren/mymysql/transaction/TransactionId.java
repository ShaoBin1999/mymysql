package com.bsren.mymysql.transaction;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionId implements Serializable {

    private static final long serialVersionUID = 1L;

    //事务id总计数器
    static final AtomicLong counter = new AtomicLong(0);
    final long txid;

    public TransactionId() {
        txid = counter.getAndIncrement();
    }

    public long getId() {
        return txid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransactionId other = (TransactionId) obj;
        return txid == other.txid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (txid ^ (txid >>> 32));
        return result;
    }

}
