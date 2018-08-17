package cn.testin.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mac on 2018/5/25.
 */
public class StringUtils {
    private final static Logger logger = LogManager.getLogger(StringUtils.class);

    /**
     * trim字符串
     *
     * @param value
     * @return
     */
    public final static String trimEmptyStr(String value) {
        if (null == value) {
            return value;
        }
        if (value.length() <= 0) {
            return null;
        }
        return value;
    }

    /**
     * 处理时间戳字符串，秒和毫秒都可以处理
     *
     * @param timeNumStr 时间戳字符串
     * @return
     */
    public final static String processTimeSpan(String timeNumStr) {
        long scale = 1l;
        long dateNum = 0l;
        if (null == timeNumStr || timeNumStr.length() == 0) {
            return null;
        }
        timeNumStr = timeNumStr.trim();
        if (timeNumStr.length() == 10) {
            scale = 1000l;
        }

        try {
            dateNum = Long.parseLong(timeNumStr);
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timeSpanDate = new Date(dateNum * scale);
            return formater.format(timeSpanDate);
        } catch (Exception ex) {
            logger.debug("转换时间戳出错，当前时间戳：" + timeNumStr, ex);
            return null;
        }
    }
}
