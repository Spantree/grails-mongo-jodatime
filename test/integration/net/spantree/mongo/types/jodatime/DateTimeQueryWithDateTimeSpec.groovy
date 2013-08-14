package net.spantree.mongo.types.jodatime

import grails.plugin.spock.IntegrationSpec

import org.joda.time.DateTime

class DateTimeQueryWithDateTimeSpec extends IntegrationSpec {

	DateTime dtNow = new DateTime().now()
	DateTime dtTomorrow = dtNow.plusDays(1)
	DateTime dtYesterday = dtNow.minusDays(1)

	def setup() {
		DateTimeObject.where{}.deleteAll()

		new DateTimeObject(jodaDate:dtNow).save(flush:true)
		new DateTimeObject(jodaDate:dtTomorrow).save(flush:true)
		new DateTimeObject(jodaDate:dtYesterday).save(flush:true)
		new DateTimeObject().save(flush:true)
	}

	def "date time equals date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDate(dtNow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert !foundJodaDates.contains(dtYesterday)
			assert foundJodaDates.contains(dtNow)
			assert !foundJodaDates.contains(dtTomorrow)
	}

	def "date time not equals date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateNotEqual(dtNow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert foundJodaDates.contains(dtYesterday)
			assert !foundJodaDates.contains(dtNow)
			assert foundJodaDates.contains(dtTomorrow)
	}

	def "date time greater than date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateGreaterThan(dtYesterday)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert !foundJodaDates.contains(dtYesterday)
			assert foundJodaDates.contains(dtNow)
			assert foundJodaDates.contains(dtTomorrow)
	}

	def "date time greater than equals date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateGreaterThanEquals(dtYesterday)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert foundJodaDates.contains(dtYesterday)
			assert foundJodaDates.contains(dtNow)
			assert foundJodaDates.contains(dtTomorrow)
	}

	def "date time less than date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateLessThan(dtTomorrow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert foundJodaDates.contains(dtYesterday)
			assert foundJodaDates.contains(dtNow)
			assert !foundJodaDates.contains(dtTomorrow)
	}

	def "date time less than equals date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateLessThanEquals(dtTomorrow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert foundJodaDates.contains(dtYesterday)
			assert foundJodaDates.contains(dtNow)
			assert foundJodaDates.contains(dtTomorrow)
	}

	def "date time is null date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateIsNull()
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert foundJodaDates.contains(null)
			assert !foundJodaDates.contains(dtYesterday)
			assert !foundJodaDates.contains(dtNow)
			assert !foundJodaDates.contains(dtTomorrow)
	}

	def "date time is not null date time"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateIsNotNull()
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert !foundJodaDates.contains(null)
			assert foundJodaDates.contains(dtYesterday)
			assert foundJodaDates.contains(dtNow)
			assert foundJodaDates.contains(dtTomorrow)
	}

	def "date time between date times"() {
		when:
			List foundObjs = DateTimeObject.findAllByJodaDateBetween(dtYesterday,dtTomorrow)
		then:
			Collection foundJodaDates = foundObjs.collect{it.jodaDate}

			assert foundJodaDates.contains(dtYesterday)
			assert foundJodaDates.contains(dtNow)
			assert foundJodaDates.contains(dtTomorrow)
	}
}
