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

import com.mongodb.BasicDBObject
import com.mongodb.DBObject;

abstract class JodaTimeMongoQueryBuilder {

	abstract public Date toDate(Object dtPart)
	
	abstract public List toDateRange(Object dtPart) 
	
	abstract public List toDateRange(Object dtPartFrom, Object dtPartTo)
	
	public buildQuery(PersistentProperty property, String key, String type, Query.PropertyCriterion criterion, DBObject nativeQuery) {
		if(criterion instanceof Between) {
			
			Date fromDt = toDate(criterion.from)
			Date toDt = toDate(criterion.to)
			
			if(!build(property, key, type, criterion, nativeQuery, fromDt, toDt)) {
				List dtRange = toDateRange(criterion.from, criterion.to)
				
				if(!dtRange && build(property, key, type, criterion, nativeQuery, dtRange[0], dtRange[1])) {
					throw new RuntimeException("Unable to parse query criterion value ${criterion.value}")
				}
			}
			
			
		}
		else {
			Date dt = toDate(criterion.value)
			
			if(!build(property, key, type, criterion, nativeQuery, dt)) {
				
				List dtRange = toDateRange(criterion.value)
				
				if(dtRange && !build(property, key, type, criterion, nativeQuery, dtRange[0], dtRange[1])) {
					throw new RuntimeException("Unable to parse query criterion value ${criterion.value}")
				}
			}
			
			
		}
	}
	
	boolean build(PersistentProperty property, String key, String type, Between criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			DBObject dbo = new BasicDBObject()
			nativeQuery["${key}.jodaType"] = type
			dbo.put(MongoQuery.MONGO_GTE_OPERATOR, fromDt);
			dbo.put(MongoQuery.MONGO_LTE_OPERATOR, toDt);
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, Equals criterion, DBObject nativeQuery, Date dt) {
		
		if(dt) {
			nativeQuery["${key}.jodaType"] = type
			nativeQuery["${key}.jodaValue"] = dt
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, Equals criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			DBObject dbo = new BasicDBObject()
			nativeQuery["${key}.jodaType"] = type
			dbo.put(MongoQuery.MONGO_GTE_OPERATOR, fromDt);
			dbo.put(MongoQuery.MONGO_LTE_OPERATOR, toDt);
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, NotEquals criterion, DBObject nativeQuery, Date dt) {
		
		if(dt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_NE_OPERATOR, dt);
			
			nativeQuery["${key}.jodaValue"] = dbo
			
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
			criteriaDbo["${key}.jodaValue"] = ltDbo
			
			criteria << criteriaDbo
			
			DBObject gtDbo = new BasicDBObject()
			gtDbo.put(MongoQuery.MONGO_GT_OPERATOR, toDt);
		
			criteriaDbo = new BasicDBObject()
			criteriaDbo["${key}.jodaValue"] = gtDbo
			
			criteria << criteriaDbo
			
			if(!nativeQuery[MongoQuery.MONGO_OR_OPERATOR])
				nativeQuery[MongoQuery.MONGO_OR_OPERATOR] = []
				
			nativeQuery[MongoQuery.MONGO_OR_OPERATOR] += criteria
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, LessThan criterion, DBObject nativeQuery, Date dt) {
		
		if(dt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_LT_OPERATOR, dt);
			
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, LessThan criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			DBObject dbo = new BasicDBObject()
			nativeQuery["${key}.jodaType"] = type
			dbo.put(MongoQuery.MONGO_LT_OPERATOR, fromDt);
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, LessThanEquals criterion, DBObject nativeQuery, Date dt) {
		
		if(dt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_LTE_OPERATOR, dt);
			
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, LessThanEquals criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			DBObject dbo = new BasicDBObject()
			nativeQuery["${key}.jodaType"] = type
			dbo.put(MongoQuery.MONGO_LTE_OPERATOR, toDt);
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, GreaterThan criterion, DBObject nativeQuery, Date dt) {
		
		if(dt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_GT_OPERATOR, dt);
			
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, GreaterThan criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			DBObject dbo = new BasicDBObject()
			nativeQuery["${key}.jodaType"] = type
			dbo.put(MongoQuery.MONGO_GT_OPERATOR, toDt);
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, GreaterThanEquals criterion, DBObject nativeQuery, Date dt) {
		
		if(dt) {
			nativeQuery["${key}.jodaType"] = type
			
			DBObject dbo = new BasicDBObject()
			dbo.put(MongoQuery.MONGO_GTE_OPERATOR, dt);
			
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, GreaterThanEquals criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		if(fromDt && toDt) {
			DBObject dbo = new BasicDBObject()
			nativeQuery["${key}.jodaType"] = type
			dbo.put(MongoQuery.MONGO_GTE_OPERATOR, fromDt);
			nativeQuery["${key}.jodaValue"] = dbo
			
			return true
		}
		
		return false
		
	}
	
	boolean build(PersistentProperty property, String key, String type, IsNotNull criterion, DBObject nativeQuery, Date dt) {
		
		nativeQuery["${key}.jodaType"] = type
		
		DBObject dbo = new BasicDBObject()
		dbo.put(MongoQuery.MONGO_NE_OPERATOR, null);
		
		nativeQuery["${key}.jodaValue"] = dbo
		
		return true
		
	}
	
	boolean build(PersistentProperty property, String key, String type, IsNotNull criterion, DBObject nativeQuery, Date fromDt, Date toDt) {

		DBObject dbo = new BasicDBObject()
		nativeQuery["${key}.jodaType"] = type
		dbo.put(MongoQuery.MONGO_NE_OPERATOR, null);
		nativeQuery["${key}.jodaValue"] = dbo
		
		return true
		
	}
	
	boolean build(PersistentProperty property, String key, String type, IsNull criterion, DBObject nativeQuery, Date dt) {

		nativeQuery["${key}.jodaType"] = type
		nativeQuery["${key}.jodaValue"] = null
		
		return true
		
	}
	
	boolean build(PersistentProperty property, String key, String type, IsNull criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		
		nativeQuery["${key}.jodaType"] = type
		nativeQuery["${key}.jodaValue"] = null
		
		return true
				
	}
	
	boolean build(PersistentProperty property, String key, String type,  Query.PropertyCriterion criterion, DBObject nativeQuery, Date dt) {
		return false
	}
	
	boolean build(PersistentProperty property, String key, String type,  Query.PropertyCriterion criterion, DBObject nativeQuery, Date fromDt, Date toDt) {
		return false
	}
}
