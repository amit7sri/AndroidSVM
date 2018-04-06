package group22.android.com.assign3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by smanj on 4/10/2017.
 */

public class Scatter3DActivity extends Activity {
    private static final String URL = "file:///android_asset/3d.html";
    private WebView mWebView;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        refreshWebView();

        /*Bundle extras = getIntent().getExtras();
        *//*double[] x = new double[50];
        double[] y = new double[50];
        double[] z = new double[50];*//*
        double[] x = {1, 2, 3};
        double[] y = {5, 6, 8};
        double[] z = {10, 1, 12};*/

       /* mWebView.addJavascriptInterface(new JsHandler(x, y, z), "JSInterface");
        mWebView.loadUrl("file:///android_asset/index.html");*/


        /*mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String user = ((EditText) findViewById(R.id.edit_text)).getText().toString();
                if (user.isEmpty()) {
                    user = "World";
                }
                String javascript = "javascript: document.getElementById('msg').innerHTML='Hello " + user + "!';";
                view.loadUrl(javascript);
            }
        });
        refreshWebView();
        findViewById(R.id.button).setOnClickListener(this);*/
    }

    private void refreshWebView() {
        mWebView.loadUrl(URL);
    }

    /*@Override
    public void onClick(View v) {
        refreshWebView();
    }*/
}
