package com.example.aparking

data class Authority(
    var authority: String
)
data class User(
    var id: Int = 99999,
    var phone: String = "+79268255158",
    var fullName: String = " Шкляр Михаил",
    var enabled: Boolean = true,
    var username: String = "+79268255158",
    var authorities: List<Authority> = listOf(),
    var password: String = "",
    var accountNonExpired: Boolean = true,
    var accountNonLocked: Boolean = true,
    var credentialsNonExpired: Boolean = true,
    var birthday: String = "17.07.2001",
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

    companion object {
        @Volatile
        private var INSTANCE: User? = null

        fun getInstance(): User =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: User().also { INSTANCE = it }
            }
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

    fun selectCar(carTitle: String) {
        carsList.forEach { car ->
            car.isSelected = car.carTitle == carTitle
        }
    }
}