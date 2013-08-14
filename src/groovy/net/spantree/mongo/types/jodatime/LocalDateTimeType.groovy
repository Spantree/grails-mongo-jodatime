package net.spantree.mongo.types.jodatime

import net.spantree.mongo.types.jodatime.query.builders.JodaTimeMongoQueryBuilder

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import org.joda.time.LocalDateTime

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class LocalDateTimeType extends AbstractMappingAwareCustomTypeMarshaller<LocalDateTime, DBObject, DBObject> {

	static final DateTime epochZero = new DateTime(1970,1,1,0,0,0,0,DateTimeZone.UTC)

	static final String JODA_TYPE = LocalDateTime.name

	JodaTimeMongoQueryBuilder queryBuilder = new JodaTimeMongoQueryBuilder()

	LocalDateTimeType() {
		super(LocalDateTime)
	}

	DBObject toDBObject(LocalDateTime value) {

		if (!value) {
			return null
		}

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

	@Override
	protected writeInternal(PersistentProperty property, String key, LocalDateTime value, DBObject nativeTarget) {
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
