package com.axys.redeflexmobile.ui.adquirencia.visit.route;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.services.googlemapsapi.GoogleMapsApi;
import com.axys.redeflexmobile.shared.services.tasks.ClienteSyncTask;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.adquirencia.prospectinfo.ProspectInfoActivity;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectView;
import com.axys.redeflexmobile.ui.base.BaseFragment;
import com.axys.redeflexmobile.ui.component.customdistancebutton.CustomDistanceButton;
import com.axys.redeflexmobile.ui.dialog.CadastroClienteDialog;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.getColor;
import static com.axys.redeflexmobile.shared.util.ImageUtils.bitmapDescriptorFromVector;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.TWO_INT;
import static com.axys.redeflexmobile.ui.adquirencia.prospectinfo.ProspectInfoActivity.PROSPECT_ID;

/**
 * @author Rogério Massa on 29/11/18.
 */

public class VisitProspectRouteFragment extends BaseFragment<VisitProspectView> implements
        VisitProspectRouteView,
        OnMapReadyCallback,
        OnPolylineClickListener {

    public static final int ROUTE_WIDTH = 10;
    private static final int REQUEST_CODE = 1;
    private static final String[] PERMISSIONS_COLLECTION = new String[]{ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION};
    private static final String ADDRESS_FORMAT = "%s, %s %s, %s - %s";
    private static final String POLY_LINE_FORMAT = "HASH%s";
    @Inject VisitProspectRoutePresenter presenter;

    @BindView(R.id.visit_route_cv_contact) CardView cvContact;
    @BindView(R.id.visit_route_tv_fantasy_name) TextView tvFantasyName;
    @BindView(R.id.visit_route_tv_address) TextView tvAddress;
    @BindView(R.id.visit_route_tv_social_name) TextView tvSocialName;
    @BindView(R.id.visit_route_cdb_distance) CustomDistanceButton cdbDistance;
    @BindView(R.id.visit_route_bt_initialize) Button btInitialize;

    private GoogleMap googleMap;
    private View mapView;

    private String selectedPolyLine;
    private List<GoogleMapsApi> routes;
    private List<PolyLineItem> insertedPolyLines;
    private CompositeDisposable compositeDisposable;

    public static VisitProspectRouteFragment newInstance() {
        return new VisitProspectRouteFragment();
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_visit_prospect_route;
    }

    @Override
    public void initialize() {
        parentActivity.setToolbarTitle("Rotas");
        insertedPolyLines = new ArrayList<>();

        presenter.setCustomer(parentActivity.getCustomer());
        presenter.setSalesman(parentActivity.getSalesman());

        initializeMap();

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxView.clicks(btInitialize)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> presenter.initializeAttendance(), throwable -> {
                }));
        compositeDisposable.add(RxView.clicks(cvContact)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> onOpenCustomerInfoSelected(), throwable -> {
                }));
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onInitializeAttendance() {
        parentActivity.takePicture();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setAllGesturesEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.setOnPolylineClickListener(this);

        customizeMyLocationButton();
        requestMyLocationPermission();
        fillClient();
    }

    @Override
    public void fillRoutes(List<GoogleMapsApi> routes) {
        this.routes = routes;
        if (routes == null || routes.isEmpty()) {
            cdbDistance.setVisibility(View.GONE);
            return;
        }
        initializePolyLines();
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        String tag = (String) polyline.getTag();
        if (tag == null) return;

        selectedPolyLine = tag;
        Stream.ofNullable(insertedPolyLines).forEach(polyItem -> {
            boolean isSelected = polyItem.polyline.getTag() == tag;
            polyItem.polyline.setZIndex(isSelected ? SINGLE_INT : EMPTY_INT);
            polyItem.polyline.setColor(getColor(requireContext(), isSelected
                    ? R.color.visit_prospect_route_color
                    : R.color.visit_prospect_route_color_alternative));
            if (isSelected) cdbDistance.setInfoText(polyItem.fullInfo);
        });
    }

    private void initializePolyLines() {
        removeAllPolyLines();
        if (selectedPolyLine == null) selectedPolyLine = String.format(POLY_LINE_FORMAT, EMPTY_INT);

        List<PolyLineItem> polyLineItems = new ArrayList<>();
        Stream.ofNullable(routes).forEachIndexed((index, googleMapsApi) -> {
            boolean isSelected = selectedPolyLine.equals(String.format(POLY_LINE_FORMAT, index));

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(googleMapsApi.latLng)
                    .width(ROUTE_WIDTH)
                    .color(getColor(requireContext(), isSelected
                            ? R.color.visit_prospect_route_color
                            : R.color.visit_prospect_route_color_alternative));

            PolyLineItem item = new PolyLineItem();
            item.tag = String.format(POLY_LINE_FORMAT, index);
            item.polylineOptions = polylineOptions;
            item.isSelected = isSelected;
            item.fullInfo = new String[]{googleMapsApi.distance.text, googleMapsApi.duration.text};
            polyLineItems.add(item);
        });

        Stream.ofNullable(polyLineItems)
                .sortBy(polyLineItem -> polyLineItem.isSelected)
                .forEach(polyLineItem -> {
                    if (!polyLineItem.isSelected) {
                        insertPolyLine(polyLineItem);
                    } else {
                        insertPolyLine(polyLineItem);
                        cdbDistance.setVisibility(View.VISIBLE);
                        cdbDistance.setInfoText(polyLineItem.fullInfo);
                    }
                });
    }

    private void insertPolyLine(PolyLineItem polyLineItem) {
        Polyline polyline = googleMap.addPolyline(polyLineItem.polylineOptions);
        polyline.setClickable(true);
        polyline.setTag(polyLineItem.tag);
        polyLineItem.polyline = polyline;
        insertedPolyLines.add(polyLineItem);
    }

    private void removeAllPolyLines() {
        Stream.ofNullable(insertedPolyLines).forEach(polyLineItem -> polyLineItem.polyline.remove());
        insertedPolyLines.clear();
    }

    private void onOpenCustomerInfoSelected() {
        RouteClientProspect prospect = parentActivity.getCustomer();

        if (prospect == null) {
            return;
        }

        if (prospect.getType() == RouteClientProspect.TYPE_CUSTOMER) {
            Bundle bundle = new Bundle();
            bundle.putString(Config.CodigoCliente, String.valueOf(parentActivity.getCustomerId()));
            Utilidades.openNewActivity(requireContext(), ClienteInfoActivity.class, bundle, false);
            return;
        }

        if (prospect.getType() == RouteClientProspect.TYPE_PROSPECT) {
            Bundle bundle = new Bundle();
            bundle.putInt(PROSPECT_ID, parentActivity.getCustomerId());
            Utilidades.openNewActivity(requireContext(), ProspectInfoActivity.class, bundle, false);
        }
    }

    private void fillClient() {
        RouteClientProspect client = presenter.getCustomer();
        if (client == null) {
            return;
        }

        this.populateClientFields(client);

        this.googleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        GPSTracker tracker = new GPSTracker(requireContext());
        LatLng positionStart = new LatLng(tracker.getLatitude(), tracker.getLongitude());
        builder.include(positionStart);

        LatLng positionEnd = new LatLng(client.getLatitude(), client.getLongitude());
        this.googleMap.addMarker(new MarkerOptions().position(positionEnd)
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker_red_wraped)));
        builder.include(positionEnd);

        int[] paddings = new int[]{350, 250, 180, 80, 30, 10, 0};
        for (int padding : paddings) {
            if (animateCamera(builder.build(), padding)) {
                animateCamera(builder.build(), 50);
                break;
            }
        }
        presenter.getRoutes(positionStart, positionEnd);
    }

    private boolean animateCamera(LatLngBounds latLngBounds, int padding) {
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, padding));
            return true;
        } catch (Exception ex) {
            Timber.e(ex);
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    private void requestMyLocationPermission() {
        if (!hasPermission() || hasPermissionCanceled()) {
            showOneButtonDialog(getString(R.string.app_name),
                    getString(R.string.frg_main_map_item_permission),
                    view -> ActivityCompat.requestPermissions(requireActivity(),
                            PERMISSIONS_COLLECTION, REQUEST_CODE));
            return;
        }
        this.googleMap.setMyLocationEnabled(true);
    }

    private boolean hasPermission() {
        return checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                && checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
    }

    private boolean hasPermissionCanceled() {
        return ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_COARSE_LOCATION);
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_visit_route_map);
        if (mapFragment == null) {
            return;
        }
        this.mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
    }

    private void customizeMyLocationButton() {
        View locationButtonParent = (View) mapView.findViewById(SINGLE_INT).getParent();
        ImageView locationButton = locationButtonParent.findViewById(TWO_INT);
        locationButton.setImageResource(R.drawable.ic_map_my_location_button_wraped);
        RelativeLayout.LayoutParams locationButtonLayoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        locationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, EMPTY_INT);
        locationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        int spacingNormal = (int) getResources().getDimension(R.dimen.spacing_normal);
        int spacingBottom = (int) getResources().getDimension(R.dimen.spacing_colossal);
        locationButtonLayoutParams.setMargins(EMPTY_INT, EMPTY_INT, spacingNormal, spacingBottom + spacingNormal);
        locationButton.setLayoutParams(locationButtonLayoutParams);
    }

    private void populateClientFields(RouteClientProspect client) {
        this.tvFantasyName.setText(client.getNameFantasy());
        this.tvSocialName.setText(getString(R.string.visit_route_tv_social_name, client.getNameFull()));
        this.tvAddress.setText(String.format(ADDRESS_FORMAT,
                client.getAddressName(),
                client.getAddressNumber(),
                client.getNeighborhood(),
                client.getCity(),
                client.getFederalState()));
    }

    @Override
    public void openCustomerRegisterDialog(String customerId) {
        try {
            CadastroClienteDialog dialog = new CadastroClienteDialog();
            dialog.myCompleteListenerCadastro = registerUpdated -> {
                try {
                    if (registerUpdated != null && registerUpdated.getAlterado().equals("S")) {
                        new ClienteSyncTask(requireContext(), SINGLE_INT).execute();
                        presenter.updateCustomer(Integer.parseInt(customerId));
                    }
                } catch (Exception ex) {
                    Mensagens.mensagemErro(requireContext(), ex.getMessage(), false);
                }
            };

            Bundle args = new Bundle();
            args.putString(Config.CodigoCliente, customerId);
            args.putBoolean(Config.AtualizaContato, true);
            dialog.setArguments(args);
            dialog.show(getChildFragmentManager(), dialog.getClass().getSimpleName());

        } catch (Exception ex) {
            Mensagens.mensagemErro(requireContext(), ex.getMessage(), false);
        }
    }

    @Override
    public GPSTracker getGpsTracker() {
        return new GPSTracker(getContext());
    }

    @Override
    public boolean validacaoDataAparelhoAdquirencia() {
        return Validacoes.validacaoDataAparelhoAdquirencia(getContext());
    }

    @Override
    public void validacaoOsSemAgendamento() {
        Mensagens.osSemAgendamento(getContext());
    }

    @Override
    public void validacaoNaoEstaNasImediacoes() {
        Mensagens.naoEstaNasImediacoes(getContext());
    }

    private class PolyLineItem {
        String tag;
        PolylineOptions polylineOptions;
        boolean isSelected;
        String[] fullInfo;
        Polyline polyline;
    }
}
