package es.udc.ws.bikeModel.serviceutil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConversor {

	public static Calendar getCalendar(String stringDate) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = (Date) format.parse(stringDate);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	public static String getDate(Calendar rentableFrom) {

		int day = rentableFrom.get(Calendar.DAY_OF_MONTH);
		int month = rentableFrom.get(Calendar.MONTH) - Calendar.JANUARY + 1;
		int year = rentableFrom.get(Calendar.YEAR);

		String date = new String(year + "-" + month + "-" + day);
		return date;
	}

}
