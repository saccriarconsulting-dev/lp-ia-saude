package com.axys.redeflexmobile.ui.adquirencia.visit.catalog;

import android.widget.ImageView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity;
import com.axys.redeflexmobile.ui.base.BaseFragment;
import com.bumptech.glide.Glide;

import butterknife.BindView;

/**
 * @author Rogério Massa on 04/01/19.
 */

public class VisitProspectCatalogItemFragment extends BaseFragment<VisitProspectActivity> {

    @BindView(R.id.visit_prospect_catalog_item_iv_image) ImageView ivImage;

    private String image;

    public static VisitProspectCatalogItemFragment newInstance(String image) {
        VisitProspectCatalogItemFragment fragment = new VisitProspectCatalogItemFragment();
        fragment.setImage(image);
        return fragment;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_visit_prospect_catalog_item;
    }

    @Override
    public void initialize() {
        if (image == null) {
            return;
        }

        Glide.with(this)
                .load(image)
                .into(ivImage);
    }
}
