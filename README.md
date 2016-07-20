RippleSplash
====================
This is a way to run Splash screen with ripple animation.
Support from android 2.2 (API 8+)

Demo
========

  <img src="screenshots/ripplesplash_demo.gif" width="30%">
  
  For testing, you can download this [APK](demo/ripplesplash_demo.apk)  

Usage
========
The library is available on mavenCentral(). Just add the dependency to your build.gradle:
```
compile 'com.github.toanvc:RippleSplash:1.0'
```
And make sure that you enable mavenCentral in build.gradle of project
```
  repositories {
        jcenter()
        //enable mavenCentral
        mavenCentral()
  }
```

Inside main layout, input this view at the parent groupview, and it requires ability to match parent view(overlay all view in main_layout). We should use RelativeLayout or FrameLayout for parent groupview
```
    <com.toan.ripplesplash.RippleOverlayView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        ripple:rp_color="@color/cyan_800"
        ripple:rp_duration="1100"
        ripple:rp_alpha="255"/>
```

We use rp_color with the same of background color for SplashScreen. This makes best performance for our animation.


