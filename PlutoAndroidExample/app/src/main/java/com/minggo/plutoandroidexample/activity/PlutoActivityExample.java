package com.minggo.plutoandroidexample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.bitmap.AsyncTask;
import com.minggo.pluto.common.CommonAsyncTask;
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
    @BindView(R.id.bt_softinput)
    public Button softinputBt;
    @BindView(R.id.bt_network_available)
    public Button networkBt;
    @BindView(R.id.bt_hand_message)
    public Button handleBt;
    @BindView(R.id.bt_cancel_asyntask)
    public Button cancelAsynTaskBt;
    @BindView(R.id.tv_learntime)
    public TextView learnTimeTv;
    private Calculator calculator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_example);
        ButterKnife.bind(this);
        calculator = new Calculator();
        calculator.execute();
    }


    @OnClick({R.id.bt_toast,R.id.bt_dialog,R.id.bt_softinput,R.id.bt_network_available,R.id.bt_hand_message,R.id.bt_cancel_asyntask})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_dialog:
                loadingDialog.show();
                break;
            case R.id.bt_toast:
                showToast("just use showToast(String string)");
                break;
            case R.id.bt_softinput:
                showSoftInput();
                break;
            case R.id.bt_network_available:
                showToast("Network is available "+isNetworkConnected());
                break;
            case R.id.bt_hand_message:
                mUiHandler.sendEmptyMessage(10000);
                break;
            case R.id.bt_cancel_asyntask:
                cancelAsyncTask(calculator);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what){
            case 10000:
                showToast("Message is recieved!");
                break;

            default:

                learnTimeTv.setText("learning time "+msg.what+"s");
                break;
        }
    }

    private class Calculator extends CommonAsyncTask<Void,Void,Integer> {
        private int i;

        @Override
        protected Integer doInBackground(Void... params) {
            while (i<1000){
                if (isCancelled()){
                    i=0;
                    showToast("learning time is stopped");
                    break;
                }
                i++;
                mUiHandler.sendEmptyMessage(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (isCancelled()){
                return 0;
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mUiHandler.sendEmptyMessage(i);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_sllow_in, R.anim.push_right_out);
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
