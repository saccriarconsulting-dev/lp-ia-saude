package com.axys.redeflexmobile.ui.venda.merchandising;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBMerchandising;
import com.axys.redeflexmobile.shared.bd.DBProdutosMerchandising;
import com.axys.redeflexmobile.shared.models.venda.Imagem;
import com.axys.redeflexmobile.shared.models.venda.ProdutoMerchandising;
import com.axys.redeflexmobile.shared.models.venda.merchandising.padrao.MerchandisingPadrao;
import com.axys.redeflexmobile.shared.models.venda.merchandising.padrao.MerchandisingProdutoPadrao;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_Imagem;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.venda.abertura.VendaAberturaActivity;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelecionarProdutoActivity extends BaseActivity implements SelecionarProdutoAdapter.ISelecionarProdutoAdapterListener {


    @BindView(R.id.merchandising_produto_tv_titulo) TextView tvTitulo;
    @BindView(R.id.merchandising_produto_rv_claro) RecyclerView rvClaro;
    @BindView(R.id.merchandising_produto_rv_oi) RecyclerView rvOi;
    @BindView(R.id.merchandising_produto_rv_vivo) RecyclerView rvVivo;
    @BindView(R.id.merchandising_produto_rv_tim) RecyclerView rvTim;
    @BindView(R.id.merchadising_produto_sp_claro) Spinner spClaro;
    @BindView(R.id.merchadising_produto_sp_oi) Spinner spOi;
    @BindView(R.id.merchadising_produto_sp_vivo) Spinner spVivo;
    @BindView(R.id.merchadising_produto_sp_tim) Spinner spTim;
    @BindView(R.id.merchandising_produto_foto) LinearLayout llFoto;
    @BindView(R.id.merchandising_produto_ll_container) LinearLayout llContainer;
    @BindView(R.id.merchandising_produto_bt_proximo) Button btProximo;
    private DBProdutosMerchandising dbProdutosMerchandising;
    private DBMerchandising dbMerchandising;
    private boolean merchandisingInterno;
    private boolean merchandisingExterno;
    private int tipoMerchandising;
    private Imagem imagem;
    private MerchandisingPadrao merchandisingPadraoInterno;
    private SelecionarProdutoAdapter produtosClaroAdapter;
    private SelecionarProdutoAdapter produtosOiAdapter;
    private SelecionarProdutoAdapter produtosVivoAdapter;
    private SelecionarProdutoAdapter produtosTimAdapter;
    private List<ProdutoMerchandising> produtosClaro;
    private List<ProdutoMerchandising> produtosOi;
    private List<ProdutoMerchandising> produtosVivo;
    private List<ProdutoMerchandising> produtosTim;
    private List<ProdutoMerchandising> produtosAdicionadosClaro = new ArrayList<>();
    private List<ProdutoMerchandising> produtosAdicionadosOi = new ArrayList<>();
    private List<ProdutoMerchandising> produtosAdicionadosVivo = new ArrayList<>();
    private List<ProdutoMerchandising> produtosAdicionadosTim = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_selecionar_produto;
    }

    @Override
    protected void initialize() {

        dbMerchandising = new DBMerchandising(this);
        dbProdutosMerchandising = new DBProdutosMerchandising(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                merchandisingInterno = bundle.getBoolean(ConstantesMerchandising.MERCHANDISING_INTERNO);
                merchandisingExterno = bundle.getBoolean(ConstantesMerchandising.MERCHANDISING_EXTERNO);
                tipoMerchandising = bundle.getInt(ConstantesMerchandising.TIPO_MERCHANDISING);

                String jsonMerchandisingInterno = bundle.getString(ConstantesMerchandising.JSON_MERCHANDISING);

                if (jsonMerchandisingInterno != null) {
                    merchandisingPadraoInterno = new Gson().fromJson(jsonMerchandisingInterno, MerchandisingPadrao.class);
                    imagem = new Gson().fromJson(bundle.getString(ConstantesMerchandising.JSON_IMAGEM_MERCHANDISING), Imagem.class);
                }

                configuracaoInicial();
            }
        }

        llFoto.findViewById(R.id.merchandising_foto_tv_local).setVisibility(View.GONE);

        iniciarSpinnerClaro();
        iniciarSpinnerOi();
        iniciarSpinnerVivo();
        iniciarSpinnerTim();

        iniciarRecycler();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onClickExcluir(ProdutoMerchandising produto) {
        produto.setData(null);

        produtosAdicionadosClaro.remove(produto);
        produtosAdicionadosOi.remove(produto);
        produtosAdicionadosVivo.remove(produto);
        produtosAdicionadosTim.remove(produto);

        atualizarAdapters();
    }

    @Override
    public void onClickCalendario(ProdutoMerchandising produto, TextView tvData) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            produto.setData(calendar.getTime());
            atualizarAdapters();
        };

        new DatePickerDialog(this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getStringByResId(R.string.merchandising_mensagem_confirmacao_cancelar));
                alerta.showConfirm((dialog, which) -> {
                    if (tipoMerchandising == ConstantesMerchandising.TIPO_EXTERNO && merchandisingInterno) {
                        Bundle bundle = new Bundle();
                        String jsonMerchandisingInterno = getIntent().getExtras().getString(ConstantesMerchandising.JSON_MERCHANDISING);
                        String jsonImagem = getIntent().getExtras().getString(ConstantesMerchandising.JSON_IMAGEM_MERCHANDISING);

                        bundle.putInt(Config.CodigoVisita, getIntent().getExtras().getInt(Config.CodigoVisita));
                        bundle.putString(Config.CodigoCliente, getIntent().getExtras().getString(Config.CodigoCliente));
                        bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_INTERNO, merchandisingInterno);
                        bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_EXTERNO, merchandisingExterno);
                        bundle.putString(ConstantesMerchandising.JSON_MERCHANDISING, jsonMerchandisingInterno);
                        bundle.putString(ConstantesMerchandising.JSON_IMAGEM_MERCHANDISING, jsonImagem);
                        bundle.putInt(ConstantesMerchandising.TIPO_MERCHANDISING, ConstantesMerchandising.TIPO_INTERNO);

                        Utilidades.openNewActivity(this, SelecionarProdutoActivity.class, bundle, true);
                    } else {
                        onBackPressed();
                    }
                }, null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util_Imagem.TAG_PHOTO && resultCode == RESULT_OK) {
            editarFoto();
        }
    }

    @OnClick(R.id.merchandising_produto_bt_proximo)
    public void onClickProximo() {
        if (ConstantesMerchandising.TIPO_OBRIGATORIO != tipoMerchandising && this.produtosAdicionadosClaro.isEmpty() && this.produtosAdicionadosOi.isEmpty() && this.produtosAdicionadosVivo.isEmpty() && this.produtosAdicionadosTim.isEmpty()) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.merchandising_erro_produto), false);
            return;
        }

        if (this.imagem == null || !this.imagem.fotoTirada()) {
            Mensagens.mensagemErro(this, getStringByResId(R.string.merchandising_erro_imagem), false);
            return;
        }

        long idMerchandising = dbMerchandising.obterIdMerchandisingPorIdVisita(
                getIntent().getExtras().getInt(Config.CodigoVisita),
                ConstantesMerchandising.ID_PADRAO
        );

        Bundle bundle = new Bundle();
        bundle.putInt(Config.CodigoVisita, getIntent().getExtras().getInt(Config.CodigoVisita));
        bundle.putString(Config.CodigoCliente, getIntent().getExtras().getString(Config.CodigoCliente));

        MerchandisingPadrao merchandisingPadrao = new MerchandisingPadrao();
        merchandisingPadrao.setIdMerchandising((int) idMerchandising);
        merchandisingPadrao.setMerchanInterno(merchandisingInterno ? ConstantesMerchandising.SIM : ConstantesMerchandising.NAO);
        merchandisingPadrao.setMerchanExterno(merchandisingExterno ? ConstantesMerchandising.SIM : ConstantesMerchandising.NAO);

        if (ConstantesMerchandising.TIPO_OBRIGATORIO == tipoMerchandising) {
            Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getStringByResId(R.string.merchandising_mensagem_confirmacao));
            alerta.showConfirm((dialog, which) -> {
                merchandisingPadrao.setCaminhoFotoExterno(imagem.getUri().toString());
                dbMerchandising.salvarMerchandisingPadrao(merchandisingPadrao);
                finalizarMerchandising(bundle);
            }, null);

            return;
        }

        merchandisingPadrao.setProdutoPadraoList(obterPrudutosSelecionados());
        bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_INTERNO, merchandisingInterno);
        bundle.putBoolean(ConstantesMerchandising.MERCHANDISING_EXTERNO, merchandisingExterno);

        if (btProximo.getText().equals(getStringByResId(R.string.merchandising_produto_bt_proximo))) {
            merchandisingPadrao.setCaminhoFotoInterno(imagem.getUri().toString());
            bundle.putString(ConstantesMerchandising.JSON_MERCHANDISING, new Gson().toJson(merchandisingPadrao));
            bundle.putString(ConstantesMerchandising.JSON_IMAGEM_MERCHANDISING, new Gson().toJson(imagem));
            bundle.putInt(ConstantesMerchandising.TIPO_MERCHANDISING, ConstantesMerchandising.TIPO_EXTERNO);

            Utilidades.openNewActivity(this, SelecionarProdutoActivity.class, bundle, true);
            return;
        }

        if (merchandisingPadraoInterno == null) {
            if (!merchandisingExterno) {
                merchandisingPadrao.setCaminhoFotoInterno(imagem.getUri().toString());
            } else {
                merchandisingPadrao.setCaminhoFotoExterno(imagem.getUri().toString());
            }

            Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getStringByResId(R.string.merchandising_mensagem_confirmacao));
            alerta.showConfirm((dialog, which) -> {
                salvarMerchandisingPadrao(merchandisingPadrao, bundle);
            }, null);

            return;
        }

        merchandisingPadrao.setCaminhoFotoExterno(imagem.getUri().toString());
        merchandisingPadrao.setCaminhoFotoInterno(this.merchandisingPadraoInterno.getCaminhoFotoInterno());
        merchandisingPadrao.getProdutoPadraoList().addAll(this.merchandisingPadraoInterno.getProdutoPadraoList());

        Alerta alerta = new Alerta(this, getResources().getString(R.string.app_name), getStringByResId(R.string.merchandising_mensagem_confirmacao));
        alerta.showConfirm((dialog, which) -> {
            salvarMerchandisingPadrao(merchandisingPadrao, bundle);
        }, null);
    }

    private void salvarMerchandisingPadrao(MerchandisingPadrao merchandisingPadrao, Bundle bundle) {
        dbMerchandising.salvarMerchandisingPadrao(merchandisingPadrao);

        int idUltimoRegistroMerchandising = (int) dbMerchandising.obterIdUltimoRegistro();
        for (MerchandisingProdutoPadrao item : merchandisingPadrao.getProdutoPadraoList()) {
            item.setIdPadrao(idUltimoRegistroMerchandising);
        }

        dbMerchandising.adicionarProdutos(merchandisingPadrao.getProdutoPadraoList());
        finalizarMerchandising(bundle);
    }

    private void finalizarMerchandising(Bundle bundle) {
        Toast.makeText(this, getStringByResId(R.string.merchandising_mensagem_sucesso), Toast.LENGTH_LONG).show();
        Utilidades.openNewActivity(this, VendaAberturaActivity.class, bundle, true);
    }

    private List<MerchandisingProdutoPadrao> obterPrudutosSelecionados() {
        List<MerchandisingProdutoPadrao> produtoPadraoList = new ArrayList<>();
        List<ProdutoMerchandising> produtosAdicionados = new ArrayList<>();

        produtosAdicionados.addAll(produtosAdicionadosClaro);
        produtosAdicionados.addAll(produtosAdicionadosOi);
        produtosAdicionados.addAll(produtosAdicionadosVivo);
        produtosAdicionados.addAll(produtosAdicionadosTim);

        for (ProdutoMerchandising item : produtosAdicionados) {
            produtoPadraoList.add(new MerchandisingProdutoPadrao(item.getId(), item.getData(), this.tipoMerchandising));
        }

        return produtoPadraoList;
    }

    private void editarFoto() {
        if (this.imagem == null || this.imagem.getUri() == null) {
            return;
        }

        this.imagem.setFotoTirada(true);

        Bitmap image = Util_Imagem.createScaledBitmap(this, this.imagem, Util_Imagem.IMAGE_SIZE);
        if (image == null) {
            return;
        }

        if (this.imagem.getUri().toString().contains(Util_Imagem.DEVICE_FILE)) {
            Util_Imagem.saveImage(image, this.imagem.getPath(), Util_Imagem.IMAGE_QUALITY);
        }

        ImageView imageView = (ImageView) llFoto.findViewById(R.id.merchandising_foto_iv_foto);
        imageView.setImageURI(imagem.getUri());
    }

    private void atualizarAdapters() {
        produtosClaroAdapter.notifyDataSetChanged();
        produtosOiAdapter.notifyDataSetChanged();
        produtosVivoAdapter.notifyDataSetChanged();
        produtosTimAdapter.notifyDataSetChanged();
    }

    private void configuracaoInicial() {
        String toolbar;

        if (tipoMerchandising == ConstantesMerchandising.TIPO_INTERNO) {
            toolbar = getStringByResId(R.string.merchandising_produto_titulo_interno);
            if (merchandisingInterno && !merchandisingExterno) {
                btProximo.setText(getStringByResId(R.string.merchandising_produto_bt_concluir));
            }
        } else if (tipoMerchandising == ConstantesMerchandising.TIPO_EXTERNO) {
            btProximo.setText(getStringByResId(R.string.merchandising_produto_bt_concluir));
            toolbar = getStringByResId(R.string.merchandising_produto_titulo_externo);
        } else {
            llContainer.setVisibility(View.GONE);
            toolbar = getStringByResId(R.string.merchandising_produto_titulo_nenhum);
            btProximo.setText(getStringByResId(R.string.merchandising_produto_bt_concluir));
        }

        setTitle(toolbar);
        tvTitulo.setText(toolbar);
        llFoto.findViewById(R.id.merchandising_foto_bt_tirar_foto).setOnClickListener(view -> tirarFoto());
        llFoto.findViewById(R.id.merchandising_foto_tv_remover).setOnClickListener(view -> {
                    ((ImageView) llFoto.findViewById(R.id.merchandising_foto_iv_foto)).setImageResource(R.drawable.ic_loja);
                    imagem = null;
                }
        );

        if (merchandisingPadraoInterno != null && tipoMerchandising == ConstantesMerchandising.TIPO_INTERNO) {
            List<ProdutoMerchandising> listaCompletaProdutos = new ArrayList<>();
            listaCompletaProdutos.addAll(dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.CLARO));
            listaCompletaProdutos.addAll(dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.OI));
            listaCompletaProdutos.addAll(dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.VIVO));
            listaCompletaProdutos.addAll(dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.TIM));

            for (MerchandisingProdutoPadrao item : merchandisingPadraoInterno.getProdutoPadraoList()) {
                ProdutoMerchandising produtoMerchandising = obterProdutoPorIdProduto(listaCompletaProdutos, item);

                if (produtoMerchandising.getOperadora() == ConstantesMerchandising.ID_CLARO) {
                    produtosAdicionadosClaro.add(produtoMerchandising);
                    continue;
                }

                if (produtoMerchandising.getOperadora() == ConstantesMerchandising.ID_OI) {
                    produtosAdicionadosOi.add(produtoMerchandising);
                    continue;
                }

                if (produtoMerchandising.getOperadora() == ConstantesMerchandising.ID_VIVO) {
                    produtosAdicionadosVivo.add(produtoMerchandising);
                    continue;
                }

                if (produtoMerchandising.getOperadora() == ConstantesMerchandising.ID_TIM) {
                    produtosAdicionadosTim.add(produtoMerchandising);
                }
            }

            if (imagem != null) {
                ImageView imageView = (ImageView) llFoto.findViewById(R.id.merchandising_foto_iv_foto);
                imageView.setImageURI(imagem.getUri());
            }
        }
    }

    private ProdutoMerchandising obterProdutoPorIdProduto(List<ProdutoMerchandising> listaCompletaProdutos, MerchandisingProdutoPadrao merchandisingProdutoPadrao) {
        for (ProdutoMerchandising item : listaCompletaProdutos) {
            if (item.getId() == merchandisingProdutoPadrao.getIdProduto()) {
                item.setData(merchandisingProdutoPadrao.getData());
                return item;
            }
        }

        return null;
    }

    private void tirarFoto() {
        Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentPhoto.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        String imagemNome = String.valueOf(System.currentTimeMillis()) + Util_Imagem.PHOTO_EXTENSION;
        this.imagem = new Imagem();
        this.imagem.setNome(imagemNome);

        try {
            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(imagemNome, Util_Imagem.PHOTO_EXTENSION, storageDir);
            this.imagem.setPath(imageFile.getAbsolutePath());
            this.imagem.setUri(FileProvider.getUriForFile(this, (this.getApplicationContext().getPackageName() + Util_Imagem.PROVIDER), imageFile));
            intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, this.imagem.getUri());
            startActivityForResult(intentPhoto, Util_Imagem.TAG_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarSpinnerTim() {
        produtosTim = dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.TIM);
        ArrayAdapter<ProdutoMerchandising> adapterSpinnerTim = new ArrayAdapter<ProdutoMerchandising>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                produtosTim) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(produtosTim.get(getCount()).getNome());
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        spTim.setAdapter(adapterSpinnerTim);
        spTim.setSelection(adapterSpinnerTim.getCount());
        spTim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spTim.setSelection(adapterSpinnerTim.getCount());

                ProdutoMerchandising produtoMerchandising = produtosTim.get(i);

                if (produtoMerchandising.getId() != produtosTim.get(adapterSpinnerTim.getCount()).getId()) {
                    if (produtosAdicionadosTim.contains(produtoMerchandising)) {
                        Toast.makeText(SelecionarProdutoActivity.this, "Produto já está adicionado", Toast.LENGTH_LONG).show();
                        return;
                    }

                    produtosAdicionadosTim.add(produtoMerchandising);
                    produtosTimAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void iniciarSpinnerVivo() {
        produtosVivo = dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.VIVO);
        ArrayAdapter<ProdutoMerchandising> adapterSpinnerVivo = new ArrayAdapter<ProdutoMerchandising>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                produtosVivo) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(produtosVivo.get(getCount()).getNome());
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        spVivo.setAdapter(adapterSpinnerVivo);
        spVivo.setSelection(adapterSpinnerVivo.getCount());
        spVivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spVivo.setSelection(adapterSpinnerVivo.getCount());

                ProdutoMerchandising produtoMerchandising = produtosVivo.get(i);

                if (produtoMerchandising.getId() != produtosVivo.get(adapterSpinnerVivo.getCount()).getId()) {
                    if (produtosAdicionadosVivo.contains(produtoMerchandising)) {
                        Toast.makeText(SelecionarProdutoActivity.this, "Produto já está adicionado", Toast.LENGTH_LONG).show();
                        return;
                    }

                    produtosAdicionadosVivo.add(produtoMerchandising);
                    produtosVivoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void iniciarSpinnerOi() {
        produtosOi = dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.OI);
        ArrayAdapter<ProdutoMerchandising> adapterSpinnerOi = new ArrayAdapter<ProdutoMerchandising>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                produtosOi) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(produtosOi.get(getCount()).getNome());
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        spOi.setAdapter(adapterSpinnerOi);
        spOi.setSelection(adapterSpinnerOi.getCount());
        spOi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spOi.setSelection(adapterSpinnerOi.getCount());

                ProdutoMerchandising produtoMerchandising = produtosOi.get(i);

                if (produtoMerchandising.getId() != produtosOi.get(adapterSpinnerOi.getCount()).getId()) {
                    if (produtosAdicionadosOi.contains(produtoMerchandising)) {
                        Toast.makeText(SelecionarProdutoActivity.this, "Produto já está adicionado", Toast.LENGTH_LONG).show();
                        return;
                    }

                    produtosAdicionadosOi.add(produtoMerchandising);
                    produtosOiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void iniciarSpinnerClaro() {
        produtosClaro = dbProdutosMerchandising.obterProdutos(ConstantesMerchandising.CLARO);
        ArrayAdapter<ProdutoMerchandising> adapterSpinnerClaro = new ArrayAdapter<ProdutoMerchandising>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                produtosClaro) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(produtosClaro.get(getCount()).getNome());
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };

        spClaro.setAdapter(adapterSpinnerClaro);
        spClaro.setSelection(adapterSpinnerClaro.getCount());
        spClaro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spClaro.setSelection(adapterSpinnerClaro.getCount());

                ProdutoMerchandising produtoMerchandising = produtosClaro.get(i);

                if (produtoMerchandising.getId() != produtosClaro.get(adapterSpinnerClaro.getCount()).getId()) {
                    if (produtosAdicionadosClaro.contains(produtoMerchandising)) {
                        Toast.makeText(SelecionarProdutoActivity.this, "Produto já está adicionado", Toast.LENGTH_LONG).show();
                        return;
                    }

                    produtosAdicionadosClaro.add(produtoMerchandising);
                    produtosClaroAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void iniciarRecycler() {
        produtosClaroAdapter = new SelecionarProdutoAdapter(this, produtosAdicionadosClaro, this);
        rvClaro.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvClaro.setAdapter(produtosClaroAdapter);

        produtosOiAdapter = new SelecionarProdutoAdapter(this, produtosAdicionadosOi, this);
        rvOi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOi.setAdapter(produtosOiAdapter);

        produtosVivoAdapter = new SelecionarProdutoAdapter(this, produtosAdicionadosVivo, this);
        rvVivo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvVivo.setAdapter(produtosVivoAdapter);

        produtosTimAdapter = new SelecionarProdutoAdapter(this, produtosAdicionadosTim, this);
        rvTim.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvTim.setAdapter(produtosTimAdapter);
    }
}
