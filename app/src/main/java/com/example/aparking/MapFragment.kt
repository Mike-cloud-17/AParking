package com.example.aparking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.yandex.mapkit.*
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.MasstransitRouter
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError


class MapFragment : Fragment(), Session.SearchListener, DrivingRouteListener {
    private val viewModel: MapViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private val map: Map
        get() = mapView.map
    private lateinit var trafficButton: Button
    private val mapKit = MapKitFactory.getInstance()
    private var currentLocation: Point? = null
    private var destinationPoint: Point? = null

    private lateinit var locationMapKit: UserLocationLayer
    private lateinit var searchEdit: EditText
    private lateinit var searchManager: SearchManager
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingSession: DrivingSession

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.mapview)
        mapObjects = mapView.map.mapObjects

        searchManager =
            SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()

        // Отображение пробок
        trafficButton = view.findViewById(R.id.trafficbutton)
        val jams = mapKit.createTrafficLayer(mapView.mapWindow)
        var jamsIsOn = false
        trafficButton.setOnClickListener {
            when (jamsIsOn) {
                false -> {
                    jamsIsOn = true
                    jams.isTrafficVisible = true
                    trafficButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.orange
                        )
                    ) // Orange
                }
                true -> {
                    jamsIsOn = false
                    jams.isTrafficVisible = false
                    trafficButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow
                        )
                    ) // Yellow
                }
            }
        }

        // Открытие бокового меню
        val menuButton = view.findViewById<ImageButton>(R.id.menu_button)
        menuButton.setOnClickListener {
            val drawerLayout = (activity as MapActivity).getDrawerLayout()
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Создаем иконку с нашей локацией
        locationMapKit = mapKit.createUserLocationLayer(mapView.mapWindow)
        locationMapKit.isVisible = true

        searchEdit = view.findViewById(R.id.search_text)
        searchEdit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.text.toString())
                true
            } else {
                false
            }
        }

        // Запрос пермишенов
        if (hasLocationPermission()) {
            enableUserLocation()
        } else {
            requestLocationPermission()
        }

        viewModel.getShowRouteLiveData().observe(viewLifecycleOwner, this::showRoute)
    }

    private fun hasLocationPermission(): Boolean {
        return LOCATION_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            LOCATION_PERMISSIONS,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun enableUserLocation() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("Location", "GPS is disabled.")
            return
        }

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        currentLocation = location?.let { Point(it.latitude, location.longitude) }

        currentLocation?.let { CameraPosition(it, 15.0f, 0.0f, 0.0f) }?.let {
            map.move(
                it,
                Animation(Animation.Type.SMOOTH, 8f),
                null
            )
        }

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("LocationUpdates", "Location has changed!")
                val currentLocation = Point(location.latitude, location.longitude)
                map.move(
                    CameraPosition(currentLocation, 15.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 10f),
                    null
                )
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }

        // Обновляем геолокацию каждые 1000 мс или каждые 10 метров
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            10f,
            locationListener
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    enableUserLocation()
                } else {
                    // Разрешение отклонено
                }
            }
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    private fun performSearch(query: String) {
        val searchOptions = SearchOptions()

        // Use the current location as the geometry for the search, if available.
        val geometry = currentLocation?.let { Geometry.fromPoint(it) }
            ?: Geometry.fromPoint(
                Point(
                    0.0,
                    0.0
                )
            ) // Fallback to (0, 0) if current location is not available.

        searchManager.submit(query, geometry, searchOptions, this)
    }

    override fun onSearchResponse(results: Response) {
        mapObjects.clear()
        val searchResult = results.collection.children
        for (child in searchResult) {
            child.obj?.geometry?.let {
                // Add a placemark on the map for the first search result.
                val placemark = it[0].point?.let { it1 -> map.mapObjects.addPlacemark(it1) }
                val imageProvider = ImageProvider.fromResource(
                    requireContext(),
                    R.drawable.search_result
                )
                placemark?.setIcon(imageProvider)
            }
        }
        destinationPoint = searchResult[0].obj?.geometry?.firstOrNull()?.point
    }

    private fun showRoute(value: Boolean) {
        if (value && currentLocation != null && destinationPoint != null)
            requestRoutes(currentLocation!!, destinationPoint!!)
    }

    override fun onSearchError(error: Error) {
        // Handle error.
        Log.e("YandexMapKit", "Search error: $error")
        var errorMessage = "Неизвестная ошибка"
        if (error is RemoteError) {
            errorMessage = "Беспроводная ошибка"
        } else if (error is NetworkError) {
            errorMessage = "Проблема с интернетом"
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun requestRoutes(start: Point, end: Point) {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        requestPoints.add(
            RequestPoint(
                start,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                end,
                RequestPointType.WAYPOINT,
                null
            )
        )
        drivingSession =
            drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }

    override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
        mapObjects.addPolyline(routes[0].geometry)
    }

    override fun onDrivingRoutesError(p0: Error) {
        TODO("Not yet implemented")
    }
}