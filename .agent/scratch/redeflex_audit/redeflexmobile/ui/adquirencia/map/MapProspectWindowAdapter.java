package com.axys.redeflexmobile.ui.adquirencia.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rogério Massa on 27/11/18.
 */

public class MapProspectWindowAdapter implements InfoWindowAdapter {

    private final Context context;
    private final View infoView;
    @BindView(R.id.map_item_info_view_tv_name) TextView tvInfoName;
    @BindView(R.id.map_item_info_view_iv_alert) ImageView ivInfo;
    @BindView(R.id.map_item_info_view_tv_revenues) TextView tvInfoRevenues;
    @BindView(R.id.map_item_info_view_tv_billing) TextView tvInfoBilling;
    private List<Ranking> items;

    @SuppressLint("InflateParams")
    MapProspectWindowAdapter(Context context) {
        this.infoView = LayoutInflater.from(context)
                .inflate(R.layout.activity_map_prospect_item_info_view, null);
        ButterKnife.bind(this, infoView);
        this.context = context;
    }

    public List<Ranking> getItems() {
        return items;
    }

    public void setItems(List<Ranking> items) {
        this.items = items;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (items == null || items.isEmpty()) {
            return infoView;
        }

        Ranking item = Stream.ofNullable(items)
                .filter(value -> value.getClientId() == Integer.valueOf(marker.getTitle()))
                .findFirst()
                .get();

        if (item != null) {
            tvInfoName.setText(item.getFantasyName());
            tvInfoRevenues.setText(context.getString(R.string.frg_main_map_item_revenues,
                    StringUtils.maskCurrencyDouble(item.getExpectedRevenue().toString())));
            tvInfoBilling.setText(context.getString(R.string.frg_main_map_item_billing,
                    StringUtils.maskCurrencyDouble(item.getRevenue().toString())));
        }
        return infoView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // unused
        return null;
    }

}