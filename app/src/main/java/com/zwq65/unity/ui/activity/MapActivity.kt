package com.zwq65.unity.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.maps.utils.SpatialRelationUtil
import com.amap.api.maps.utils.overlay.SmoothMoveMarker
import com.blankj.utilcode.util.LogUtils
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseDaggerActivity
import com.zwq65.unity.ui.contract.MapContract
import kotlinx.android.synthetic.main.activity_map.*
import pub.devrel.easypermissions.EasyPermissions


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2019/5/29
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class MapActivity : BaseDaggerActivity<MapContract.View, MapContract.Presenter<MapContract.View>>(), MapContract.View {

    private var aMap: AMap? = null
    private var mUiSettings: UiSettings? = null
    override val layoutId: Int
        get() = R.layout.activity_map

    override fun initBaseTooBar(): Boolean {
        return true
    }

    override fun dealIntent(intent: Intent) {

    }

    override fun initView() {

    }

    override fun initData(savedInstanceState: Bundle?) {
        requestLocationPermission()
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView?.onCreate(savedInstanceState)
        setUpMap()
        initLocation()
        createMaker()
    }

    private fun setUpMap() {
        aMap = mapView?.map
        //设置希望展示的地图缩放级别(地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细)
        aMap?.moveCamera(CameraUpdateFactory.zoomTo(17f))
        aMap?.mapTextZIndex = 2
        // 显示实时交通状况
        aMap?.isTrafficEnabled = true
        /**
         * 地图ui
         */
        mUiSettings = aMap?.uiSettings
        //控制比例尺控件是否显示
        mUiSettings?.isScaleControlsEnabled = true
        //设置缩放按钮是否显示
        mUiSettings?.isMyLocationButtonEnabled = false
        //设置默认定位按钮是否显示
        mUiSettings?.isZoomControlsEnabled = false

    }

    private fun initLocation() {
        /**
         * 定位
         */
        val myLocationStyle = MyLocationStyle()
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000)
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(true)
        //        myLocationStyle.myLocationIcon()
        //设置定位蓝点精度圆圈的边框颜色
        myLocationStyle.strokeColor(R.color.colorPrimary)
        //设置定位蓝点精度圆圈的填充颜色
        myLocationStyle.radiusFillColor(R.color.colorPrimary)
        //设置定位蓝点的Style
        aMap?.myLocationStyle = myLocationStyle
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap?.isMyLocationEnabled = true
        aMap?.setOnMyLocationChangeListener {
            //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
            LogUtils.i("location.latitude:${it.latitude}   location.longitude:${it.longitude}")
        }
    }

    private fun createMaker() {
        // 获取轨迹坐标点
        val points = mutableListOf(LatLng(30.279474, 120.12117), LatLng(30.279574, 120.12117), LatLng(30.279674, 120.12117), LatLng(30.279774, 120.12117))
        val bounds = LatLngBounds(points[0], points[points.size - 2])
        aMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))

        val smoothMarker = SmoothMoveMarker(aMap)

        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car))

        val drivePoint = points.get(0)
        val pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint)
        points.set(pair.first, drivePoint)
        val subList = points.subList(pair.first, points.size)

        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList)
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(40)
        // 开始滑动
        smoothMarker.startSmoothMove()
    }

    private fun requestLocationPermission() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //未获取权限
            val perms = ArrayList<String>()
            perms.add(Manifest.permission.ACCESS_FINE_LOCATION)
            EasyPermissions.requestPermissions(this, "请求定位权限", 1, Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView?.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView?.onSaveInstanceState(outState)
    }

}

