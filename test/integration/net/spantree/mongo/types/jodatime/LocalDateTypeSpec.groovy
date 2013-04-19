package net.spantree.mongo.types.jodatime

import grails.plugin.spock.IntegrationSpec
import org.joda.time.LocalDate
import spock.lang.Specification
import com.gmongo.GMongo
import com.mongodb.DBAddress
import com.mongodb.ServerAddress

class LocalDateTypeSpec extends IntegrationSpec {
	def "local date can be persisted and then read back out"() {
		when:
			LocalDate dt = new LocalDate().now()
			
			LocalDateObject dateObj = new LocalDateObject()
			dateObj.jodaLocalDate = dt
			dateObj.save(flush: true)
			
			dateObj = LocalDateObject.findByJodaLocalDate(dt)
		then:
			assert dateObj?.jodaLocalDate?.equals(dt)
	}
	
	def "local date between query succeeds"() {
		when:
			LocalDate now = new LocalDate().now()
			LocalDate from = now.minusDays(1)
			LocalDate to = now.plusDays(1)
			
			LocalDateObject dateObj = new LocalDateObject()
			dateObj.jodaLocalDate = now
			dateObj.save(flush: true)
			
			List foundObjs = LocalDateObject.findAllByJodaLocalDateBetween(from,to)
		then:
			assert foundObjs
	}
}
