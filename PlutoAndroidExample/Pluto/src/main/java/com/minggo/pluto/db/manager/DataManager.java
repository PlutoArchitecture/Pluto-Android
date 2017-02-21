package com.minggo.pluto.db.manager;

/**
 * Created by minggo on 2017/2/21.
 */

public interface DataManager {

    void saveData(Object key,Object object);
    <T> T queryData(Object key,Class<T> clazz);
    <T> void deleteData(Object key,Class<T> clazz);
    void updateData(Object key,Object object);

}
