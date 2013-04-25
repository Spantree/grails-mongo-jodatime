package net.spantree.mongo.types.jodatime.query.builders;

import java.util.Date;
import java.util.List;


import org.grails.datastore.mapping.model.PersistentProperty;
import org.grails.datastore.mapping.query.Query;
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.ReadablePartial
import org.joda.time.DateTimeFieldType
import org.joda.time.Years

import com.mongodb.DBObject;

public class LocalDateTimeMongoQueryBuilder extends JodaTimeMongoQueryBuilder{
	
	public Date toDate(Object dtPart) {
		
		switch(dtPart) {
			case DateTime:
				return new Date(dtPart.toLocalDateTime().getLocalMillis())
			case LocalDateTime:
				return new Date(dtPart.toDateTime(DateTimeZone.UTC).toLocalDateTime().getLocalMillis())
			case ReadablePartial:
				if(dtPart.isSupported(DateTimeFieldType.monthOfYear())
					&& dtPart.isSupported(DateTimeFieldType.year())
					&& dtPart.isSupported(DateTimeFieldType.dayOfMonth())
					&& dtPart.isSupported(DateTimeFieldType.hourOfDay())
					&& dtPart.isSupported(DateTimeFieldType.minuteOfHour())
					&& dtPart.isSupported(DateTimeFieldType.secondOfMinute())
					&& dtPart.isSupported(DateTimeFieldType.millisOfSecond())
					) {
						
					new Date( new LocalDateTime(dtPart.get(DateTimeFieldType.year()),
						dtPart.get(DateTimeFieldType.monthOfYear()),
						dtPart.get(DateTimeFieldType.dayOfMonth()),
						dtPart.get(DateTimeFieldType.hourOfDay()),
						dtPart.get(DateTimeFieldType.minuteOfHour()),
						dtPart.get(DateTimeFieldType.secondOfMinute()),
						dtPart.get(DateTimeFieldType.millisOfSecond())
						).getLocalMillis() )
						
				}
		}
	}
	
	public List toDateRange(Object dtPart) {
		
		switch(dtPart) {
			case LocalDate:
				DateTime from = dtPart.toDateTime(LocalTime.MIDNIGHT,DateTimeZone.UTC)
				DateTime to = from.plusDays(1).minusMillis(1)
				
				return [new Date(from.getMillis()), new Date(to.getMillis())]
		}
	}
	
	public List toDateRange(Object dtPartFrom, Object dtPartTo) {
		
		if(dtPartFrom.class == dtPartTo.class) {
			switch(dtPartFrom) {
				case LocalDate:
					DateTime from = dtPartFrom.toDateTime(LocalTime.MIDNIGHT,DateTimeZone.UTC)
					DateTime to = dtPartTo.toDateTime(LocalTime.MIDNIGHT,DateTimeZone.UTC).plusDays(1).minusMillis(1)
					
					return [new Date(from.getMillis()), new Date(to.getMillis())]
			}
		}
	}
	
}
