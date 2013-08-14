import net.spantree.mongo.types.jodatime.LocalDateType
import net.spantree.mongo.types.jodatime.LocalDateTimeType
import net.spantree.mongo.types.jodatime.DateTimeType
import net.spantree.mongo.types.jodatime.IntervalType

class MongoJodatimeGrailsPlugin {
	def groupId = 'net.spantree'
	
    // the plugin version
    def version = "0.1.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
		"grails-app/domain/net/spantree/mongo/types/jodatime/DateTimeObject.groovy",
		"grails-app/domain/net/spantree/mongo/types/jodatime/IntervalObject.groovy",
		"grails-app/domain/net/spantree/mongo/types/jodatime/LocalDateObject.groovy",
		"grails-app/domain/net/spantree/mongo/types/jodatime/LocalDateTimeObject.groovy",
    ]

    // TODO Fill in these fields
    def title = "Grails Mongo Jodatime Plugin" // Headline display name of the plugin
    def author = "Gary Turovsky"
    def authorEmail = "gary@spantree.net"
    def description = '''\
Grails plugin for persisting Joda Time types to a Mongo database (using mongodb)
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-mongo-jodatime"

    def license = "APACHE"

    def organization = [ name: "Spantree Technology Group, LLC", url: "http://www.spantree.net/" ]

    def developers = [ [ name: "Gary Turovsky", email: "gary@spantree.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/Spantree/grails-mongo-jodatime" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
       localDateType(LocalDateType)
	   localDateTimeType(LocalDateTimeType)
	   dateTimeType(DateTimeType)
	   intervalType(IntervalType)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
