package com.petbulance.domain.utils

import kotlin.math.*

object LocationUtils {
    /**
     * Haversine 공식을 이용한 두 지점 간 거리 계산
     * @return 거리(미터), 계산 불가 시 null
     */
    fun calculateDistance(
        lat1: Double?,
        lng1: Double?,
        lat2: Double?,
        lng2: Double?
    ): Double? {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return null
        }

        val earthRadiusMeters = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusMeters * c
    }
}