package com.axys.redeflexmobile.ui.redeflex;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static org.apache.commons.lang3.ArrayUtils.contains;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ProdutosVendAdapter;
import com.axys.redeflexmobile.shared.bd.DBCartaoPonto;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBPermissao;
import com.axys.redeflexmobile.shared.bd.DBRotaAdquirencia;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoTroca;
import com.axys.redeflexmobile.shared.bd.DBSuporte;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRoutesProspectStatus;
import com.axys.redeflexmobile.shared.models.AuditagemEstoque;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Pendencias;
import com.axys.redeflexmobile.shared.models.Permissao;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.adquirencia.RoutesProspect;
import com.axys.redeflexmobile.shared.services.bus.ColaboradorBus;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.DateUtils;
import com.axys.redeflexmobile.shared.util.DeviceUtils;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.adquirencia.map.MapProspectActivity;
import com.axys.redeflexmobile.ui.adquirencia.ranking.RankingActivity;
import com.axys.redeflexmobile.ui.adquirencia.relatorio.RelatorioAdquirenciaActivity;
import com.axys.redeflexmobile.ui.adquirencia.routes.RoutesProspectActivity;
import com.axys.redeflexmobile.ui.adquirencia.visit.VisitProspectActivity;
import com.axys.redeflexmobile.ui.cartaoponto.CartaoPontoActivity;
import com.axys.redeflexmobile.ui.cliente.lista.ClienteListaActivity;
import com.axys.redeflexmobile.ui.clientemigracao.ClientMigrationActivity;
import com.axys.redeflexmobile.ui.clientpendent.ClientePendenteActivity;
import com.axys.redeflexmobile.ui.comprovante.container.ComprovanteSkyTaActivity;
import com.axys.redeflexmobile.ui.redeflex.solicitacaopid.Activity_ListaSolicitacaoPID;
import com.axys.redeflexmobile.ui.register.list.RegisterListActivity;
import com.axys.redeflexmobile.ui.simulationrate.registerlist.RegisterProspectListActivity;
import com.axys.redeflexmobile.ui.solictrocaprodutos.listagemsolicitacaotroca.ListagemSolicitacaoTrocaActivity;
import com.google.android.material.navigation.NavigationView;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends SharedAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TIPO_OPERACAO = "mTipoOperacao";
    public static final String LOGIN = "Login";

    private Menu menu;
    private LinearLayout layoutProximaRota;
    private LinearLayout layoutResumoVenda;
    private TextView txtOla;
    private TextView txtData;
    private DBColaborador dbColaborador;
    private DBCartaoPonto dbCartaoPonto;
    private Colaborador colaborador;
    private final PublishSubject<Integer> menuEvent = PublishSubject.create();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private List<Permissao> permissoesMenu = new ArrayList<>();

    //region Submenu listas
    private final int[] submenuRelatorio = new int[]{
            R.id.nav_relatorio_vendas,
            R.id.nav_relatorio_adquirencia,
            R.id.nav_relatorio_supervisor};

    private int[] submenuFerramentas = new int[]{
            R.id.nav_envio_anexo,
            R.id.nav_cartao_ponto,
            R.id.nav_senha_master,
            R.id.nav_verificar_sinal,
            R.id.nav_barcode,
            R.id.nav_mensagens,
            R.id.nav_suporte};

    private final int[] submenuFerramentasSemCartao = new int[]{
            R.id.nav_envio_anexo,
            R.id.nav_senha_master,
            R.id.nav_verificar_sinal,
            R.id.nav_barcode,
            R.id.nav_mensagens,
            R.id.nav_suporte};

    private final int[] submenuAdquirencia = new int[]{
            R.id.nav_adquirencia_rotas,
            R.id.nav_adquirencia_mapa,
            R.id.nav_adquirencia_ranking};

    private final int[] submenuMercadoria = new int[]{
            R.id.nav_mercadoria_solicitacao,
            R.id.nav_mercadoria_troca};

    private final int[] submenuClientes = new int[]{
            R.id.nav_base_cliente,
            R.id.nav_cliente_pendente};

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criarObjetos();
        //salvarTokenFirebaseOnPreference();

        try {
            colaborador = dbColaborador.get();

            if (colaborador == null) {
                Mensagens.colaboradorSemImeiVinculado(MainActivity.this);
                return;
            }

            String retornoCartaoPonto = Utilidades.recuperarStringSharedPreference(this, Config.AbrirCartaoPonto);
            if (retornoCartaoPonto != null && retornoCartaoPonto.equals("S")) {
                Utilidades.salvarStringSharedPreference(this, Config.AbrirCartaoPonto, "N");
                finishAffinity();
                startActivity(new Intent(this, CartaoPontoActivity.class));
                return;
            }

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                int isLogin = bundle.getInt(LOGIN);
                int baixarTudo = bundle.getInt(TIPO_OPERACAO);
                bundle.putInt(TIPO_OPERACAO, baixarTudo);
                ArrayList<Pendencias> lista = new DBSuporte(MainActivity.this).getPendencias();
                if (isLogin == 1 && (baixarTudo == 0 || lista != null && !lista.isEmpty())) {
                    Utilidades.openNewActivity(this, SyncActivity.class, bundle, false);
                }
            }

            Utilidades.idVendedor = String.valueOf(colaborador.getId());
            Utilidades.IMEI = (Utilidades.IMEI.equals("404")
                    ? Utilidades.retornaIMEI(MainActivity.this)
                    : Utilidades.IMEI);

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            Utilidades.ativaInativaAlarmes(MainActivity.this, true);
            carregarDados();
            verificarAuditagemPendente();
        } catch (Exception ex) {
            Mensagens.mensagemErro(MainActivity.this, ex.getMessage(), false);
        }

        verificarRegistroPontoDiaAtual();
        apresentarToolbar();
        inicializarDrawerDisposable();
    }

//    private void salvarTokenFirebaseOnPreference() {
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String token = instanceIdResult.getToken();
//                DeviceUtils.saveStringInSharedPreferences(MainActivity.this, DeviceUtils.PREFS_FCM_TOKEN, token);
//            }
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();
        permissoesMenu = obterPermissoes();

        new Thread(() -> {
            try {
                if (Validacoes.validacoesHorarioInicioAplicativo(this, false)) {
                    runOnUiThread(() -> Mensagens.horarioComercialInicial(MainActivity.this));
                } else if (!Utilidades.verificarHorarioComercial(MainActivity.this, true)) {
                    runOnUiThread(() -> Mensagens.horarioComercial(MainActivity.this));
                    return;
                }

                try {
                    Colaborador colaboradorSync = ColaboradorBus.getServer(MainActivity.this);
                    if (colaboradorSync != null && colaboradorSync.isOk() && colaboradorSync.getId() > 0) {
                        new DBColaborador(MainActivity.this).atualiza(colaboradorSync);
                        colaborador = colaboradorSync;
                    }
                } catch (Exception e) {
                    Timber.e(e);
                }

                runOnUiThread(this::carregarDados);

                new DBSolicitacaoTroca(MainActivity.this).removerAntigasPorPeriodo();
            } catch (Exception ex) {
                runOnUiThread(() -> Mensagens.mensagemErro(MainActivity.this, ex.getMessage(), false));
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            resetarMenu();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            resetarMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
        disposables.dispose();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_relatorios:
                apresentarSubmenus(submenuRelatorio);
                return true;
            case R.id.nav_ferramentas:
                apresentarMenuFerramentas();
                return true;
            case R.id.nav_adquirencia:
                apresentarSubmenus(submenuAdquirencia);
                return true;
            case R.id.nav_mercadoria:
                apresentarSubmenus(submenuMercadoria);
                return true;
            case R.id.nav_clientes_info:
                apresentarSubmenus(submenuClientes);
                return true;
        }

        menuEvent.onNext(item.getItemId());
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem registroPonto = menu.findItem(R.id.action_registro_ponto);
        if (colaborador != null && colaborador.isCartaoPonto()) {
            registroPonto.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_registro_ponto) {
            startActivity(new Intent(this, CartaoPontoActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void exibirConfirmacaoAuditagemEstoque() {
        carregarDados();
    }

    private void carregarDados() {
        try {
            //Colaborador
            carregarInfosColaborador();

            //Rotas
            Utilidades.listarRotas(MainActivity.this);

            //Resumo Venda
            carregarInformacoesRota();

            //Pendências
            Utilidades.listarPendencias(MainActivity.this);

        } catch (Exception ex) {
            Mensagens.mensagemErro(MainActivity.this, ex.getMessage(), false);
        }
    }

    private void carregarInfosColaborador() {
        if (colaborador == null) {
            colaborador = dbColaborador.get();
        }
        txtOla.setText(String.format("Olá, %s", Util_IO.toTitleCase(colaborador.getNome())));
        txtData.setText(String.format("Hoje, %s", Util_IO.getDataHojePtBr()));
    }

    private void carregarInformacoesRota() {
        if (colaborador.isRfma()) {
            esconderRotas();
            return;
        }

        ArrayList<Produto> listResumo = new DBVenda(MainActivity.this).getProdutosVendidosHoje();
        if (listResumo == null || listResumo.isEmpty()) {
            layoutResumoVenda.setVisibility(View.GONE);
            return;
        }

        layoutResumoVenda.setVisibility(View.VISIBLE);
        RecyclerView mRecyclerView = findViewById(R.id.mResumoVenda);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(new ProdutosVendAdapter(MainActivity.this, listResumo));
    }

    private List<String> carregarMenusMapeados() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        Menu menu = popupMenu.getMenu();
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);

        int index = EMPTY_INT;
        MenuItem menuItem = menu.getItem(index);
        List<String> menus = new ArrayList<>();

        while (menuItem != null) {
            menus.add(getResources().getResourceName(menuItem.getItemId()).split("/")[1]);
            try {
                menuItem = menu.getItem(++index);
            } catch (RuntimeException ignored) {
                menuItem = null;
            }
        }
        return menus;
    }

    private void criarObjetos() {
        dbColaborador = new DBColaborador(MainActivity.this);
        dbCartaoPonto = new DBCartaoPonto(MainActivity.this);

        menu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();
        layoutProximaRota = findViewById(R.id.layoutProxRota);

        txtOla = findViewById(R.id.txtOla);
        txtData = findViewById(R.id.txtData);
        layoutResumoVenda = findViewById(R.id.layoutResumoVenda);
    }

    private void abrirPendencias() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Config.ATIVAR_DECREMENTAL_FUNCAO, colaborador.isVerificaClientePendencia());
        Utilidades.openNewActivity(MainActivity.this, ClientePendenteActivity.class, bundle, false);
    }

    private void esconderRotas() {
        layoutProximaRota.setVisibility(View.GONE);
    }

    private void apresentarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
                resetarMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                try {
                    verificarRegistroPontoDiaAtual();
                    prepararMenu();
                } catch (Exception ex) {
                    Timber.e(ex, "onDrawerOpened: ");
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private List<Permissao> obterPermissoes() {
        List<Permissao> permissoes = new DBPermissao(MainActivity.this).getPermissoes();

        if ((colaborador.isCartaoPonto() && dbCartaoPonto.obterRegistroDia().isEmpty())) {
            permissoes.clear();
            permissoes.add(new Permissao("nav_sync"));
            permissoes.add(new Permissao("nav_ferramentas"));
            permissoes.add(new Permissao("nav_cartao_ponto"));
            return permissoes;
        }

        permissoes = Stream.ofNullable(permissoes)
                .filter(permissao -> !"nav_sync".equals(permissao.getIdApp())
                        && !"nav_ferramentas".equals(permissao.getIdApp())
                        && !"nav_cartao_ponto".equals(permissao.getIdApp()))
                .toList();

        permissoes.add(new Permissao("nav_sync"));
        permissoes.add(new Permissao("nav_ferramentas"));

        if (colaborador.isCartaoPonto()) {
            permissoes.add(new Permissao("nav_cartao_ponto"));
        }

        return permissoes;
    }

    private void prepararMenu() {
        if (colaborador == null) {
            colaborador = dbColaborador.get();
        }

        TextView txtVersao = findViewById(R.id.txtVersao);
        txtVersao.setText(String.format("Versão %s", BuildConfig.VERSION_NAME));

        TextView txtNomeColaborador = findViewById(R.id.txtNomeColaborador);
        txtNomeColaborador.setText(colaborador != null && colaborador.getNome() != null
                ? Util_IO.toTitleCase(colaborador.getNome())
                : "Desconhecido");

        int[] submenus = ArrayUtils.addAll(submenuRelatorio, submenuFerramentas);
        submenus = ArrayUtils.addAll(submenus, submenuAdquirencia);
        submenus = ArrayUtils.addAll(submenus, submenuMercadoria);
        submenus = ArrayUtils.addAll(submenus, submenuClientes);

        MenuItem menuItem;

        setChatPermissionPreference();

        for (Permissao permissao : permissoesMenu) {
            int id = this.getResources().getIdentifier(permissao.getIdApp(), "id", getPackageName());
            menuItem = menu.findItem(id);
            if (menuItem != null && !contains(submenus, id)) {
                menuItem.setVisible(true);
            }
        }

        exibirMenus(R.id.nav_ferramentas, submenuFerramentas);
        exibirMenus(R.id.nav_adquirencia, submenuAdquirencia);
        exibirMenus(R.id.nav_mercadoria, submenuMercadoria);
        exibirMenus(R.id.nav_clientes_info, submenuClientes);
    }

    private void setChatPermissionPreference() {
        for (Permissao permissao : permissoesMenu) {
            if (permissao.getIdApp().equals("nav_chat_bot")) {
                DeviceUtils.saveChatPreference(this, permissao.getAtivo());
                return;
            }
        }
        DeviceUtils.saveChatPreference(this, "N");
    }

    private void exibirMenus(int menuId, int[] submenus) {
        MenuItem menuItem = menu.findItem(menuId);

        if (menuItem != null && !menuItem.isVisible() && verificarSubMenus(permissoesMenu, submenus)) {
            menuItem.setVisible(true);
        }
    }

    private boolean verificarSubMenus(List<Permissao> permissions, int[] submenu) {
        for (Permissao item : permissions) {
            int id = this.getResources().getIdentifier(item.getIdApp(), "id", getPackageName());
            if (ArrayUtils.contains(submenu, id)) {
                return true;
            }
        }
        return false;
    }

    private void resetarMenu() {
        List<String> menusIds = carregarMenusMapeados();

        List<String> newIds = Stream.ofNullable(permissoesMenu)
                .map(Permissao::getIdApp)
                .toList();

        menusIds.removeAll(newIds);
        resetarSubmenus(Stream.ofNullable(menusIds)
                .mapToInt(menuItem -> getResources().getIdentifier(menuItem, "id", getPackageName()))
                .toArray());

        resetarSubmenus(submenuFerramentas);
        resetarSubmenus(submenuAdquirencia);
        resetarSubmenus(submenuRelatorio);
        resetarSubmenus(submenuMercadoria);
        resetarSubmenus(submenuClientes);
    }

    private void resetarSubmenus(int[] submenus) {
        for (int temp : submenus) {
            MenuItem menuItem = menu.findItem(temp);
            if (menuItem != null) {
                menuItem.setVisible(false);
            }
        }
    }

    private void apresentarMenuFerramentas() {
        MenuItem menuItem;
        if (!colaborador.isCartaoPonto()) {
            submenuFerramentas = submenuFerramentasSemCartao;
            menuItem = menu.findItem(R.id.nav_cartao_ponto);
            if (menuItem != null) {
                menuItem.setVisible(false);
            }
        }
        apresentarSubmenus(submenuFerramentas);
    }

    private void apresentarSubmenus(int[] submenus) {
        MenuItem menuItem;

        for (Permissao permissao : permissoesMenu) {
            int id = this.getResources().getIdentifier(permissao.getIdApp(), "id", getPackageName());
            menuItem = menu.findItem(id);
            if (menuItem != null && contains(submenus, id)) {
                menuItem.setVisible(!menuItem.isVisible());
            }
        }
    }

    private void abrirRotasAdiquirencia() {
        if (!colaborador.isValidaOrdemRota()) {
            Utilidades.openNewActivity(MainActivity.this, RoutesProspectActivity.class, null, false);
            return;
        }

        DBRotaAdquirencia dbRotaAdquirencia = new DBRotaAdquirencia(this);
        List<RoutesProspect> routes = dbRotaAdquirencia.pegarTodas();

        final RoutesProspect[] nextRoute = {null};
        List<RoutesProspect> todayRoutes = Stream.ofNullable(routes)
                .filter(route -> {
                    if (route.getStatus() == EnumRoutesProspectStatus.NEXT_VISIT.getValue()) {
                        nextRoute[EMPTY_INT] = route;
                    }
                    return route.getDayOfWeek() == DateUtils.getDayOfWeek();
                })
                .toList();

        if (nextRoute[EMPTY_INT] == null) {
            if (todayRoutes.isEmpty()) {
                Mensagens.mensagemErro(this, getString(R.string.app_name),
                        getString(R.string.routes_prospect_activity_error_routes_today_empty), false);
            } else {
                Mensagens.mensagemErro(this, getString(R.string.app_name),
                        getString(R.string.routes_prospect_activity_error_routes_finished), false);
            }
            return;
        }

        Bundle bundle = new Bundle();
        if (nextRoute[EMPTY_INT].getIdScheduled() != null && nextRoute[EMPTY_INT].getIdScheduled() > EMPTY_INT) {
            bundle.putInt(VisitProspectActivity.PARAM_ID_ROUTE, nextRoute[EMPTY_INT].getIdScheduled());
        } else {
            bundle.putInt(VisitProspectActivity.PARAM_ID_ROUTE, nextRoute[EMPTY_INT].getId());
        }

        if (nextRoute[EMPTY_INT].getCustomerId() != null && nextRoute[EMPTY_INT].getCustomerId() > EMPTY_INT) {
            bundle.putInt(VisitProspectActivity.PARAM_ID_CUSTOMER, nextRoute[EMPTY_INT].getCustomerId());
        } else {
            bundle.putInt(VisitProspectActivity.PARAM_ID_PROSPECT, nextRoute[EMPTY_INT].getProspectId());
        }

        Utilidades.openNewActivity(this, VisitProspectActivity.class, bundle, false);
    }

    private void verificarRegistroPontoDiaAtual() {
        if (dbCartaoPonto.obterRegistroDia().isEmpty()) {
            resetarMenu();
        }
    }

    private void verificarAuditagemPendente() {
        DBEstoque dbEstoque = new DBEstoque(this);

        List<AuditagemEstoque> produtos = dbEstoque.getAuditagem();
        if (produtos.isEmpty()) {
            return;
        }

        Alerta alerta = new Alerta(
                this,
                getResources().getString(R.string.app_name),
                "Você possui auditagem pendente, será necessário finaliza-lá para prosseguir."
        );

        alerta.show((dialog, which) ->
                Utilidades.openNewActivity(
                        MainActivity.this,
                        AuditagemVendedorActivity.class,
                        null,
                        false
                ));

    }

    private void inicializarDrawerDisposable() {
        disposables.add(menuEvent
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .subscribe(menuId -> {
                    int id = menuId;

                    try {
                        switch (id) {
                            case R.id.nav_sync:
                                if (Utilidades.isConectado(MainActivity.this)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(TIPO_OPERACAO, 1);
                                    Utilidades.openNewActivity(MainActivity.this, SyncActivity.class, bundle, false);
                                } else
                                    Utilidades.retornaMensagem(MainActivity.this, "Verifique sua conexão com a Internet", false);
                                break;
                            case R.id.nav_os:
                                Utilidades.openNewActivity(MainActivity.this, OS2Activity.class, null, false);
                                break;
                            case R.id.nav_mercadoria_solicitacao:
                                Utilidades.openNewActivity(this, SolicitacaoMercActivity.class, null, false);
                                break;
                            case R.id.nav_mercadoria_troca:
                                Utilidades.openNewActivity(this, ListagemSolicitacaoTrocaActivity.class, null, false);
                                break;
                            case R.id.nav_auditagem:
                                Utilidades.openNewActivity(MainActivity.this, AuditagemVendedorActivity.class, null, false);
                                break;

                            case R.id.nav_relatorio_vendas:
                                Utilidades.openNewActivity(MainActivity.this, RelatorioActivity.class, null, false);
                                break;
                            case R.id.nav_relatorio_supervisor:
                                Utilidades.openNewActivity(MainActivity.this, RelatorioSupervisorActivity.class, null, false);
                                break;
                            case R.id.nav_rota:
                                try {
                                    Utilidades.openRota(MainActivity.this, false);
                                } catch (Exception ex) {
                                    Utilidades.retornaMensagem(MainActivity.this, "Erro: " + ex.getMessage(), true);
                                }
                                break;
                            case R.id.nav_oportunidadevenda:
                                Utilidades.openNewActivity(MainActivity.this, OportunidadeVendaActivity.class, null, false);
                                break;
                            case R.id.nav_credenciado:
                                Utilidades.openNewActivity(MainActivity.this, CredenciadosActivity.class, null, false);
                                break;
                            case R.id.nav_envio_anexo:
                                startActivity(ComprovanteSkyTaActivity.getStart(this));
                                return;
                            case R.id.nav_cartao_ponto:
                                Utilidades.openNewActivity(MainActivity.this, CartaoPontoActivity.class, null, false);
                                break;
                            case R.id.nav_senha_master:
                                Utilidades.openNewActivity(MainActivity.this, SenhaMasterActivity.class, null, false);
                                break;
                            case R.id.nav_verificar_sinal:
                                // TODO: Implmentar quando tiver funcionalidade disponível
                                break;
                            case R.id.nav_barcode:
                                Utilidades.openNewActivity(MainActivity.this, LocalizaChipActivity.class, null, false);
                                break;
                            case R.id.nav_mensagens:
                                Utilidades.openNewActivity(MainActivity.this, MensagensActivity.class, null, false);
                                break;
                            case R.id.nav_suporte:
                                Utilidades.openSuporte(MainActivity.this);
                                break;
                            case R.id.nav_cobrancas:
                                Utilidades.openNewActivity(MainActivity.this, Cobranca2Activity.class, null, false);
                                break;
                            case R.id.nav_solicitacao_preco_dif:
                                Utilidades.openNewActivity(MainActivity.this, ConsultarSolicitacoesPrecoDifActivity.class, null, false);
                                break;
                            case R.id.nav_remessas:
                                Utilidades.openNewActivity(MainActivity.this, RemessaActivity.class, null, false);
                                break;
                            case R.id.nav_vendas:
                                Bundle bundle = new Bundle();
                                bundle.putString("IdCliente", "");
                                Utilidades.openNewActivity(MainActivity.this, VendaActivity.class, bundle, false);
                                break;
                            case R.id.nav_cliente:
                                Utilidades.openNewActivity(MainActivity.this, RegisterListActivity.class, null, false);
                                break;
                            case R.id.nav_estoque:
                                Utilidades.openNewActivity(MainActivity.this, EstoqueActivity.class, null, false);
                                break;
                            case R.id.nav_chamados:
                                Utilidades.openNewActivity(MainActivity.this, ChamadosActivity.class, null, false);
                                break;
                            case R.id.nav_devolucao:
                                Utilidades.openNewActivity(MainActivity.this, DevolucoesActivity.class, null, false);
                                break;
                            case R.id.nav_relatorio_adquirencia:
                                Utilidades.openNewActivity(MainActivity.this, RelatorioAdquirenciaActivity.class, null, false);
                                break;
                            case R.id.nav_adquirencia_rotas:
                                abrirRotasAdiquirencia();
                                break;
                            case R.id.nav_adquirencia_mapa:
                                Utilidades.openNewActivity(MainActivity.this, MapProspectActivity.class, null, false);
                                break;
                            case R.id.nav_adquirencia_ranking:
                                Utilidades.openNewActivity(MainActivity.this, RankingActivity.class, null, false);
                                break;
                            case R.id.nav_base_cliente:
                                Utilidades.openNewActivity(MainActivity.this, ClienteListaActivity.class, null, false);
                                break;
                            case R.id.nav_cliente_pendente:
                                abrirPendencias();
                                break;
                            case R.id.nav_migracao_cliente:
                                Utilidades.openNewActivity(MainActivity.this, ClientMigrationActivity.class, null, false);
                                break;
                            case R.id.nav_simulacao_taxa:
                                Utilidades.openNewActivity(
                                        MainActivity.this,
                                        RegisterProspectListActivity.class,
                                        null,
                                        false
                                );
                                break;
                            case R.id.nav_chat_bot:
                                Colaborador colaborador = new DBColaborador(this).get();
                                String url = BuildConfig.CHATBOT_URL + "?ci=" + colaborador.getUsuarioChatbot() + "&servico=" + BuildConfig.CHATBOT_SERVICO + "&aplicacao=persona";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                break;
                            case R.id.nav_pid:
                                Utilidades.openNewActivity(MainActivity.this, Activity_ListaSolicitacaoPID.class, null, false);
                                break;
                            default:
                                break;
                        }
                    } catch (Exception ex) {
                        Utilidades.retornaMensagem(MainActivity.this, "Erro: " + ex.getMessage(), true);
                    }

                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }, Timber::e));
    }
}
