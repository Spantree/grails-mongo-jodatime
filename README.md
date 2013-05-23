# Grails Mongo Joda Time GORM Support Plugin

## Introduction
This is a plugin that works with the [Grails MongoDB GORM plugin](http://grails.org/plugin/mongodb) to allow you to persist and work with Joda Time objects in your Mongo database.  By default, the MongoDB plugin can persist [java.util.Date](http://docs.oracle.com/javase/6/docs/api/java/util/Date.html) objects, which are mostly deprecated and a general pain to use.

## Design goals

### Querying
We **don't** want this:

```groovy
class DomainClass {
	Date someDate //ugh, Date objects are a pain!
}
```

We **would** like this:

```groovy
class DomainClass {
	DateTime someDate //yay, Joda Time is useful!
}
```

And then we'd like to be able to be able to do queries like this:

```groovy
//Find by using Joda Time objects
DomainClass.findBySomeDate(new DateTime())

//Find by comparing different Joda Time objects to each other
DomainClass.findAll{ someDate == new LocalDate()}
```

### Persistence
We want the plugin to lay out our date objects in such a way that they're easy to query from outside the Grails environment. And, this aspect should eventually be configurable.

Here is how the above DomainClass currently looks when it is persisted:

```json
{
  "_id" : ObjectId( "517f0ef83d1af39278d7afad" ),
  "someDate" : {
    "jodaType" : "org.joda.time.DateTime",
    "interval_start" : Date( 1367281400512 ),
    "interval_end" : Date( 1367281400512 ),
    "jodaField_zone" : "America/Chicago",
    "time" : 1367281400512,
    "jodaField_dayOfMonth" : 29,
    "jodaField_hourOfDay" : 19,
    "jodaField_minuteOfHour" : 23,
    "jodaField_millisOfSecond" : 512,
    "jodaField_monthOfYear" : 4,
    "jodaField_year" : 2013
  },
  "version" : 1
}
```

## Current support

This plugin currently supports persisting and querying these Joda Time objects:

* DateTime
* Interval
* LocalDate
* LocalDateTime

It is currently possible to query any of the above objects using any of the other above objects.  

It is reasonably straightforward to add further support, but time and budgetary constraints prevent us ([Spantree](www.spantree.net)) from doing so at this point.  We will add features as we need them or if any pull requests come in.


