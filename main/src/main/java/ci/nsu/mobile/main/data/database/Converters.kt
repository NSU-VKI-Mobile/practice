package ci.nsu.mobile.main.data.database

import androidx.room3.TypeConverter


class Converters {
    @TypeConverter
    fun fromDouble(value: Double?): Double? = value

    @TypeConverter
    fun toDouble(value: Double?): Double? = value
}