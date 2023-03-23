package com.bsren.mymysql.storage;

public interface PageId {

    int[] serialize();

    int getTableId();

    int hashCode();

    boolean equals(Object o);

    int getPageNo();

}
