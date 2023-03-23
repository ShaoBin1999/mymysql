package com.bsren.mymysql.storage;

import com.bsren.mymysql.transaction.TransactionId;

public interface Page {

    PageId getId();

    byte[] getPageData();

    TransactionId isDirty();

    void markDirty(boolean dirty, TransactionId tid);

    Page getBeforeImage();

    void setBeforeImage();
}
