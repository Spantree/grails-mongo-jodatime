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
import org.joda.time.LocalDate
import org.joda.time.ReadablePartial
import org.joda.time.DateTimeFieldType

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import java.util.List;


class LocalDateType extends AbstractMappingAwareCustomTypeMarshaller<LocalDate, DBObject, DBObject> {
	
	static String JODA_TYPE = LocalDate.class.name
	
	LocalDateType() {
		super(LocalDate)
	}
	
	public DBObject toDBObject(LocalDate value) {
		
		if(value) {
			return [
				jodaValue: value?.toDate()?:null,
				jodaType: JODA_TYPE
			] as BasicDBObject
		}
		
		return null
		
	}
	
	@Override
	protected Object writeInternal(PersistentProperty property, String key, LocalDate value, DBObject nativeTarget) {
		
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
	protected LocalDate readInternal(PersistentProperty property, String key, DBObject nativeSource) {
	    final value = nativeSource[key]
	    if(value?.jodaType == JODA_TYPE) {
	        return new LocalDate(value.jodaValue)
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
	
	public List toDateRange(Object dtPart) {
		
	}
	
	public List toDateRange(Object dtPartFrom, Object dtPartTo) {
		
	}
}