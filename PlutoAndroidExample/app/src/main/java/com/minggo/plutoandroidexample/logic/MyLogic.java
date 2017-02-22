package com.minggo.plutoandroidexample.logic;

import android.os.Handler;

import com.minggo.pluto.logic.LogicManager;

/**
 * Created by minggo on 2017/2/22.
 */

public class MyLogic extends LogicManager {


    /**
     * 业务逻辑父类构造方法
     *
     * @param handler
     * @param clazz            List<Clazz<T>>中的Class<T>；或者直接是Class<T>
     * @param logicManagerType 12中枚举类型
     */
    public <T> MyLogic(Handler handler, Class<T> clazz, LogicManagerType logicManagerType) {
        super(handler, clazz, logicManagerType);
    }

    @Override
    protected void innerDoInBackgroundPre() {
        super.innerDoInBackgroundPre();


    }

    @Override
    protected void innerDoInBackgroundEnd(Object object) {
        super.innerDoInBackgroundEnd(object);
    }


}
