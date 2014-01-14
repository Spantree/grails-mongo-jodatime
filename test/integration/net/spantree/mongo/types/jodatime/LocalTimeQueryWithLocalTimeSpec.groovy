package net.spantree.mongo.types.jodatime

import grails.plugin.spock.IntegrationSpec
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime

class LocalTimeQueryWithLocalTimeSpec extends IntegrationSpec {

    LocalTime ltNow = new LocalTime()
    LocalTime ltEarlier = ltNow.minusMillis(10)
    LocalTime ltLater = ltNow.plusMillis(10)

    def setup() {
        LocalDateTimeObject.where {}.deleteAll()

        new LocalTimeObject(jodaLocalTime: ltNow).save(flush: true)
        new LocalTimeObject(jodaLocalTime: ltLater).save(flush: true)
        new LocalTimeObject(jodaLocalTime: ltEarlier).save(flush: true)
        new LocalTimeObject().save(flush: true)
    }

    def "local time equals local time"() {
        when:
        List foundObjs = LocalTimeObject.findAll { jodaLocalTime == ltNow}
        then:
        Collection foundJodaLocalTimes = foundObjs.collect { it.jodaLocalTime }

        assert !foundJodaLocalTimes.contains(ltEarlier)
        assert foundJodaLocalTimes.contains(ltNow)
        assert !foundJodaLocalTimes.contains(ltLater)
    }

    def "local time not equals local time"() {
        when:
        List foundObjs = LocalTimeObject.findAll { jodaLocalTime != ltNow }
        then:
        Collection foundJodaLocalTimes = foundObjs.collect { it.jodaLocalTime }

        assert foundJodaLocalTimes.contains(ltEarlier)
        assert !foundJodaLocalTimes.contains(ltNow)
        assert foundJodaLocalTimes.contains(ltLater)
    }

    def "local time greater than local time"() {
        when:
        List foundObjs = LocalTimeObject.findAll { jodaLocalTime > ltNow }
        then:
        Collection foundJodaLocalTimes = foundObjs.collect { it.jodaLocalTime }

        assert !foundJodaLocalTimes.contains(ltEarlier)
        assert !foundJodaLocalTimes.contains(ltNow)
        assert foundJodaLocalTimes.contains(ltLater)
    }

    def "local date time greater than equals local date"() {
        when:
        List foundObjs = LocalTimeObject.findAll { jodaLocalTime >= ltNow }
        then:
        Collection foundJodaLocalTimes = foundObjs.collect { it.jodaLocalTime }

        assert !foundJodaLocalTimes.contains(ltEarlier)
        assert foundJodaLocalTimes.contains(ltNow)
        assert foundJodaLocalTimes.contains(ltLater)
    }

    def "local date time less than local date"() {
        when:
        List foundObjs = LocalTimeObject.findAll { jodaLocalTime < ltNow }
        then:
        Collection foundJodaLocalTimes = foundObjs.collect { it.jodaLocalTime }

        assert foundJodaLocalTimes.contains(ltEarlier)
        assert !foundJodaLocalTimes.contains(ltNow)
        assert !foundJodaLocalTimes.contains(ltLater)
    }

    def "local date time less than equals local date"() {
        when:
        List foundObjs = LocalTimeObject.findAll { jodaLocalTime <= ltNow }
        then:
        Collection foundJodaLocalTimes = foundObjs.collect { it.jodaLocalTime }

        assert foundJodaLocalTimes.contains(ltEarlier)
        assert foundJodaLocalTimes.contains(ltNow)
        assert !foundJodaLocalTimes.contains(ltLater)
    }

    def "local date time between local date"() {
        when:
        List foundObjs = LocalTimeObject.findAll { jodaLocalTime <= ltLater && jodaLocalTime >= ltEarlier }
        then:
        Collection foundJodaLocalTimes = foundObjs.collect { it.jodaLocalTime }

        assert foundJodaLocalTimes.contains(ltEarlier)
        assert foundJodaLocalTimes.contains(ltNow)
        assert foundJodaLocalTimes.contains(ltLater)
    }
}
