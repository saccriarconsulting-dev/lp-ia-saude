package com.axys.redeflexmobile.ui.clientemigracao;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.CheckBox;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.shared.models.migracao.ClientMigrationResponse;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.clientemigracao.ClientMigrationViewHolder.ClientMigrationViewHolderListener;
import com.axys.redeflexmobile.ui.clientemigracao.dialog.RegisterTokenMigrationAlertDialog;
import com.axys.redeflexmobile.ui.clientemigracao.dialog.RegisterTokenMigrationDialog;
import com.axys.redeflexmobile.ui.clientemigracao.register.RegisterMigrationActivity;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.MIGRATION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.MIGRATION_DISAPPROVED;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterStatus.WAITING_CONFIRMATION;

/**
 * @author Lucas Marciano on 23/03/2020
 */
@SuppressLint("NonConstantResourceId")
public class ClientMigrationActivity extends BaseActivity implements ClientMigrationView, ClientMigrationViewHolderListener {
    private static final int BUTTON_WAIT_DURATION = 2200;
    public static final String CLIENT_ID_KEY = "CLIENT_ID_KEY";

    @Inject
    ClientMigrationPresenter presenter;
    @Inject
    ClientMigrationAdapter adapter;

    @BindView(R.id.loading_view)
    View viewLoading;
    @BindView(R.id.rv_clients_migracao)
    RecyclerView rvClientsMigrationList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cliente_migracao_chkMigrado)
    CheckBox chkMigrado;
    @BindView(R.id.cliente_migracao_chkPendenteMigracao)
    CheckBox chkPendenteMigracao;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected int getContentView() {
        return R.layout.activity_cliente_migracao;
    }

    @Override
    protected void initialize() {
        setupToolbar();
        InicializaEventos();
        setupRecyclerView();

        presenter.loadClientsMigration(0);
    }

    private void InicializaEventos() {
        chkMigrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkMigrado.setChecked(true);
                chkPendenteMigracao.setChecked(false);
                presenter.loadClientsMigration(0);
            }
        });

        chkPendenteMigracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkMigrado.setChecked(false);
                chkPendenteMigracao.setChecked(true);
                presenter.loadClientsMigration(1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void fillAdapter(List<ClientMigrationResponse> list) {
        adapter.setList(list);
    }

    @Override
    public void showTokenValidationSuccess() {
        final RegisterTokenMigrationAlertDialog dialog = RegisterTokenMigrationAlertDialog.newInstance(
                RegisterTokenMigrationAlertDialog.Behavior.POSITIVE);
        dialog.show(getSupportFragmentManager(), RegisterTokenMigrationAlertDialog.class.getName());
        presenter.loadClientsMigration(0);
    }

    @Override
    public void showTokenValidationFailure(ClientMigrationResponse clientMigrationResponse) {
        final RegisterTokenMigrationAlertDialog dialog = RegisterTokenMigrationAlertDialog.newInstance(
                RegisterTokenMigrationAlertDialog.Behavior.NEGATIVE);
        dialog.show(getSupportFragmentManager(), RegisterTokenMigrationAlertDialog.class.getName());
        dialog.setNegativeCallback(() -> showTokenValidationModal(clientMigrationResponse));
    }

    @Override
    public void showMainLoading() {
        viewLoading.setVisibility(View.VISIBLE);
        rvClientsMigrationList.setVisibility(View.GONE);
    }

    @Override
    public void hideMainLoading() {
        viewLoading.setVisibility(View.GONE);
        rvClientsMigrationList.setVisibility(View.VISIBLE);
    }

    @Override
    public void clickEvent(ClientMigrationResponse clientMigrationResponse) {
        // Verifica se existe migração
        if (clientMigrationResponse.getRegisterMigrationSub() == null)
            return;

        final EnumRegisterStatus status = EnumRegisterStatus.getEnumByValue(clientMigrationResponse.getRegisterMigrationSub().getSituacao(), MIGRATION);
        if (status == null) {
            return;
        }
        if (status == WAITING_CONFIRMATION) {
            showTokenValidationModal(clientMigrationResponse);
        } else if (status == MIGRATION_DISAPPROVED) {
            showProposalScreen(clientMigrationResponse);
        }
    }

    /**
     * Open modal that show a modal to validate the token.
     *
     * @param clientMigrationResponse [ClientMigrationResponse]
     */
    private void showTokenValidationModal(ClientMigrationResponse clientMigrationResponse) {
        final RegisterTokenMigrationDialog dialog = RegisterTokenMigrationDialog
                .newInstance(token -> presenter.validateToken(token, clientMigrationResponse));
        dialog.show(getSupportFragmentManager(), RegisterTokenMigrationDialog.class.getName());
    }

    /**
     * Open the proposal screen.
     *
     * @param clientMigrationResponse [ClientMigrationResponse]
     */
    private void showProposalScreen(ClientMigrationResponse clientMigrationResponse) {
        Intent intent = new Intent(this, RegisterMigrationActivity.class);
        intent.putExtra(CLIENT_ID_KEY, Integer.parseInt(
                clientMigrationResponse.getClient().getId()
        ));
        startActivity(intent);
    }

    /**
     * Create toolbar for the activity.
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_clients_migration));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        compositeDisposable.add(
                RxToolbar.navigationClicks(toolbar)
                        .throttleFirst(BUTTON_WAIT_DURATION, TimeUnit.MILLISECONDS)
                        .subscribe(v -> onBackPressed(), Timber::e)
        );
    }

    /**
     * Configure the RecyclerView.
     */
    private void setupRecyclerView() {
        EmptyListObject empty = new EmptyListObject(getString(R.string.message_no_clients_migration_title),
                getString(R.string.message_no_clients_migration_describe));
        adapter.setEmptyListObject(empty);

        rvClientsMigrationList.setLayoutManager(new LinearLayoutManager(this));
        rvClientsMigrationList.setAdapter(adapter);
    }
}
