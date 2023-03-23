package com.bsren.mymysql.storage;

public interface PageId {

    byte[] serialize();

    int getTableId();

    int hashCode();

    boolean equals(Object o);

    int getPageId();

}
