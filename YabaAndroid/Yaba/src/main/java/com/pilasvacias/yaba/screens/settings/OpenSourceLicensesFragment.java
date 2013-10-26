package com.pilasvacias.yaba.screens.settings;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.pilasvacias.yaba.R;

import butterknife.InjectView;
import butterknife.Views;

public class OpenSourceLicensesFragment extends DialogFragment {

    // Constants
    private static final String LICENSES_FILENAME = "open_source_licenses.html";
    // Inject views
    @InjectView(R.id.openSourceLicenses_webView)
    WebView webView;
    @InjectView(R.id.openSourceLicenses_progressBar)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.open_source_licenses_fragment, container, false);
        Views.inject(this, rootView);
        configureDialog();
        configureWebView();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            //TODO: Modify licenses file to include any license used.
            webView.loadUrl("file:///android_asset/" + LICENSES_FILENAME);
        } else {
            webView.restoreState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    private void configureDialog() {
        Dialog dialog = getDialog();
        dialog.setTitle(R.string.open_source_licenses);
    }

    private void configureWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
    }
}