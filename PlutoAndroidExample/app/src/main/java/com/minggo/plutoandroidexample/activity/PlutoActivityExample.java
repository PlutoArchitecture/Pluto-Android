package com.minggo.plutoandroidexample.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.dialog.PlutoDialog;
import com.minggo.plutoandroidexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by minggo on 2017/2/1.
 */
public class PlutoActivityExample extends PlutoActivity implements OnClickListener {

    @BindView(R.id.bt_toast)
    public Button toastBt;
    @BindView(R.id.bt_dialog)
    public Button dialgBt;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_example);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick({R.id.bt_toast,R.id.bt_dialog})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_dialog:
                loadingDialog.show();
                break;
            case R.id.bt_toast:
                showToast("just use showToast(String string)");
                break;
            default:
                break;
        }
    }
}
