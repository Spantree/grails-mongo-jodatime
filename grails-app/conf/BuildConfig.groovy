grails.project.work.dir = 'target'

grails.project.repos.default = "spantree"

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	dependencies {
		compile "joda-time:joda-time:2.1"
		test "org.spockframework:spock-grails-support:0.7-groovy-2.0", {
			export = false
		}
	}

	plugins {
		build ':release:2.2.1', ':rest-client-builder:1.0.3', {
			export = false
		}

		compile ":mongodb:1.1.0.GA"

		compile(":spock:0.7") {
			exclude "spock-grails-support"
			export = false
		}
	}
}
