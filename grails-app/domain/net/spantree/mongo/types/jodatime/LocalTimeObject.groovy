package net.spantree.mongo.types.jodatime

import groovy.transform.ToString
import org.bson.types.ObjectId
import org.joda.time.LocalTime

@ToString(includeNames = true)
class LocalTimeObject {
    static mapWith = 'mongo'

    ObjectId id
    LocalTime jodaLocalTime
}
