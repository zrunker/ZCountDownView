package cc.ibooker.zcountdownviewlib.utils;

/**
 * 字符串工具
 * Created by 邹峰立 on 2018/1/15.
 */
public class StringUtil {
    /**
     * 整数(秒数)转换为时分秒数组
     *
     * @param time 秒数
     * @return 时分秒
     */
    public static String[] secToTimes(long time) {
        String[] timeStrs = new String[3];
        int hour;
        int minute;
        int second;
        if (time <= 0) {
            timeStrs[0] = "00";
            timeStrs[1] = "00";
            timeStrs[2] = "00";
            return timeStrs;
        } else {
            minute = (int) (time / 60);
            if (minute < 60) {
                second = (int) (time % 60);
                timeStrs[0] = "00";
                timeStrs[1] = unitFormat(minute);
                timeStrs[2] = unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    timeStrs[0] = "99";
                    timeStrs[1] = "59";
                    timeStrs[2] = "59";
                    return timeStrs;
                }
                minute = minute % 60;
                second = (int) (time - hour * 3600 - minute * 60);
                timeStrs[0] = unitFormat(hour);
                timeStrs[1] = unitFormat(minute);
                timeStrs[2] = unitFormat(second);
            }
        }
        return timeStrs;
    }

    // 格式化事件规格
    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
