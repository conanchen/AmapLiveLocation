package com.github.conanchen.amap.livelocation;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class AmapLiveLocation {
    private static String TAG = AmapLiveLocation.class.getSimpleName();
    private final AMapLocationClient mAMapLocationClient;

    public LiveData<AMapLocation> locate() {
        return new LiveData<AMapLocation>() {
            @Override
            protected void onActive() {
                mAMapLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        postValue(aMapLocation);
                    }
                });
                mAMapLocationClient.startLocation();
            }

            @Override
            protected void onInactive() {
                if (null != mAMapLocationClient) {
                    mAMapLocationClient.stopLocation();
                }
            }
        };
    }

    private AmapLiveLocation(Context context, AMapLocationClientOption.AMapLocationMode mode, boolean onceLocationLatest, boolean needAddress, boolean wifiScan, boolean mockEnable, long timeout, boolean cache) {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(mode)
                .setOnceLocation(true)
                .setInterval(2000)
                .setNeedAddress(needAddress);

        option.setMockEnable(mockEnable);
        option.setWifiScan(wifiScan);
        option.setHttpTimeOut(timeout);
        option.setLocationCacheEnable(cache);
        option.setOnceLocationLatest(onceLocationLatest);

        mAMapLocationClient = new AMapLocationClient(context.getApplicationContext());
        mAMapLocationClient.setLocationOption(option);
    }


    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Context context;
        private AMapLocationClientOption.AMapLocationMode mode;
        private boolean onceLocationLatest;
        private boolean needAddress;
        private boolean wifiScan;
        private boolean mockEnable;
        private long timeout;
        private boolean cache;

        // set default value 
        private Builder() {
            mode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
            needAddress = true;
            wifiScan = true;
            onceLocationLatest = false;
            timeout = 20000;
            mockEnable = false;
            cache = false;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setMode(AMapLocationClientOption.AMapLocationMode mode) {
            this.mode = mode;
            return this;
        }

        public Builder setOnceLocationLatest(boolean onceLocationLatest) {
            this.onceLocationLatest = onceLocationLatest;
            return this;
        }

        public Builder setNeedAddress(boolean needAddress) {
            this.needAddress = needAddress;
            return this;
        }

        public Builder setWifiScan(boolean wifiScan) {
            this.wifiScan = wifiScan;
            return this;
        }

        public Builder setMockEnable(boolean mockEnable) {
            this.mockEnable = mockEnable;
            return this;
        }

        public Builder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder setCache(boolean cache) {
            this.cache = cache;
            return this;
        }

        public AmapLiveLocation build() {
            String missing = "";
            if (context == null) {
                missing += " context ";
            }

            if (!missing.isEmpty()) {
                throw new IllegalStateException("Missing required properties:" + missing);
            }

            return new AmapLiveLocation(context, mode, onceLocationLatest, needAddress,
                    wifiScan, mockEnable, timeout, cache);
        }
    }
} 