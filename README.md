# AmapLiveLocation
Live Location with AMAP and Android Architecture Component 

[![](https://jitpack.io/v/conanchen/AmapLiveLocation.svg)](https://jitpack.io/#conanchen/AmapLiveLocation)

# Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

# Step 2. Add the dependency

```gradle
dependencies {
    compile 'com.github.conanchen:AmapLiveLocation:1.0.3'
}
```

# Step3. Update AndroidManifest.xml
```
<application ...>
    <meta-data android:name="com.amap.api.v2.apikey" android:value="your amap api key">
        //高德开放平台申请的key
    </meta-data>
</application>
```
