package com.minggo.plutoandroidexample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mobstat.StatService;
import com.minggo.plutoandroidexample.R;

/**
 * Created by minggo on 2017/2/17.
 */
public class PlutoExceptionExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_exception_example);
    }



    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
