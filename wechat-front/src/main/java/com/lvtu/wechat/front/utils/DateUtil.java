package com.lvtu.wechat.front.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DateUtil {
    private static Logger log = Logger.getLogger(DateUtil.class);
	public static final String PATTERN_YYYY_MM_DD_HHMM = "yyyy-MM-dd HH:mm";
	public static final String PATTERN_yyyy_MM_dd = "yyyy-MM-dd";
	public static final SimpleDateFormat PATTERN_YYYY_MM_DD_HHMM_FORMAT = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HHMM);
	public static String dateFormat(java.util.Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				log.info("dateUtil.date="+date);
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 格式化输出日期
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return 返回字符型日期
	 */
	public static java.util.Date parseDate(String dateStr, String format) {
		java.util.Date date = null;
		try {
			java.text.DateFormat df = new java.text.SimpleDateFormat(format);
			date = (java.util.Date) df.parse(dateStr);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 日期格式转换，如2012-12-12 转换成 2012年12月12日 formatString("2012-12-12","yyyy-MM-dd","yyyy年MM月dd日");
	 * @param date    日期字符串 
	 * @param srcData 原字符串格式
	 * @param desDate 目的字符串格式
	 * @return
	 */
	public static String formatString(String date,String srcData,String desDate) {
		try {
			Date d = DateUtil.parseDate(date,srcData);
			return DateUtil.dateFormat(d, desDate);
		}catch(Exception e){
			
		}
		return date;
	}
	
	/**
	 * String转Date
	 * @param sdate 日期字符串
	 * @param fmString 指定日期格式
	 * @return
	 */
	public static Date toDate(String sdate, String fmString) {
		DateFormat df = new SimpleDateFormat(fmString);
		try {
			return df.parse(sdate);
		} catch (ParseException e) {
			throw new RuntimeException("日期格式不正确 ");
		}
	}
	
	
	/**
	 * 日期加天数
	 */
	public static String getStrDayOfMonth(Date date,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return dateFormat(cal.getTime(),"yyyy-MM-dd");
	}
	
	/**
	 * 获取月份的天数 ，比如date当前日期，day ：3  则返回20130803
	 */
	public static Date getDayOfMonth(Date date,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return  cal.getTime();
	}
	
	/**
	 * 月份相加 
	 */
	public static Date getMontOfAdd(Date date,int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+month);
		return  cal.getTime();
	}
	
	/**
	 * 月份相加添加天数 
	 */
	public static Date getDateAddDay(Date date,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+day);
		return  cal.getTime();
	}
	
	/**
	 * 添加小时数 
	 */
	public static Date getDateAddHour(Date date,int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+hours);
		return  cal.getTime();
	}
	
	/**
	 * 获取某个月的最大日期 . 
	 * @param date
	 * @return num 
	 */
	public static int getMaxDayOfMonth(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return  cal.getActualMaximum(Calendar.DAY_OF_MONTH);    
	}
	/**
	 * date 减去当前日期 . 剩余0天0时0分
	 * @return str
	 */
	public static String getRemainTimeByCurrentDate(Date date) {
		String str = "剩余0天0时0分"; 
		if(null != date ) {
			Date d = new Date();
			long seconds = (date.getTime() - d.getTime())/1000;
			if(seconds > 0) { // 秒
				long day = seconds/(3600*24); // 天数
				long house = (seconds%(3600*24)) /3600; // 小时
				long min = (seconds%(3600))/60;// 分
				return "剩" + day+"天"+house+"时"+min+"分";
			}
			
		}
		return str;
	}
	
	/**
	 * 获取当前时间秒数
	 * @return
	 */
	public static long getCurrentTimeLong() {
		return System.currentTimeMillis()/1000l;
	}
	
	
	/**
	 * 是否大于当前日期
	 * @param d    要比较的日期 
	 * @param day  天数 ，正数表示相加 ，负数减去某个天数.
	 * @return true  or  false 
	 */
	public static boolean afterCurrentDate(Date d,int day) {
		try {
			if(null == d) {
				return false;
			}
			// 如果d加一天在当前时间之后返回true . 
			return getDateAddDay(d,1).after(new Date());
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 返回当前时间 毫秒 
	 * @param date
	 * @return
	 */
	public static long getDateTime(String date) {
		if(StringUtils.isEmpty(date)) {
			return 0l;
		}
		Date d = DateUtil.parseDate(date,"yyyy-MM-dd");
		return d.getTime();
	}
	
	/**
	 * 返回当前时间 毫秒 
	 * @param date
	 * @param format
	 * @return
	 */
	public static long getDateTime(String date,String format) {
		if(StringUtils.isEmpty(date)) {
			return 0l;
		}
		Date d = DateUtil.parseDate(date,format);
		return d.getTime();
	}
	
	/**
     * 返回今天0点
     * @param date
     * @return
     */
    public static Date getStartOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }
    
    /**
     * 返回给定日期0点
     * @param date
     * @return
     */
    public static Date getStartOfADay(Calendar calendar) {
        if(null!=calendar){
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            return calendar.getTime();
        }else{
            return null;
        }
    }
    
    /**
     * 返回给定日期0点
     * @param date
     * @return
     */
    public static Date getStartOfADay(Date date) {
        if(null!=date){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            return calendar.getTime();
        }else{
            return null;
        }
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date d = parseDate("2013-08-02","yyyy-MM-dd");
		getDateAddHour(d,1);
		System.out.println(dateFormat(d,"yyyy-MM-dd HH:mm:ss"));
		
		
		/*System.out.println(parseDate("2012-12-12","yyyy-MM-dd"));
		System.out.println(formatString("2012-12-12","yyyy-MM-dd","yyyy年MM月dd日"));
		
		System.out.print(getCurrentTimeLong());*/
		System.out.println(getStartOfToday());
	}

	/**
	 * 分钟相减 
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getMinuteDecrease(Date date,int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - minute);
		return  cal.getTime();
	}

	/**
	 * 根据出生日期得到年龄
	 * @param birthDay 生日
	 */
	public static int getAge(Date birthDay) {
		return getAge(birthDay, new Date());
	}

	/**
	 * 根据出生日期和目标日期得到年龄
	 * @param birthDay 生日
	 * @param fromDate 目标日期
	 * @return 年龄
	 */
	public static int getAge(Date birthDay, Date fromDate) {
		if(birthDay == null || fromDate == null) {
			return -1;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);

		if (cal.before(birthDay)) {
			return -1;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;// 注意此处，如果不加1的话计算结果是错误的
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;
		if (monthNow < monthBirth
				|| (monthNow == monthBirth && dayOfMonthNow < dayOfMonthBirth)) {
			age--;
		}
		return age;
	}
	
	/**
	 * min 以分为单位返回“约X小时X分钟”
	 * @param min
	 * @return
	 */
	public static String getHm(Long min){
		StringBuffer time = new StringBuffer("");
		if(null == min){
			return null;
		}
		Long hour = min/60;
		Long minute = min%60;
		if(0 != hour){
			time.append(hour+"h");
		}
		if(0 != minute){
			time.append(minute+"m");
		}
		return time.toString();
	}

	public static Date getEarliestDate(Date[] dates) {
		Date earliestDate = null;
		for (Date date : dates) {
			if (null == date) {
				continue;
			}

			if (earliestDate == null || date.getTime() < earliestDate.getTime()) {
				earliestDate = date;
			}
		}
		return earliestDate;
	}

	/** * 获取指定日期是星期几

	 * 参数为null时表示获取当前日期是星期几

	 * @param date

	 * @return

	 */

	public static String getWeekOfDate(Date date) {

		String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

		Calendar calendar = Calendar.getInstance();

		if(date != null){

			calendar.setTime(date);

		}

		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (w < 0){

			w = 0;

		}

		return weekOfDays[w];

	}

	
	public static Date parseTimeWithoutSeconds(String str) {
		Date startTime = null;
		try {
			startTime = PATTERN_YYYY_MM_DD_HHMM_FORMAT.parse(str);
		} catch (ParseException e) {
			//ignore
		}
		return startTime;
	}

	public static String formatDate(Date date, String format) {
		return getFormatDate(date, format);
	}

	/**
	 * 格式时间
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getFormatDate(Date date, String format) {
		if (date != null) {
			SimpleDateFormat f = new SimpleDateFormat(format);
			return f.format(date);
		} else {
			return null;
		}
	}
	/**
	 * 两个字符串类型日期大小
	 * @return 1 -1 0
	 */
	public static int compare_date(String DATE1, String DATE2,String pattern) {
		//DATE1比DATE2大吗
		DateFormat df = new SimpleDateFormat(pattern);
		try {
		Date dt1 = df.parse(DATE1);
		Date dt2 = df.parse(DATE2);
		if (dt1.getTime() > dt2.getTime()) {
		return 1;
		} else if (dt1.getTime() < dt2.getTime()) {
		return -1;
		} else {
		return 0;
		}
		} catch (Exception exception) {
		exception.printStackTrace();
		}
		return 0;
		}
	
	
	/**
	 * date1是否早于date2
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean inAdvance(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		return date1.getTime() < date2.getTime();
	}
}
