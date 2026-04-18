package com.axys.redeflexmobile.ui.adquirencia.release;

import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.shared.models.adquirencia.Release;
import com.axys.redeflexmobile.ui.adquirencia.release.ReleaseViewHolder.ReleaseViewHolderListener;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

import static com.axys.redeflexmobile.shared.util.NumberUtils.NEGATIVE_SINGLE_INT;

/**
 * @author Rogério Massa on 04/12/18.
 */

public class ReleaseActivity extends BaseActivity implements ReleaseView,
        SwipeRefreshLayout.OnRefreshListener,
        ReleaseViewHolderListener {

    private static final String CLIENT_ID_FLAG = "clientId";

    @Inject ReleasePresenter presenter;
    @Inject ReleaseAdapter adapter;

    @BindView(R.id.release_rv_list) RecyclerView rvList;
    @BindView(R.id.release_srl_refresh) SwipeRefreshLayout srlRefresh;
    @BindView(R.id.release_cpl_loading) ComponentProgressLoading cplLoading;

    private int clientId;
    private CompositeDisposable compositeDisposable;

    public static Intent getIntent(Context context, int clientId) {
        Intent intent = new Intent(context, ReleaseActivity.class);
        intent.putExtra(CLIENT_ID_FLAG, clientId);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_release;
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cplLoading;
    }

    @Override
    protected void initialize() {
        setTitle(R.string.act_release_title);
        showBackButtonToolbar();

        clientId = getIntent().getIntExtra(CLIENT_ID_FLAG, NEGATIVE_SINGLE_INT);
        if (clientId == NEGATIVE_SINGLE_INT) {
            finish();
        }
        compositeDisposable = new CompositeDisposable();
        initializeElements();
        presenter.loadReleases(clientId);
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onRefresh() {
        presenter.reloadReleases(clientId);
        srlRefresh.setRefreshing(false);
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    @Override
    public void onClickItem(Release release) {
        ReleaseCodeDialog dialog = ReleaseCodeDialog.newInstance(release);
        dialog.show(getSupportFragmentManager(), ReleaseCodeDialog.TAG);
    }

    @Override
    public void showSwipeLoading() {
        srlRefresh.setRefreshing(true);
    }

    @Override
    public void hideSwipeLoading() {
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void fillReleases(List<Release> list) {
        adapter.setList(list);
    }

    private void initializeElements() {
        srlRefresh.setOnRefreshListener(this);
        adapter.setEmptyListObject(new EmptyListObject(getString(R.string.cpt_empty_list_release_title),
                getString(R.string.cpt_empty_list_release_text)));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
    }
}
