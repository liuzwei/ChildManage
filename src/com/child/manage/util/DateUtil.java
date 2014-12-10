package com.child.manage.util;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期相关
 * @author dds
 *
 */
public final class DateUtil implements Serializable {

	private static final long serialVersionUID = -3098985139095632110L;

	private DateUtil() {
	}

	/**
	 * 格式化日期字符串
	 * @param sdate 日期字符串
	 * @param format 格式
	 * @return
	 */
	public static String dateFormat(String sdate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		java.sql.Date date = java.sql.Date.valueOf(sdate);
		String dateString = formatter.format(date);

		return dateString;
	}

	/**
	 * 返回日期间隔天数
	 * @param sd 起始日期
	 * @param ed 结束日期
	 * @return
	 */
	public static long getIntervalDays(String sd, String ed) {
		return ((java.sql.Date.valueOf(ed)).getTime() - (java.sql.Date
				.valueOf(sd)).getTime()) / (3600 * 24 * 1000);
	}

	/**
	 * 返回间隔月份
	 * @param beginMonth
	 * @param endMonth
	 * @return
	 */
	public static int getInterval(String beginMonth, String endMonth) {
		int intBeginYear = Integer.parseInt(beginMonth.substring(0, 4));
		int intBeginMonth = Integer.parseInt(beginMonth.substring(beginMonth
				.indexOf("-") + 1));
		int intEndYear = Integer.parseInt(endMonth.substring(0, 4));
		int intEndMonth = Integer.parseInt(endMonth.substring(endMonth
				.indexOf("-") + 1));

		return ((intEndYear - intBeginYear) * 12)
				+ (intEndMonth - intBeginMonth) + 1;
	}

	/**
	 * 返回格式化 日期
	 * @param sDate
	 * @param dateFormat
	 * @return
	 */
	public static Date getDate(String sDate, String dateFormat) {
		SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
		ParsePosition pos = new ParsePosition(0);

		return fmt.parse(sDate, pos);
	}


	/**
	 * 返回当前日期 无时间
	 * @return
	 */
	public static String getCurrentDate() {
		return getFormatDateTime(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 返回当前日期+时间
	 * @return
	 */
	public static String getCurrentDateTime() {
		return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化日期 yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date) {
		return getFormatDateTime(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化当前日期
	 * @param format 格式串
	 * @return
	 */
	public static String getFormatDate(String format) {
		return getFormatDateTime(new Date(), format);
	}


	/**
	 * 格式化
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getFormatDateTime(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}