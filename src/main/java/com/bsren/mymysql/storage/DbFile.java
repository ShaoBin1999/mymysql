package com.bsren.mymysql.storage;

import com.bsren.mymysql.exception.DbException;
import com.bsren.mymysql.exception.TransactionAbortedException;
import com.bsren.mymysql.transaction.TransactionId;

import java.io.IOException;
import java.util.List;

public interface DbFile {

    Page readPage(PageId id);

    void writePage(Page p) throws IOException;

    List<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException;

    List<Page> deleteTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException;

    DbFileIterator iterator(TransactionId tid);

    int getId();

    TupleDesc getTupleDesc();
}
