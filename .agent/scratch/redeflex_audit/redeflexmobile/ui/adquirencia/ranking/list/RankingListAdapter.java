package com.axys.redeflexmobile.ui.adquirencia.ranking.list;

import android.view.View;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.ui.adquirencia.ranking.list.RankingListViewHolder.RankingListViewHolderListener;
import com.axys.redeflexmobile.ui.base.BaseAdapter;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder.IBaseViewHolderExpandableListener;

/**
 * @author Rogério Massa on 22/11/18.
 */

public class RankingListAdapter extends BaseAdapter<Ranking, RankingListViewHolder> implements
        IBaseViewHolderExpandableListener {

    private Integer opened;
    private RankingListViewHolderListener viewListener;

    RankingListAdapter(RankingListViewHolderListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int getLayoutItem() {
        return R.layout.fragment_main_ranking_list_item;
    }

    @Override
    public BaseViewHolder holder(View view) {
        return new RankingListViewHolder(view, this, this, viewListener);
    }

    @Override
    public boolean isOpened(int position) {
        return this.opened != null && this.opened == position;
    }

    @Override
    public void openClose(int position) {
        if (this.opened == null) {
            this.opened = position;
        } else if (this.opened == position) {
            this.opened = null;
        } else {
            int last = this.opened;
            this.opened = position;
            notifyItemChanged(last);
        }
        notifyItemChanged(position);
    }
}
