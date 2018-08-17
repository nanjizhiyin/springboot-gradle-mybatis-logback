package cn.testin.scheduled;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Diskmanager {
	private static Logger logger = LogManager.getLogger(Diskmanager.class);

	private static String filePath = "usr/dec/";

	private static Timer kTimer = new Timer();
	private static TimerTask kTask = null;
	/**
	 * @Author gaojindan
	 * @Date 18:06 2018/8/1
	 * @Desc 开始清理任务
	 * @Param dayNum:天数
	 * @return
	 **/
	public static void startClear(Integer dayNum){

		//再启动
		if (null != kTask){
			kTask.cancel();
		}
		kTask = new TimerTask() {
			@Override
			public void run() {
				//删除文件
				deleteDir(filePath,dayNum);
			}
		};
		kTimer.schedule(kTask, buildTime(), 1000 * 60 * 60 * 24);
	}
	private static Date buildTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();
		if (time.before(new Date())) {
			//若果当前时间已经是凌晨1点后，需要往后加1天，否则任务会立即执行。
			//很多系统往往系统启动时就需要立即执行一次任务，但下面又需要每天凌晨1点执行，怎么办呢？
			//很简单，就在系统初始化话时单独执行一次任务（不需要用定时器，只是执行那段任务的代码）
			time = addDay(time, 1);
		}
		return time;
	}

	private static Date addDay(Date date, int days) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, days);
		return startDT.getTime();
	}

	 /*
	  * @author: gaojindan
	  * @date: 2018/7/5 下午4:54
	  * @des: 递归删除目录下的所有文件及子目录下所有文件
	  * @param: dir 将要删除的文件目录
	  * @param: fileInterval 时间阀值
	  * @return: 删除结果
	  */
	private static boolean deleteDir(String path,Integer dayNum) {
		File dir = new File(path);
		if (dir.isDirectory()) {
			//这是个文件夹
			String[] children = dir.list();
			if (children.length > 0){
				//递归删除目录中的子目录下的文件
				for (int i=0; i<children.length; i++) {
					String tmpPath = new File(dir, children[i]).getPath();
					deleteDir(tmpPath,dayNum);
				}
			}
			//获取子目录数量
			children = dir.list();
			if (children.length == 0 && !filePath.equals(dir.getPath())){
				//不是指定的目录并且目录为空了,就删除此文件夹吧
				return runDelete(dir,null);
			}else{
				return false;
			}
		}else{
			//这是个文件
			return runDelete(dir,dayNum);
		}
	}

	/*
     * @author: gaojindan
     * @date: 2018/7/5 下午4:54
     * @des: 递归删除目录下的所有文件及子目录下所有文件
     * @param: dir 将要删除的文件目录
     * @param: fileInterval 时间阀值
     * @return: 删除结果
     */
	private static boolean runDelete(File dir,Integer dayNum){

		if (null != dayNum && 0 != dayNum){
			long lastModified =	dir.lastModified();
			long nowTime = new Date().getTime();
			if (nowTime - lastModified > dayNum*24*60*60*1000){
				//N小时之前创建的,可以删除

				Integer sp = Integer.valueOf(100);
				if (sp > 0){
					try {
						//休眠0.1秒
						Thread.sleep(sp);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				boolean success = dir.delete();
				if (success) {
					logger.debug("删除成功:"+dir.getPath());
				}
				return success;
			}else{
				return false;
			}
		}else{
			//可以删除
			boolean success = dir.delete();
			if (success) {
				logger.debug("删除成功:"+dir.getPath());
			}
			return success;
		}
	}
}
