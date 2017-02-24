package com.minggo.plutoandroidexample.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.minggo.pluto.fragment.PlutoFragment;
import com.minggo.plutoandroidexample.R;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minggo on 2017/2/4.
 */
public class BlogFragment extends PlutoFragment {

    private Activity activity;

    private View mainView;
    @BindView(R.id.wb)
    public WebView webView;
    public int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mainView = inflater.inflate(R.layout.fragment_blog, container, false);
        ButterKnife.bind(this, mainView);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContentView(mainView);
        initUI();
    }

    private void initUI() {

        //去除长按菜单
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        // 设置WebView属性，能够执行Javascript脚本
        WebSettings settings = webView.getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
        }
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        if (type == 1) {
            webView.loadUrl("https://m8en.com");
        } else {
            webView.loadUrl("http://www.jianshu.com/u/a5016e728b89");
        }


    }

    @Override
    protected void showData() {
        setContentShown(true);
    }

}
