package com.bsren.mymysql.storage;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class RecordId implements Serializable {

    private static final long serialVersionUID = 1L;
    private final PageId pageId;
    private final int tupleNo;
}
