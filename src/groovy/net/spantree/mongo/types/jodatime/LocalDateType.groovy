package net.spantree.mongo.types.jodatime

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.grails.datastore.mapping.query.Query.Equals
import org.joda.time.LocalDate
import org.joda.time.ReadablePartial
import org.joda.time.YearMonth

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class LocalDateType extends AbstractMappingAwareCustomTypeMarshaller<LocalDate, DBObject, DBObject> {
	LocalDateType() {
		super(LocalDate)
	}
	
	public DBObject toDBObject(LocalDate value) {
		[
			date: value.toDate(),
			year: value.year,
			month: value.monthOfYear,
			quarter: Math.floor((value.monthOfYear-1)/3)+1 as Integer,
			dayOfMonth: value.dayOfMonth,
			dayOfWeek: value.dayOfWeek
		] as BasicDBObject
	}
	
	@Override
	protected Object writeInternal(PersistentProperty property, String key, LocalDate value, DBObject nativeTarget) {
		if(value) {
			obj = toDBObject(value)
			nativeTarget.put(key, obj)
			return obj
		}
		return
	}
	
	@Override
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, DBObject nativeQuery) {
		if(criterion instanceof Equals) {
			if(criterion.value instanceof ReadablePartial) {
				ReadablePartial v = criterion.value
				if(v instanceof YearMonth || v instanceof LocalDate) {
					nativeQuery["${key}.year"] = v.year
					nativeQuery["${key}.month"] = v.monthOfYear
				}
				if(v instanceof LocalDate) {
					nativeQuery["${key}.dayOfMonth"] = v.dayOfMonth
				}
			}	
		} else {
			throw new RuntimeException("unsupported query type for property $property")
		}
	}
	
	@Override
	protected LocalDate readInternal(PersistentProperty property, String key, DBObject nativeSource) {
	    final value = nativeSource[key]
	    if(value?.year && value?.month && value?.dayOfMonth) {
	        return new LocalDate(value.year, value.month, value.dayOfMonth)
	    }
	    return null
	}
}