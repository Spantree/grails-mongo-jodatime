grails.project.work.dir = 'target'

grails.project.fork = [
    test: false, //[maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	dependencies {
		compile "joda-time:joda-time:2.4"
		test "org.spockframework:spock-grails-support:0.7-groovy-2.0", {
			export = false
		}
        //compile('org.mongodb:mongo-java-driver:2.10.1')

    }

	plugins {
		build ':release:3.0.1', ':rest-client-builder:1.0.3', {
			export = false
		}
		compile(":mongodb:3.0.2")
//		compile(":spock:0.7") {
//			exclude "spock-grails-support"
//			export = false
//		}
	}
}
