package com.bsren.mymysql.execution;

import com.bsren.mymysql.storage.Tuple;
import com.bsren.mymysql.storage.field.Field;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class Predicate implements Serializable {

    private static final long serialVersionUID = 1L;
    private Field opField;
    private int fieldNo;
    private Op op;

    public enum Op implements Serializable {
        EQUALS, GREATER_THAN, LESS_THAN, LESS_THAN_OR_EQ, GREATER_THAN_OR_EQ, LIKE, NOT_EQUALS;

        public static Op getOp(int i) {
            return values()[i];
        }

        public String toString() {
            if (this == EQUALS)
                return "=";
            if (this == GREATER_THAN)
                return ">";
            if (this == LESS_THAN)
                return "<";
            if (this == LESS_THAN_OR_EQ)
                return "<=";
            if (this == GREATER_THAN_OR_EQ)
                return ">=";
            if (this == LIKE)
                return "LIKE";
            if (this == NOT_EQUALS)
                return "<>";
            throw new IllegalStateException("impossible to reach here");
        }

    }

    public Predicate(int field, Op op, Field operand) {
        // some code goes here
        this.fieldNo=field;
        this.op=op;
        this.opField=operand;
    }

    public boolean filter(Tuple t) {
        // some code goes here
        Field field = t.getField(fieldNo);
        return field.compare(op,opField);
    }
}
