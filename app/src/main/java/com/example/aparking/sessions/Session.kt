package com.example.aparking.sessions

data class Session(
    var id: Long = 1,
    var date: String = "07.05.2023",
    var interval: String = "2:23-11:50",
    var duration: String = "09:27:35",
    var address: String = "г. Москва, ул. Академика Янгеля, д. 17",
    var spotNumber: String = "S 31",
    var carTitle: String = "Белый Mercedes Эвелины Урусовой",
    var carNumber: String = "237 LPO | 21",
    var debt: Double = 973.25
) {
}