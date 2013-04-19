package net.spantree.mongo.types.jodatime

import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.mongo.query.MongoQuery;
import org.grails.datastore.mapping.query.Query
import org.grails.datastore.mapping.query.Query.Between
import org.grails.datastore.mapping.query.Query.Equals
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.ReadablePartial
import org.joda.time.DateTimeFieldType

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class LocalDateType extends AbstractMappingAwareCustomTypeMarshaller<LocalDate, DBObject, DBObject> {
	
	static String JODA_TYPE = "LocalDate"
	
	LocalDateType() {
		super(LocalDate)
	}
	
	public DBObject toDBObject(LocalDate value) {
		[
			jodaValue: value.toDate(),
			jodaType: JODA_TYPE
		] as BasicDBObject
	}
	
	@Override
	protected Object writeInternal(PersistentProperty property, String key, LocalDate value, DBObject nativeTarget) {
		if(value) {
			DBObject obj = toDBObject(value)
			nativeTarget.put(key, obj)
			return obj
		}
		return null
	}
	
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
	protected void queryInternal(PersistentProperty property, String key, Query.PropertyCriterion criterion, DBObject nativeQuery) {
		if(criterion instanceof Equals) {
			Date dt = toDate(criterion.value)
			if(dt) { 
				nativeQuery["${key}.jodaType"] = JODA_TYPE
				nativeQuery["${key}.jodaValue"] = dt
			}	
		} 
		else if(criterion instanceof Between) {
			Date fromDt = toDate(criterion.from)
			Date toDt = toDate(criterion.to)
		
			if(fromDt && toDt) {
				nativeQuery["${key}.jodaType"] = JODA_TYPE
				
				DBObject dbo = new BasicDBObject()
				dbo.put(MongoQuery.MONGO_GTE_OPERATOR, fromDt);
				dbo.put(MongoQuery.MONGO_LTE_OPERATOR, toDt);
				
				nativeQuery["${key}.jodaValue"] = dbo
			}
		}
		else {
			throw new RuntimeException("unsupported query type for property $property")
		}
	}
	
	@Override
	protected LocalDate readInternal(PersistentProperty property, String key, DBObject nativeSource) {
	    final value = nativeSource[key]
	    if(value?.jodaType == "LocalDate") {
	        return new LocalDate(value.jodaValue)
	    }
	    return null
	}
}