package cn.testin.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mac on 2018/5/25.
 */
public class DateUtils {

     /*
      * @author: gaojindan
      * @date: 2018/5/28 下午2:02
      * @des: 前一天的开始时间
      * @param: []
      * @return: 前一天的开始时间的字符串
      */
    public static String getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.add(Calendar.DATE, -1);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f1.format(todayStart.getTime());
    }
     /*
      * @author: gaojindan
      * @date: 2018/5/28 下午2:03
      * @des: 前一天的结束时间
      * @param:
      * @return: 前一天的结束时间的字符串
      */
    public static String getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.add(Calendar.DATE, -1);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);

        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f1.format(todayEnd.getTime());
    }
     /*
      * @author: gaojindan
      * @date: 2018/5/28 下午2:03
      * @des: 昨天的日期
      * @param:
      * @return: 昨天日期的字符串
      */
    public static String getExcelPath(){
        //要生成的文件路径

        Calendar todayStart = Calendar.getInstance();
        todayStart.add(Calendar.DATE, -1);
        Date yesterday = todayStart.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        String tmpdir = formater.format(yesterday);
        return tmpdir;
    }
    /**
     * 获得结束同步的时间戳(当天的0点0分0秒)
     *
     * @return
     */
    public static Date getSyncEnd() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp endTimeStamp = Timestamp.valueOf(dateFormatter.format(now.getTime()));
        return endTimeStamp;
    }
}
