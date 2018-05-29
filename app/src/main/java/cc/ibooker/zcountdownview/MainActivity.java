package cc.ibooker.zcountdownview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cc.ibooker.zcountdownviewlib.CountDownView;
import cc.ibooker.zcountdownviewlib.SingleCountDownView;

public class MainActivity extends AppCompatActivity {
    private CountDownView countdownView;
    private SingleCountDownView singleCountDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownView = findViewById(R.id.countdownView);

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

//                .setTimeTvWH(18, 40)
//                .setColonTvSize(30)

                // 开启倒计时
                .startCountDown()

                // 设置倒计时结束监听
                .setCountDownEndListener(new CountDownView.CountDownEndListener() {
                    @Override
                    public void onCountDownEnd() {
                        Toast.makeText(MainActivity.this, "倒计时结束", Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 暂停倒计时
                countdownView.pauseCountDown();
            }
        }, 5000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止倒计时
                countdownView.stopCountDown();
            }
        }, 15000);

        /**
         * 单个倒计时使用
         */
        singleCountDownView = findViewById(R.id.singleCountDownView);
        // 开启倒计时
        singleCountDownView.setTime(60)
                .setDefaultText("默认获取验证吗")
                .setTimeColorHex("#FF7198")
                .setTimePrefixText("前缀")
                .setTimeSuffixText("后缀")
                .startCountDown();
        singleCountDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleCountDownView.setTextColor(Color.parseColor("#FF7198"));
                singleCountDownView.setBackgroundResource(R.mipmap.code_border_highlight);
                singleCountDownView.startCountDown();
            }
        });
        singleCountDownView.setSingleCountDownEndListener(new SingleCountDownView.SingleCountDownEndListener() {
            @Override
            public void onSingleCountDownEnd() {
                Toast.makeText(MainActivity.this, "倒计时结束", Toast.LENGTH_SHORT).show();
                // 倒计时结束
                singleCountDownView.setTextColor(Color.parseColor("#BBBBBB"));
                singleCountDownView.setBackgroundResource(R.mipmap.code_border_normal);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        singleCountDownView.destorySingleCountDownView();
        countdownView.destoryCountDownView();
    }
}
