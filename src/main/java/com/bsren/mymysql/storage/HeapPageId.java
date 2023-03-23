package com.bsren.mymysql.storage;

public class HeapPageId implements PageId{

    private final int tableId;
    private final int pageNumber;

    public HeapPageId(int tableId, int pgNo) {
        this.tableId=tableId;
        this.pageNumber=pgNo;
    }

    public int getTableId() {
        return tableId;
    }

    public int getPageNo() {
        return pageNumber;
    }

    public int hashCode() {
        String hash = ""+tableId+pageNumber;
        return hash.hashCode();
    }

    public boolean equals(Object o) {
        if(o instanceof PageId){
            PageId obj = (PageId) o;
            return obj.getTableId() == tableId && obj.getPageNo() == pageNumber;
        }
        return false;
    }

    public int[] serialize() {
        int[] data = new int[2];
        data[0] = getTableId();
        data[1] = getPageNo();
        return data;
    }
}
