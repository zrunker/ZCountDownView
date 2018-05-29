package cc.ibooker.zcountdownviewlib;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单一控件显示倒计时
 * Created by 邹峰立 on 2018/1/16.
 */
public class SingleCountDownView extends android.support.v7.widget.AppCompatTextView {
    private static final int UPDATE_UI_CODE = 101;
    private int retryInterval = 60, time = 60;
    private boolean isContinue = true;
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private String text = "获取验证码", prefixText = "", timeColorHex = "#FF7198", suffixText = "秒后重发";

    public SingleCountDownView(Context context) {
        this(context, null);
    }

    public SingleCountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleCountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化
    private void init() {
        this.setGravity(Gravity.CENTER);
        this.setText(text);
    }

    /**
     * 设置时间字体颜色
     *
     * @param colorHex 16进制颜色
     * @return SingleCountDownView
     */
    public SingleCountDownView setTimeColorHex(String colorHex) {
        this.timeColorHex = colorHex;
        return this;
    }

    /**
     * 设置文本显示
     *
     * @param text 文本内容
     */
    public SingleCountDownView setDefaultText(String text) {
        this.text = text;
        this.setText(text);
        return this;
    }

    /**
     * 设置时间前缀
     *
     * @param prefixText 时间前缀
     * @return SingleCountDownView
     */
    public SingleCountDownView setTimePrefixText(String prefixText) {
        this.prefixText = prefixText;
        return this;
    }

    /**
     * 设置时间后缀
     *
     * @param suffixText 时间后缀
     * @return SingleCountDownView
     */
    public SingleCountDownView setTimeSuffixText(String suffixText) {
        this.suffixText = suffixText;
        return this;
    }

    /**
     * 设置倒计时时间戳 - 毫秒 默认60
     *
     * @param time 时间戳 - 毫秒
     */
    public SingleCountDownView setTime(int time) {
        this.time = time;
        this.retryInterval = this.time;
        return this;
    }

    /**
     * 开启倒计时
     *
     * @return CountDownView
     */
    public SingleCountDownView startCountDown() {
        this.time = this.retryInterval;
        this.isContinue = true;
        countDown();
        return this;
    }

    /**
     * 暂停倒计时
     *
     * @return CountDownView
     */
    public SingleCountDownView pauseCountDown() {
        this.isContinue = false;
        return this;
    }

    /**
     * 关闭倒计时
     *
     * @return CountDownView
     */
    public SingleCountDownView stopCountDown() {
        this.time = 0;
        return this;
    }

    /**
     * 销毁
     */
    public void destorySingleCountDownView() {
        if (mExecutorService != null)
            mExecutorService.shutdownNow();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }
    }

    /**
     * 实现倒计时的功能
     */
    private void countDown() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isContinue) {
                        isContinue = time-- > 1;
//                        String text = getResources().getString(R.string.single_countdown, time + "");
                        String text = prefixText + "<font color=" + timeColorHex + ">" + time + "</font>" + suffixText;
                        Message message = new Message();
                        message.obj = text;
                        message.what = UPDATE_UI_CODE;
                        myHandler.sendMessage(message);
                        // 沉睡一秒
                        Thread.sleep(1000);
                    }
                    isContinue = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (mExecutorService == null || mExecutorService.isShutdown())
            mExecutorService = Executors.newCachedThreadPool();
        mExecutorService.execute(thread);
    }

    /**
     * 更新UI
     */
    private Handler myHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        // 定义一个对象用来引用Activity中的方法
        private final WeakReference<SingleCountDownView> mSingleCountDownView;

        MyHandler(SingleCountDownView singleCountDownView) {
            mSingleCountDownView = new WeakReference<>(singleCountDownView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SingleCountDownView currentSingleCountDownView = mSingleCountDownView.get();
            switch (msg.what) {
                case UPDATE_UI_CODE:
                    // 刷新UI
                    if (msg.obj != null) {
                        currentSingleCountDownView.setText(Html.fromHtml(msg.obj.toString()));
                        if (currentSingleCountDownView.time < currentSingleCountDownView.retryInterval
                                && currentSingleCountDownView.time > 0) {
                            currentSingleCountDownView.setEnabled(false);
                        } else {
                            currentSingleCountDownView.setEnabled(true);
                        }
                    }
                    // 倒计时结束
                    if (!currentSingleCountDownView.isContinue) {
                        currentSingleCountDownView.setText(currentSingleCountDownView.text);

                        if (currentSingleCountDownView.singleCountDownEndListener != null)
                            currentSingleCountDownView.singleCountDownEndListener.onSingleCountDownEnd();
                    }
                    break;
            }
        }
    }

    /**
     * 定义倒计时结束接口
     */
    public interface SingleCountDownEndListener {
        void onSingleCountDownEnd();
    }

    private SingleCountDownEndListener singleCountDownEndListener;

    public void setSingleCountDownEndListener(SingleCountDownEndListener singleCountDownEndListener) {
        this.singleCountDownEndListener = singleCountDownEndListener;
    }

}
