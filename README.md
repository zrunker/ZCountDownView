# ZCountDownView
倒计时，自定义实现单个时间倒计时控件，和自定义实现时分秒倒计时控件。

>作者：邹峰立，微博：zrunker，邮箱：zrunker@yahoo.com，微信公众号：书客创作，个人平台：[www.ibooker.cc](http://www.ibooker.cc)。

>本文选自[书客创作](http://www.ibooker.cc)平台第140篇文章。[阅读原文](http://www.ibooker.cc/article/140/detail) 。

![书客创作](http://upload-images.jianshu.io/upload_images/3480018-fe0b6f5909e6cdf4..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

倒计时控件在一些APP当中还是很常见的，倒计时功能实现起来也比较简单，本篇文章就单个倒计时和时分秒倒计时两种倒计时，给大家提供一个倒计时开源框架ZCountDownView，使用该框架能够很容易实现倒计时功能和界面。在讲解该框架之前，首先看看该框架能够实现的效果：

![倒计时效果图](http://upload-images.jianshu.io/upload_images/3480018-af566f42ee56f9d1..gif?imageMogr2/auto-orient/strip)

那么该如何使用该框架呢？

#### 首先引入资源

这里提供两种方式引入资源文件。

1、使用gradle：
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.zrunker:ZCountDownView:v1.1'
}
```
2、使用maven：
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.zrunker</groupId>
	<artifactId>ZCountDownView</artifactId>
	<version>v1.1</version>
</dependency>
```
#### 使用

**一、单个倒计时**

1、引入布局

```
<cc.ibooker.zcountdownviewlib.SingleCountDownView
        android:id="@+id/singleCountDownView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@mipmap/code_border_highlight"
        android:paddingEnd="15dp"
        android:paddingLeft="15dp"
        android:paddingRight="15dp"
        android:paddingStart="15dp"
        android:textColor="#FF7198"
        android:textSize="13sp" />
```

2、设置 - 单个倒计时控件具备TextView一切属性
```
/**
 * 单个倒计时使用
 */
SingleCountDownView singleCountDownView = findViewById(R.id.singleCountDownView);
// 单个倒计时点击事件监听
singleCountDownView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
            singleCountDownView.setTextColor(Color.parseColor("#FF7198"));
            singleCountDownView.setBackgroundResource(R.mipmap.code_border_highlight);
            // 开启倒计时
            singleCountDownView.setTime(90)
                  .setTimeColorHex("#FF7198")
                  .setTimePrefixText("前缀")
                  .setTimeSuffixText("后缀")
                  .startCountDown();
      }
});

// 单个倒计时结束事件监听
singleCountDownView.setSingleCountDownEndListener(new SingleCountDownView.SingleCountDownEndListener() {
      @Override
      public void onSingleCountDownEnd() {
            Toast.makeText(MainActivity.this, "倒计时结束", Toast.LENGTH_SHORT).show();
            // 倒计时结束
            singleCountDownView.setText("获取验证码");
            singleCountDownView.setTextColor(Color.parseColor("#BBBBBB"));
            singleCountDownView.setBackgroundResource(R.mipmap.code_border_normal);
      }
});
```
更多使用方法，这里不在一一展示。

**二、时分秒倒计时**

1、引入布局

```
<cc.ibooker.zcountdownviewlib.CountDownView
        android:id="@+id/countdownView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:layout_marginTop="20dp" />
```

2、设置 - 时分秒倒计时控件具备LinearLayout一切属性
```
CountDownView countdownView = findViewById(R.id.countdownView);

// 基本属性设置
countdownView.setCountTime(111111) // 设置倒计时时间戳
      .setHourTvBackgroundRes(R.mipmap.panicbuy_time)
      .setHourTvTextColorHex("#FFFFFF")
      .setHourTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
      .setHourTvTextSize(21)

      .setHourColonTvBackgroundColorHex("#00FFFFFF")
      .setHourColonTvSize(18, 0)
      .setHourColonTvTextColorHex("#FF7198")
      .setHourColonTvGravity(CountDownView.CountDownViewGravity.GRAVITY_CENTER)
      .setHourColonTvTextSize(21)

      .setMinuteTvBackgroundRes(R.mipmap.panicbuy_time)
      .setMinuteTvTextColorHex("#FFFFFF")
      .setMinuteTvTextSize(21)

      .setMinuteColonTvSize(18, 0)
      .setMinuteColonTvTextColorHex("#FF7198")
      .setMinuteColonTvTextSize(21)

      .setSecondTvBackgroundRes(R.mipmap.panicbuy_time)
      .setSecondTvTextColorHex("#FFFFFF")
      .setSecondTvTextSize(21)

//      .setTimeTvWH(18, 40)
//      .setColonTvSize(30)

      // 开启倒计时
      .startCountDown()

      // 设置倒计时结束监听
      .setCountDownEndListener(new CountDownView.CountDownEndListener() {
            @Override
            public void onCountDownEnd() {
                  Toast.makeText(MainActivity.this, "倒计时结束", Toast.LENGTH_SHORT).show();
            }
      });

// 测试暂停倒计时
new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
            // 暂停倒计时
            countdownView.pauseCountDown();
      }
}, 5000);

// 测试停止倒计时
new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
            // 停止倒计时
            countdownView.stopCountDown();
      }
}, 15000);
```

更多使用方法，这里不在一一展示。


[Github地址](https://github.com/zrunker/ZCountDownView)
[阅读原文](http://www.ibooker.cc/article/140/detail) 

----------
![微信公众号：书客创作](http://upload-images.jianshu.io/upload_images/3480018-cb1666507417f384..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
