package com.telegence.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.telegence.app.R;
import com.telegence.app.SimpleClasses.Variables;

public class SupportFragemnt extends Fragment {
    View view;
    Context context;

    ProgressBar progress_bar;
    WebView webView;
    String url="";
    String title;
    TextView title_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_support, container, false);
        context=getContext();

        url = Variables.transaction;
        title_txt=view.findViewById(R.id.title_txt);
        title_txt.setText("Customer Support");

        webView=view.findViewById(R.id.webview);
        progress_bar=view.findViewById(R.id.progress_bar);
        webView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int progress) {
            if(progress>=80) {
                progress_bar.setVisibility(View.GONE);
            }
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return false;
            }
        });


        return view;
    }


}