package com.minggo.pluto.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.minggo.pluto.R;
import com.minggo.pluto.util.DisplayUtil;

/**
 * 通过对话框
 * Created by minggo on 2017/1/23.
 */

public class PlutoDialog {

    private Context context;
    private int type;
    public final static int LOADING = 1;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private View mainView;
    private int screenWidthPixels;
    private float _20Pixels;

    public PlutoDialog(Context context,int type){
        this.context = context;
        this.type = type;
        calcWidth();
    }

    public void show(){
        creatDialog();
        alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int) (screenWidthPixels-_20Pixels*2);
        alertDialog.getWindow().setAttributes(params);
    }

    private void creatDialog(){
        builder = new AlertDialog.Builder(context);
        switch (type){
            case LOADING:
                mainView = View.inflate(context, R.layout.dialog_progress,null);
                break;
            default:
                break;
        }
        builder.setView(mainView);

    }
    private void calcWidth() {
        screenWidthPixels = DisplayUtil.getScreenWidthPixels(context);
        _20Pixels = DisplayUtil.dip2pxByFloat(context, 20);
    }

    public boolean isShowing() {
        return alertDialog != null && alertDialog.isShowing();
    }

    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void cancel() {
        if (alertDialog != null) {
            alertDialog.cancel();
        }
    }

    public void setCancelable(boolean b) {
        if (alertDialog != null) {
            alertDialog.setCancelable(b);
        } else if (builder != null) {
            builder.setCancelable(b);
        }
    }
}
