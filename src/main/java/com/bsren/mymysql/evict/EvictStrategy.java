package com.bsren.mymysql.evict;

import com.bsren.mymysql.storage.PageId;

public interface EvictStrategy {


    void modifyData(PageId pageId);

    PageId getEvictPageId();

}
