package com.example.aparking

import com.yandex.mapkit.geometry.Point

data class ComparablePoint(
    val latitude: Double,
    val longitude: Double
): Comparable<ComparablePoint> {
    override fun compareTo(other: ComparablePoint): Int {
        val retVal = latitude.compareTo(other.latitude)
        return if (retVal != 0) {
            retVal
        } else longitude.compareTo(other.longitude)
    }

    fun toPoint() = Point(latitude, longitude)

    companion object {
        fun fromPoint(point: Point) = with(point) {
            ComparablePoint(latitude, longitude)
        }
    }
}
