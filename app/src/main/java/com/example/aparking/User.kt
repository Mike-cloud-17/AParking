package com.example.aparking

import java.time.LocalDateTime

data class User(
    var name: String = "Михаил",
    var surname: String = "Шкляр",
    var birthday: LocalDateTime = LocalDateTime.of(2001, 7, 17, 12, 25)
) {
    private var carsList: MutableList<Car> = emptyList<Car>().toMutableList()

    init {
        // По факту запрашиваем машины с бэка
        carsList = mutableListOf(
            Car("Nissan Михаила", "777 KNS | 18"),
            Car("Porsche", "777 KKK | 77"),
            Car("Белый Mercedes Эвелины Урусовой", "237 LPO | 21"),
            Car("Lada Sedan для Макарова Сергея Львовича", "555 VVV | 55")
        )
    }

    fun getCars(): MutableList<Car> = carsList

    fun getCarNumber(carTitle: String): String {
        carsList.forEach { car ->
            if (car.carTitle == carTitle)
                return car.carNumber
        }
        return ""
    }

    fun getCarsTitleList(): List<String> {
        val carsTitleList = emptyList<String>().toMutableList()
        carsList.forEach { carsTitleList.add(it.carTitle) }
        return carsTitleList
    }
}