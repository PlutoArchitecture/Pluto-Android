package com.minggo.plutoandroidexample.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.minggo.pluto.common.CommonAsyncTask;
import com.minggo.pluto.fragment.PlutoFragment;
import com.minggo.plutoandroidexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by minggo on 2017/2/4.
 */
public class ExampleFragment extends PlutoFragment implements OnClickListener{
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
    private Activity activity;
    private View mainView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calculator = new Calculator();
        calculator.execute();
        
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = ((Activity) activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_example, container, false);
        ButterKnife.bind(this,mainView);
        initUI();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContentView(mainView);
    }

    @Override
    protected void showData() {
        setContentShown(true);
    }


    private void initUI() {

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
}
