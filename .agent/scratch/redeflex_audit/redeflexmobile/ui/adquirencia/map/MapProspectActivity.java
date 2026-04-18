package com.axys.redeflexmobile.ui.adquirencia.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRankingStatus;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod.CURRENT;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod.NINETY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod.SIXTY;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumMapPeriod.THIRTY;
import static com.axys.redeflexmobile.shared.util.ImageUtils.bitmapDescriptorFromVector;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.TWO_INT;

/**
 * @author Rogério Massa on 23/11/18.
 */

public class MapProspectActivity extends BaseActivity implements MapProspectView,
        OnMapReadyCallback, OnInfoWindowClickListener, OnTabSelectedListener {

    private static final int REQUEST_CODE = 1;
    private static final String[] PERMISSIONS_COLLECTION = new String[]{ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION};

    @Inject MapProspectPresenter presenter;

    @BindView(R.id.map_prospect_toolbar) Toolbar toolbar;
    @BindView(R.id.map_item_tl_options) TabLayout tlOptions;
    @BindView(R.id.map_prospect_cpl_loading) ComponentProgressLoading cplLoading;

    private View mapView;
    private GoogleMap googleMap;
    private MapProspectWindowAdapter mapInfoViewAdapter;
    private HashMap<String, EnumMapPeriod> tabList;

    @Override
    protected int getContentView() {
        return R.layout.activity_map_prospect_item;
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cplLoading;
    }

    @Override
    public void initialize() {
        setSupportActionBar(toolbar);
        setTitle("Mapa");
        showBackButtonToolbar();

        mapInfoViewAdapter = new MapProspectWindowAdapter(this);
        initializeTabLayout();
        initializeMap();
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setInfoWindowAdapter(mapInfoViewAdapter);
        googleMap.setOnInfoWindowClickListener(this);

        customizeMyLocationButton();
        requestMyLocationPermission();
        presenter.getMapItems(CURRENT);
    }

    @Override
    public void fillMarkers(List<Ranking> list) {
        googleMap.clear();
        mapInfoViewAdapter.setItems(list);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        GPSTracker gpsTracker = new GPSTracker(this);
        if (!gpsTracker.isGPSEnabled) {
            builder.include(new LatLng(GPSTracker.REDEFLEX_LATITUDE, GPSTracker.REDEFLEX_LONGITUDE));
        } else {
            builder.include(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
        }

        Stream.ofNullable(list).forEachIndexed((index, mapItem) -> {
            EnumRankingStatus status = EnumRankingStatus.getEnumByValue(mapItem.getStatus());
            LatLng position = new LatLng(mapItem.getLatitude(), mapItem.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(position)
                    .title(String.valueOf(mapItem.getClientId()))
                    .icon(bitmapDescriptorFromVector(this, status.getMapMarker())));
            builder.include(position);
        });
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 150));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Ranking item = Stream.ofNullable(mapInfoViewAdapter.getItems())
                .filter(value -> value.getClientId() == Integer.valueOf(marker.getTitle()))
                .findFirst()
                .get();

        if (item == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(Config.CodigoCliente, String.valueOf(item.getClientId()));
        Utilidades.openNewActivity(this, ClienteInfoActivity.class, bundle, false);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getText() == null) {
            return;
        }
        String key = tab.getText().toString();
        EnumMapPeriod mapPeriodEnum = tabList.get(key);
        presenter.getMapItems(mapPeriodEnum);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //unused
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //unused
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestMyLocationPermission();
    }

    @SuppressLint("MissingPermission")
    private void requestMyLocationPermission() {
        if (!hasPermission() || hasPermissionCanceled()) {
            showOneButtonDialog(getString(R.string.app_name),
                    getString(R.string.frg_main_map_item_permission),
                    v -> ActivityCompat.requestPermissions(this,
                            PERMISSIONS_COLLECTION, REQUEST_CODE));
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
    }

    private boolean hasPermissionCanceled() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION);
    }

    private void initializeTabLayout() {
        tabList = new HashMap<>();
        tabList.put(getString(R.string.frg_main_map_tab_current), CURRENT);
        tabList.put(getString(R.string.frg_main_map_tab_thirty), THIRTY);
        tabList.put(getString(R.string.frg_main_map_tab_sixty), SIXTY);
        tabList.put(getString(R.string.frg_main_map_tab_ninety), NINETY);

        tlOptions.addTab(tlOptions.newTab().setText(getString(R.string.frg_main_map_tab_current)));
        tlOptions.addTab(tlOptions.newTab().setText(getString(R.string.frg_main_map_tab_thirty)));
        tlOptions.addTab(tlOptions.newTab().setText(getString(R.string.frg_main_map_tab_sixty)));
        tlOptions.addTab(tlOptions.newTab().setText(getString(R.string.frg_main_map_tab_ninety)));
        tlOptions.addOnTabSelectedListener(this);
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_main_map_item_map);
        if (mapFragment == null) {
            return;
        }
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
    }

    private void customizeMyLocationButton() {
        View locationButtonParent = (View) mapView.findViewById(SINGLE_INT).getParent();
        ImageView locationButton = locationButtonParent.findViewById(TWO_INT);
        locationButton.setImageResource(R.drawable.ic_map_my_location_button_wraped);
        LayoutParams locationButtonLayoutParams = (LayoutParams) locationButton.getLayoutParams();
        locationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, EMPTY_INT);
        locationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        int spacingNormal = (int) getResources().getDimension(R.dimen.spacing_normal);
        locationButtonLayoutParams.setMargins(EMPTY_INT, EMPTY_INT, spacingNormal, spacingNormal);
        locationButton.setLayoutParams(locationButtonLayoutParams);
    }
}
