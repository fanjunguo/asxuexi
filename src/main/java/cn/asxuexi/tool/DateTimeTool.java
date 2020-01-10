package cn.asxuexi.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeTool {
	/**
	 * 默认日期字符串格式：yyyy-MM-dd,如：2012-12-12
	 */
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	/**
	 * 默认时间字符串格式：HH:mm:ss,如：12:12:12
	 */
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	/**
	 * 默认日期时间字符串格式：yyyy-MM-dd HH:mm:ss,如：2012-12-12 12:12:12
	 */
	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * 秒级时间戳转化为日期字符串(yyyy-MM-dd)
	 * 
	 * @param ms
	 *            时间戳
	 * @return
	 */
	public String transForDate(Long ms) {
		long j = ms * 1000;
		Date date = new Date(j);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-");
		String format = df.format(date);
		SimpleDateFormat dfa = new SimpleDateFormat("MM-");
		String formata = dfa.format(date);
		SimpleDateFormat dfa1 = new SimpleDateFormat("dd");
		String formata1 = dfa1.format(date);
		String ss = format + formata + formata1;
		return ss;
	}

	/**
	 * 秒级时间戳转化为日期字符串(yyyy-MM-dd)
	 * 
	 * @param secondTimestamp
	 *            秒级时间戳
	 * @return
	 */
	public static String getDateStr(Long secondTimestamp) {
		Instant instant = Instant.ofEpochMilli(secondTimestamp * 1000);
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		String dateStr = localDateTime.format(DATE_FORMATTER);
		return dateStr;
	}

	/**
	 * LocalDate对象格式化为日期字符串(yyyy-MM-dd)
	 * 
	 * @param date
	 *            LocalDate对象
	 * @return
	 */
	public static String getDateStr(LocalDate date) {
		String dateStr = date.format(DATE_FORMATTER);
		return dateStr;
	}

	/**
	 * 将日期字符串(yyyy-MM-dd)转化为秒级时间戳
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @return
	 */
	public static Long getSecondTimestamp(String dateStr) {
		LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
		long epochMilli = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		return epochMilli / 1000;

	}

	/**
	 * @author 张顺
	 * @作用 获取现在的时间
	 * @return 时间 2019-09-29
	 */
	public String getDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String getDateTime = df.format(new Date());// new Date()为获取当前系统时间
		return getDateTime;
	}

	/**
	 * @author 张顺
	 * @param str
	 *            时间 2018-08-28
	 * @return 时间戳
	 */
	public  String dateForTrans(String str) {
		String res = "";
		Date d = null;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			res = "0";
		}
		// 转化成时间戳
		long ts = d.getTime() / 1000;
		res = String.valueOf(ts);
		return res;
	}
}
