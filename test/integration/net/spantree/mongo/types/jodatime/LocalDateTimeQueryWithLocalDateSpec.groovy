package net.spantree.mongo.types.jodatime

import grails.plugin.spock.IntegrationSpec
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import spock.lang.Specification
import com.gmongo.GMongo
import com.mongodb.DBAddress
import com.mongodb.ServerAddress
import spock.lang.Shared

class LocalDateTimeQueryWithLocalDateSpec extends IntegrationSpec {

	LocalDateTime dtNow = new LocalDateTime().now()
	LocalDateTime dtTomorrow = dtNow.plusDays(1)
	LocalDateTime dtYesterday = dtNow.minusDays(1)
	
	LocalDate dNow = new LocalDate().now()
	LocalDate dTomorrow = dNow.plusDays(1)
	LocalDate dYesterday = dNow.minusDays(1)
	
	def setup() {
		LocalDateTimeObject.where{}.deleteAll()
		
		new LocalDateTimeObject(jodaLocalDate:dtNow).save(flush:true)
		new LocalDateTimeObject(jodaLocalDate:dtTomorrow).save(flush:true)
		new LocalDateTimeObject(jodaLocalDate:dtYesterday).save(flush:true)
		new LocalDateTimeObject().save(flush:true)
	}
	
	
	def "local date time equals local date"() {
		when:
			List foundObjs = LocalDateTimeObject.findAll{ jodaLocalDate == dNow}
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date time not equals local date"() {
		when:
			List foundObjs = LocalDateTimeObject.findAll{ jodaLocalDate != dNow}
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date time greater than local date"() {
		when:
			List foundObjs = LocalDateTimeObject.findAll{ jodaLocalDate > dNow}
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date time greater than equals local date"() {
		when:
			List foundObjs = LocalDateTimeObject.findAll{ jodaLocalDate >= dNow}
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert !foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date time less than local date"() {
		when:
			List foundObjs = LocalDateTimeObject.findAll{ jodaLocalDate < dNow}
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert !foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date time less than equals local date"() {
		when:
			List foundObjs = LocalDateTimeObject.findAll{ jodaLocalDate <= dNow}
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert !foundJodaLocalDates.contains(dtTomorrow)
	}
	
	def "local date time between local date"() {
		when:
			List foundObjs = LocalDateTimeObject.findAll{ jodaLocalDate <= dTomorrow && jodaLocalDate >= dYesterday}
		then:
			Collection foundJodaLocalDates = foundObjs.collect{it.jodaLocalDate}
			
			assert foundJodaLocalDates.contains(dtYesterday)
			assert foundJodaLocalDates.contains(dtNow)
			assert foundJodaLocalDates.contains(dtTomorrow)
	}

}
