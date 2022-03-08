package com.mwallet.tps.utility;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utilities {

	static final String RNDSTRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static SecureRandom rnd = new SecureRandom();

	public static String getCurrentTimeStamp() {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = new Date();
		return formatter1.format(date);
	}

	public static String getMySQLDateTimeConvertor(String datetimestring) throws Exception {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = formatter1.parse(datetimestring);
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		return formatter2.format(date);

	}

	public static String getDateTimeFormatInFullForDisplay(String datetimestring) throws Exception {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = formatter1.parse(datetimestring);
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMMM-yyyy , EEEE HH:mm:ss");
		return formatter2.format(date);

	}

	public static String getStellarDateConvertor(String datestring) throws Exception {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");// 2021-11-02T05:51:31Z
		java.util.Date date = formatter1.parse(datestring);
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		return formatter2.format(date);

	}

	public static String getMySQLDateConvertor(String datestring) throws Exception {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter1.parse(datestring);
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
		return formatter2.format(date);

	}

	public static String getMySQLDateConvertorForCardDoe(String datestring) throws Exception {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = formatter1.parse(datestring);
		SimpleDateFormat formatter2 = new SimpleDateFormat("MM-yy");
		return formatter2.format(date);

	}

	public static String getMYSQLCurrentTimeStampForDashGraphs() throws Exception {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = new Date();
		return formatter1.format(date);
	}

	public static String getMoneyinDecimalFormat(String toformat) throws ParseException {
		DecimalFormat moneyFormat = new DecimalFormat("#,###,##0.00");
		return moneyFormat.format(Double.parseDouble(toformat)).toString();
	}

	public static String getMoneyinSimpleDecimalFormat(String toformat) throws ParseException {
		DecimalFormat moneyFormat = new DecimalFormat("######0.00");
		return moneyFormat.format(Double.parseDouble(toformat)).toString();
	}

	public static String getMoneyinNoDecimalFormat(String toformat) throws ParseException {
		DecimalFormat moneyFormat = new DecimalFormat("#,###,##0");
		return moneyFormat.format(Double.parseDouble(toformat)).toString();
	}

	public static String getUTCtoISTDateTimeConvertor(String dateTimeInUTCFormat) throws Exception {

		DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = utcFormat.parse(dateTimeInUTCFormat);

		DateFormat pstFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		pstFormat.setTimeZone(TimeZone.getTimeZone("IST"));

		return (pstFormat.format(date));

	}

}
