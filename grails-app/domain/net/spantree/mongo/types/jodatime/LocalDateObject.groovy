package net.spantree.mongo.types.jodatime

import groovy.transform.ToString

import org.bson.types.ObjectId
import org.joda.time.LocalDate

@ToString(includeNames=true)
class LocalDateObject {
	static mapWith = 'mongo'

	ObjectId id
	LocalDate jodaLocalDate
}
