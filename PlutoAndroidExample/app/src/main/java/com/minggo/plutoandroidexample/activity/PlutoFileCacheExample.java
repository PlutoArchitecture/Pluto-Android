package com.minggo.plutoandroidexample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.db.manager.DataManagerProxy;
import com.minggo.pluto.db.manager.DataManagerProxy.DataType;
import com.minggo.plutoandroidexample.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by minggo on 2017/2/7.
 */
public class PlutoFileCacheExample extends PlutoActivity implements OnClickListener{

    @BindView(R.id.bt_save)
    public Button saveBt;
    @BindView(R.id.bt_get)
    public Button getBt;
    @BindView(R.id.bt_charge_expired)
    public Button chargeBt;

    private String data;
    private String key;

    private DataManagerProxy dataManagerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_file_cache_example);
        ButterKnife.bind(this);
        dataManagerProxy = new DataManagerProxy(DataType.FILECACHE);

        data = "This is String data";
        key = "plutokey";
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

    @OnClick({R.id.bt_charge_expired,R.id.bt_save,R.id.bt_get})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_charge_expired:
                showToast("File is expired = "+ dataManagerProxy.isExpiredFile(key,1));
                break;
            case R.id.bt_save:
                dataManagerProxy.saveData(key,data);

                showToast("Data is saved");
                break;
            case R.id.bt_get:
                showToast(dataManagerProxy.queryData(key,String.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_sllow_in, R.anim.push_right_out);
    }
}
