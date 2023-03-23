package com.bsren.mymysql.storage;

import com.bsren.mymysql.exception.DbException;
import com.bsren.mymysql.exception.TransactionAbortedException;

import java.util.NoSuchElementException;

public interface DbFileIterator {

    void open() throws DbException, TransactionAbortedException;

    boolean hasNext() throws DbException, TransactionAbortedException;

    Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException;

    void rewind() throws DbException, TransactionAbortedException;

    void close();
}
