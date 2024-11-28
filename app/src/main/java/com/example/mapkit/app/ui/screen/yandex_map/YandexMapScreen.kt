package com.example.mapkit.app.ui.screen.yandex_map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mapkit.R
import com.example.mapkit.app.base.BaseFragment
import com.example.mapkit.app.model.SearchResult
import com.example.mapkit.app.ui.dialog.DetailsBottomDialog
import com.example.mapkit.app.ui.dialog.HomeAlertDialog
import com.example.mapkit.app.ui.dialog.SearchBottomDialog
import com.example.mapkit.core.ktx.gone
import com.example.mapkit.core.ktx.toast
import com.example.mapkit.core.ktx.visible
import com.example.mapkit.databinding.ScreenYandexBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class YandexMapScreen : BaseFragment(R.layout.screen_yandex), View.OnClickListener,
    SearchBottomDialog.Callback, DetailsBottomDialog.Callback, HomeAlertDialog.Callback {

    override val viewModel by viewModels<YandexMapScreenVM>()
    private val binding by viewBinding(ScreenYandexBinding::bind)
    private lateinit var mainNavigation: NavController
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapObjects: MapObjectCollection

    private var isFirst = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        statusBarColorDark()
        setStatusBarColor(R.color.white_light)

        mapObjects = binding.yandexMap.map.mapObjects.addCollection()

        mapObjects.clear()

        val placemark = mapObjects.addPlacemark(
            Point(41.31123860396167, 69.27942401750931),
            ImageProvider.fromResource(requireContext(), R.drawable.ic_place) // Your custom marker icon
        )

        placemark.setIconStyle(
            IconStyle().apply {
                scale = 1.5f // Adjust size of the marker
                anchor = PointF(0.5f, 1.0f) // Anchor the marker to its bottom center
            }
        )

        binding.yandexMap.map.move(
            CameraPosition(Point(41.31116049078514, 69.27963200452294), 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1.5f),
            null
        )

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            enableUserLocation()
        }

        binding.btnLocation.setOnClickListener {
            moveToMyLocation()
        }

        binding.cardSearch.setOnClickListener {
            SearchBottomDialog.newInstance(this).show(childFragmentManager,SearchBottomDialog::class.simpleName)
            binding.cardSearch.gone()

        }

    }

    private fun enableUserLocation() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(binding.yandexMap.mapWindow)

        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true

        userLocationLayer.setObjectListener(object : UserLocationObjectListener {
            override fun onObjectAdded(userLocationView: UserLocationView) {

            }

            override fun onObjectUpdated(userLocationView: UserLocationView, p1: ObjectEvent) {

            }

            override fun onObjectRemoved(userLocationView: UserLocationView) {}
        })
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onStart() {
        super.onStart()
        binding.yandexMap.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        binding.yandexMap.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun moveToMyLocation() {
        val location = userLocationLayer.cameraPosition()?.target

        if (location != null) {
            // Animate the camera to the user's current location
            binding.yandexMap.map.move(
                CameraPosition(location, 17.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1.5f), // Smooth animation for 1.5 seconds
                null
            )
        } else {

            toast("Unable to find your location.")
        }
    }

    override fun onClick(p0: View?) {
        p0.apply {
            when(id){
                R.id.btn_location -> {

                }
            }
        }
    }

    override fun searchText(query: String) {
        val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        val searchOptions = SearchOptions()
        val userLocation = userLocationLayer.cameraPosition()?.target

        searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(binding.yandexMap.map.visibleRegion),
            searchOptions,
            object : Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    val results = response.collection.children.mapNotNull {

                        val resultPoint = it.obj?.geometry?.get(0)?.point

                        val distance = userLocation?.latitude?.let { it1 ->
                            userLocation.longitude.let { it2 ->
                                resultPoint?.latitude?.let { it3 ->
                                    resultPoint.longitude.let { it4 ->
                                        calculateDistance(
                                            it1, it2,
                                            it3, it4
                                        )
                                    }
                                }
                            }
                        }

                            val distanceText = if (distance!! >= 1000) {
                                "%.1f km".format(distance / 1000)
                            } else {
                                "%.1f m".format(distance)
                            }

                            val point = it.obj?.geometry?.get(0)?.point
                            val name = it.obj?.name.orEmpty()
                            val address = it.obj?.descriptionText.orEmpty()

                            if (point != null) SearchResult(name = name, address = address, distance = distanceText, point = point) else null
                    }

                        val dialog = childFragmentManager.findFragmentByTag(SearchBottomDialog::class.simpleName) as? SearchBottomDialog
                        dialog?.updateSearchResults(results)


                }

                override fun onSearchError(error: com.yandex.runtime.Error) {
                    toast("Search error")
                }
            }
        )
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0 // Radius of Earth in meters

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c // Distance in meters
    }

    override fun itemClicked(item: SearchResult) {
        val location = item.point // Assume your SearchResult has a Point field for coordinates
        if (location != null) {
            binding.yandexMap.map.move(
                CameraPosition(location, 17.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1.5f),
                null
            )
        }

        val selectedLocation = item.point

        if (selectedLocation != null) {
            // Clear existing map objects (removes previous markers)
            mapObjects.clear()

            // Add a marker (placemark) to the selected location
            val placemark = mapObjects.addPlacemark(
                selectedLocation,
                ImageProvider.fromResource(requireContext(), R.drawable.ic_place) // Your custom marker icon
            )

            placemark.setIconStyle(
                IconStyle().apply {
                    scale = 1.5f // Adjust size of the marker
                    anchor = PointF(0.5f, 1.0f) // Anchor the marker to its bottom center
                }
            )

            // Move the camera to the marker's location
            binding.yandexMap.map.move(
                CameraPosition(selectedLocation, 17.0f, 0.0f, 0.0f), // Zoom level: 17.0f
                Animation(Animation.Type.SMOOTH, 1.5f),
                null
            )
        } else {
            toast("Unable to find location for the selected item.")
        }

        DetailsBottomDialog.newInstance(this,item).show(childFragmentManager,DetailsBottomDialog::class.simpleName)
    }

    override fun itemAdd(item: SearchResult) {
        HomeAlertDialog.newInstance(this,item).show(childFragmentManager)
    }

    override fun itemRemove(item: SearchResult) {

    }

    override fun onDismiss(text:String) {
        binding.txtSearch.text = text
        binding.cardSearch.visible()
    }

    override fun insertResult(item: SearchResult) {
        viewModel.insertResult(item)
    }
}