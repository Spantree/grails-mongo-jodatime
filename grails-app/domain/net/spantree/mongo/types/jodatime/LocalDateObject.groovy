package net.spantree.mongo.types.jodatime

import org.bson.types.ObjectId
import org.joda.time.LocalDate
import groovy.transform.ToString

@ToString(includeNames=true)
class LocalDateObject {
	static mapWith = 'mongo'
	
	ObjectId id
	LocalDate jodaLocalDate
}
