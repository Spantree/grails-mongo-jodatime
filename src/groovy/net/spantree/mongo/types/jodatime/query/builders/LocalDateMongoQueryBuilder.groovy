package net.spantree.mongo.types.jodatime.query.builders

import java.util.Date;
import java.util.List;


import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.ReadablePartial
import org.joda.time.DateTimeFieldType

class LocalDateMongoQueryBuilder extends JodaTimeMongoQueryBuilder {
	public Date toDate(Object dtPart) {
		if(dtPart instanceof ReadablePartial
			&& dtPart.isSupported(DateTimeFieldType.monthOfYear())
			&& dtPart.isSupported(DateTimeFieldType.year())
			&& dtPart.isSupported(DateTimeFieldType.dayOfMonth())
			) {
				
			new LocalDate(dtPart.get(DateTimeFieldType.year()), dtPart.get(DateTimeFieldType.monthOfYear()), dtPart.get(DateTimeFieldType.dayOfMonth())).toDate()
		}
	}

	@Override
	public List toDateRange(Object dtPart) {
		
	}

	@Override
	public List toDateRange(Object dtPartFrom, Object dtPartTo) {
		
	}
}
