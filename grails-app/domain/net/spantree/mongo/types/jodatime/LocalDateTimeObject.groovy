package net.spantree.mongo.types.jodatime

import groovy.transform.ToString

import org.bson.types.ObjectId
import org.joda.time.LocalDateTime

@ToString(includeNames=true)
class LocalDateTimeObject {
	static mapWith = 'mongo'

	ObjectId id
	LocalDateTime jodaLocalDate

    static constraints = {
        jodaLocalDate nullable: true
    }
}
