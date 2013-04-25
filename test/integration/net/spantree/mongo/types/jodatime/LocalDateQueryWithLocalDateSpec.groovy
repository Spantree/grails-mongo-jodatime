package net.spantree.mongo.types.jodatime

import grails.plugin.spock.IntegrationSpec
import org.joda.time.LocalDate
import spock.lang.Specification
import com.gmongo.GMongo
import com.mongodb.DBAddress
import com.mongodb.ServerAddress
import spock.lang.Shared

class LocalDateQueryWithLocalDateSpec extends IntegrationSpec {

	LocalDate dtNow = new LocalDate().now()
	LocalDate dtTomorrow = dtNow.plusDays(1)
	LocalDate dtYesterday = dtNow.minusDays(1)
	
	def setup() {
		LocalDateObject.where{}.deleteAll()
		
		new LocalDateObject(jodaLocalDate:dtNow).save(flush:true)
		new LocalDateObject(jodaLocalDate:dtTomorrow).save(flush:true)
		new LocalDateObject(jodaLocalDate:dtYesterday).save(flush:true)
		new LocalDateObject().save(flush:true)
	}
	
	def "local date equals"() {
		when:
			LocalDateObject dateObj = LocalDateObject.findByJodaLocalDate(dtNow)
		then:
			assert dateObj?.jodaLocalDate?.equals(dtNow)
	}
	
	def "local date not equals"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateNotEqual(dtNow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date greater than"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateGreaterThan(dtYesterday)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date greater than equals"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateGreaterThanEquals(dtYesterday)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date less than"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateLessThan(dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date less than equals"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateLessThanEquals(dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date is null"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateIsNull()
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(null)
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date is not null"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateIsNotNull()
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert !foundJodaLocalDates.contains(null)
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date between query succeeds"() {
		when:
			List foundObjs = LocalDateObject.findAllByJodaLocalDateBetween(dtYesterday,dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
}
