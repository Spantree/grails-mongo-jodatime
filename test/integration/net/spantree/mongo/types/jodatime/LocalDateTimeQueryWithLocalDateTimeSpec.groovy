package net.spantree.mongo.types.jodatime

import grails.test.spock.IntegrationSpec

import org.joda.time.LocalDateTime

class LocalDateTimeQueryWithLocalDateTimeSpec extends IntegrationSpec {

	LocalDateTime dtNow = new LocalDateTime().now()
	LocalDateTime dtTomorrow = dtNow.plusDays(1)
	LocalDateTime dtYesterday = dtNow.minusDays(1)

	def setup() {
		LocalDateTimeObject.where{}.deleteAll()

        new LocalDateTimeObject(jodaLocalDate: new LocalDateTime().plusYears(1)).save(flush: true)
		new LocalDateTimeObject(jodaLocalDate:dtNow).save(flush: true)
		new LocalDateTimeObject(jodaLocalDate:dtTomorrow).save()
		new LocalDateTimeObject(jodaLocalDate:dtYesterday).save()
		new LocalDateTimeObject().save(flush:true)
	}


	def "local date time equals local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDate(dtNow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert !foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time not equals local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateNotEqual(dtNow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time greater than local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateGreaterThan(dtYesterday)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert !foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time greater than equals local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateGreaterThanEquals(dtYesterday)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time less than local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateLessThan(dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time less than equals local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateLessThanEquals(dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time is null  local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateIsNull()
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert foundJodaLocalDates.contains(null)
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time is not null local date time"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateIsNotNull()
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert !foundJodaLocalDates.contains(null)
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}

	def "local date time between local date times"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateBetween(dtYesterday,dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}

			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}

}
