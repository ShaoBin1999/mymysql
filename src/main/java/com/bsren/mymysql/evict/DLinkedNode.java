package com.bsren.mymysql.evict;

import com.bsren.mymysql.storage.PageId;

class DLinkedNode {
    PageId value;
    DLinkedNode prev;
    DLinkedNode next;
    public DLinkedNode() {}

    public DLinkedNode(PageId value) {
        this.value = value;
    }

    public PageId getValue() {
        return value;
    }
}
