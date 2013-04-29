package net.spantree.mongo.types.jodatime

import net.spantree.mongo.types.jodatime.query.builders.JodaTimeMongoQueryBuilder
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
import org.joda.time.Interval
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.ReadablePartial
import org.joda.time.DateTimeFieldType
import org.joda.time.Years

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class DateTimeType extends AbstractMappingAwareCustomTypeMarshaller<DateTime, DBObject, DBObject> {
	
	static DateTime epochZero = new DateTime(1970,1,1,0,0,0,0,DateTimeZone.UTC)
	
	static List properties = [
			DateTimeFieldType.DAY_OF_MONTH_TYPE,
			DateTimeFieldType.HOUR_OF_DAY_TYPE,
			DateTimeFieldType.MINUTE_OF_HOUR_TYPE,
			DateTimeFieldType.MILLIS_OF_SECOND_TYPE,
			DateTimeFieldType.MONTH_OF_YEAR_TYPE,
			DateTimeFieldType.YEAR_TYPE
		]
	
	static String JODA_TYPE = DateTime.class.name
	
	JodaTimeMongoQueryBuilder queryBuilder = new JodaTimeMongoQueryBuilder()
	
	DateTimeType() {
		super(DateTime)
	}
	
	public DBObject toDBObject(DateTime value) {
		
		if(value) {
			Interval dtInterval = new Interval(value,value)
			
			BasicDBObject obj = [
				jodaType: JODA_TYPE
			] as BasicDBObject
		
			obj["${queryBuilder.INTERVAL_START}"] = new Date(dtInterval.startMillis)
			obj["${queryBuilder.INTERVAL_END}"] = new Date(dtInterval.endMillis)
			
			obj["jodaField_zone"] = value.zone.toString()
			obj["${queryBuilder.TIME}"] = value.getMillis()
			
			properties.each{  DateTimeFieldType fieldType ->
				obj["jodaField_${fieldType.name}"] = value.get(fieldType)
			}
			return obj
		}
		
		return null
		
	}
	
	@Override
	protected Object writeInternal(PersistentProperty property, String key, DateTime value, DBObject nativeTarget) {
		
		DBObject obj = toDBObject(value)
		nativeTarget.put(key, obj)
		return obj
	}
	
	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, DBObject nativeQuery) {
		queryBuilder.buildQuery(property, key, JODA_TYPE, criterion, nativeQuery)
	}
	
	@Override
	protected DateTime readInternal(PersistentProperty property, String key, DBObject nativeSource) {
		final value = nativeSource[key]
		
		
		if(value?.jodaType == JODA_TYPE) {
			long millis = value["${queryBuilder.TIME}"]
			return epochZero.plus(millis)
		}
		return null
	}
	
}

