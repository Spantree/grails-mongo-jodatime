package net.spantree.mongo.types.jodatime

import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.LocalDateTime
import groovy.transform.ToString

@ToString(includeNames=true)
class IntervalObject {
	static mapWith = 'mongo'
	
	ObjectId id
	Interval jodaInterval
}
