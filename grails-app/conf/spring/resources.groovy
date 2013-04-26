import net.spantree.mongo.types.jodatime.LocalDateType
import net.spantree.mongo.types.jodatime.LocalDateTimeType
import net.spantree.mongo.types.jodatime.DateTimeType
import net.spantree.mongo.types.jodatime.IntervalType

beans = {
	localDateType(LocalDateType)
	localDateTimeType(LocalDateTimeType)
	dateTimeType(DateTimeType)
	intervalType(IntervalType)
}