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
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.ReadablePartial
import org.joda.time.DateTimeFieldType

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class LocalDateTimeType extends AbstractMappingAwareCustomTypeMarshaller<LocalDateTime, DBObject, DBObject> {
	
	static String JODA_TYPE = LocalDateTime.class.name
	
	LocalDateTimeType() {
		super(LocalDateTime)
	}
	
	public DBObject toDBObject(LocalDateTime value) {
		
		if(value) {
			return [
				jodaValue: value?.toDate()?:null,
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
	
	public Date toDate(Object dtPart) {
		if(dtPart instanceof ReadablePartial
			&& dtPart.isSupported(DateTimeFieldType.monthOfYear())
			&& dtPart.isSupported(DateTimeFieldType.year())
			&& dtPart.isSupported(DateTimeFieldType.dayOfMonth())
			&& dtPart.isSupported(DateTimeFieldType.hourOfDay())
			&& dtPart.isSupported(DateTimeFieldType.minuteOfHour())
			&& dtPart.isSupported(DateTimeFieldType.secondOfMinute())
			&& dtPart.isSupported(DateTimeFieldType.millisOfSecond())
			) {
				
			new LocalDateTime(dtPart.get(DateTimeFieldType.year()), 
				dtPart.get(DateTimeFieldType.monthOfYear()), 
				dtPart.get(DateTimeFieldType.dayOfMonth()),
				dtPart.get(DateTimeFieldType.hourOfDay()),
				dtPart.get(DateTimeFieldType.minuteOfHour()),
				dtPart.get(DateTimeFieldType.secondOfMinute()),
				dtPart.get(DateTimeFieldType.millisOfSecond()),
				).toDate()
		}
	}
	
	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, DBObject nativeQuery) {

		Date dt = toDate(criterion.value)
		if(dt) {
			DBObject dbo = new BasicDBObject()
			nativeQuery["${key}.jodaType"] = JODA_TYPE
			
			switch(criterion) {
				case Equals:
					nativeQuery["${key}.jodaValue"] = dt
					break
				case NotEquals:
					dbo.put(MongoQuery.MONGO_NE_OPERATOR, dt);
					nativeQuery["${key}.jodaValue"] = dbo
					break
				case LessThan:
					dbo.put(MongoQuery.MONGO_LT_OPERATOR, dt);
					nativeQuery["${key}.jodaValue"] = dbo
					break
				case LessThanEquals:
					dbo.put(MongoQuery.MONGO_LTE_OPERATOR, dt);
					nativeQuery["${key}.jodaValue"] = dbo
					break
				case GreaterThan:
					dbo.put(MongoQuery.MONGO_GT_OPERATOR, dt);
					nativeQuery["${key}.jodaValue"] = dbo
					break
				case GreaterThanEquals:
					dbo.put(MongoQuery.MONGO_GTE_OPERATOR, dt);
					nativeQuery["${key}.jodaValue"] = dbo
					break
				// IsNotNull and IsNull are implemented directly in org.grails.datastore.mapping.mongo.query.MongoQuery
				// but repeated here for completeness
				case IsNotNull:
					dbo.put(MongoQuery.MONGO_NE_OPERATOR, null);
					nativeQuery["${key}.jodaValue"] = dbo
					break
				case IsNull:
					nativeQuery["${key}.jodaValue"] = null
					break
				case Between:
					Date fromDt = dt
					Date toDt = toDate(criterion.to)
					
					dbo.put(MongoQuery.MONGO_GTE_OPERATOR, fromDt);
					dbo.put(MongoQuery.MONGO_LTE_OPERATOR, toDt);
					nativeQuery["${key}.jodaValue"] = dbo
					break
				default:
					throw new RuntimeException("Unsupported query criterion type $criterion for property $property")
			}
		}
		else {
			throw new RuntimeException("Unable to parse query criterion value ${criterion.value}")
		}
	}
	
	@Override
	protected LocalDateTime readInternal(PersistentProperty property, String key, DBObject nativeSource) {
		final value = nativeSource[key]
		if(value?.jodaType == JODA_TYPE) {
			return new LocalDateTime(value.jodaValue)
		}
		return null
	}
}

