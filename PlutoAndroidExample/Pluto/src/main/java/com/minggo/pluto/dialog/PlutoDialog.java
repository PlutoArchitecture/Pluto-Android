package com.minggo.pluto.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.minggo.pluto.R;
import com.minggo.pluto.util.*;

/**
 * 通过对话框
 * Created by minggo on 2017/1/23.
 */

public class PlutoDialog {

    private Context context;
    private int type;
    public static final int DEFAULT = 0;//系统默认对话框
    public static final int LOADING = 1;//请稍等提示
    public static final int TEXT_ONLIY = 2;// 提示信息确认
    public static final int DEFAULT_EXIT = 3;//默认退出提醒对话
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private View mainView;
    private int screenWidthPixels;
    private float _20Pixels;
    private String content;
    private String title;
    private String leftText;
    private String rightText;
    private PlutoDialogListener listener;

    public PlutoDialog(Context context,int type){

        this.context = context;
        this.type = type;
        calcWidth();
    }

    public PlutoDialog(Context context,int type,String content){

        this.context = context;
        this.type = type;
        this.content = content;
        calcWidth();
    }
    public PlutoDialog(Context context,int type,PlutoDialogListener listener){

        this.context = context;
        this.type = type;
        this.listener = listener;
        calcWidth();
    }

    public PlutoDialog(Context context,int type,String content,PlutoDialogListener listener){

        this.context = context;
        this.type = type;
        this.content = content;
        this.listener = listener;
        calcWidth();
    }

    public PlutoDialog(Context context,int type,String title,String content,String leftText,String rightText,PlutoDialogListener listener){
        this.context = context;
        this.type = type;
        this.title = title;
        this.content = content;
        this.leftText = leftText;
        this.rightText = rightText;
        this.listener = listener;
        calcWidth();
    }


    public void show(){
        creatDialog();
        if (type!=DEFAULT&&DEFAULT_EXIT!=type){

            alertDialog = builder.create();
            alertDialog.show();
            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            params.width = (int) (screenWidthPixels-_20Pixels*2);
            alertDialog.getWindow().setAttributes(params);
        }

    }

    private void creatDialog(){
        builder = new AlertDialog.Builder(context);
        switch (type){
            case DEFAULT:
                alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(content)
                        .setNegativeButton(leftText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.cancel();
                            }
                        })
                        .setPositiveButton(rightText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.confirm();
                                alertDialog.dismiss();
                                alertDialog.cancel();


                            }
                        }).show();
                break;
            case DEFAULT_EXIT:
                alertDialog = new AlertDialog.Builder(context).setTitle(R.string.exit_title).setMessage(R.string.exit_content)
                        .setNegativeButton(R.string.exit_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.cancel();
                            }
                        })
                        .setPositiveButton(R.string.exit_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.confirm();
                                alertDialog.dismiss();
                                alertDialog.cancel();
                            }
                        }).show();
                break;
            case LOADING:
                mainView = View.inflate(context, R.layout.dialog_progress,null);
                break;
            case TEXT_ONLIY:
                mainView = View.inflate(context, R.layout.dialog_text,null);
                break;
            default:
                break;
        }

        initUI();
    }

    private void initUI(){
        if (type==LOADING){
            builder.setView(mainView);
        }else if (type==TEXT_ONLIY){
            builder.setView(mainView);
            ((TextView) mainView.findViewById(R.id.tv_dialog_tips)).setText(content);
        }
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

    public interface PlutoDialogListener{
        void confirm();
        void cancel();
    }
}
