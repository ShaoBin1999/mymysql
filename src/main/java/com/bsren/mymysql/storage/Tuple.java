package com.bsren.mymysql.storage;

import com.bsren.mymysql.storage.field.Field;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;


public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;
    private TupleDesc tupleDesc;
    private final Field[] fields;
    private RecordId recordId;

    public Tuple(TupleDesc td) {
        tupleDesc = td;
        fields = new Field[td.numFields()];
    }

    public TupleDesc getTupleDesc() {
        return tupleDesc;
    }

    public RecordId getRecordId() {
        return recordId;
    }

    public void setRecordId(RecordId rid) {
        recordId=rid;
    }

    public void setField(int i, Field f) {
        fields[i]=f;
    }

    public Field getField(int i) {
        return fields[i];
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN
     *
     * where \t is any whitespace (except a newline)
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(fields[0]);
        for  (int i=1; i<fields.length; i++) {
            sb.append("\t").append(fields[i]);
        }
        return sb.toString();
    }

    public Iterator<Field> fields() {
        return Arrays.asList(fields).iterator();
    }

    public void resetTupleDesc(TupleDesc td) {
        tupleDesc=td;
    }
}
