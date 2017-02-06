package com.minggo.plutoandroidexample.activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.api.ApiEngine;
import com.minggo.pluto.api.ApiUrl;
import com.minggo.pluto.common.CommonAsyncTask;
import com.minggo.pluto.util.EncryptUtils;
import com.minggo.plutoandroidexample.R;
import com.minggo.plutoandroidexample.model.User;
import com.minggo.plutoandroidexample.neturl.ExampleURL;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by minggo on 2017/2/4.
 */
public class PlutoAPIEngineExample extends PlutoActivity implements OnClickListener{

    @BindView(R.id.bt_data_only_network)
    public Button dataOnlyNetworkBt;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_apiengine_example);
        ButterKnife.bind(this);
        gson = new Gson();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_sllow_in, R.anim.push_right_out);
    }

    @OnClick({R.id.bt_data_only_network})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_data_only_network:
                loadingDialog.show();
                new OnlyNetworkData().execute();
                break;
            default:
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        loadingDialog.dismiss();
        switch (msg.what){
            case 10002:
                showToast(gson.toJson(msg.obj));
                break;
            default:
                break;
        }
    }

    private class OnlyNetworkData extends CommonAsyncTask<Void,Void,User>{

        @Override
        protected User doInBackground(Void... params) {

            Map<String,Object> param = ApiEngine.addRequiredParam();
            param.remove(ApiUrl.PASS);
            param.put(ApiUrl.PASS, new EncryptUtils().getMD5Str("minggo"+param.get(ApiUrl.IMEI)+"charmword"));
            param.put("password", "123456");
            param.put("email", "minggo8en@gmail.com");
            User user = ApiEngine.getModelByNoCache(ExampleURL.USER_LOGIN,param,User.class);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            mUiHandler.obtainMessage(10002,user).sendToTarget();
        }
    }
}
