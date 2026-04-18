package com.axys.redeflexmobile.ui.adquirencia.routes.list;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRotasDiasDaSemana;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.adquirencia.routes.RoutesProspectView;
import com.axys.redeflexmobile.ui.adquirencia.routes.list.RoutesProspectListViewHolder.RoutesProspectListViewHolderListener;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity;
import com.axys.redeflexmobile.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity.FLAG_VISIT;

/**
 * @author Rogério Massa on 29/10/18.
 */
public class RoutesProspectListFragment extends BaseFragment<RoutesProspectView> implements
        RoutesProspectListView,
        RoutesProspectListViewHolderListener,
        SearchView.OnQueryTextListener {

    public static final int MINIMUM_SEARCH_LENGTH = 3;
    @Inject RoutesProspectListAdapter adapter;

    @BindView(R.id.routes_list_rv_list) RecyclerView rvList;

    private EnumRotasDiasDaSemana dayOfWeek;
    private CompositeDisposable compositeDisposable;

    public static RoutesProspectListFragment newInstance(EnumRotasDiasDaSemana dayOfWeek) {
        RoutesProspectListFragment fragment = new RoutesProspectListFragment();
        fragment.setDayOfWeek(dayOfWeek);
        return fragment;
    }

    public void setDayOfWeek(EnumRotasDiasDaSemana dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Pesquisar cliente...");

        TextView searchField = searchView.findViewById(R.id.search_src_text);
        searchField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
    }

    @Override
    public int getContentRes() {
        return R.layout.activity_routes_prospect_list;
    }

    @Override
    public void initialize() {
        compositeDisposable = new CompositeDisposable();

        EmptyListObject empty = new EmptyListObject(getString(R.string.cpt_empty_list_routes_title));
        adapter.setEmptyListObject(empty);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(adapter);
        adapter.setList(parentActivity.getRoutesProspect(dayOfWeek));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String filterString) {
        filter(filterString);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String filterString) {
        filter(filterString);
        return false;
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void onClickItem(RoutesProspect route) {
        Bundle bundle = new Bundle();
        if (route.getIdScheduled() != null && route.getIdScheduled() > EMPTY_INT) {
            bundle.putInt(VisitProspectActivity.PARAM_ID_ROUTE, route.getIdScheduled());
        } else {
            bundle.putInt(VisitProspectActivity.PARAM_ID_ROUTE, route.getId());
        }

        if (route.getCustomerId() != null && route.getCustomerId() > EMPTY_INT) {
            bundle.putInt(VisitProspectActivity.PARAM_ID_CUSTOMER, route.getCustomerId());
        } else {
            bundle.putInt(VisitProspectActivity.PARAM_ID_PROSPECT, route.getProspectId());
        }

        Utilidades.openNewActivityForResult(requireContext(), VisitProspectActivity.class, FLAG_VISIT, bundle);
    }

    private void filter(String filterString) {
        if (filterString.length() < MINIMUM_SEARCH_LENGTH) {
            adapter.setList(parentActivity.getRoutesProspect(dayOfWeek));
        } else {
            adapter.setList(parentActivity.getRoutesProspect(filterString));
        }
    }
}
