package com.bsren.mymysql.storage.field;

import com.bsren.mymysql.common.Type;
import com.bsren.mymysql.execution.Predicate;

import java.io.DataOutputStream;
import java.io.IOException;

public class IntField implements Field {

    private static final long serialVersionUID = 1L;

    private final int value;

    public int getValue() {
        return value;
    }

    public IntField(int i) {
        value = i;
    }

    public String toString() {
        return Integer.toString(value);
    }

    public int hashCode() {
        return value;
    }

    public boolean equals(Object field) {
        if (!(field instanceof IntField)) return false;
        return ((IntField) field).value == value;
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeInt(value);
    }

    public boolean compare(Predicate.Op op, Field val) {
        IntField iVal = (IntField) val;
        switch (op) {
            case EQUALS:
            case LIKE:
                return value == iVal.value;
            case NOT_EQUALS:
                return value != iVal.value;
            case GREATER_THAN:
                return value > iVal.value;
            case GREATER_THAN_OR_EQ:
                return value >= iVal.value;
            case LESS_THAN:
                return value < iVal.value;
            case LESS_THAN_OR_EQ:
                return value <= iVal.value;
        }

        return false;
    }

    public Type getType() {
        return Type.INT_TYPE;
    }
}
