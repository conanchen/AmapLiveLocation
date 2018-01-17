package com.github.conanchen.amap.livelocation;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView hello = (TextView) findViewById(R.id.hello);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final AmapLiveLocation amapLiveLocation = AmapLiveLocation.builder()
                .setContext(this)
                .setMode(Hight_Accuracy)                       //定位模式，默认值是 Height_Accuracy
                .setCache(true)                                //缓存策略，默认关闭
                .setOnceLocationLatest(true)                   //获取最近3s内精度最高的一次定位结果，默认关闭
                .setNeedAddress(false)                         //返回地址描述，默认开启
                .setMockEnable(true)                           //外界在定位SDK通过GPS定位时模拟位置，默认关闭
                .setWifiScan(false)                            //主动刷新设备wifi模块,默认开启
                .setTimeout(5000)                              //通过网络定位获取结果的超时时间，默认值是 20000
                .build();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Snackbar.make(view, "准备请求定位权限去测试定位。", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Dexter.withActivity(MainActivity.this)
                                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION
                                        , Manifest.permission.ACCESS_COARSE_LOCATION
                                        , Manifest.permission.READ_PHONE_STATE
                                        , Manifest.permission.READ_EXTERNAL_STORAGE
                                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        Snackbar.make(view, "访问位置权限打开了，测试定位,请等待HelloWorld的变化。", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        amapLiveLocation.locate().observe(MainActivity.this, new Observer<AMapLocation>() {
                                            @Override
                                            public void onChanged(@Nullable AMapLocation aMapLocation) {
                                                hello.setText(String.format("now:%s@(lat,lon)=(%f,%f) address=%s",
                                                        DateFormat.getTimeInstance().format(new Date()),
                                                        aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getAddress()));
                                            }
                                        });

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        Snackbar.make(view, "访问位置权限打开才能定位哦，打开定位权限吧。", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                }).onSameThread()
                                .check();
                        ;

                    }
                }).start();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
