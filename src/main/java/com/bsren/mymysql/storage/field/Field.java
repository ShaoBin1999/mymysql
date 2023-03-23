package com.bsren.mymysql.storage.field;

import com.bsren.mymysql.common.Type;
import com.bsren.mymysql.execution.Predicate;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public interface Field extends Serializable {

    void serialize(DataOutputStream dos) throws IOException;

    boolean compare(Predicate.Op op, Field value);

    Type getType();

    int hashCode();
    boolean equals(Object field);

    String toString();
}
