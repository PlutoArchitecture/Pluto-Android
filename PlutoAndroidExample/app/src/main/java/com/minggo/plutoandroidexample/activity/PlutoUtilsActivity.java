package com.minggo.plutoandroidexample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.minggo.plutoandroidexample.R;
/**
 * Created by minggo on 2017/2/20.
 */
public class PlutoUtilsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_utils);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_sllow_in, R.anim.push_right_out);
    }

}
