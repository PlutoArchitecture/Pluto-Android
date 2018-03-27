package com.minggo.plutoandroidexample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mobstat.StatService;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.plutoandroidexample.R;

import butterknife.ButterKnife;

/**
 * Created by minggo on 2017/2/7.
 */
public class PlutoNetworkExample extends PlutoActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_network_example);
        ButterKnife.bind(this);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_sllow_in, R.anim.push_right_out);
    }
}
