package com.minggo.plutoandroidexample.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.api.ApiUrl;
import com.minggo.pluto.api.PlutoApiEngine;
import com.minggo.pluto.logic.LogicManager;
import com.minggo.pluto.logic.LogicParam.ParamName;
import com.minggo.pluto.model.Result;
import com.minggo.pluto.util.EncryptUtils;
import com.minggo.pluto.util.LogUtils;
import com.minggo.plutoandroidexample.R;
import com.minggo.plutoandroidexample.logic.MyParam.LoginParam;
import com.minggo.plutoandroidexample.logic.MyParam.ServerUrlParam;
import com.minggo.plutoandroidexample.logic.MyParam.UploadParam;
import com.minggo.plutoandroidexample.model.ServerURL;
import com.minggo.plutoandroidexample.model.User;
import com.minggo.plutoandroidexample.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.minggo.pluto.logic.LogicManager.LogicManagerType.GET__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN;
import static com.minggo.pluto.logic.LogicManager.LogicManagerType.GET__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN;
import static com.minggo.pluto.logic.LogicManager.LogicManagerType.GET__MODEL__ONLY_NETWORK;
import static com.minggo.pluto.logic.LogicManager.LogicManagerType.POST__MODEL__UPLOALD_FILE;

/**
 * Created by minggo on 2017/2/4.
 */
public class PlutoAPIEngineExample extends PlutoActivity implements OnClickListener {

    public static final String TAG = "PlutoAPIEngineExample";

    @BindView(R.id.bt_data_1)
    public Button dataOnlyNetworkBt;
    @BindView(R.id.bt_data_2)
    public Button cacheAdvanceBt;
    @BindView(R.id.bt_data_3)
    public Button cacheLimitedBt;
    @BindView(R.id.bt_data_4)
    public Button onlyResultBt;
    @BindView(R.id.bt_data_5)
    Button btData5;

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

    @OnClick({R.id.bt_data_1, R.id.bt_data_2, R.id.bt_data_3, R.id.bt_data_4,R.id.bt_data_5})
    @Override
    public void onClick(View view) {
        Map<String, Object> param = PlutoApiEngine.addRequiredParam();
        param.remove(ApiUrl.PASS);
        String pass = new EncryptUtils().getMD5Str("minggo" + param.get(ApiUrl.IMEI) + "charmword");
        switch (view.getId()) {
            case R.id.bt_data_1:

                loadingDialog.show();

                new LogicManager(mUiHandler, User.class, GET__MODEL__ONLY_NETWORK)
                        .setParamClass(LoginParam.class)
                        .setParam(ParamName.PASSWORD, 123456)
                        .setParam(ParamName.EMAIL, "minggo8en@gmail.com")
                        .setParam(ApiUrl.PASS, pass)
                        .setArg1(1)
                        .execute();
                break;
            case R.id.bt_data_2:
                loadingDialog.show();

                new LogicManager(mUiHandler, ServerURL.class, GET__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN)
                        .setParamClass(ServerUrlParam.class)
                        .setCacheKey(ServerUrlParam.CACHEKEY)
                        .setParam(ApiUrl.PASS, pass)
                        .execute();

                break;
            case R.id.bt_data_3:
                loadingDialog.show();

                new LogicManager(mUiHandler, ServerURL.class, GET__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN)
                        .setParamClass(ServerUrlParam.class)
                        .setCacheKey(ServerUrlParam.CACHEKEY)
                        .setLimitedTime(1)
                        .setParam(ApiUrl.PASS, pass)
                        .execute();
                break;
            case R.id.bt_data_4:
                loadingDialog.show();
                new LogicManager(mUiHandler, Result.class, GET__MODEL__ONLY_NETWORK)
                        .setParamClass(LoginParam.class)
                        .setParam(ParamName.PASSWORD, 123456)
                        .setParam(ParamName.EMAIL, "minggo8en@gmail.com")
                        .setParam(ApiUrl.PASS, pass)
                        .setArg1(2)
                        .execute();
                break;
            case R.id.bt_data_5:
                loadingDialog.show();
                Map<String, File> files = new HashMap<>();
                //File file = new File(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
                File file = new File(Environment.getExternalStorageDirectory().getPath(),"zang5.jpg");
                if (file.exists()){
                    showToast("图文存在");
                }else {
                    showToast("图文不存在");
                }
                files.put("file",file);
                new LogicManager(mUiHandler, String.class, POST__MODEL__UPLOALD_FILE)
                        .setParamClass(UploadParam.class)
                        .setParam("userid", "123456")
                        .setParam("language", "bod")
                        .setFiles(files)
                        .execute();
                break;
            default:
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 1000);

        if (msg.obj != null)
            showToast(gson.toJson(msg.obj));

        switch (msg.what) {
            case LoginParam.WHAT:
                if (msg.arg1 == 1 && msg.obj != null) {
                    User user = ((User) msg.obj);
                    LogUtils.info(TAG, ">>>>>>username=" + user.username);
                } else if (msg.arg1 == 2 && msg.obj != null) {
                    Result<User> result = ((Result<User>) msg.obj);
                    LogUtils.info(TAG, ">>>>>>username=" + result.content);
                }

                break;
            default:
                break;
        }

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
