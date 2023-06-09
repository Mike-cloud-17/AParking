package com.example.aparking.sessions

import com.example.aparking.ParkingSpot
import kotlin.random.Random

class SessionsRepository {
    private var sessions: MutableList<Session> = emptyList<Session>().toMutableList()

    init {
        generateSessions()
    }

    private fun generateSessions(): Unit {

        sessions = mutableListOf(
            Session(
                0,
                "06.05.2023",
                "8:32-10:35",
                "02:03:24",
                "г. Москва, ул. Покровский бульвар, д. 11с1",
                "A 21",
                "Nissan Михаила",
                "777 KNS | 02",
                289.64,
            ),
            Session(),
            Session(
                2,
                "25.12.2022",
                "23:30 - 8:00",
                "8:30:01",
                "г. Москва, ул. Варшавское шоссе, д. 16к2",
                "S 32",
                "Nissan Tiida",
                "288 KVO | 77",
                333.34
            ),
            Session(
                3,
                "14.05.2023",
                "12:32 - 14:21",
                "1:49:16",
                "г. Алматы, ул. Кажымукана, д. 12",
                "D 67",
                "Volvo 2",
                "789 KLP | 67",
                676.87
            ),
            Session(
                4,
                "07.04.2023",
                "15:10 - 17:30",
                "2:20:18",
                "г. Алматы, ул. Райымбека, д. 3",
                "H 158",
                "Porsche 3",
                "149 MVB | 79",
                678.99
            ),
            Session(
                5,
                "08.03.2023",
                "10:10 - 10:11",
                "00:01:17",
                "г. Москва, ул. Тверская, д. 13",
                "L 27",
                "Porsche 3",
                "198 ОРТ | 79",
                150.00
            ),
            Session(
                6,
                "16.12.2022",
                "10:10 - 18:43",
                "8:33:31",
                "г. Астана, ул. Гоголя, д. 7",
                "A 56",
                "Mercedes",
                "888 BBB | 77",
                380.22
            ),
            Session(
                7,
                "20.04.2023",
                "7:30 - 18:46",
                "11:16:23",
                "г. Москва, ул. Каширское шоссе, д. 13",
                "B 88",
                "BMV Бориса Игоревича",
                "777 PPP | 77",
                350.00
            )
        )
    }

    fun getSessions(): MutableList<Session> = sessions
}
