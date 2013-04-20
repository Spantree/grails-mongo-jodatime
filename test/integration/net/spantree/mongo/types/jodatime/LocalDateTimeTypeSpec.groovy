package net.spantree.mongo.types.jodatime

import grails.plugin.spock.IntegrationSpec
import org.joda.time.LocalDateTime
import spock.lang.Specification
import com.gmongo.GMongo
import com.mongodb.DBAddress
import com.mongodb.ServerAddress
import spock.lang.Shared

class LocalDateTimeTypeSpec extends IntegrationSpec {

	LocalDateTime dtNow = new LocalDateTime().now()
	LocalDateTime dtTomorrow = dtNow.plusDays(1)
	LocalDateTime dtYesterday = dtNow.minusDays(1)
	
	def setup() {
		LocalDateTimeObject.where{}.deleteAll()
		
		new LocalDateTimeObject(jodaLocalDate:dtNow).save(flush:true)
		new LocalDateTimeObject(jodaLocalDate:dtTomorrow).save(flush:true)
		new LocalDateTimeObject(jodaLocalDate:dtYesterday).save(flush:true)
		new LocalDateTimeObject().save(flush:true)
	}
	
	def "local date equals"() {
		when:
			LocalDateTimeObject dateObj = LocalDateTimeObject.findByJodaLocalDate(dtNow)
		then:
			assert dateObj?.jodaLocalDate?.equals(dtNow)
	}
	
	def "local date not equals"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateNotEqual(dtNow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date greater than"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateGreaterThan(dtYesterday)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date greater than equals"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateGreaterThanEquals(dtYesterday)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date less than"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateLessThan(dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date less than equals"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateLessThanEquals(dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date is null"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateIsNull()
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(null)
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date is not null"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateIsNotNull()
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert !foundJodaLocalDates.contains(null)
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date between query succeeds"() {
		when:
			List foundObjs = LocalDateTimeObject.findAllByJodaLocalDateBetween(dtYesterday,dtTomorrow)
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
}
