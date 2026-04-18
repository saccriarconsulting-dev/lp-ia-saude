package com.axys.redeflexmobile.ui.adquirencia.ranking.list;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.adquirencia.ranking.RankingView;
import com.axys.redeflexmobile.ui.adquirencia.ranking.list.RankingListViewHolder.RankingListViewHolderListener;
import com.axys.redeflexmobile.ui.base.BaseFragment;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.clienteinfo.ClienteInfoActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Rogério Massa on 22/11/18.
 */

public class RankingListFragment extends BaseFragment<RankingView> implements RankingListView,
        RankingListViewHolderListener {

    @Inject RankingListAdapter adapter;

    @BindView(R.id.ranking_list_rv_list) RecyclerView rvList;

    private List<Ranking> rankingList;
    private CompositeDisposable compositeDisposable;

    public static RankingListFragment newInstance(List<Ranking> rankingList) {
        RankingListFragment fragment = new RankingListFragment();
        fragment.rankingList = rankingList;
        return fragment;
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_main_ranking_list;
    }

    @Override
    public void initialize() {
        compositeDisposable = new CompositeDisposable();

        EmptyListObject empty = new EmptyListObject(getString(R.string.cpt_empty_list_ranking_title),
                getString(R.string.cpt_empty_list_ranking_text));
        adapter.setEmptyListObject(empty);

        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(adapter);
        adapter.setList(rankingList);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void onClickItem(Ranking ranking) {
        Bundle bundle = new Bundle();
        bundle.putString(Config.CodigoCliente, String.valueOf(ranking.getClientId()));
        Utilidades.openNewActivity(requireContext(), ClienteInfoActivity.class, bundle, false);
    }
}
