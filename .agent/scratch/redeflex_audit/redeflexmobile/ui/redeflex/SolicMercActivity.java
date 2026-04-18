package com.axys.redeflexmobile.ui.redeflex;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.ItemSolicitacaoAdapter;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBSolicitacaoMercadoria;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.Validacoes;
import com.axys.redeflexmobile.ui.dialog.NovoProdutoDialog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class SolicMercActivity extends AppCompatActivity implements NovoProdutoDialog.OnCompleteListener
        , ItemSolicitacaoAdapter.OnListener {
    private static final int DELAY_TO_NEXT_ACTION = 1200;

    ArrayList<Produto> listProdutos;
    ItemSolicitacaoAdapter mAdapter;
    ListView listView;
    DBEstoque dbEstoque;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<Integer> menuEventos = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solic_merc);

        Utilidades.getDataServidorESalvaBanco(SolicMercActivity.this);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Solicitação de Mercadoria");
            }

            iniciarEventos();
            criarObjetos();
            carregarDados();
        } catch (Exception ex) {
            Mensagens.mensagemErro(SolicMercActivity.this, ex.getMessage(), true);
        }
    }

    private void criarObjetos() {
        dbEstoque = new DBEstoque(SolicMercActivity.this);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void carregarDados() {
        try {
            listProdutos = dbEstoque.getEstoqueSugerido();
            mAdapter = new ItemSolicitacaoAdapter(this, R.layout.item_solic_merc, listProdutos);
            mAdapter.myListener = this;
            listView.setAdapter(mAdapter);
        } catch (Exception ex) {
            Mensagens.mensagemErro(SolicMercActivity.this, ex.getMessage(), false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    @Override
    public void onCompleteNovoProduto(Produto produto) {
        if (produto != null) {
            listProdutos.add(produto);
            mAdapter = new ItemSolicitacaoAdapter(SolicMercActivity.this, R.layout.item_solic_merc, listProdutos);
            mAdapter.myListener = SolicMercActivity.this;
            listView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solic_merc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuEventos.onNext(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        try {
            if (!Validacoes.validacaoDataAparelho(SolicMercActivity.this)) {
                return;
            }
            if (listProdutos != null && !listProdutos.isEmpty()) {
                for (Produto produto : listProdutos) {
                    if (produto.getQtde() == 0) {
                        produto.setQtde(produto.getEstoqueSugerido());
                    }
                }

                new DBSolicitacaoMercadoria(SolicMercActivity.this).NovaSolicitacao(listProdutos);
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Alerta alerta = new Alerta(SolicMercActivity.this, getResources().getString(R.string.app_name), "Nenhum Produto foi incluído, Verifique!");
                alerta.show();
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(SolicMercActivity.this, ex.getMessage(), false);
        }
    }

    private void limparTudo() {
        listProdutos.clear();
        mAdapter = new ItemSolicitacaoAdapter(this, R.layout.item_solic_merc, listProdutos);
        mAdapter.myListener = this;
        listView.setAdapter(mAdapter);
    }

    private void openNovoProduto() {
        if (!Validacoes.validacaoDataAparelho(SolicMercActivity.this)) {
            return;
        }
        NovoProdutoDialog.listProdutos = dbEstoque.getProdutos();

        if (listProdutos != null && listProdutos.size() > 0) {
            for (Produto produto : listProdutos) {
                if (NovoProdutoDialog.listProdutos != null && NovoProdutoDialog.listProdutos.size() > 0) {
                    for (Produto produto1 : NovoProdutoDialog.listProdutos) {
                        if (produto1.getId().equals(produto.getId())) {
                            NovoProdutoDialog.listProdutos.remove(produto1);
                            break;
                        }
                    }
                }
            }
        }

        if (NovoProdutoDialog.listProdutos.isEmpty()) {
            Alerta alerta = new Alerta(SolicMercActivity.this, getResources().getString(R.string.app_name), "Não existem produtos disponiveis para solicitação");
            alerta.show();
        } else {
            NovoProdutoDialog dialog = new NovoProdutoDialog();
            dialog.myCompleteListener = this;
            dialog.show(getSupportFragmentManager(), "tag");
        }
    }

    private void iniciarEventos() {
        Disposable disposable = menuEventos.throttleFirst(DELAY_TO_NEXT_ACTION, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(menuId -> {
                    if (menuId == android.R.id.home) {
                        if (listProdutos != null && !listProdutos.isEmpty()) {
                            Alerta alerta = new Alerta(SolicMercActivity.this, getResources().getString(R.string.app_name), "Os dados não foram salvos. Deseja continuar sem salvar?");
                            alerta.showConfirm((dialog, which) -> onBackPressed(), null);
                        } else onBackPressed();
                    } else if (menuId == R.id.action_addproduto) {
                        openNovoProduto();
                    } else if (menuId == R.id.action_limpar) {
                        limparTudo();
                    } else if (menuId == R.id.action_salvar) {
                        salvar();
                    }
                }, Timber::e);

        disposables.add(disposable);
    }

    @Override
    public void onUpdateItem(Produto item, int newqtde) {
        if (!Validacoes.validacaoDataAparelho(SolicMercActivity.this)) {
            return;
        }
        if (listProdutos != null && listProdutos.size() > 0) {
            for (Produto produto : listProdutos) {
                if (produto.getId().equals(item.getId())) {
                    produto.setQtde(newqtde);
                    break;
                }
            }
        }
    }
}
