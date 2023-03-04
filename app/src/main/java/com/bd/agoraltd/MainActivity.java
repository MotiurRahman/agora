package com.bd.agoraltd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bd.agoraltd.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static String FACEBOOK_URL = "https://www.facebook.com/bdcareerorg";
    public static String FACEBOOK_PAGE_ID = "10804098278";

    private ProgressBar proBar;
    private WebView agoraLtd;
    private static String URL = "https://agorasuperstores.com";

    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (isNetworkConnected()) {
                        agoraLtd.loadUrl(URL);

                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    }

                    return true;
                case R.id.navigation_notifications:
                    if (isNetworkConnected()) {
                      //  agoraLtd.loadUrl("https://agorasuperstores.com/customerFeedback");

                        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                    return true;

                case R.id.navigation_orders:
                    if (isNetworkConnected()) {
                     agoraLtd.loadUrl("https://agorasuperstores.com/order/customerOrders");

                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        // Webview
        agoraLtd = (WebView) findViewById(R.id.web1);
        WebSettings webSettings = agoraLtd.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Improve wevView performance

        agoraLtd.clearCache(true);
        //agoraLtd.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        agoraLtd.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        agoraLtd.getSettings().setAppCacheEnabled(false);
        agoraLtd.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        agoraLtd.setInitialScale(1);
        agoraLtd.getSettings().setDisplayZoomControls(false);
        agoraLtd.getSettings().setBuiltInZoomControls(true);
        agoraLtd.setHorizontalScrollBarEnabled(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);

      // webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
       // webSettings.setEnableSmoothTransition(true);

        if (isNetworkConnected()) {
//            Bundle bundle = getIntent().getExtras();
//            String url = bundle.getString("URL");
            agoraLtd.loadUrl(URL);
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }

        // Get Device token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        System.out.println("Token:"+token);
                       // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        FirebaseMessaging.getInstance().subscribeToTopic("agora_ltd")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        System.out.println("Subscribed to topic successfully");
                    } else {
                        System.out.println("ttopic"+task.getException());
                    }
                }
            });

        //bdJobsCareers.loadUrl("https://scheduler-hcir-int-us1.sec3ure.com/Scheduler?HCIRID=977272&SSOID=977272&token=87ae4b83a2e47c31d480c5749253653a");
        agoraLtd.setWebViewClient(new mywebClient());

        proBar = (ProgressBar) findViewById(R.id.progressBar1);


//        // OneSignal Initialization
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
    }


    //For internet connection

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    //End internet connection


    //For webview progress bar loading

    public class mywebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            proBar.setVisibility(View.GONE);
            //setTitle(view.getTitle());

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            proBar.setVisibility(View.VISIBLE);
            //setTitle("Loading.....");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    //End webview progress bar


    @Override
    public void onBackPressed() {

        if (agoraLtd.canGoBack()) {
            agoraLtd.goBack();
        } else {
            super.onBackPressed();
        }
    }


    ///Animation/////
    @Override
    public void finish() {
        super.finish();
        //  overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // overridePendingTransitionEnter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.close) {
//            finish();
//
//        }


        return super.onOptionsItemSelected(item);
    }

}
