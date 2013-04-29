package net.spantree.mongo.types.jodatime.query.builders

import java.util.Date;
import java.util.List;

import org.grails.datastore.mapping.query.Query;
import org.grails.datastore.mapping.query.Query.Between
import org.grails.datastore.mapping.query.Query.Equals
import org.grails.datastore.mapping.query.Query.GreaterThan
import org.grails.datastore.mapping.query.Query.GreaterThanEquals
import org.grails.datastore.mapping.query.Query.IsNotNull
import org.grails.datastore.mapping.query.Query.IsNull
import org.grails.datastore.mapping.query.Query.LessThan
import org.grails.datastore.mapping.query.Query.LessThanEquals
import org.grails.datastore.mapping.query.Query.NotEquals

import org.grails.datastore.mapping.model.PersistentProperty;
import org.grails.datastore.mapping.mongo.query.MongoQuery;
import org.joda.time.Interval

import com.mongodb.BasicDBObject
import com.mongodb.DBObject;
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.DateTimeZone

class JodaTimeMongoQueryBuilder {
	
	public static String INTERVAL_START = "interval_start"
	public static String INTERVAL_END = "interval_end"
	public static String TIME = "time"
	
	static String MONGO_AND_OPERATOR = '$and'
	
	public Interval toInterval(Object dt) {
		if(dt) {
			switch(dt) {
				case LocalDate:
					DateTime dtTimeStart = dt.toDateTimeAtStartOfDay(DateTimeZone.UTC)
					DateTime dtTimeEnd = dtTimeStart.plusDays(1).minusMillis(1)
					return new Interval(dtTimeStart,dtTimeEnd)
					break
				case LocalDateTime:
					DateTime dtTime = dt.toDateTime(DateTimeZone.UTC)
					return new Interval(dtTime,dtTime)
					break
				case DateTime:
					return new Interval(dt,dt)
					break
				case Interval:
					return dt
					break
			}
		}
	}
	
	
	public buildQuery(PersistentProperty property, String key, String type, Query.PropertyCriterion criterion, DBObject nativeQuery) {
		
		
		if(criterion instanceof Between) {
			
			Interval dtIntervalFrom = toInterval(criterion.from)
			Interval dtIntervalTo = toInterval(criterion.to)
			
			Date dtFrom = new Date(dtIntervalFrom.startMillis)
			Date dtTo = new Date(dtIntervalTo.endMillis)
			
			if(!build(property, key, type, criterion, nativeQuery, dtFrom, dtTo)) {
				throw new RuntimeException("Unable to parse query criterion value ${criterion.value}")
			}
		}
		else {
			Interval dtInterval = toInterval(criterion.value)
			
			Date dtFrom = new Date(dtInterval.startMillis)
			Date dtTo = new Date(dtInterval.endMillis)
			
			if(!build(property, key, type, criterion, nativeQuery, dtFrom, dtTo)) {
				throw new RuntimeException("Unable to parse query criterion value ${criterion.value}")
			}
			
			
		}
	}
	
	boolean build(PersistentProperty property, String key, String type, Between criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			def criteria = []
							
			DBObject ltDbo = new BasicDBObject()
			ltDbo.put(MongoQuery.MONGO_GTE_OPERATOR, fromDt);
		
			DBObject criteriaDbo = new BasicDBObject()
			criteriaDbo["${key}.${INTERVAL_START}"] = ltDbo
			
			criteria << criteriaDbo
			
			DBObject gtDbo = new BasicDBObject()
			gtDbo.put(MongoQuery.MONGO_LTE_OPERATOR, toDt);
		
			criteriaDbo = new BasicDBObject()
			criteriaDbo["${key}.${INTERVAL_END}"] = gtDbo
			
			criteria << criteriaDbo
			
			if(!nativeQuery[MONGO_AND_OPERATOR])
				nativeQuery[MONGO_AND_OPERATOR] = []
				
			nativeQuery[MONGO_AND_OPERATOR] += criteria
			
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, Equals criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			nativeQuery["${key}.jodaType"] = type
			nativeQuery["${key}.${INTERVAL_START}"] = fromDt
			nativeQuery["${key}.${INTERVAL_END}"] = toDt
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, NotEquals criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			def criteria = []
							
			DBObject ltDbo = new BasicDBObject()
			ltDbo.put(MongoQuery.MONGO_LT_OPERATOR, fromDt);
		
			DBObject criteriaDbo = new BasicDBObject()
			criteriaDbo["${key}.${INTERVAL_END}"] = ltDbo
			
			criteria << criteriaDbo
			
			DBObject gtDbo = new BasicDBObject()
			gtDbo.put(MongoQuery.MONGO_GT_OPERATOR, toDt);
		
			criteriaDbo = new BasicDBObject()
			criteriaDbo["${key}.${INTERVAL_START}"] = gtDbo
			
			criteria << criteriaDbo
			
			if(!nativeQuery[MongoQuery.MONGO_OR_OPERATOR])
				nativeQuery[MongoQuery.MONGO_OR_OPERATOR] = []
				
			nativeQuery[MongoQuery.MONGO_OR_OPERATOR] += criteria
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, LessThan criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_LT_OPERATOR, fromDt);
			nativeQuery["${key}.${INTERVAL_END}"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, LessThanEquals criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_LTE_OPERATOR, toDt);
			nativeQuery["${key}.${INTERVAL_END}"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, GreaterThan criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_GT_OPERATOR, toDt);
			nativeQuery["${key}.${INTERVAL_START}"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, GreaterThanEquals criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_GTE_OPERATOR, fromDt);
			nativeQuery["${key}.${INTERVAL_START}"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, IsNotNull criterion, DBObject nativeQuery, Date fromDt, Date toDt) {

		nativeQuery["${key}.jodaType"] = type
		
		DBObject dbo = new BasicDBObject()
		dbo.put(MongoQuery.MONGO_NE_OPERATOR, null);
		nativeQuery["${key}.${INTERVAL_START}"] = dbo
		
		dbo = new BasicDBObject()
		dbo.put(MongoQuery.MONGO_NE_OPERATOR, null);
		nativeQuery["${key}.${INTERVAL_END}"] = dbo
		
		return true
		
	}
	
	boolean build(PersistentProperty property, String key, String type, IsNull criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		nativeQuery["${key}.jodaType"] = type
		nativeQuery["${key}.${INTERVAL_START}"] = null
		nativeQuery["${key}.${INTERVAL_END}"] = null
		
		return true
				
	}
	
	boolean build(PersistentProperty property, String key, String type,  Query.PropertyCriterion criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		return false
	}
}
