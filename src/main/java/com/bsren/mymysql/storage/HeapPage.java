package com.bsren.mymysql.storage;

import com.bsren.mymysql.common.Database;
import com.bsren.mymysql.exception.DbException;
import com.bsren.mymysql.storage.field.Field;
import com.bsren.mymysql.transaction.TransactionId;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HeapPage implements Page{

    final HeapPageId pid;
    final TupleDesc td;
    final byte[] header;
    final Tuple[] tuples;
    final int numSlots;
    private TransactionId tid;
    byte[] oldData;
    private final Byte oldDataLock= (byte) 0;

    public HeapPage(HeapPageId id, byte[] data) throws IOException {

        this.pid = id;
        this.td = Database.getCatalog().getTupleDesc(id.getTableId());
        this.numSlots = getNumTuples();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

        // allocate and read the header slots of this page
        header = new byte[getHeaderSize()];
        for (int i = 0; i < header.length; i++){
            header[i] = dis.readByte();
        }
        tuples = new Tuple[numSlots];

        try {
            // allocate and read the actual records of this page
            for (int i = 0; i < tuples.length; i++) {
                tuples[i] = readNextTuple(dis, i);
            }
        }catch(NoSuchElementException e){
            e.printStackTrace();
        }
        dis.close();
        setBeforeImage();
    }

    public TransactionId isDirty() {
        return tid;
    }

    public void markDirty(boolean dirty, TransactionId tid) {
        this.tid=dirty?tid:null;
    }

    public HeapPageId getId() {
        return pid;
    }

    public HeapPage getBeforeImage(){
        try {
            byte[] oldDataRef = null;
            synchronized(oldDataLock) {
                oldDataRef = oldData;
            }
            return new HeapPage(pid,oldDataRef);
        } catch (IOException e) {
            e.printStackTrace();
            //should never happen -- we parsed it OK before!
            System.exit(1);
        }
        return null;
    }

    public void setBeforeImage() {
        synchronized(oldDataLock) {
            oldData = getPageData().clone();
        }
    }


    public byte[] getPageData() {
        int len = BufferPool.getPageSize();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
        DataOutputStream dos = new DataOutputStream(baos);

        for (byte b : header) {
            try {
                dos.writeByte(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        for (int i=0; i<tuples.length; i++) {
            // empty slot
            if (!isSlotUsed(i)) {
                for (int j=0; j<td.getSize(); j++) {
                    try {
                        dos.writeByte(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                continue;
            }

            // non-empty slot
            for (int j=0; j<td.numFields(); j++) {
                Field f = tuples[i].getField(j);
                try {
                    f.serialize(dos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // padding
        int zerolen = BufferPool.getPageSize() - (header.length + td.getSize() * tuples.length); //- numSlots * td.getSize();
        byte[] zeroes = new byte[zerolen];
        try {
            dos.write(zeroes, 0, zerolen);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }


    private int getNumTuples() {
        return (BufferPool.getPageSize()<<3)/((td.getSize()<<3)+1);
    }

    private int getHeaderSize() {
        return (int)Math.ceil(getNumTuples()*1.0/8);

    }

    public boolean isSlotUsed(int i) {
        return 0!= (header[i>>3]&(1<<(i&7)));
    }

    public static byte[] createEmptyPageData() {
        int len = BufferPool.getPageSize();
        return new byte[len]; //all 0
    }

    private void markSlotUsed(int i, boolean value) {
        if (value) {
            header[i>>3] |= (1<<(i&7));
        } else {
            header[i>>3] &= ~(1<<(i&7));
        }
    }

    public void deleteTuple(Tuple t) throws DbException {
        if(pid.equals(t.getRecordId().getPageId())){
            int tup = t.getRecordId().getTupleNo();
            if(tup<0 || tup>=numSlots){
                return;
            }
            if(!isSlotUsed(tup)){
                throw new DbException("delete on empty slot");
            }else{
                tuples[tup]=null;
                markSlotUsed(tup,false);
                return;
            }
        }
        throw new DbException("tuple not no the page");
    }

    public void insertTuple(Tuple t) throws DbException {
        TupleDesc tupleDesc = t.getTupleDesc();
        if(!tupleDesc.equals(td)){
            throw new DbException("tuple not match");
        }
        for(int i=0;i<numSlots;i++){
            if(!isSlotUsed(i)){
                t.setRecordId(new RecordId(pid,i));
                tuples[i]=t;
                markSlotUsed(i,true);
                return;
            }
        }
        throw new DbException("no empty slots");
    }


    private Tuple readNextTuple(DataInputStream dis, int slotId) throws NoSuchElementException {
        if (!isSlotUsed(slotId)) {
            for (int i=0; i<td.getSize(); i++) {
                try {
                    dis.readByte();
                } catch (IOException e) {
                    throw new NoSuchElementException("error reading empty tuple");
                }
            }
            return null;
        }

        // read fields in the tuple
        Tuple t = new Tuple(td);
        RecordId rid = new RecordId(pid, slotId);
        t.setRecordId(rid);
        try {
            for (int j=0; j<td.numFields(); j++) {
                Field f = td.getFieldType(j).parse(dis);
                t.setField(j, f);
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new NoSuchElementException("parsing error!");
        }
        return t;
    }

    public Iterator<Tuple> iterator() {
        // some code goes here
        return new Iterator<Tuple>() {
            private int idx=0;
            @Override
            public boolean hasNext() {
                while(idx<numSlots && !isSlotUsed(idx)){
                    idx++;
                }
                return idx<numSlots;
            }

            @Override
            public Tuple next() {
                if(hasNext()){
                    return tuples[idx++];
                }else{
                    throw new NoSuchElementException();
                }
            }
        };
    }

}
