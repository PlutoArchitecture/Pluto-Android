package com.minggo.plutoandroidexample.activity;

import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.api.PlutoApiEngine;
import com.minggo.pluto.api.ApiUrl;
import com.minggo.pluto.common.CommonAsyncTask;
import com.minggo.pluto.model.Result;
import com.minggo.pluto.util.EncryptUtils;
import com.minggo.plutoandroidexample.R;
import com.minggo.plutoandroidexample.model.ServerURL;
import com.minggo.plutoandroidexample.model.User;
import com.minggo.plutoandroidexample.neturl.ExampleURL;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by minggo on 2017/2/4.
 */
public class PlutoAPIEngineExample extends PlutoActivity implements OnClickListener{

    @BindView(R.id.bt_data_1)
    public Button dataOnlyNetworkBt;
    @BindView(R.id.bt_data_2)
    public Button cacheAdvanceBt;
    @BindView(R.id.bt_data_3)
    public Button cacheLimitedBt;
    @BindView(R.id.bt_data_4)
    public Button onlyResultBt;

    private static final int ONLY_FROM_NETWORK = 1;//数据仅从网络获取
    private static final int CACHE_ADVANCE_THEN_NETWORK_RETURN = 2; //缓存优先返回获取网络数据后刷新数据
    private static final int CACHE_LIMITED_TIME_THEN_NETWORK_RETURN = 3;//缓存过去后获取网络数据
    private static final int ONLY_RESULT_FROM_NETWORK = 4;//紧获取不加工的网络数据

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

    @OnClick({R.id.bt_data_1,R.id.bt_data_2,R.id.bt_data_3,R.id.bt_data_4})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_data_1:
                loadingDialog.show();
                new ObtainworkData(ONLY_FROM_NETWORK).execute();
                break;
            case R.id.bt_data_2:
                loadingDialog.show();
                new ObtainworkData(CACHE_ADVANCE_THEN_NETWORK_RETURN).execute();
                break;
            case R.id.bt_data_3:
                loadingDialog.show();
                new ObtainworkData(CACHE_LIMITED_TIME_THEN_NETWORK_RETURN).execute();
                break;
            case R.id.bt_data_4:
                loadingDialog.show();
                new ObtainworkData(ONLY_RESULT_FROM_NETWORK).execute();
                break;
            default:
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        loadingDialog.dismiss();
        showToast(gson.toJson(msg.obj));
    }

    private class ObtainworkData extends CommonAsyncTask<Void,Void,Object>{

        private int type;

        public ObtainworkData(int type){
            this.type = type;
        }

       @Override
        protected Object doInBackground(Void... params) {

           Map<String,Object> param = PlutoApiEngine.addRequiredParam();
           param.remove(ApiUrl.PASS);
           param.put(ApiUrl.PASS, new EncryptUtils().getMD5Str("minggo"+param.get(ApiUrl.IMEI)+"charmword"));

           if (type == ONLY_FROM_NETWORK) {
               param.put("password", "123456");
               param.put("email", "minggo8en@gmail.com");
               User user = PlutoApiEngine.getModelByNoCache(ExampleURL.USER_LOGIN,param,User.class);
               return user;
           }else if (type == CACHE_ADVANCE_THEN_NETWORK_RETURN){
               String cacheKey = "list_cache";
               List<ServerURL> list = PlutoApiEngine.getListByCacheAdvance(ExampleURL.SEVER_URLS,param,cacheKey,mUiHandler,CACHE_ADVANCE_THEN_NETWORK_RETURN,ServerURL.class);
               return list;
           }else if(type == CACHE_LIMITED_TIME_THEN_NETWORK_RETURN){
               String cacheKey = "list_key";
               int timeLimited = 2;//hour
               List<ServerURL> list = PlutoApiEngine.getListByLimitTime(ExampleURL.SEVER_URLS,param,cacheKey,mUiHandler,CACHE_ADVANCE_THEN_NETWORK_RETURN,timeLimited,ServerURL.class);
               return list;
           }else if (type == ONLY_RESULT_FROM_NETWORK){
               param.remove(ApiUrl.PASS);
               param.put(ApiUrl.PASS, new EncryptUtils().getMD5Str("minggo"+param.get(ApiUrl.IMEI)+"charmword"));
               param.put("password", "123456");
               param.put("email", "minggo8en@gmail.com");
               Result<User> result = PlutoApiEngine.getResultOnly(ExampleURL.USER_LOGIN,param);
               return result;
           }
           //...不一一列举了，可以参看PlutoApiEngine类方法。
           // 1.带get开头的是get方法处理，带post开头的是post方法。
           // 2.带model是返回java model，带list是返回list。
           // 3.带Result是指返回纯粹的不加工的Result实体。
           // 4.自己也可以根据实际的需求在APIEngine中扩展方法。
           return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            mUiHandler.obtainMessage(type,object).sendToTarget();
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
