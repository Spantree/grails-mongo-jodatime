package net.spantree.mongo.types.jodatime

import net.spantree.mongo.types.jodatime.query.builders.JodaTimeMongoQueryBuilder

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import org.joda.time.LocalDate

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class LocalDateType extends AbstractMappingAwareCustomTypeMarshaller<LocalDate, DBObject, DBObject> {
	
	static final String JODA_TYPE = LocalDate.name
	JodaTimeMongoQueryBuilder queryBuilder = new JodaTimeMongoQueryBuilder()

	LocalDateType() {
		super(LocalDate)
	}

	DBObject toDBObject(LocalDate value) {

		if (!value) {
			return null
		}

		DateTime dtTimeStart = value.toDateTimeAtStartOfDay(DateTimeZone.UTC)
		DateTime dtTimeEnd = dtTimeStart.plusDays(1).minusMillis(1)

		Interval dtInterval = new Interval(dtTimeStart,dtTimeEnd)

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
	protected writeInternal(PersistentProperty property, String key, LocalDate value, DBObject nativeTarget) {
		DBObject obj = toDBObject(value)
		nativeTarget.put(key, obj)
		return obj
	}

	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, DBObject nativeQuery) {
		queryBuilder.buildQuery(property, key, JODA_TYPE, criterion, nativeQuery)
	}

	@Override
	protected LocalDate readInternal(PersistentProperty property, String key, DBObject nativeSource) {
	    final value = nativeSource[key]
	    if(value?.jodaType == JODA_TYPE) {
	        return new LocalDate(value["${queryBuilder.INTERVAL_START}"])
	    }
	    return null
	}
}
