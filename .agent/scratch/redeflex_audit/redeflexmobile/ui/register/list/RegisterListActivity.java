package com.axys.redeflexmobile.ui.register.list;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.ALREADY_REGISTERED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.DISAPPROVED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.INVALID_DOCUMENT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.SCHEDULED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.SEND_TRANSACTIONING;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.SENT_ACQUISITION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.WAITING_CONFIRMATION;
import static com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister.RETORNO_DESBLOQUEIO_ID_TERMINAL;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.FROZEN_DURATION;
import static com.axys.redeflexmobile.shared.util.StringUtils.EMPTY_STRING;
import static com.axys.redeflexmobile.ui.register.list.dialog.RegisterTokenAlertDialog.Behavior;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.shared.models.customerregister.RegisterListItem;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.ui.adquirencia.release.ReleaseActivity;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity;
import com.axys.redeflexmobile.ui.register.list.RegisterListViewHolder.RegisterListViewHolderListener;
import com.axys.redeflexmobile.ui.register.list.dialog.RegisterTokenAlertDialog;
import com.axys.redeflexmobile.ui.register.list.dialog.RegisterTokenDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Rogério Massa on 21/11/18.
 */
@SuppressLint("NonConstantResourceId")
public class RegisterListActivity extends BaseActivity implements RegisterListView,
        RegisterListViewHolderListener, OnRefreshListener, OnQueryTextListener {

    private static final int CUSTOMER_REGISTER_CODE = 10;

    @Inject RegisterListPresenter presenter;
    @Inject RegisterListAdapter adapter;

    @BindView(R.id.main_register_srl_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.main_register_rv_list) RecyclerView rvRegister;
    @BindView(R.id.main_register_fab_register) FloatingActionButton fabRegister;
    @BindView(R.id.main_register_cpl_loading) ComponentProgressLoading cplLoading;
    private EditText etSearch;

    private CompositeDisposable compositeDisposable;

    public static RegisterListActivity newInstance() {
        return new RegisterListActivity();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main_register;
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, RegisterListActivity.class);
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cplLoading;
    }

    @Override
    public void initialize() {
        this.setTitle(R.string.register_toolbar_title);
        this.showBackButtonToolbar();
        this.initializeRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(this);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RxView.clicks(fabRegister)
                .throttleFirst(FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(o -> onClickItem(null), throwable -> {
                }));

        presenter.getRegisters(EMPTY_STRING);
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(getComponentName()) : null);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        presenter.getRegisters(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        presenter.getRegisters(s);
        return false;
    }

    @Override
    public void showLoading() {
        super.showLoading();
        rvRegister.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        rvRegister.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingDelayed() {
        new Handler().postDelayed(this::hideLoading, 300);
    }

    @Override
    public void fillRegisterList(List<RegisterListItem> list) {
        adapter.setList(list);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        presenter.getRegisters(EMPTY_STRING);
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void onClickItem(RegisterListItem register) {
        if (register == null) {
            startActivityForResult(RegisterCustomerActivity.getIntent(this), CUSTOMER_REGISTER_CODE);
            return;
        }

        final EnumRegisterStatus status = EnumRegisterStatus.getEnumByValue(register.getStatus());
        if (status == null) {
            return;
        }

        if (status == INVALID_DOCUMENT || status == DISAPPROVED) {
            startActivityForResult(RegisterCustomerActivity.getIntent(this,
                    String.valueOf(register.getClientId())), CUSTOMER_REGISTER_CODE);
        } else if (status == SCHEDULED || status == ALREADY_REGISTERED) {
            if (register.isConfirmed()) {
                Toast.makeText(this, R.string.register_list_token_waiting_sync, Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(RegisterCustomerActivity.getIntent(this,
                    String.valueOf(register.getClientId())), CUSTOMER_REGISTER_CODE);
        } else if (status == SEND_TRANSACTIONING || status == SENT_ACQUISITION
                && RETORNO_DESBLOQUEIO_ID_TERMINAL.equals(register.getReason().trim().toUpperCase())) {
            presenter.validateToOpenRelease(register);
        } else if (status == WAITING_CONFIRMATION) {
            showTokenDialog(register);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOMER_REGISTER_CODE) {
            presenter.getRegisters(EMPTY_STRING);
        }
    }

    @Override
    public IGPSTracker getGpsTracker() {
        return new GPSTracker(this);
    }

    @Override
    public void validacaoNaoEstaNasImediacoes() {
        Mensagens.naoEstaNasImediacoesPOS(this);
    }

    @Override
    public void openRelease(RegisterListItem registerListItem) {
        startActivityForResult(ReleaseActivity.getIntent(this, registerListItem.getId()), CUSTOMER_REGISTER_CODE);
    }

    @Override
    public void showTokenValidationSuccess() {
        final RegisterTokenAlertDialog dialog = RegisterTokenAlertDialog.newInstance(Behavior.POSITIVE);
        dialog.show(getSupportFragmentManager(), RegisterTokenAlertDialog.class.getName());
        etSearch.setText(EMPTY_STRING);
    }

    @Override
    public void showTokenValidationFailure(final RegisterListItem item) {
        final RegisterTokenAlertDialog dialog = RegisterTokenAlertDialog.newInstance(Behavior.NEGATIVE);
        dialog.show(getSupportFragmentManager(), RegisterTokenAlertDialog.class.getName());
        dialog.setNegativeCallback(() -> showTokenDialog(item));
    }

    private void initializeRecyclerView() {
        EmptyListObject empty = new EmptyListObject(getString(R.string.cpt_empty_list_register_title),
                getString(R.string.cpt_empty_list_register_text));
        adapter.setEmptyListObject(empty);

        rvRegister.setLayoutManager(new LinearLayoutManager(this));
        rvRegister.setAdapter(adapter);
        rvRegister.addOnScrollListener(this.getRecyclerScrollListener());
    }

    private void showTokenDialog(final RegisterListItem item) {
        final RegisterTokenDialog dialog = RegisterTokenDialog
                .newInstance(token -> presenter.validateToken(item, token));
        dialog.show(getSupportFragmentManager(), RegisterTokenDialog.class.getName());
    }

    private RecyclerView.OnScrollListener getRecyclerScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < EMPTY_INT) {
                    fabRegister.animate().translationY(EMPTY_INT).start();
                } else if (dy > EMPTY_INT) {
                    int margin = (int) getResources().getDimension(R.dimen.spacing_normal);
                    fabRegister.animate().translationY(fabRegister.getHeight() + margin).start();
                }
            }
        };
    }
}
