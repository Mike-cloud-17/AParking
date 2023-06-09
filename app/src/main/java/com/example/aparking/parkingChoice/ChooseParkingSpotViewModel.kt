package com.example.aparking.parkingChoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aparking.*
import kotlin.random.Random.Default.nextInt

class ChooseParkingSpotViewModel() : ViewModel() {
    val parkingSpots = MutableLiveData<List<ParkingSpot>>()
    private val cars = MutableLiveData<List<Car>>()
    val carNumber = MutableLiveData<String>()
    val isConfirmed = MutableLiveData<Boolean>()

    // Уведомление о нажатии кнопки
    private val _isParkingStarted = MutableLiveData(false)
    val isParkingStarted: LiveData<Boolean> = _isParkingStarted

    // Инициализируем текущего пользователя
    private val user = User.getInstance()

    init {
        // Инициализируем список парковочных мест / по факту запрашиваем его с бэка
        val parkingSpotsFromBack = listOf<ParkingSpot>().toMutableList()
        val parkingSpotsNumber = nextInt(3, 10)
        for (i in 1..parkingSpotsNumber) {
            parkingSpotsFromBack.add(ParkingSpotsRepository().getParkingSpots()[i])
        }
        parkingSpots.value = parkingSpotsFromBack

        // Инициализируем автомобили пользователя автомобиля
        cars.value = user.getCars()

        carNumber.value = "777 KNS | 02"
        // Пользователь еще не подтвердил свой выбор
        isConfirmed.value = false
    }

    // Осуществляем выбор места
    fun selectSpot(spot: ParkingSpot) {
        parkingSpots.value = parkingSpots.value?.map {
            if (it.id == spot.id) it.copy(isSelected = !it.isSelected) else it.copy(isSelected = false)
        }
    }

    // Подтверждем выбор автомобился
    fun confirmCar() {
        isConfirmed.value = true
    }

    // Убираем подтверждение выбора автомобиля
    fun unConfirmCar() {
        isConfirmed.value = false
    }

    // Получаем список названий автомобилей
    fun getCarTitles() = user.getCarsTitleList()

    // Получаем номер автомобиля по названию
    fun getCarNumber(carTitle: String) = user.getCarNumber(carTitle)

    fun startParking() {
        _isParkingStarted.value = true
    }
}
