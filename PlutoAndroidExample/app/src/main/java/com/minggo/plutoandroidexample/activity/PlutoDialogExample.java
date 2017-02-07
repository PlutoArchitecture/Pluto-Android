package com.minggo.plutoandroidexample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.dialog.PlutoDialog;
import com.minggo.pluto.dialog.PlutoDialog.PlutoDialogListener;
import com.minggo.plutoandroidexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by minggo on 2017/2/4.
 */
public class PlutoDialogExample extends PlutoActivity implements OnClickListener,PlutoDialogListener {

    @BindView(R.id.bt_system_dialog)
    public Button systemDialogBt;
    @BindView(R.id.bt_system_define_dialog)
    public Button defineSystemDialogBt;
    @BindView(R.id.bt_loading_dialog)
    public Button defineloadingBt;
    @BindView(R.id.bt_text_dialog)
    public Button defineTextDialogBt;
    private PlutoDialog plutoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_example);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_sllow_in, R.anim.push_right_out);
    }

    @OnClick({R.id.bt_system_dialog,R.id.bt_system_define_dialog,R.id.bt_loading_dialog,R.id.bt_text_dialog})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_system_dialog:
                plutoDialog = new PlutoDialog(this,PlutoDialog.DEFAULT_EXIT,this);
                plutoDialog.show();
                break;
            case R.id.bt_system_define_dialog:
                plutoDialog = new PlutoDialog(this,PlutoDialog.DEFAULT,"Title","Dialog show message","left button","right button",this);
                plutoDialog.show();
                break;
            case R.id.bt_loading_dialog:
                plutoDialog = new PlutoDialog(this,PlutoDialog.LOADING);
                plutoDialog.show();
                break;
            case R.id.bt_text_dialog:
                plutoDialog = new PlutoDialog(this, PlutoDialog.TEXT_ONLIY, "MIT License\n" +
                        "\n" +
                        "Copyright (c) 2017 minggo \n" +
                        "email <minggo8en@gmail.com>\n" +
                        "\n" +
                        "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                        "of this software and associated documentation files (the \"Software\"), to deal\n" +
                        "in the Software without restriction, including without limitation the rights\n" +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                        "copies of the Software, and to permit persons to whom the Software is\n" +
                        "furnished to do so, subject to the following conditions:\n" +
                        "\n" +
                        "The above copyright notice and this permission notice shall be included in all\n" +
                        "copies or substantial portions of the Software.\n" +
                        "\n" +
                        "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                        "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                        "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                        "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                        "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                        "SOFTWARE.");

                plutoDialog.show();
                plutoDialog.setCancelable(true);
                break;
            default:
                break;

        }
    }

    @Override
    public void confirm() {
        showToast("confirm button is clicked");
    }

    @Override
    public void cancel() {
        showToast("cancel button is clicked");
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
