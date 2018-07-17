package fr.emmanuelroodlyyahoo.nyts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

import fr.emmanuelroodlyyahoo.nyts.R;
import fr.emmanuelroodlyyahoo.nyts.model.Article;

public class ArticleActivity extends AppCompatActivity {


    private ShareActionProvider myShareAction;
    String mylink;
    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        article = (Article) getIntent().getSerializableExtra("article");

        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(article.getWebUrl());
                return  true;
            }
        });
        webView.loadUrl(article.getWebUrl());

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return  true;
    }

    public void onShareLink(MenuItem item) {
        Intent sharelink = new Intent();
        sharelink.setAction(Intent.ACTION_SEND);
        sharelink.setType("text/plain");
        sharelink.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());
        sharelink.putExtra(Intent.EXTRA_SUBJECT, "Go on Navigator");
        startActivity(sharelink);
    }
}
