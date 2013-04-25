package net.spantree.mongo.types.jodatime

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.mongo.query.MongoQuery;
import org.grails.datastore.mapping.query.Query
import org.grails.datastore.mapping.query.Query.Between
import org.grails.datastore.mapping.query.Query.Equals
import org.grails.datastore.mapping.query.Query.GreaterThan
import org.grails.datastore.mapping.query.Query.GreaterThanEquals
import org.grails.datastore.mapping.query.Query.IsNotNull
import org.grails.datastore.mapping.query.Query.IsNull
import org.grails.datastore.mapping.query.Query.LessThan
import org.grails.datastore.mapping.query.Query.LessThanEquals
import org.grails.datastore.mapping.query.Query.NotEquals
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.ReadablePartial
import org.joda.time.DateTimeFieldType
import org.joda.time.Years

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class LocalDateTimeType extends AbstractMappingAwareCustomTypeMarshaller<LocalDateTime, DBObject, DBObject> {
	
	static DateTime epochZero = new DateTime(1970,1,1,0,0,0,0,DateTimeZone.UTC)
	
	static String JODA_TYPE = LocalDateTime.class.name
	
	LocalDateTimeType() {
		super(LocalDateTime)
	}
	
	public DBObject toDBObject(LocalDateTime value) {
		
		if(value) {
			long millis = value.getLocalMillis()
			return [
				jodaValue: new Date(millis),
				jodaType: JODA_TYPE
			] as BasicDBObject
		}
		
		return null
		
	}
	
	@Override
	protected Object writeInternal(PersistentProperty property, String key, LocalDateTime value, DBObject nativeTarget) {
		
		DBObject obj = toDBObject(value)
		nativeTarget.put(key, obj)
		return obj
	}
	
	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, DBObject nativeQuery) {
		
		if(criterion instanceof Between) {
			
			Date fromDt = toDate(criterion.from)
			Date toDt = toDate(criterion.to)
			
			if(!JodaTimeMongoQueryBuilder.build(property, key, JODA_TYPE, criterion, nativeQuery, fromDt, toDt)) {
				List dtRange = toDateRange(criterion.from, criterion.to)
				
				if(!dtRange && JodaTimeMongoQueryBuilder.build(property, key, JODA_TYPE, criterion, nativeQuery, dtRange[0], dtRange[1])) {
					throw new RuntimeException("Unable to parse query criterion value ${criterion.value}")
				}
			}
			
			
		}
		else {
			Date dt = toDate(criterion.value)
			
			if(!JodaTimeMongoQueryBuilder.build(property, key, JODA_TYPE, criterion, nativeQuery, dt)) {
				
				List dtRange = toDateRange(criterion.value)
				
				if(dtRange && !JodaTimeMongoQueryBuilder.build(property, key, JODA_TYPE, criterion, nativeQuery, dtRange[0], dtRange[1])) {
					throw new RuntimeException("Unable to parse query criterion value ${criterion.value}")
				}
			}
			
			
		}
	}
	
	@Override
	protected LocalDateTime readInternal(PersistentProperty property, String key, DBObject nativeSource) {
		final value = nativeSource[key]
		if(value?.jodaType == JODA_TYPE) {
			long millis = value.jodaValue.getTime()
			return epochZero.plus(millis).toLocalDateTime()
		}
		return null
	}
	
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

