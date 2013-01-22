grails.servlet.version = "2.5"
grails.project.work.dir = "target/$grailsVersion"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.project.dependency.resolution = {
	
    inherits "global"
	log "warn"
	checksums true
    
	repositories {
		inherits true
		
        grailsPlugins()
        grailsHome()
        grailsCentral()
		
		mavenCentral()
		mavenRepo "http://maven.springframework.org/milestone/"
		mavenRepo "http://download.java.net/maven/2/"
    }
    
	dependencies {
		runtime 'mysql:mysql-connector-java:5.1.13'
	}
	
	plugins {
		runtime "org.grails.plugins:hibernate:$grailsVersion"
		build "org.grails.plugins:tomcat:$grailsVersion"

		compile "org.grails.plugins:cloud-foundry:1.2.3"
		compile "org.grails.plugins:quartz:0.4.2"
		compile "org.grails.plugins:mail:1.0.1"
    }
}
