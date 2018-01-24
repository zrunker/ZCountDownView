package cc.ibooker.zcountdownviewlib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.ibooker.zcountdownviewlib.utils.StringUtil;

/**
 * 自定义倒计时View
 * Created by 邹峰立 on 2018/1/15.
 */
public class CountDownView extends LinearLayout {
    private static final int UPDATE_UI_CODE = 101;

    public enum CountDownViewGravity {
        GRAVITY_CENTER,
        GRAVITY_LEFT,
        GRAVITY_RIGHT,
        GRAVITY_TOP,
        GRAVITY_BOTTOM
    }

    private Context context;
    private TextView hourTv, minuteTv, secondTv, hourColonTv, minuteColonTv;
    private long timeStamp;// 倒计时时间戳
    private boolean isContinue = false;// 是否开启倒计时
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();// 缓存线程池

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    // 初始化方法
    private void init() {
        // 设置总体布局属性
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        // 添加子控件
        // 小时控件
        hourTv = new TextView(context);
        hourTv.setTextColor(Color.parseColor("#FFFFFF"));
        hourTv.setBackgroundColor(Color.parseColor("#FF7198"));
        hourTv.setTextSize(12);
        hourTv.setGravity(Gravity.CENTER);
        this.addView(hourTv);
        // 小时冒号控件
        hourColonTv = new TextView(context);
        hourColonTv.setTextColor(Color.parseColor("#FF7198"));
        hourColonTv.setTextSize(12);
        hourColonTv.setText(R.string.colon);
        hourColonTv.setGravity(Gravity.CENTER);
        this.addView(hourColonTv);
        // 分钟控件
        minuteTv = new TextView(context);
        minuteTv.setTextColor(Color.parseColor("#FFFFFF"));
        minuteTv.setBackgroundColor(Color.parseColor("#FF7198"));
        minuteTv.setTextSize(12);
        minuteTv.setGravity(Gravity.CENTER);
        this.addView(minuteTv);
        // 分钟冒号控件
        minuteColonTv = new TextView(context);
        minuteColonTv.setTextColor(Color.parseColor("#FF7198"));
        minuteColonTv.setTextSize(12);
        minuteColonTv.setText(R.string.colon);
        minuteColonTv.setGravity(Gravity.CENTER);
        this.addView(minuteColonTv);
        // 秒控件
        secondTv = new TextView(context);
        secondTv.setTextColor(Color.parseColor("#FFFFFF"));
        secondTv.setBackgroundColor(Color.parseColor("#FF7198"));
        secondTv.setTextSize(12);
        secondTv.setGravity(Gravity.CENTER);
        this.addView(secondTv);
    }

    /**
     * 设置时间控件尺寸
     *
     * @param width  宽 0取默认值
     * @param height 高 0取默认值
     * @return CountDownView
     */
    public CountDownView setTimeTvWH(int width, int height) {
        if (width > 0 && height > 0) {
            ViewGroup.LayoutParams params = new LayoutParams(width, height);
            hourTv.setLayoutParams(params);
            minuteTv.setLayoutParams(params);
            secondTv.setLayoutParams(params);
        }
        return this;
    }

    /**
     * 设置时间控件字体大小
     *
     * @param size 字体大小
     * @return CountDownView
     */
    public CountDownView setTimeTvSize(float size) {
        hourTv.setTextSize(size);
        minuteTv.setTextSize(size);
        secondTv.setTextSize(size);
        return this;
    }

    /**
     * 设置时间控件字体颜色
     *
     * @param colorHex 字体颜色十六进制 “#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setTimeTvTextColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourTv.setTextColor(color);
        minuteTv.setTextColor(color);
        secondTv.setTextColor(color);
        return this;
    }

    /**
     * 设置时间控件背景
     *
     * @param colorHex 背景颜色16进制“#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setTimeTvBackgroundColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourTv.setBackgroundColor(color);
        minuteTv.setBackgroundColor(color);
        secondTv.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置时间控件背景
     *
     * @param res 背景资源ID
     * @return CountDownView
     */
    public CountDownView setTimeTvBackgroundRes(int res) {
        hourTv.setBackgroundResource(res);
        minuteTv.setBackgroundResource(res);
        secondTv.setBackgroundResource(res);
        return this;
    }

    /**
     * 修改时间控件背景
     *
     * @param drawable 背景资源Drawable
     * @return CountDownView
     */
    public CountDownView setTimeTvBackground(Drawable drawable) {
        if (drawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                hourTv.setBackground(drawable);
                minuteTv.setBackground(drawable);
                secondTv.setBackground(drawable);
            }
        return this;
    }

    /**
     * 设置时间控件内部字体位置 - 默认居中
     *
     * @param countDownViewGravity 左上右下中
     */
    public CountDownView setTimeTvGravity(CountDownViewGravity countDownViewGravity) {
        int gravity = Gravity.CENTER;
        if (countDownViewGravity == CountDownViewGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_LEFT) {
            gravity = Gravity.START;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP;
        }
        hourTv.setGravity(gravity);
        minuteTv.setGravity(gravity);
        secondTv.setGravity(gravity);
        return this;
    }


    /**
     * 设置冒号控件尺寸
     *
     * @param width  宽 0取默认值
     * @param height 高 0取默认值
     * @return CountDownView
     */
    public CountDownView setColonTvWH(int width, int height) {
        ViewGroup.LayoutParams params = new LayoutParams(width, height);
        hourColonTv.setLayoutParams(params);
        minuteColonTv.setLayoutParams(params);
        return this;
    }

    /**
     * 设置冒号控件字体大小
     *
     * @param size 字体大小
     * @return CountDownView
     */
    public CountDownView setColonTvSize(float size) {
        hourColonTv.setTextSize(size);
        minuteColonTv.setTextSize(size);
        return this;
    }

    /**
     * 设置冒号控件字体颜色
     *
     * @param colorHex 字体颜色十六进制 “#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setColonTvTextColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourColonTv.setTextColor(color);
        minuteColonTv.setTextColor(color);
        return this;
    }

    /**
     * 设置冒号控件背景
     *
     * @param colorHex 背景颜色16进制“#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setColonTvBackgroundColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourColonTv.setBackgroundColor(color);
        minuteColonTv.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置冒号控件背景
     *
     * @param res 背景资源ID
     * @return CountDownView
     */
    public CountDownView setColonTvBackgroundRes(int res) {
        hourColonTv.setBackgroundResource(res);
        minuteColonTv.setBackgroundResource(res);
        return this;
    }

    /**
     * 修改冒号控件背景
     *
     * @param drawable 背景资源Drawable
     * @return CountDownView
     */
    public CountDownView setColonTvBackground(Drawable drawable) {
        if (drawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                hourColonTv.setBackground(drawable);
                minuteColonTv.setBackground(drawable);
            }
        return this;
    }

    /**
     * 设置冒号控件内部字体位置 - 默认居中
     *
     * @param countDownViewGravity 左上右下中
     */
    public CountDownView setColonTvGravity(CountDownViewGravity countDownViewGravity) {
        int gravity = Gravity.CENTER;
        if (countDownViewGravity == CountDownViewGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_LEFT) {
            gravity = Gravity.START;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP;
        }
        hourColonTv.setGravity(gravity);
        minuteColonTv.setGravity(gravity);
        return this;
    }


    /**
     * 设置小时控件尺寸
     *
     * @param width  宽 0取默认值
     * @param height 高 0取默认值
     * @return CountDownView
     */
    public CountDownView setHourTvSize(int width, int height) {
        ViewGroup.LayoutParams hourParams = hourTv.getLayoutParams();
        if (hourParams != null) {
            if (width > 0)
                hourParams.width = width;
            if (height > 0)
                hourParams.height = height;
            hourTv.setLayoutParams(hourParams);
        }
        return this;
    }

    /**
     * 设置小时控件背景
     *
     * @param res 背景资源ID
     * @return CountDownView
     */
    public CountDownView setHourTvBackgroundRes(int res) {
        hourTv.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置小时控件背景
     *
     * @param drawable 背景资源Drawable
     * @return CountDownView
     */
    public CountDownView setHourTvBackground(Drawable drawable) {
        if (drawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                hourTv.setBackground(drawable);
            }
        return this;
    }

    /**
     * 设置小时控件背景
     *
     * @param colorHex 背景颜色16进制“#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setHourTvBackgroundColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourTv.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置小时控件字体大小
     *
     * @param size 字体大小
     * @return CountDownView
     */
    public CountDownView setHourTvTextSize(float size) {
        hourTv.setTextSize(size);
        return this;
    }

    /**
     * 设置小时控件字体颜色
     *
     * @param colorHex 字体颜色十六进制 “#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setHourTvTextColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourTv.setTextColor(color);
        return this;
    }

    /**
     * 设置小时控件内部字体位置 - 默认居中
     *
     * @param countDownViewGravity 左上右下中
     */
    public CountDownView setHourTvGravity(CountDownViewGravity countDownViewGravity) {
        int gravity = Gravity.CENTER;
        if (countDownViewGravity == CountDownViewGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_LEFT) {
            gravity = Gravity.START;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP;
        }
        hourTv.setGravity(gravity);
        return this;
    }

    /**
     * 设置小时控件内边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setHourTvPadding(int left, int top, int right, int bottom) {
        hourTv.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 设置小时控件外边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setHourTvMargins(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        minuteTv.setLayoutParams(params);
        return this;
    }

    /**
     * 设置小时控件是否为粗体
     *
     * @param bool true/false
     * @return CountDownView
     */
    public CountDownView setHourTvBold(boolean bool) {
        hourTv.getPaint().setFakeBoldText(bool);
        return this;
    }


    /**
     * 设置分钟控件尺寸
     *
     * @param width  宽 0取默认值
     * @param height 高 0取默认值
     * @return CountDownView
     */
    public CountDownView setMinuteTvSize(int width, int height) {
        ViewGroup.LayoutParams minuteParams = minuteTv.getLayoutParams();
        if (minuteParams != null) {
            if (width > 0)
                minuteParams.width = width;
            if (height > 0)
                minuteParams.height = height;
            minuteTv.setLayoutParams(minuteParams);
        }
        return this;
    }

    /**
     * 设置分钟控件背景
     *
     * @param res 背景资源ID
     * @return CountDownView
     */
    public CountDownView setMinuteTvBackgroundRes(int res) {
        minuteTv.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置分钟控件背景
     *
     * @param drawable 背景资源Drawable
     * @return CountDownView
     */
    public CountDownView setMinuteTvBackground(Drawable drawable) {
        if (drawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                minuteTv.setBackground(drawable);
            }
        return this;
    }

    /**
     * 设置分钟控件背景
     *
     * @param colorHex 背景颜色16进制“#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setMinuteTvBackgroundColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        minuteTv.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置分钟控件字体大小
     *
     * @param size 字体大小
     * @return CountDownView
     */
    public CountDownView setMinuteTvTextSize(float size) {
        minuteTv.setTextSize(size);
        return this;
    }

    /**
     * 设置分钟控件字体颜色
     *
     * @param colorHex 字体颜色十六进制 “#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setMinuteTvTextColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        minuteTv.setTextColor(color);
        return this;
    }

    /**
     * 设置分钟控件内部字体位置 - 默认居中
     *
     * @param countDownViewGravity 左上右下中
     */
    public CountDownView setMinuteTvGravity(CountDownViewGravity countDownViewGravity) {
        int gravity = Gravity.CENTER;
        if (countDownViewGravity == CountDownViewGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_LEFT) {
            gravity = Gravity.START;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP;
        }
        minuteTv.setGravity(gravity);
        return this;
    }

    /**
     * 设置分钟控件内边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setMinuteTvPadding(int left, int top, int right, int bottom) {
        minuteTv.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 设置分钟控件外边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setMinuteTvMargins(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        minuteTv.setLayoutParams(params);
        return this;
    }

    /**
     * 设置分钟控件是否为粗体
     *
     * @param bool true/false
     * @return CountDownView
     */
    public CountDownView setMinuteTvBold(boolean bool) {
        minuteTv.getPaint().setFakeBoldText(bool);
        return this;
    }


    /**
     * 设置秒控件尺寸
     *
     * @param width  宽 0取默认值
     * @param height 高 0取默认值
     * @return CountDownView
     */
    public CountDownView setSecondTvSize(int width, int height) {
        ViewGroup.LayoutParams secondParams = secondTv.getLayoutParams();
        if (secondParams != null) {
            if (width > 0)
                secondParams.width = width;
            if (height > 0)
                secondParams.height = height;
            secondTv.setLayoutParams(secondParams);
        }
        return this;
    }

    /**
     * 设置秒控件背景
     *
     * @param res 背景资源ID
     * @return CountDownView
     */
    public CountDownView setSecondTvBackgroundRes(int res) {
        secondTv.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置秒控件背景
     *
     * @param drawable 背景资源Drawable
     * @return CountDownView
     */
    public CountDownView setSecondTvBackground(Drawable drawable) {
        if (drawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                secondTv.setBackground(drawable);
            }
        return this;
    }

    /**
     * 设置秒控件背景
     *
     * @param colorHex 背景颜色16进制“#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setSecondTvBackgroundColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        secondTv.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置秒控件字体大小
     *
     * @param size 字体大小
     * @return CountDownView
     */
    public CountDownView setSecondTvTextSize(float size) {
        secondTv.setTextSize(size);
        return this;
    }

    /**
     * 设置秒控件字体颜色
     *
     * @param colorHex 字体颜色十六进制 “#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setSecondTvTextColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        secondTv.setTextColor(color);
        return this;
    }

    /**
     * 设置秒控件内部字体位置
     *
     * @param countDownViewGravity 左上右下中
     */
    public CountDownView setSecondTvGravity(CountDownViewGravity countDownViewGravity) {
        int gravity = Gravity.CENTER;
        if (countDownViewGravity == CountDownViewGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_LEFT) {
            gravity = Gravity.START;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP;
        }
        secondTv.setGravity(gravity);
        return this;
    }

    /**
     * 设置秒控件内边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setSecondTvPadding(int left, int top, int right, int bottom) {
        secondTv.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 设置秒控件外边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setSecondTvMargins(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        secondTv.setLayoutParams(params);
        return this;
    }

    /**
     * 设置秒控件是否为粗体
     *
     * @param bool true/false
     * @return CountDownView
     */
    public CountDownView setSecondTvBold(boolean bool) {
        secondTv.getPaint().setFakeBoldText(bool);
        return this;
    }


    /**
     * 设置小时冒号控件尺寸
     *
     * @param width  宽 0取默认值
     * @param height 高 0取默认值
     * @return CountDownView
     */
    public CountDownView setHourColonTvSize(int width, int height) {
        ViewGroup.LayoutParams hourColonParams = hourColonTv.getLayoutParams();
        if (hourColonParams != null) {
            if (width > 0)
                hourColonParams.width = width;
            if (height > 0)
                hourColonParams.height = height;
            hourColonTv.setLayoutParams(hourColonParams);
        }
        return this;
    }

    /**
     * 设置小时冒号控件背景
     *
     * @param res 背景资源ID
     * @return CountDownView
     */
    public CountDownView setHourColonTvBackgroundRes(int res) {
        hourColonTv.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置小时冒号控件背景
     *
     * @param drawable 背景资源Drawable
     * @return CountDownView
     */
    public CountDownView setHourColonTvBackground(Drawable drawable) {
        if (drawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                hourColonTv.setBackground(drawable);
            }
        return this;
    }

    /**
     * 设置小时冒号控件背景
     *
     * @param colorHex 背景颜色16进制“#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setHourColonTvBackgroundColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourColonTv.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置小时冒号控件字体大小
     *
     * @param size 字体大小
     * @return CountDownView
     */
    public CountDownView setHourColonTvTextSize(float size) {
        hourColonTv.setTextSize(size);
        return this;
    }

    /**
     * 设置小时冒号控件字体颜色
     *
     * @param colorHex 字体颜色十六进制 “#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setHourColonTvTextColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        hourColonTv.setTextColor(color);
        return this;
    }

    /**
     * 设置小时冒号控件内部字体位置
     *
     * @param countDownViewGravity 左上右下中
     */
    public CountDownView setHourColonTvGravity(CountDownViewGravity countDownViewGravity) {
        int gravity = Gravity.CENTER;
        if (countDownViewGravity == CountDownViewGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_LEFT) {
            gravity = Gravity.START;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP;
        }
        hourColonTv.setGravity(gravity);
        return this;
    }

    /**
     * 设置小时冒号控件内边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setHourColonTvPadding(int left, int top, int right, int bottom) {
        hourColonTv.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 设置小时冒号控件外边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setHourColonTvMargins(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        hourColonTv.setLayoutParams(params);
        return this;
    }

    /**
     * 设置小时冒号控件是否为粗体
     *
     * @param bool true/false
     * @return CountDownView
     */
    public CountDownView setHourColonTvBold(boolean bool) {
        hourColonTv.getPaint().setFakeBoldText(bool);
        return this;
    }


    /**
     * 设置分钟冒号控件尺寸
     *
     * @param width  宽 0取默认值
     * @param height 高 0取默认值
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvSize(int width, int height) {
        ViewGroup.LayoutParams minuteColonParams = minuteColonTv.getLayoutParams();
        if (minuteColonParams != null) {
            if (width > 0)
                minuteColonParams.width = width;
            if (height > 0)
                minuteColonParams.height = height;
            minuteColonTv.setLayoutParams(minuteColonParams);
        }
        return this;
    }

    /**
     * 设置分钟冒号控件背景
     *
     * @param res 背景资源ID
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvBackgroundRes(int res) {
        minuteColonTv.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置分钟冒号控件背景
     *
     * @param drawable 背景资源Drawable
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvBackground(Drawable drawable) {
        if (drawable != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                minuteColonTv.setBackground(drawable);
            }
        return this;
    }

    /**
     * 设置分钟冒号控件背景
     *
     * @param colorHex 背景颜色16进制“#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvBackgroundColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        minuteColonTv.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置分钟冒号控件字体大小
     *
     * @param size 字体大小
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvTextSize(float size) {
        minuteColonTv.setTextSize(size);
        return this;
    }

    /**
     * 设置分钟冒号控件字体颜色
     *
     * @param colorHex 字体颜色十六进制 “#FFFFFF”
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvTextColorHex(String colorHex) {
        int color = Color.parseColor(colorHex);
        minuteColonTv.setTextColor(color);
        return this;
    }

    /**
     * 设置分钟冒号控件内部字体位置
     *
     * @param countDownViewGravity 左上右下中
     */
    public CountDownView setMinuteColonTvGravity(CountDownViewGravity countDownViewGravity) {
        int gravity = Gravity.CENTER;
        if (countDownViewGravity == CountDownViewGravity.GRAVITY_BOTTOM) {
            gravity = Gravity.BOTTOM;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_CENTER) {
            gravity = Gravity.CENTER;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_LEFT) {
            gravity = Gravity.START;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_RIGHT) {
            gravity = Gravity.END;
        } else if (countDownViewGravity == CountDownViewGravity.GRAVITY_TOP) {
            gravity = Gravity.TOP;
        }
        minuteColonTv.setGravity(gravity);
        return this;
    }

    /**
     * 设置分钟冒号控件内边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvPadding(int left, int top, int right, int bottom) {
        minuteColonTv.setPadding(left, top, right, bottom);
        return this;
    }

    /**
     * 设置分钟冒号控件外边距
     *
     * @param left   左边距 px
     * @param top    上边距 px
     * @param right  右边距 px
     * @param bottom 下边距 px
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvMargins(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        minuteColonTv.setLayoutParams(params);
        return this;
    }

    /**
     * 设置分钟冒号控件是否为粗体
     *
     * @param bool true/false
     * @return CountDownView
     */
    public CountDownView setMinuteColonTvBold(boolean bool) {
        minuteColonTv.getPaint().setFakeBoldText(bool);
        return this;
    }


    /**
     * 设置倒计时-时间戳
     *
     * @param timeStamp 倒计时时间戳，不能大于99小时
     * @return CountDownView
     */
    public CountDownView setCountTime(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    /**
     * 开启倒计时
     *
     * @return CountDownView
     */
    public CountDownView startCountDown() {
        if (timeStamp <= 1) {
            this.isContinue = false;
        } else {
            this.isContinue = true;
            countDown();
        }
        return this;
    }

    /**
     * 暂停倒计时
     *
     * @return CountDownView
     */
    public CountDownView pauseCountDown() {
        this.isContinue = false;
        return this;
    }

    /**
     * 关闭倒计时
     *
     * @return CountDownView
     */
    public CountDownView stopCountDown() {
        this.timeStamp = 0;
        return this;
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
                        isContinue = timeStamp-- > 1;
                        String[] times = StringUtil.secToTimes(timeStamp);
                        Message message = new Message();
                        message.obj = times;
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
     *
     * @param text     显示文本内容
     * @param textView 待显示的控件
     */
    private void updateTvText(String text, TextView textView) {
        textView.setText(text);
    }

    private Handler myHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        // 定义一个对象用来引用Activity中的方法
        private final WeakReference<CountDownView> mCountDownView;

        MyHandler(CountDownView countDownView) {
            mCountDownView = new WeakReference<>(countDownView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CountDownView currentCountDownView = mCountDownView.get();
            switch (msg.what) {
                case UPDATE_UI_CODE:// 刷新UI
                    if (msg.obj != null) {
                        String[] times = (String[]) msg.obj;
                        for (int i = 0; i < times.length; i++) {
                            switch (i) {
                                case 0:// 时
                                    currentCountDownView.updateTvText(times[0], currentCountDownView.hourTv);
                                    break;
                                case 1:// 分
                                    currentCountDownView.updateTvText(times[1], currentCountDownView.minuteTv);
                                    break;
                                case 2:// 秒
                                    currentCountDownView.updateTvText(times[2], currentCountDownView.secondTv);
                                    break;
                            }
                        }
                    }
                    // 倒计时结束
                    if (!currentCountDownView.isContinue) {
                        if (currentCountDownView.countDownEndListener != null)
                            currentCountDownView.countDownEndListener.onCountDownEnd();
                    }
                    break;
            }
        }
    }

    /**
     * 定义倒计时结束接口
     */
    public interface CountDownEndListener {
        void onCountDownEnd();
    }

    private CountDownEndListener countDownEndListener;

    public void setCountDownEndListener(CountDownEndListener countDownEndListener) {
        this.countDownEndListener = countDownEndListener;
    }

}
