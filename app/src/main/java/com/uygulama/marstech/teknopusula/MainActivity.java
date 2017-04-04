package com.uygulama.marstech.teknopusula;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView mwebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this).init();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"iletisim@teknopusula.com"});  //developer 's email
                Email.putExtra(Intent.EXTRA_SUBJECT,
                        "Konuyu Ekleyin"); // Email 's Subject
                Email.putExtra(Intent.EXTRA_TEXT, "Sayın Editör," + "");  //Email 's Greeting text
                startActivity(Intent.createChooser(Email, "Mail Gönder:"));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!checkInternetConnection(this)) {
            uyar();
        } else {


            baglan();



        }

    }


    public  boolean checkInternetConnection(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void uyar() {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Bağlantı Hatası");
        alertDialog.setMessage("Lütfen İnternet Bağlantınızı Kontrol Ediniz.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tamam",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (!checkInternetConnection(getApplicationContext())) {
                            uyar();
                        } else {

                            dialog.dismiss();


                        }

                    }
                });
        alertDialog.show();
    }

    public void baglan() {

        //WebView oluşturduk ve hız parametrelerini ekledk
        mwebView = (WebView) this.findViewById(R.id.myWebView);
        mwebView.reload();
        WebSettings webSettings = mwebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mwebView.setWebViewClient(new WebViewClient());
        mwebView.setWebViewClient(new myWebViewClient());
        mwebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        mwebView.loadUrl("https://teknopusula.com");
        mwebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mwebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mwebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mwebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        //çıkış butonu
        if (id == R.id.action_cikis) {
            finish();
            System.exit(0);
        }

        else if (id==R.id.action_refresh) {
            if (!checkInternetConnection(this)) {
                uyar();
            } else {


                mwebView.reload();



            }

        }

        return super.onOptionsItemSelected(item);
    }

    //geri tuşuna basıldığında uygulamadan çıkmaması için geri sayfaya gelmesi için
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyCode) {

                case KeyEvent.KEYCODE_BACK:
                    if (mwebView.canGoBack()) {

                        mwebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_anasayfa) {
            mwebView.loadUrl("https://teknopusula.com/");
        } else if (id == R.id.nav_mobil) {
            mwebView.loadUrl("https://teknopusula.com/mobil/");
        } else if (id == R.id.nav_nasil_yapilir) {
            mwebView.loadUrl("https://teknopusula.com/nasil-yapilir/");
        } else if (id == R.id.nav_oyun) {
            mwebView.loadUrl("https://teknopusula.com/oyun/");
        } else if (id == R.id.nav_egitim) {
            mwebView.loadUrl("https://teknopusula.com/egitim/");
        } else if (id == R.id.nav_sosyal_medya) {
            mwebView.loadUrl("https://teknopusula.com/sosyal-medya/");
        } else if (id == R.id.nav_hakkimizda) {
            mwebView.loadUrl("https://teknopusula.com/hakkimizda/");
        } else if (id == R.id.nav_iletisim) {
            mwebView.loadUrl("https://teknopusula.com/iletisim/");


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Progress Bar : Sayfa yüklenirken mesaj vermek
    private class myWebViewClient extends WebViewClient {
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            pd = new ProgressDialog(MainActivity.this);

            if (!checkInternetConnection(getApplicationContext())) {
                uyar();
            } else {


                pd.setTitle("Lütfen Bekleyiniz...");
                pd.setMessage("Sayfa Yükleniyor...");
                pd.show();

            }

        }

        //Sayfa yüklenince progress barı kapat
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            pd.dismiss();
        }
    }







}