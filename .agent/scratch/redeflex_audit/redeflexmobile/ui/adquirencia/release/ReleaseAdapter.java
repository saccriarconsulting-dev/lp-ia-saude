package com.axys.redeflexmobile.ui.adquirencia.release;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.Release;
import com.axys.redeflexmobile.ui.adquirencia.release.ReleaseViewHolder.ReleaseViewHolderListener;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

/**
 * @author Rogério Massa on 04/12/18.
 */

public class ReleaseAdapter extends BaseAdapter<Release, ReleaseViewHolder> {

    private ReleaseViewHolderListener viewListener;

    ReleaseAdapter(ReleaseViewHolderListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.activity_release_item;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new ReleaseViewHolder(view, this, viewListener);
    }
}
