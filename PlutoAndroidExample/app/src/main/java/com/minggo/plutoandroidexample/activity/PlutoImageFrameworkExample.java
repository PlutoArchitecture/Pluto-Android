package com.minggo.plutoandroidexample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.bitmap.FinalBitmap;
import com.minggo.plutoandroidexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by minggo on 2017/2/7.
 */
public class PlutoImageFrameworkExample extends PlutoActivity implements OnClickListener {

    @BindView(R.id.bt_load_image)
    public Button loadBt;
    @BindView(R.id.iv_notification)
    public ImageView imageView1;
    @BindView(R.id.iv_charmword)
    public ImageView imageView2;
    @BindView(R.id.iv_2048)
    public ImageView imageView3;
    private FinalBitmap finalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_image_framework_example);
        ButterKnife.bind(this);
        finalBitmap = finalBitmap.create(this);
        finalBitmap.configLoadingImage(R.drawable.pluto_corner);
        finalBitmap.configLoadfailImage(R.drawable.pluto_corner);
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

    @OnClick(R.id.bt_load_image)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_load_image:
                Glide.with(this).load("http://m8en.com:8877/content/logo_battery_notification.png").placeholder(R.drawable.pluto_corner).into(imageView1);
                Glide.with(this).load("http://m8en.com:8877/content/charmword_thumbnail.png").placeholder(R.drawable.pluto_corner).into(imageView2);
                Glide.with(this).load("http://m8en.com:8877/content/logo_2048_thumbnail.png").placeholder(R.drawable.pluto_corner).into(imageView3);
                //finalBitmap.display(imageView1, "http://m8en.com:8877/content/logo_battery_notification.png");
                //finalBitmap.display(imageView2, "http://m8en.com:8877/content/charmword_thumbnail.png");
                //finalBitmap.display(imageView3, "http://m8en.com:8877/content/logo_2048_thumbnail.png");
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
