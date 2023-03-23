package com.bsren.mymysql.storage;

import com.bsren.mymysql.exception.DbException;
import com.bsren.mymysql.exception.TransactionAbortedException;

import java.util.NoSuchElementException;

public abstract class AbstractDbFileIterator implements DbFileIterator{

    private Tuple next = null;

    public boolean hasNext() throws DbException, TransactionAbortedException {
        if (next == null) next = readNext();
        return next != null;
    }

    public Tuple next() throws DbException, TransactionAbortedException,
            NoSuchElementException {
        if (next == null) {
            next = readNext();
            if (next == null) throw new NoSuchElementException();
        }

        Tuple result = next;
        next = null;
        return result;
    }

    protected abstract Tuple readNext() throws DbException, TransactionAbortedException;

    public void close() {
        next = null;
    }
}
