package com.example.aparking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
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
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
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
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import com.yandex.runtime.ui_view.ViewProvider
import java.util.TreeMap
import kotlin.random.Random


class MapFragment : Fragment(), Session.SearchListener, MapObjectTapListener, ClusterTapListener,
    DrivingRouteListener, ClusterListener {
    private val viewModel: MapViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private val map: Map
        get() = mapView.map
    private lateinit var trafficButton: Button
    private lateinit var pointBottomSheet: PointBottomSheet
    private lateinit var clusterBottomSheet: ClusterBottomSheet
    private var parkingSpots: MutableMap<ComparablePoint, ParkingSpot> = TreeMap()
    private val mapKit = MapKitFactory.getInstance()
    private var currentLocation: Point? = null
    private var destinationPoint: Point? = null
    private var searchResult: List<Point>? = null
    private var closestMoscowSpot = ParkingSpot(
        9999,
        isOccupied = false,
        latitude = 55.754742,
        longitude = 37.649025,
        distanceToSpot = 50,
        address = "Покровский бульвар, 11с1"
    )
    private var query = ""

    private lateinit var locationMapKit: UserLocationLayer
    private lateinit var searchEdit: EditText
    private lateinit var searchManager: SearchManager
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var clustersCollection: ClusterizedPlacemarkCollection
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingSession: DrivingSession

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pointBottomSheet = PointBottomSheet()
        clusterBottomSheet = ClusterBottomSheet()
    }

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
        ParkingSpotsRepository()
            .getParkingSpots()
            .forEach {
                parkingSpots[ComparablePoint(it.latitude!!, it.longitude!!)] = it
            }

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

        showMapObjects()

        viewModel.getShowRouteLiveData().observe(viewLifecycleOwner, this::showRoute)
        viewModel.getShowRouteToLiveData().observe(viewLifecycleOwner, this::showRouteTo)
        viewModel.getShowCurrentLocationLiveData().observe(viewLifecycleOwner, this::showCurrentLocation)
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
        currentLocation = location?.let { Point(it.latitude, it.longitude) }

        currentLocation?.let { CameraPosition(it, 15.0f, 0.0f, 0.0f) }?.let {
            map.move(
                it,
                Animation(Animation.Type.SMOOTH, .5f),
                null
            )
        }

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("LocationUpdates", "Location has changed!")
                currentLocation = Point(location.latitude, location.longitude)
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
        this.query = query
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
        searchResult = results.collection.children.mapNotNull { it.obj?.geometry?.firstOrNull()?.point }
        showMapObjects()
        destinationPoint = searchResult?.firstOrNull()
        destinationPoint?.let {
            map.move(
                CameraPosition(it, 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, .5f),
                null
            )
        }
    }

    private fun showMapObjects() {
        mapObjects.clear()
        searchResult?.let {
            for (child in it) {
                val imageProvider = ImageProvider.fromBitmap(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_search_result
                    )!!.toBitmap(90, 75)
                )
                child.let { it1 -> mapObjects.addPlacemark(it1, imageProvider) }
            }
        }
        clustersCollection = mapObjects.addClusterizedPlacemarkCollection(this)
        drawSpot(closestMoscowSpot)
        for (spot in parkingSpots) {
            drawSpot(spot.value)
        }
        clustersCollection.clusterPlacemarks(60.0, 15)
    }

    private fun drawSpot(spot: ParkingSpot) {
        val imageProvider = ImageProvider.fromBitmap(
            AppCompatResources.getDrawable(
                requireContext(),
                if (spot.isOccupied == null || spot.isOccupied!!)
                    R.drawable.ic_parking_red
                else
                    R.drawable.ic_parking_green
            )!!.toBitmap(90, 90)
        )
        val location = Point(spot.latitude!!, spot.longitude!!)
        val placemark = clustersCollection.addPlacemark(location, imageProvider)
        placemark.addTapListener(this@MapFragment)
    }

    private fun showRoute(value: Boolean) {
        if (value && currentLocation != null) {
            if (destinationPoint != null && searchEdit.text.toString().isNotBlank()) {
                if (query == "покровский бульвар, 11с1")
                    requestRoutes(currentLocation!!, closestMoscowSpot.let {
                        Point(it.latitude!!, it.longitude!!)
                    })
                else
                    requestRoutes(currentLocation!!, destinationPoint!!)
            } else {
                val closestSpot = parkingSpots
                                .filter { it.value.distanceToSpot != null }
                                .minByOrNull { it.value.distanceToSpot!! }?.key
                closestSpot?.let {
                    requestRoutes(currentLocation!!, it.toPoint())
                }
            }
        }
        else if (!value)
            showMapObjects()
    }

    private fun showRouteTo(destination: Point) {
        showMapObjects()
        requestRoutes(currentLocation!!, destination)
    }

    private fun showCurrentLocation(value: Boolean) {
        if (value && currentLocation != null)
            map.move(
                CameraPosition(currentLocation!!, 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, .5f),
                null
            )
    }

    override fun onSearchError(error: Error) {
        // Handle error.
        Log.e("YandexMapKit", "Search error: $error")
        var errorMessage = "Проблема с интернет соединением"
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
        if (routes.isNotEmpty())
            mapObjects.addPolyline(routes[0].geometry)
    }

    override fun onDrivingRoutesError(p0: Error) {
        TODO("Not yet implemented")
    }

    override fun onClusterAdded(cluster: Cluster) {
        val spots = cluster.placemarks.map { parkingSpots[ComparablePoint.fromPoint(it.geometry)] }
        val occupied = spots.count { it?.isOccupied == true }
        val compareResult = occupied.compareTo(spots.size / 2.0)
        val view = ClusterCircleView(requireContext())
        view.setText(cluster.size.toString())
        if (compareResult > 0)
            view.setColor(Color.RED)
        else if (compareResult == 0)
            view.setColor(Color.YELLOW)
        else
            view.setColor(Color.GREEN)
        cluster.appearance.setView(ViewProvider(view))
        cluster.addClusterTapListener(this@MapFragment)
    }

    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
        if (mapObject is PlacemarkMapObject) {
            if (ComparablePoint.fromPoint(mapObject.geometry) ==
                ComparablePoint.fromPoint(
                    closestMoscowSpot.let { Point(it.latitude!!, it.longitude!!) }
                )
            ) {
                viewModel.setSpot(closestMoscowSpot)
            } else {
                val spot = parkingSpots[ComparablePoint.fromPoint(mapObject.geometry)]!!
                viewModel.setSpot(spot)
            }
            pointBottomSheet.show(parentFragmentManager, "PointBottomSheet")
        }
        return true
    }

    override fun onClusterTap(cluster: Cluster): Boolean {
        val spots = cluster.placemarks.map { parkingSpots[ComparablePoint.fromPoint(it.geometry)] }
        val occupied = spots.count { it?.isOccupied == true }
        val free = spots.count { !it?.isOccupied!! }
        viewModel.setSpot(spots.first()!!)
        viewModel.setLoad(occupied, free)
        clusterBottomSheet.show(parentFragmentManager, "ClusterBottomSheet")
        return true
    }
}