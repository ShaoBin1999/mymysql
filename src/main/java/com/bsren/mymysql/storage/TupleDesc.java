package com.bsren.mymysql.storage;

import com.bsren.mymysql.common.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TupleDesc implements Serializable {

    private static final long serialVersionUID = 1L;

    private final TDItem[] tdItems;

    public Iterator<TDItem> iterator() {
        return Arrays.asList(tdItems).iterator();
    }

    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        tdItems = new TDItem[typeAr.length];
        for(int i=0;i<typeAr.length;i++) {
            tdItems[i] = new TDItem(typeAr[i], fieldAr[i]);
        }
    }

    public TupleDesc(Type[] typeAr) {
        String[] strings = new String[typeAr.length];
        Arrays.fill(strings,"");
        tdItems = new TDItem[typeAr.length];
        for(int i=0;i<typeAr.length;i++) {
            tdItems[i] = new TDItem(typeAr[i], strings[i]);
        }
    }

    public int numFields() {
        return tdItems.length;
    }

    public String getFieldName(int i) throws NoSuchElementException {
        if(i<0 || i>=tdItems.length){
            throw new NoSuchElementException("pos "+i+" is not a valid index");
        }
        return tdItems[i].fieldName;
    }

    public Type getFieldType(int i) throws NoSuchElementException {
        if(i<0 || i>=tdItems.length){
            throw new NoSuchElementException("pos "+i+" is not a valid index");
        }
        return tdItems[i].fieldType;
    }

    public int fieldNameToIndex(String name) throws NoSuchElementException {
        for(int i=0;i<tdItems.length;i++){
            if(tdItems[i].fieldName.equals(name)){
                return i;
            }
        }
        throw new NoSuchElementException("not find fieldName "+name);
    }

    public int getSize() {
        int size=0;
        for (TDItem tdItem : tdItems) {
            size += tdItem.fieldType.getLen();
        }
        return size;
    }

    /**
     * 可能会出现重名问题
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        Type[] type = new Type[td1.numFields()+ td2.numFields()];
        String[] name = new String[td1.numFields()+ td2.numFields()];
        for(int i=0;i< td1.numFields();i++){
            type[i]=td1.tdItems[i].fieldType;
            name[i]=td1.tdItems[i].fieldName;
        }
        for(int i=0;i< td2.numFields();i++){
            type[i+ td1.numFields()]=td2.tdItems[i].fieldType;
            name[i+ td1.numFields()]=td2.tdItems[i].fieldName;
        }
        return new TupleDesc(type,name);
    }

    public boolean equals(Object o) {
        if(this.getClass().isInstance(o)){
            TupleDesc obj = (TupleDesc) o;
            if(numFields()==obj.numFields()){
                for(int i=0;i<numFields();i++){
                    if(!tdItems[i].fieldType.equals(obj.tdItems[i].fieldType)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     *
     * @return String describing this descriptor.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<tdItems.length-1;i++){
            sb.append(tdItems[i].fieldType).append("(").append(tdItems[i].fieldName).append("),");
        }
        sb.append(tdItems[tdItems.length-1].fieldType).append("(").append(tdItems[tdItems.length-1].fieldName).append(")");
        return sb.toString();
    }



    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        public final Type fieldType;
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldType = t;
            this.fieldName = n;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

}
