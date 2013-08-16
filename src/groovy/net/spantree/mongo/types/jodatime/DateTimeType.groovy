package net.spantree.mongo.types.jodatime

import net.spantree.mongo.types.jodatime.query.builders.JodaTimeMongoQueryBuilder

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import org.joda.time.DateTimeZone
import org.joda.time.Interval

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class DateTimeType extends AbstractMappingAwareCustomTypeMarshaller<DateTime, DBObject, DBObject> {

	static final DateTime epochZero = new DateTime(1970,1,1,0,0,0,0,DateTimeZone.UTC)

	static List properties = [
			DateTimeFieldType.DAY_OF_MONTH_TYPE,
			DateTimeFieldType.HOUR_OF_DAY_TYPE,
			DateTimeFieldType.MINUTE_OF_HOUR_TYPE,
			DateTimeFieldType.MILLIS_OF_SECOND_TYPE,
			DateTimeFieldType.MONTH_OF_YEAR_TYPE,
			DateTimeFieldType.YEAR_TYPE
	]

	static final String JODA_TYPE = DateTime.name

	JodaTimeMongoQueryBuilder queryBuilder = new JodaTimeMongoQueryBuilder()

	DateTimeType() {
		super(DateTime)
	}

	DBObject toDBObject(DateTime value) {

		if (!value) {
			return null
		}

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

	@Override
	protected writeInternal(PersistentProperty property, String key, DateTime value, DBObject nativeTarget) {
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
