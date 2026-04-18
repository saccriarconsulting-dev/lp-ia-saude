package com.axys.redeflexmobile.ui.simulationrate.registerlist;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.shared.models.registerrate.ProspectingClientAcquisition;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListViewHolder.RegisterListViewHolderListener;
import com.axys.redeflexmobile.ui.simulationrate.registerpersoninfo.RegisterRateActivity;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;

/**
 * @author Lucas Marciano on 30/04/2020
 */
@SuppressLint("NonConstantResourceId")
public class RegisterProspectListActivity extends BaseActivity implements
        RegisterProspectListView,
        RegisterListViewHolderListener,
        OnRefreshListener,
        OnQueryTextListener {

    private static final int LENGTH_SEARCH_LIMIT = 3;
    private static final int BUTTON_WAIT_DURATION = 1200;
    public static final String KEY_PROSPECT_ID = "KEY_PROSPECT_ID";
    public static final String KEY_PROSPECT_IS_NEW = "KEY_PROSPECT_IS_NEW";
    public static final String KEY_ITEM_LISTED = "KEY_ITEM_LISTED";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rv_list_prospect) RecyclerView rvListProspect;
    @BindView(R.id.fb_new_prospect) FloatingActionButton fbNewProspect;
    @BindView(R.id.loading_view) ProgressBar viewLoading;
    @BindView(R.id.main_register_srl_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @Inject RegisterProspectListPresenter presenter;
    @Inject RegisterProspectListAdapter adapter;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private EditText etSearch;

    @Override
    protected int getContentView() {
        return R.layout.activity_register_list;
    }

    @Override
    protected void initialize() {
        setupToolbar();
        setupActions();
        swipeRefreshLayout.setOnRefreshListener(this);
        setupRecyclerView();

        presenter.loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void showLoading() {
        rvListProspect.setVisibility(View.GONE);
        viewLoading.setVisibility(View.VISIBLE);
        fbNewProspect.hide();
    }

    @Override
    public void hideLoading() {
        rvListProspect.setVisibility(View.VISIBLE);
        viewLoading.setVisibility(View.GONE);
        fbNewProspect.show();
    }

    @Override
    public void fillAdapter(List<ProspectingClientAcquisition> prospectList) {
        adapter.setList(prospectList);
    }

    @Override
    public void clickEvent(ProspectingClientAcquisition prospect) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PROSPECT_ID, prospect.getId());
        bundle.putBoolean(KEY_PROSPECT_IS_NEW, false);
        bundle.putBoolean(KEY_ITEM_LISTED, true);

        Utilidades.openNewActivity(
                this,
                RegisterRateActivity.class,
                bundle,
                true
        );
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        presenter.loadData();
        etSearch.setText(EMPTY_STRING);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s.length() >= LENGTH_SEARCH_LIMIT) {
            presenter.loadDataByFilter(s);
        } else {
            presenter.loadData();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() >= LENGTH_SEARCH_LIMIT)
            presenter.loadDataByFilter(s);
        else
            presenter.loadData();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setOnQueryTextListener(this);

        etSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        try {
            final Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(etSearch, R.drawable.register_list_activity_search_bar_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.register_rate_list_title));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );
    }

    private void setupActions() {
        compositeDisposable.add(RxView.clicks(fbNewProspect)
                .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(KEY_PROSPECT_IS_NEW, true);
                    Utilidades.openNewActivity(this, RegisterRateActivity.class,
                            bundle, true);
                }
                , Timber::e)
        );
    }

    private void setupRecyclerView() {
        EmptyListObject empty = new EmptyListObject(getString(R.string.message_no_register_list),
                getString(R.string.message_no_register_list_describe));
        adapter.setEmptyListObject(empty);

        rvListProspect.setLayoutManager(new LinearLayoutManager(this));
        rvListProspect.setAdapter(adapter);
    }
}
