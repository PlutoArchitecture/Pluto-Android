package com.minggo.pluto.db.manager;

/**
 * Created by minggo on 2017/2/21.
 */

public class DataManagerStub implements DataManager {


    @Override
    public void saveData(Object key, Object object) {

    }

    @Override
    public <T> T queryData(Object key, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> void deleteData(Object key, Class<T> clazz) {

    }

    @Override
    public void updateData(Object key, Object object) {

    }
}
