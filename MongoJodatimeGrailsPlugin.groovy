import net.spantree.mongo.types.jodatime.DateTimeType
import net.spantree.mongo.types.jodatime.IntervalType
import net.spantree.mongo.types.jodatime.LocalDateTimeType
import net.spantree.mongo.types.jodatime.LocalDateType
import net.spantree.mongo.types.jodatime.LocalTimeType

class MongoJodatimeGrailsPlugin {
	def version = "0.1.4"
	def grailsVersion = "2.4 > *"
	def pluginExcludes = [
		"grails-app/domain/**",
	]

	def title = "Grails Mongo Jodatime Plugin"
	def description = 'Persists Joda Time types to a Mongo database (using mongodb)'
	def documentation = "http://grails.org/plugin/mongo-jodatime"

	def license = "APACHE"
	def organization = [name: "Spantree Technology Group, LLC", url: "http://www.spantree.net/"]
	def developers = [[name: "Gary Turovsky", email: "gary@spantree.net"], [name: "Kevin Greene", email: "kevin@spantree.net"]]
	def issueManagement = [system: "GITHUB", url: "https://github.com/Spantree/grails-mongo-jodatime/issues"]
	def scm = [url: "https://github.com/Spantree/grails-mongo-jodatime"]

	def doWithSpring = {
		localDateType(LocalDateType)
		localDateTimeType(LocalDateTimeType)
		dateTimeType(DateTimeType)
		intervalType(IntervalType)
		localTimeType(LocalTimeType)
	}
}
