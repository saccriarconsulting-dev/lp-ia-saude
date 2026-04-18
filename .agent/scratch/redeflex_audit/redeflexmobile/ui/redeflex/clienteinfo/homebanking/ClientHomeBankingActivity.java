package com.axys.redeflexmobile.ui.redeflex.clienteinfo.homebanking;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.shared.models.clientinfo.ClientHomeBanking;
import com.axys.redeflexmobile.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.axys.redeflexmobile.ui.redeflex.Config.KEY_INFO_CLIENT_MDR_TAXES;

/**
 * @author lucasmarciano on 01/07/20
 */
@SuppressLint("NonConstantResourceId")
public class ClientHomeBankingActivity extends BaseActivity implements ClientHomeBankingView {

    @BindView(R.id.pb_load_data) ProgressBar pbLoad;
    @BindView(R.id.rv_client_home_banking) RecyclerView rvListItems;

    @Inject ClientHomeBankingPresenter presenter;
    @Inject ClientHomeBankingAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_client_home_banking;
    }

    @Override
    protected void initialize() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            configureToolbar();
            configureRecyclerView();

            String clientId = bundle.getString(KEY_INFO_CLIENT_MDR_TAXES);
            presenter.loadData(clientId);
        } else {
            Toast.makeText(this, R.string.message_not_found_client_taxes, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void fillAdapterList(List<ClientHomeBanking> list) {
        if (list.size() > 0)
            adapter.setList(list);
    }

    @Override
    public void showMainLoading() {
        pbLoad.setVisibility(View.VISIBLE);
        rvListItems.setVisibility(View.GONE);
    }

    @Override
    public void hideMainLoading() {
        pbLoad.setVisibility(View.GONE);
        rvListItems.setVisibility(View.VISIBLE);
    }

    private void configureToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getStringByResId(R.string.title_home_bank));
        }
    }

    private void configureRecyclerView() {
        EmptyListObject empty = new EmptyListObject(
                getString(R.string.message_no_home_banking_title),
                getString(R.string.message_no_home_banking_describe)
        );
        adapter.setEmptyListObject(empty);
        rvListItems.setLayoutManager(new LinearLayoutManager(this));
        rvListItems.setAdapter(adapter);
    }
}
