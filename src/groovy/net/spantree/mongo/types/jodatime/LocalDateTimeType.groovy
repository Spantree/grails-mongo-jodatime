package net.spantree.mongo.types.jodatime

import net.spantree.mongo.types.jodatime.query.builders.LocalDateTimeMongoQueryBuilder;

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

class LocalDateTimeType extends AbstractMappingAwareCustomTypeMarshaller<LocalDateTime, DBObject, DBObject> {
	
	static DateTime epochZero = new DateTime(1970,1,1,0,0,0,0,DateTimeZone.UTC)
	
	static String JODA_TYPE = LocalDateTime.class.name
	
	LocalDateTimeMongoQueryBuilder queryBuilder = new LocalDateTimeMongoQueryBuilder()
	
	LocalDateTimeType() {
		super(LocalDateTime)
	}
	
	public DBObject toDBObject(LocalDateTime value) {
		
		if(value) {
			DateTime dtTime = value.toDateTime(DateTimeZone.UTC)
			Interval dtInterval = new Interval(dtTime,dtTime)
			
			BasicDBObject obj = [
				jodaType: JODA_TYPE
			] as BasicDBObject
		
			obj["${queryBuilder.INTERVAL_START}"] = new Date(dtInterval.startMillis)
			obj["${queryBuilder.INTERVAL_END}"] = new Date(dtInterval.endMillis)
			
			value.getFieldTypes().each{  DateTimeFieldType fieldType ->
				obj["jodaField_${fieldType.name}"] = value.get(fieldType)
			}
			
			return obj
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
		
		queryBuilder.buildQuery(property, key, JODA_TYPE, criterion, nativeQuery)
	}
	
	@Override
	protected LocalDateTime readInternal(PersistentProperty property, String key, DBObject nativeSource) {
		final value = nativeSource[key]
		if(value?.jodaType == JODA_TYPE) {
			long millis = value["${queryBuilder.INTERVAL_START}"].getTime()
			return epochZero.plus(millis).toLocalDateTime()
		}
		return null
	}
	
}

