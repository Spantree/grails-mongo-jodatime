package net.spantree.mongo.types.jodatime

import groovy.transform.ToString

import org.bson.types.ObjectId
import org.joda.time.DateTime

@ToString(includeNames=true)
class DateTimeObject {
	static mapWith = 'mongo'

	ObjectId id
	DateTime jodaDate
}
