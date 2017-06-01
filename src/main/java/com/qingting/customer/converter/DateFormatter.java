package com.qingting.customer.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateFormatter implements Converter<String, Calendar> {

	@Override
	public Calendar convert(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date utilDate = null;
		try {
			utilDate = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(utilDate);
		return calendar;
		
	}

}
