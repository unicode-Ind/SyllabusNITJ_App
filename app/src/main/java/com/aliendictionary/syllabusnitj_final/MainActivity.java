package com.aliendictionary.syllabusnitj_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;

    private SwipeRefreshLayout refreshLayout;

    private RelativeLayout noInternetLayout;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            if(msg.what ==1){
                myWebView.goBack();
            }else{
                myWebView.clearCache(true);
            }
//            switch (msg.what){
//                case 1:
//                    myWebView.goBack();
//                    break;
//            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding by id
        myWebView = (WebView) findViewById(R.id.webview);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.webviewReload);
        //noInternetLayout = (RelativeLayout) findViewById(R.id.no_internet);


        //add listener to reload
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWebView.reload();
            }
        });


        myWebView.setWebChromeClient(new MyChromeClient());
        myWebView.setWebViewClient(new BrowserClient(refreshLayout));
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        //Optimizations : improving loading speed
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCacheEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setEnableSmoothTransition(true);
        //optimization ends here

        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK
                        && event.getAction()== MotionEvent.ACTION_UP
                        && myWebView.canGoBack())
                {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }
        });

        loadWebPage();

    }

    private void loadWebPage(){

        ConnectivityManager connectivityManager =
                (ConnectivityManager) MainActivity.this.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            myWebView.loadUrl("https://unicode-ind.github.io/arvin/");
//            noInternetLayout.setVisibility(View.GONE);
//            myWebView.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this,"You don't have an active Internet Connection",Toast.LENGTH_SHORT).show();

//            noInternetLayout.setVisibility(View.VISIBLE);
//            myWebView.setVisibility(View.GONE);
        }
    }

//    public void reconnect(View view) {
//        loadWebPage();
//    }
}