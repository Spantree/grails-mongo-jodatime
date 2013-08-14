package net.spantree.mongo.types.jodatime

import groovy.transform.ToString

import org.bson.types.ObjectId
import org.joda.time.Interval

@ToString(includeNames=true)
class IntervalObject {
	static mapWith = 'mongo'

	ObjectId id
	Interval jodaInterval
}
