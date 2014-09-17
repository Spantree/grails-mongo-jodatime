package net.spantree.mongo.types.jodatime

import grails.test.spock.IntegrationSpec
import org.joda.time.DateTime
import org.joda.time.Interval

class IntervalQueryWithIntervalSpec extends IntegrationSpec {

	DateTime dtNow = new DateTime().now()
	DateTime dtTomorrow = dtNow.plusDays(1)
	DateTime dtYesterday = dtNow.minusDays(1)

	Interval iYesterday = new Interval(dtYesterday,dtNow.minusMillis(1))
	Interval iNow = new Interval(dtNow,dtTomorrow.minusMillis(1))
	Interval iTomorrow = new Interval(dtTomorrow,dtTomorrow.plusDays(1).minusMillis(1))

	def setup() {
		IntervalObject.where{}.deleteAll()

        new IntervalObject(jodaInterval: new Interval(dtNow, dtNow.plusYears(1))).save(flush: true)
		new IntervalObject(jodaInterval:iNow).save(flush:true)
		new IntervalObject(jodaInterval:iTomorrow).save(flush:true)
		new IntervalObject(jodaInterval:iYesterday).save(flush:true)
		new IntervalObject().save(flush:true)
	}

	def "interval equals interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaInterval(iNow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert !foundJodaDates.contains(iYesterday)
			assert foundJodaDates.contains(iNow)
			assert !foundJodaDates.contains(iTomorrow)
	}

	def "interval not equals interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaIntervalNotEqual(iNow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert foundJodaDates.contains(iYesterday)
			assert !foundJodaDates.contains(iNow)
			assert foundJodaDates.contains(iTomorrow)
	}

	def "interval greater than interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaIntervalGreaterThan(iYesterday)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert !foundJodaDates.contains(iYesterday)
			assert foundJodaDates.contains(iNow)
			assert foundJodaDates.contains(iTomorrow)
	}

	def "interval greater than equals interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaIntervalGreaterThanEquals(iYesterday)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert foundJodaDates.contains(iYesterday)
			assert foundJodaDates.contains(iNow)
			assert foundJodaDates.contains(iTomorrow)
	}

	def "interval less than interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaIntervalLessThan(iTomorrow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert foundJodaDates.contains(iYesterday)
			assert foundJodaDates.contains(iNow)
			assert !foundJodaDates.contains(iTomorrow)
	}

	def "interval less than equals interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaIntervalLessThanEquals(iTomorrow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert foundJodaDates.contains(iYesterday)
			assert foundJodaDates.contains(iNow)
			assert foundJodaDates.contains(iTomorrow)
	}

	def "interval is null interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaIntervalIsNull()
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert foundJodaDates.contains(null)
			assert !foundJodaDates.contains(iYesterday)
			assert !foundJodaDates.contains(iNow)
			assert !foundJodaDates.contains(iTomorrow)
	}

	def "interval is not null interval"() {
		when:
			List foundObjs = IntervalObject.findAllByJodaIntervalIsNotNull()
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert !foundJodaDates.contains(null)
			assert foundJodaDates.contains(iYesterday)
			assert foundJodaDates.contains(iNow)
			assert foundJodaDates.contains(iTomorrow)
	}

	def "interval between interval"() {
		when:
			List foundObjs = IntervalObject.where{ between('jodaInterval',iYesterday,iTomorrow) }.findAll()
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaInterval}

			assert foundJodaDates.contains(iYesterday)
			assert foundJodaDates.contains(iNow)
			assert foundJodaDates.contains(iTomorrow)
	}
}
