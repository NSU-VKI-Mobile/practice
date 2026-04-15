package ci.nsu.mobile.main

import androidx.room.TypeConverter
import java.util.Date
//Конвертирует Date в Long и обратно для сохранения в БД
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}