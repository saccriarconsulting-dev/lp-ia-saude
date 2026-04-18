package com.axys.redeflexmobile.shared.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;

import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.Util_IO;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ClienteDaoImpl implements ClienteDao {

    private final Context context;
    private final String mTabela = "Cliente";

    public ClienteDaoImpl(Context context) {
        this.context = context;
    }

    @Override
    @Nullable
    public Cliente obterPorId(String id) {
        String sql = "SELECT id" +
                ",nomeFantasia" +
                ",razaoSocial" +
                ",TipoLogradouro" +
                ",nomeLogradouro" +
                ",complementoLogradouro" +
                ",bairro" +
                ",cidade" +
                ",estado" +
                ",cep" +
                ",dddTelefone" +
                ",telefone" +
                ",dddCelular" +
                ",celular" +
                ",cpf_cnpj" +
                ",rg_ie" +
                ",IFNULL(codigoSGV,'')" +
                ",ativo" +
                ",numeroLogradouro" +
                ",idclienteintraflex" +
                ",IFNULL(codigoeFresh,'')" +
                ",IFNULL(codigoAplic,'')" +
                ",IFNULL(exibirCodigo,'')" +
                ",IFNULL(atualizarLocal,'N')" +
                ",IFNULL(senha,'')" +
                ",IFNULL(pedeSenha,'N')" +
                ",IFNULL(auditagem,'N')" +
                ",IFNULL(statuseFresh,'')" +
                ",IFNULL(statusSGV,'')" +
                ",IFNULL(fechamento,'')" +
                ",IFNULL(atualizarContato,'N')" +
                ",IFNULL(email,'')" +
                ",IFNULL(diasCortes,15)" +
                ",IFNULL(bloqueiaVendaVista,0)" +
                ",IFNULL(curva,'')" +
                ",IFNULL(atualizaBinario,0)" +
                ",IFNULL(adquirencia,0)" +
                ",IFNULL(eletronico,0)" +
                ",IFNULL(fisico,0)" +
                ",IFNULL(qtdBoletoPendente,0)" +
                ",IFNULL(valorBoletoPendente,0)" +
                ",IFNULL(curvaChip,'')" +
                ",IFNULL(cerca,0)" +
                ",IFNULL(bloqueiaAtendimento,0)" +
                ",IFNULL(qtdReprovacaoImg,0)" +
                ",IFNULL(recadastro,0)" +
                ",IFNULL(merchandising,'')" +
                ",IFNULL(idSegmentoSGV,0)" +
                ",IFNULL(pontoReferencia,'')" +
                ",IFNULL(contato,'')" +
                ",IFNULL(possuiRecadastro,'N')" +
                ",IFNULL(retornoRecadastro,'')" +
                ",IFNULL(latitude, 0)" +
                ",IFNULL(longitude, 0) " +
                ",clienteMigracaoSub " +
                ",antecipacaoAutomatica " +
                ",prazoDeNegociacao " +
                ",idDomicilio " +
                ",nomeBanco " +
                ",tipoConta " +
                ",agencia " +
                ",digitoAgencia " +
                ",conta " +
                ",digitoConta " +
                ",negociadoMigracaoSub " +
                ",idBanco " +
                ",idMcc " +
                ",telefoneSub " +
                ",emailSub " +
                ",clienteTipoAdq " +
                ",VendaConsignada " +
                ",AuditagemConsignadaObriga " +
                ",clienteMigracaoAdq " +
                "FROM [Cliente]" +
                "WHERE id = ?";

        Cliente cliente = null;
        try (Cursor cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sql, new String[]{id})) {
            while (cursor.moveToNext()) {
                cliente = new Cliente();
                cliente.setId(cursor.getString(0));
                cliente.setNomeFantasia(cursor.getString(1));
                cliente.setRazaoSocial(cursor.getString(2));
                cliente.setTipoLogradouro(cursor.getString(3));
                cliente.setNomeLogradouro(cursor.getString(4));
                cliente.setComplementoLogradouro(cursor.getString(5));
                cliente.setBairro(cursor.getString(6));
                cliente.setCidade(cursor.getString(7));
                cliente.setEstado(cursor.getString(8));
                cliente.setCep(cursor.getString(9));
                cliente.setDddTelefone(cursor.getString(10));
                cliente.setTelefone(cursor.getString(11));
                cliente.setDddCelular(cursor.getString(12));
                cliente.setCelular(cursor.getString(13));
                cliente.setCpf_cnpj(cursor.getString(14));
                cliente.setRg_ie(cursor.getString(15));
                cliente.setCodigoSGV(cursor.getString(16));
                cliente.setAtivo(cursor.getString(17));
                cliente.setNumeroLogradouro(cursor.getString(18));
                cliente.setCodigoIntraFlex(cursor.getString(19));
                cliente.setCodigoeFresh(cursor.getString(20));
                cliente.setCodigoAplic(cursor.getString(21));
                cliente.setExibirCodigo(cursor.getString(22));
                cliente.setAtualizaLocal(cursor.getString(23));
                cliente.setSenha(cursor.getString(24));
                cliente.setPedeSenha(cursor.getString(25));
                cliente.setAuditagem(cursor.getString(26));
                cliente.setStatuseFresh(cursor.getString(27));
                cliente.setStatusSGV(cursor.getString(28));
                cliente.setFechamentoFatura(cursor.getString(29));
                cliente.setAtualizaContato(cursor.getString(30));
                cliente.setEmailCliente(cursor.getString(31));
                cliente.setDiasCortes(cursor.getInt(32));
                cliente.setBloqueiaVendaVista(Util_IO.numberToBoolean(cursor.getInt(33)));
                cliente.setCurvaRecarga(cursor.getString(34));
                cliente.setAtualizaBinario(Util_IO.numberToBoolean(cursor.getInt(35)));
                cliente.setAdquirencia(Util_IO.numberToBoolean(cursor.getInt(36)));
                cliente.setEletronico(Util_IO.numberToBoolean(cursor.getInt(37)));
                cliente.setFisico(Util_IO.numberToBoolean(cursor.getInt(38)));
                cliente.setQtdBoletoPendente(cursor.getInt(39));
                cliente.setValorBoletoPendente(cursor.getDouble(40));
                cliente.setCurvaChip(cursor.getString(41));
                cliente.setCerca(cursor.getInt(42));
                cliente.setBloqueiaAtendimento(Util_IO.numberToBoolean(cursor.getInt(43)));
                cliente.setQtdLocReprovada(cursor.getInt(44));
                cliente.setRecadastro(Util_IO.numberToBoolean(cursor.getInt(45)));
                cliente.setMerchandising(cursor.getString(46));
                cliente.setIdSegmentoSGV(cursor.getString(47));
                cliente.setPontoReferencia(cursor.getString(48));
                cliente.setContato(cursor.getString(49));
                cliente.setPossuiRecadastro(cursor.getString(50));
                cliente.setRetornoRecadastro(cursor.getString(51));
                cliente.setLatitude(cursor.getDouble(52));
                cliente.setLongitude(cursor.getDouble(53));
                cliente.setClienteMigracaoSub(cursor.getInt(54) == 1);
                cliente.setAntecipacaoAutomatica(getBooleanOrNull(cursor, 55));
                cliente.setPrazoDeNegociacao(cursor.getString(56));
                cliente.setIdDomicilio(cursor.getInt(57));
                cliente.setNomeBanco(cursor.getString(58));
                cliente.setTipoConta(cursor.getString(59));
                cliente.setAgencia(cursor.getString(60));
                cliente.setDigitoAgencia(cursor.getString(61));
                cliente.setConta(cursor.getString(62));
                cliente.setDigitoConta(cursor.getString(63));
                cliente.setNegociadoMigracaoSub(cursor.getInt(64) == 1);
                cliente.setIdBanco(cursor.getInt(65));
                cliente.setIdMcc(cursor.getInt(66));
                cliente.setTelefoneSub(cursor.getString(67));
                cliente.setEmailSub(cursor.getString(68));
                cliente.setClienteTipoAdq(cursor.getInt(69));
                cliente.setVendaConsignada(Util_IO.numberToBoolean(cursor.getInt(70)));
                cliente.setAuditagemConsignadaObriga(Util_IO.numberToBoolean(cursor.getInt(71)));
                cliente.setClienteMigracaoAdq(cursor.getInt(72) == 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cliente;
    }

    @Override
    public void confirmaAuditagem(String pIdCliente) {
        ContentValues values = new ContentValues();
        values.put("auditagem", "N");
        values.put("sync", 0);
        SimpleDbHelper.INSTANCE.open(context).update(mTabela, values, "[id]=?", new String[]{pIdCliente});
    }

    public Single<Cliente> obterClientePorId(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }
                Cliente cliente = obterPorId(String.valueOf(id));
                emitter.onSuccess(cliente);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public void confirmaAtualizacaoCadastral(String idCliente) {
        ContentValues values = new ContentValues();
        values.put("recadastro", 0);
        values.put("possuiRecadastro", "S");
        values.put("retornoRecadastro", "");
        SimpleDbHelper.INSTANCE.open(context).update(mTabela, values, "[id]=?", new String[]{idCliente});
    }

    @Override
    public void confirmaAtualizacaoBinario(String idCliente) {
        ContentValues values = new ContentValues();
        values.put("atualizaBinario", 0);
        values.put("sync", 0);
        SimpleDbHelper.INSTANCE
                .open(context)
                .update(mTabela, values, "[id]=?", new String[]{idCliente});
    }

    @Override
    public Observable<List<Cliente>> obterTodos() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            List<Cliente> clientes = new ArrayList<>();
            String sql = "SELECT id" +
                    ",nomeFantasia" +
                    ",razaoSocial" +
                    ",TipoLogradouro" +
                    ",nomeLogradouro" +
                    ",complementoLogradouro" +
                    ",bairro" +
                    ",cidade" +
                    ",estado" +
                    ",cep" +
                    ",dddTelefone" +
                    ",telefone" +
                    ",dddCelular" +
                    ",celular" +
                    ",cpf_cnpj" +
                    ",rg_ie" +
                    ",IFNULL(codigoSGV,'')" +
                    ",ativo" +
                    ",numeroLogradouro" +
                    ",idclienteintraflex" +
                    ",IFNULL(codigoeFresh,'')" +
                    ",IFNULL(codigoAplic,'')" +
                    ",IFNULL(exibirCodigo,'')" +
                    ",IFNULL(atualizarLocal,'N')" +
                    ",IFNULL(senha,'')" +
                    ",IFNULL(pedeSenha,'N')" +
                    ",IFNULL(auditagem,'N')" +
                    ",IFNULL(statuseFresh,'')" +
                    ",IFNULL(statusSGV,'')" +
                    ",IFNULL(fechamento,'')" +
                    ",IFNULL(atualizarContato,'N')" +
                    ",IFNULL(email,'')" +
                    ",IFNULL(diasCortes,15)" +
                    ",IFNULL(bloqueiaVendaVista,0)" +
                    ",IFNULL(curva,'')" +
                    ",IFNULL(atualizaBinario,0)" +
                    ",IFNULL(adquirencia,0)" +
                    ",IFNULL(eletronico,0)" +
                    ",IFNULL(fisico,0)" +
                    ",IFNULL(qtdBoletoPendente,0)" +
                    ",IFNULL(valorBoletoPendente,0)" +
                    ",IFNULL(curvaChip,'')" +
                    ",IFNULL(cerca,0)" +
                    ",IFNULL(bloqueiaAtendimento,0)" +
                    ",IFNULL(qtdReprovacaoImg,0)" +
                    ",IFNULL(recadastro,0)" +
                    ",IFNULL(merchandising,'')" +
                    ",IFNULL(idSegmentoSGV,0)" +
                    ",IFNULL(pontoReferencia,'')" +
                    ",IFNULL(contato,'')" +
                    ",IFNULL(possuiRecadastro,'N')" +
                    ",IFNULL(retornoRecadastro,'')" +
                    ",IFNULL(latitude, 0)" +
                    ",IFNULL(longitude, 0) " +
                    ",clienteMigracaoSub " +
                    ",antecipacaoAutomatica " +
                    ",prazoDeNegociacao " +
                    ",idDomicilio " +
                    ",nomeBanco " +
                    ",tipoConta " +
                    ",agencia " +
                    ",digitoAgencia " +
                    ",conta " +
                    ",digitoConta " +
                    ",negociadoMigracaoSub " +
                    ",idBanco " +
                    ",idMcc " +
                    ",telefoneSub " +
                    ",emailSub " +
                    ",clienteTipoAdq " +
                    ",VendaConsignada " +
                    ",AuditagemConsignadaObriga " +
                    ",clienteMigracaoAdq " +
                    "FROM [Cliente] " +
                    "ORDER BY nomeFantasia COLLATE NOCASE";

            Cliente cliente;
            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                cliente = new Cliente();
                cliente.setId(cursor.getString(0));
                cliente.setNomeFantasia(cursor.getString(1));
                cliente.setRazaoSocial(cursor.getString(2));
                cliente.setTipoLogradouro(cursor.getString(3));
                cliente.setNomeLogradouro(cursor.getString(4));
                cliente.setComplementoLogradouro(cursor.getString(5));
                cliente.setBairro(cursor.getString(6));
                cliente.setCidade(cursor.getString(7));
                cliente.setEstado(cursor.getString(8));
                cliente.setCep(cursor.getString(9));
                cliente.setDddTelefone(cursor.getString(10));
                cliente.setTelefone(cursor.getString(11));
                cliente.setDddCelular(cursor.getString(12));
                cliente.setCelular(cursor.getString(13));
                cliente.setCpf_cnpj(cursor.getString(14));
                cliente.setRg_ie(cursor.getString(15));
                cliente.setCodigoSGV(cursor.getString(16));
                cliente.setAtivo(cursor.getString(17));
                cliente.setNumeroLogradouro(cursor.getString(18));
                cliente.setCodigoIntraFlex(cursor.getString(19));
                cliente.setCodigoeFresh(cursor.getString(20));
                cliente.setCodigoAplic(cursor.getString(21));
                cliente.setExibirCodigo(cursor.getString(22));
                cliente.setAtualizaLocal(cursor.getString(23));
                cliente.setSenha(cursor.getString(24));
                cliente.setPedeSenha(cursor.getString(25));
                cliente.setAuditagem(cursor.getString(26));
                cliente.setStatuseFresh(cursor.getString(27));
                cliente.setStatusSGV(cursor.getString(28));
                cliente.setFechamentoFatura(cursor.getString(29));
                cliente.setAtualizaContato(cursor.getString(30));
                cliente.setEmailCliente(cursor.getString(31));
                cliente.setDiasCortes(cursor.getInt(32));
                cliente.setBloqueiaVendaVista(Util_IO.numberToBoolean(cursor.getInt(33)));
                cliente.setCurvaRecarga(cursor.getString(34));
                cliente.setAtualizaBinario(Util_IO.numberToBoolean(cursor.getInt(35)));
                cliente.setAdquirencia(Util_IO.numberToBoolean(cursor.getInt(36)));
                cliente.setEletronico(Util_IO.numberToBoolean(cursor.getInt(37)));
                cliente.setFisico(Util_IO.numberToBoolean(cursor.getInt(38)));
                cliente.setQtdBoletoPendente(cursor.getInt(39));
                cliente.setValorBoletoPendente(cursor.getDouble(40));
                cliente.setCurvaChip(cursor.getString(41));
                cliente.setCerca(cursor.getInt(42));
                cliente.setBloqueiaAtendimento(Util_IO.numberToBoolean(cursor.getInt(43)));
                cliente.setQtdLocReprovada(cursor.getInt(44));
                cliente.setRecadastro(Util_IO.numberToBoolean(cursor.getInt(45)));
                cliente.setMerchandising(cursor.getString(46));
                cliente.setIdSegmentoSGV(cursor.getString(47));
                cliente.setPontoReferencia(cursor.getString(48));
                cliente.setContato(cursor.getString(49));
                cliente.setPossuiRecadastro(cursor.getString(50));
                cliente.setRetornoRecadastro(cursor.getString(51));
                cliente.setLatitude(cursor.getDouble(52));
                cliente.setLongitude(cursor.getDouble(53));
                cliente.setClienteMigracaoSub(cursor.getInt(54) == 1);
                cliente.setAntecipacaoAutomatica(getBooleanOrNull(cursor, 55));
                cliente.setPrazoDeNegociacao(cursor.getString(56));
                cliente.setIdDomicilio(cursor.getInt(57));
                cliente.setNomeBanco(cursor.getString(58));
                cliente.setTipoConta(cursor.getString(59));
                cliente.setAgencia(cursor.getString(60));
                cliente.setDigitoAgencia(cursor.getString(61));
                cliente.setConta(cursor.getString(62));
                cliente.setDigitoConta(cursor.getString(63));
                cliente.setNegociadoMigracaoSub(cursor.getInt(64) == 1);
                cliente.setIdBanco(cursor.getInt(65));
                cliente.setIdMcc(cursor.getInt(66));
                cliente.setTelefoneSub(cursor.getString(67));
                cliente.setEmailSub(cursor.getString(68));
                cliente.setClienteTipoAdq(cursor.getInt(69));
                cliente.setVendaConsignada(Util_IO.numberToBoolean(cursor.getInt(70)));
                cliente.setAuditagemConsignadaObriga(Util_IO.numberToBoolean(cursor.getInt(71)));
                cliente.setClienteMigracaoAdq(cursor.getInt(72) == 1);
                // Verifica se o Cliente tem Consignacao Ativa
                cliente.setConsignacaoAtiva(clienteConsignacaoAtiva(cursor.getString(0)));
                clientes.add(cliente);
            }
            cursor.close();

            emitter.onNext(clientes);
            emitter.onComplete();
        });
    }

    private Boolean clienteConsignacaoAtiva(String idCliente)
    {
        String sql = "Select Count(*) from Consignado t0 " +
                "where t0.idCliente = " + idCliente + " and t0.TipoConsignado = 'CON' and t0.Status = 0";
        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        Cursor cursor =  db.rawQuery(sql, null);
        cursor.moveToFirst();
        Integer count = cursor.getInt(0);
        return count > 0;
    }

    @Override
    public Boolean clientePossuiPendencias(String idCliente) {
        String sql = "SELECT COUNT(*) FROM PendenciaCliente AS PC " +
                "INNER JOIN Cliente AS C ON (PC.clienteId = C.id) " +
                "WHERE PC.pendenciaMotivoId == 0 AND PC.ClienteId = "+idCliente;

        SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
        Cursor cursor =  db.rawQuery(sql, null);
        cursor.moveToFirst();
        Integer count = cursor.getInt(0);
        return count > 0;
    }

    @Override
    public Observable<List<Cliente>> obterClientesComPendencia() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT C.id" +
                    ",C.nomeFantasia" +
                    ",C.razaoSocial" +
                    ",C.TipoLogradouro" +
                    ",C.nomeLogradouro" +
                    ",C.complementoLogradouro" +
                    ",C.bairro" +
                    ",C.cidade" +
                    ",C.estado" +
                    ",C.cep" +
                    ",C.dddTelefone" +
                    ",C.telefone" +
                    ",C.dddCelular" +
                    ",C.celular" +
                    ",C.cpf_cnpj" +
                    ",C.rg_ie" +
                    ",IFNULL(C.codigoSGV,'')" +
                    ",C.ativo" +
                    ",C.numeroLogradouro" +
                    ",C.idclienteintraflex" +
                    ",IFNULL(C.codigoeFresh,'')" +
                    ",IFNULL(C.codigoAplic,'')" +
                    ",IFNULL(C.exibirCodigo,'')" +
                    ",IFNULL(C.atualizarLocal,'N')" +
                    ",IFNULL(C.senha,'')" +
                    ",IFNULL(C.pedeSenha,'N')" +
                    ",IFNULL(C.auditagem,'N')" +
                    ",IFNULL(C.statuseFresh,'')" +
                    ",IFNULL(C.statusSGV,'')" +
                    ",IFNULL(C.fechamento,'')" +
                    ",IFNULL(C.atualizarContato,'N')" +
                    ",IFNULL(C.email,'')" +
                    ",IFNULL(C.diasCortes,15)" +
                    ",IFNULL(C.bloqueiaVendaVista,0)" +
                    ",IFNULL(C.curva,'')" +
                    ",IFNULL(C.atualizaBinario,0)" +
                    ",IFNULL(C.adquirencia,0)" +
                    ",IFNULL(C.eletronico,0)" +
                    ",IFNULL(C.fisico,0)" +
                    ",IFNULL(C.qtdBoletoPendente,0)" +
                    ",IFNULL(C.valorBoletoPendente,0)" +
                    ",IFNULL(C.curvaChip,'')" +
                    ",IFNULL(C.cerca,0)" +
                    ",IFNULL(C.bloqueiaAtendimento,0)" +
                    ",IFNULL(C.qtdReprovacaoImg,0)" +
                    ",IFNULL(C.recadastro,0)" +
                    ",IFNULL(C.curvaAdquirencia,'')" +
                    ",IFNULL(C.clienteFisico,'')" +
                    ",IFNULL(C.clienteEletronico,'')" +
                    ",IFNULL(C.clienteAdquirencia,'')" +
                    ",IFNULL(C.clienteAppFlex,'')" +
                    ",IFNULL(C.merchandising,'')" +
                    ",IFNULL(C.idSegmentoSGV,0)" +
                    ",IFNULL(C.pontoReferencia,'')" +
                    ",IFNULL(C.contato,'')" +
                    ",IFNULL(C.possuiRecadastro,'N')" +
                    ",IFNULL(C.retornoRecadastro,'')" +
                    ",C.latitude" +
                    ",C.longitude" +
                    ",C.dddTelefone2" +
                    ",C.telefone2" +
                    ",C.dddCelular2" +
                    ",C.celular2" +
                    ",C.vencimentoFatura" +
                    ",C.limitePrimeiraVenda " +
                    ",C.negociadoMigracaoSub " +
                    ",C.idBanco " +
                    ",C.idMcc " +
                    ",C.telefoneSub " +
                    ",C.emailSub " +
                    ",C.clienteTipoAdq " +
                    ",IFNULL(C.clienteCorban,'')" +
                    ",C.VendaConsignada " +
                    ",C.AuditagemConsignadaObriga " +
                    " FROM PendenciaCliente AS PC " +
                    "INNER JOIN Cliente AS C ON (PC.clienteId = C.id) " +
                    "GROUP BY C.id " +
                    "ORDER BY C.nomeFantasia ASC";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            List<Cliente> clientes = moveCursor(db.rawQuery(sql, null));
            emitter.onNext(clientes);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<List<Cliente>> obterClientesMigracao() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT C.id" +
                    ",C.nomeFantasia" +
                    ",C.razaoSocial" +
                    ",C.TipoLogradouro" +
                    ",C.nomeLogradouro" +
                    ",C.complementoLogradouro" +
                    ",C.bairro" +
                    ",C.cidade" +
                    ",C.estado" +
                    ",C.cep" +
                    ",C.dddTelefone" +
                    ",C.telefone" +
                    ",C.dddCelular" +
                    ",C.celular" +
                    ",C.cpf_cnpj" +
                    ",C.rg_ie" +
                    ",IFNULL(C.codigoSGV,'')" +
                    ",C.ativo" +
                    ",C.numeroLogradouro" +
                    ",C.idclienteintraflex" +
                    ",IFNULL(C.codigoeFresh,'')" +
                    ",IFNULL(C.codigoAplic,'')" +
                    ",IFNULL(C.exibirCodigo,'')" +
                    ",IFNULL(C.atualizarLocal,'N')" +
                    ",IFNULL(C.senha,'')" +
                    ",IFNULL(C.pedeSenha,'N')" +
                    ",IFNULL(C.auditagem,'N')" +
                    ",IFNULL(C.statuseFresh,'')" +
                    ",IFNULL(C.statusSGV,'')" +
                    ",IFNULL(C.fechamento,'')" +
                    ",IFNULL(C.atualizarContato,'N')" +
                    ",IFNULL(C.email,'')" +
                    ",IFNULL(C.diasCortes,15)" +
                    ",IFNULL(C.bloqueiaVendaVista,0)" +
                    ",IFNULL(C.curva,'')" +
                    ",IFNULL(C.atualizaBinario,0)" +
                    ",IFNULL(C.adquirencia,0)" +
                    ",IFNULL(C.eletronico,0)" +
                    ",IFNULL(C.fisico,0)" +
                    ",IFNULL(C.qtdBoletoPendente,0)" +
                    ",IFNULL(C.valorBoletoPendente,0)" +
                    ",IFNULL(C.curvaChip,'')" +
                    ",IFNULL(C.cerca,0)" +
                    ",IFNULL(C.bloqueiaAtendimento,0)" +
                    ",IFNULL(C.qtdReprovacaoImg,0)" +
                    ",IFNULL(C.recadastro,0)" +
                    ",IFNULL(C.curvaAdquirencia,'')" +
                    ",IFNULL(C.clienteFisico,'')" +
                    ",IFNULL(C.clienteEletronico,'')" +
                    ",IFNULL(C.clienteAdquirencia,'')" +
                    ",IFNULL(C.clienteAppFlex,'')" +
                    ",IFNULL(C.merchandising,'')" +
                    ",IFNULL(C.idSegmentoSGV,0)" +
                    ",IFNULL(C.pontoReferencia,'')" +
                    ",IFNULL(C.contato,'')" +
                    ",IFNULL(C.possuiRecadastro,'N')" +
                    ",IFNULL(C.retornoRecadastro,'')" +
                    ",C.latitude" +
                    ",C.longitude" +
                    ",C.dddTelefone2" +
                    ",C.telefone2" +
                    ",C.dddCelular2" +
                    ",C.celular2" +
                    ",C.vencimentoFatura" +
                    ",C.limitePrimeiraVenda " +
                    ",C.clienteMigracaoSub " +
                    ",C.antecipacaoAutomatica " +
                    ",C.prazoDeNegociacao " +
                    ",C.idDomicilio " +
                    ",C.nomeBanco " +
                    ",C.tipoConta " +
                    ",C.agencia " +
                    ",C.digitoAgencia " +
                    ",C.conta " +
                    ",C.digitoConta " +
                    ",C.negociadoMigracaoSub " +
                    ",C.idBanco " +
                    ",C.idMcc " +
                    ",C.telefoneSub " +
                    ",C.emailSub " +
                    ",C.clienteTipoAdq " +
                    ",IFNULL(C.clienteCorban,'')" +
                    ",C.VendaConsignada " +
                    ",C.AuditagemConsignadaObriga " +
                    ",C.clienteMigracaoAdq " +
                    "FROM CadastroMigracaoSub AS CMB " +
                    "INNER JOIN Cliente AS C ON (CMB.idCliente = C.id) " +
                    "WHERE (CMB.idMotivoRecusa IS NULL OR CMB.idMotivoRecusa = 0) " +
                    "GROUP BY C.id " +
                    "ORDER BY C.nomeFantasia ASC";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            List<Cliente> clientes = moveCursor(db.rawQuery(sql, null));
            emitter.onNext(clientes);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<List<Cliente>> obterCadastroMigracaoSub() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT C.id" +
                    ",C.nomeFantasia" +
                    ",C.razaoSocial" +
                    ",C.TipoLogradouro" +
                    ",C.nomeLogradouro" +
                    ",C.complementoLogradouro" +
                    ",C.bairro" +
                    ",C.cidade" +
                    ",C.estado" +
                    ",C.cep" +
                    ",C.dddTelefone" +
                    ",C.telefone" +
                    ",C.dddCelular" +
                    ",C.celular" +
                    ",C.cpf_cnpj" +
                    ",C.rg_ie" +
                    ",IFNULL(C.codigoSGV,'')" +
                    ",C.ativo" +
                    ",C.numeroLogradouro" +
                    ",C.idclienteintraflex" +
                    ",IFNULL(C.codigoeFresh,'')" +
                    ",IFNULL(C.codigoAplic,'')" +
                    ",IFNULL(C.exibirCodigo,'')" +
                    ",IFNULL(C.atualizarLocal,'N')" +
                    ",IFNULL(C.senha,'')" +
                    ",IFNULL(C.pedeSenha,'N')" +
                    ",IFNULL(C.auditagem,'N')" +
                    ",IFNULL(C.statuseFresh,'')" +
                    ",IFNULL(C.statusSGV,'')" +
                    ",IFNULL(C.fechamento,'')" +
                    ",IFNULL(C.atualizarContato,'N')" +
                    ",IFNULL(C.email,'')" +
                    ",IFNULL(C.diasCortes,15)" +
                    ",IFNULL(C.bloqueiaVendaVista,0)" +
                    ",IFNULL(C.curva,'')" +
                    ",IFNULL(C.atualizaBinario,0)" +
                    ",IFNULL(C.adquirencia,0)" +
                    ",IFNULL(C.eletronico,0)" +
                    ",IFNULL(C.fisico,0)" +
                    ",IFNULL(C.qtdBoletoPendente,0)" +
                    ",IFNULL(C.valorBoletoPendente,0)" +
                    ",IFNULL(C.curvaChip,'')" +
                    ",IFNULL(C.cerca,0)" +
                    ",IFNULL(C.bloqueiaAtendimento,0)" +
                    ",IFNULL(C.qtdReprovacaoImg,0)" +
                    ",IFNULL(C.recadastro,0)" +
                    ",IFNULL(C.curvaAdquirencia,'')" +
                    ",IFNULL(C.clienteFisico,'')" +
                    ",IFNULL(C.clienteEletronico,'')" +
                    ",IFNULL(C.clienteAdquirencia,'')" +
                    ",IFNULL(C.clienteAppFlex,'')" +
                    ",IFNULL(C.merchandising,'')" +
                    ",IFNULL(C.idSegmentoSGV,0)" +
                    ",IFNULL(C.pontoReferencia,'')" +
                    ",IFNULL(C.contato,'')" +
                    ",IFNULL(C.possuiRecadastro,'N')" +
                    ",IFNULL(C.retornoRecadastro,'')" +
                    ",C.latitude" +
                    ",C.longitude" +
                    ",C.dddTelefone2" +
                    ",C.telefone2" +
                    ",C.dddCelular2" +
                    ",C.celular2" +
                    ",C.vencimentoFatura" +
                    ",C.limitePrimeiraVenda " +
                    ",C.clienteMigracaoSub " +
                    ",C.antecipacaoAutomatica " +
                    ",C.prazoDeNegociacao " +
                    ",C.idDomicilio " +
                    ",C.nomeBanco " +
                    ",C.tipoConta " +
                    ",C.agencia " +
                    ",C.digitoAgencia " +
                    ",C.conta " +
                    ",C.digitoConta " +
                    ",C.negociadoMigracaoSub " +
                    ",C.idBanco " +
                    ",C.idMcc " +
                    ",C.telefoneSub " +
                    ",C.emailSub " +
                    ",C.clienteTipoAdq " +
                    ",IFNULL(C.clienteCorban,'')" +
                    ",C.VendaConsignada " +
                    ",C.AuditagemConsignadaObriga " +
                    ",C.clienteMigracaoAdq " +
                    " FROM CadastroMigracaoSub AS CMS " +
                    "INNER JOIN Cliente AS C ON (CMS.idCliente = C.id) " +
                    "GROUP BY C.id " +
                    "ORDER BY C.nomeFantasia ASC";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            List<Cliente> clientes = moveCursorMigracao(db.rawQuery(sql, null));
            emitter.onNext(clientes);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<List<Cliente>> obterClientesComPendenciaNaoRespondido() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT C.id" +
                    ",C.nomeFantasia" +
                    ",C.razaoSocial" +
                    ",C.TipoLogradouro" +
                    ",C.nomeLogradouro" +
                    ",C.complementoLogradouro" +
                    ",C.bairro" +
                    ",C.cidade" +
                    ",C.estado" +
                    ",C.cep" +
                    ",C.dddTelefone" +
                    ",C.telefone" +
                    ",C.dddCelular" +
                    ",C.celular" +
                    ",C.cpf_cnpj" +
                    ",C.rg_ie" +
                    ",IFNULL(C.codigoSGV,'')" +
                    ",C.ativo" +
                    ",C.numeroLogradouro" +
                    ",C.idclienteintraflex" +
                    ",IFNULL(C.codigoeFresh,'')" +
                    ",IFNULL(C.codigoAplic,'')" +
                    ",IFNULL(C.exibirCodigo,'')" +
                    ",IFNULL(C.atualizarLocal,'N')" +
                    ",IFNULL(C.senha,'')" +
                    ",IFNULL(C.pedeSenha,'N')" +
                    ",IFNULL(C.auditagem,'N')" +
                    ",IFNULL(C.statuseFresh,'')" +
                    ",IFNULL(C.statusSGV,'')" +
                    ",IFNULL(C.fechamento,'')" +
                    ",IFNULL(C.atualizarContato,'N')" +
                    ",IFNULL(C.email,'')" +
                    ",IFNULL(C.diasCortes,15)" +
                    ",IFNULL(C.bloqueiaVendaVista,0)" +
                    ",IFNULL(C.curva,'')" +
                    ",IFNULL(C.atualizaBinario,0)" +
                    ",IFNULL(C.adquirencia,0)" +
                    ",IFNULL(C.eletronico,0)" +
                    ",IFNULL(C.fisico,0)" +
                    ",IFNULL(C.qtdBoletoPendente,0)" +
                    ",IFNULL(C.valorBoletoPendente,0)" +
                    ",IFNULL(C.curvaChip,'')" +
                    ",IFNULL(C.cerca,0)" +
                    ",IFNULL(C.bloqueiaAtendimento,0)" +
                    ",IFNULL(C.qtdReprovacaoImg,0)" +
                    ",IFNULL(C.recadastro,0)" +
                    ",IFNULL(C.curvaAdquirencia,'')" +
                    ",IFNULL(C.clienteFisico,'')" +
                    ",IFNULL(C.clienteEletronico,'')" +
                    ",IFNULL(C.clienteAdquirencia,'')" +
                    ",IFNULL(C.clienteAppFlex,'')" +
                    ",IFNULL(C.merchandising,'')" +
                    ",IFNULL(C.idSegmentoSGV,0)" +
                    ",IFNULL(C.pontoReferencia,'')" +
                    ",IFNULL(C.contato,'')" +
                    ",IFNULL(C.possuiRecadastro,'N')" +
                    ",IFNULL(C.retornoRecadastro,'')" +
                    ",C.latitude" +
                    ",C.longitude" +
                    ",C.dddTelefone2" +
                    ",C.telefone2" +
                    ",C.dddCelular2" +
                    ",C.celular2" +
                    ",C.vencimentoFatura" +
                    ",C.limitePrimeiraVenda " +
                    ",C.negociadoMigracaoSub " +
                    ",C.idBanco " +
                    ",C.idMcc " +
                    ",C.telefone " +
                    ",C.idMcc " +
                    ",C.telefoneSub " +
                    ",C.emailSub " +
                    ",C.clienteTipoAdq " +
                    ",IFNULL(C.clienteCorban,'')" +
                    ",C.VendaConsignada " +
                    ",C.AuditagemConsignadaObriga " +
                    "FROM PendenciaCliente AS PC " +
                    "INNER JOIN Cliente AS C ON (PC.clienteId = C.id) " +
                    "WHERE PC.pendenciaMotivoId == 0 " +
                    "GROUP BY C.id " +
                    "ORDER BY C.nomeFantasia ASC";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            List<Cliente> clientes = moveCursor(db.rawQuery(sql, null));
            emitter.onNext(clientes);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<List<Cliente>> obterClienteComPendenciaNaoRespondidoPorClienteId(String idCliente) {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT C.id" +
                    ",C.nomeFantasia" +
                    ",C.razaoSocial" +
                    ",C.TipoLogradouro" +
                    ",C.nomeLogradouro" +
                    ",C.complementoLogradouro" +
                    ",C.bairro" +
                    ",C.cidade" +
                    ",C.estado" +
                    ",C.cep" +
                    ",C.dddTelefone" +
                    ",C.telefone" +
                    ",C.dddCelular" +
                    ",C.celular" +
                    ",C.cpf_cnpj" +
                    ",C.rg_ie" +
                    ",IFNULL(C.codigoSGV,'')" +
                    ",C.ativo" +
                    ",C.numeroLogradouro" +
                    ",C.idclienteintraflex" +
                    ",IFNULL(C.codigoeFresh,'')" +
                    ",IFNULL(C.codigoAplic,'')" +
                    ",IFNULL(C.exibirCodigo,'')" +
                    ",IFNULL(C.atualizarLocal,'N')" +
                    ",IFNULL(C.senha,'')" +
                    ",IFNULL(C.pedeSenha,'N')" +
                    ",IFNULL(C.auditagem,'N')" +
                    ",IFNULL(C.statuseFresh,'')" +
                    ",IFNULL(C.statusSGV,'')" +
                    ",IFNULL(C.fechamento,'')" +
                    ",IFNULL(C.atualizarContato,'N')" +
                    ",IFNULL(C.email,'')" +
                    ",IFNULL(C.diasCortes,15)" +
                    ",IFNULL(C.bloqueiaVendaVista,0)" +
                    ",IFNULL(C.curva,'')" +
                    ",IFNULL(C.atualizaBinario,0)" +
                    ",IFNULL(C.adquirencia,0)" +
                    ",IFNULL(C.eletronico,0)" +
                    ",IFNULL(C.fisico,0)" +
                    ",IFNULL(C.qtdBoletoPendente,0)" +
                    ",IFNULL(C.valorBoletoPendente,0)" +
                    ",IFNULL(C.curvaChip,'')" +
                    ",IFNULL(C.cerca,0)" +
                    ",IFNULL(C.bloqueiaAtendimento,0)" +
                    ",IFNULL(C.qtdReprovacaoImg,0)" +
                    ",IFNULL(C.recadastro,0)" +
                    ",IFNULL(C.curvaAdquirencia,'')" +
                    ",IFNULL(C.clienteFisico,'')" +
                    ",IFNULL(C.clienteEletronico,'')" +
                    ",IFNULL(C.clienteAdquirencia,'')" +
                    ",IFNULL(C.clienteAppFlex,'')" +
                    ",IFNULL(C.merchandising,'')" +
                    ",IFNULL(C.idSegmentoSGV,0)" +
                    ",IFNULL(C.pontoReferencia,'')" +
                    ",IFNULL(C.contato,'')" +
                    ",IFNULL(C.possuiRecadastro,'N')" +
                    ",IFNULL(C.retornoRecadastro,'')" +
                    ",C.latitude" +
                    ",C.longitude" +
                    ",C.dddTelefone2" +
                    ",C.telefone2" +
                    ",C.dddCelular2" +
                    ",C.celular2" +
                    ",C.vencimentoFatura" +
                    ",C.limitePrimeiraVenda " +
                    ",C.negociadoMigracaoSub " +
                    ",C.idBanco " +
                    ",C.idMcc " +
                    ",C.telefone " +
                    ",C.idMcc " +
                    ",C.telefoneSub " +
                    ",C.emailSub " +
                    ",C.clienteTipoAdq " +
                    ",C.VendaConsignada " +
                    ",C.AuditagemConsignadaObriga " +
                    "FROM PendenciaCliente AS PC " +
                    "INNER JOIN Cliente AS C ON (PC.clienteId = C.id) " +
                    "WHERE PC.pendenciaMotivoId == 0 AND PC.ClienteId = "+idCliente;


            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            List<Cliente> clientes = moveCursor(db.rawQuery(sql, null));
            emitter.onNext(clientes);
            emitter.onComplete();
        });
    }

    @Override
    public Single<Boolean> updateNegotiationMigrateSub(int clintId) {
        return Single.create(emitter -> {
            try {
                SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
                ContentValues values = new ContentValues();
                values.put("negociadoMigracaoSub", 1);

                int response = db.update(
                        mTabela,
                        values,
                        "id = ?",
                        new String[]{String.valueOf(clintId)}
                );
                emitter.onSuccess(response > 1);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private List<Cliente> moveCursor(Cursor cursor) {
        List<Cliente> clientes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Cliente cliente = new Cliente();
            cliente.setId(cursor.getString(0));
            cliente.setNomeFantasia(cursor.getString(1));
            cliente.setRazaoSocial(cursor.getString(2));
            cliente.setTipoLogradouro(cursor.getString(3));
            cliente.setNomeLogradouro(cursor.getString(4));
            cliente.setComplementoLogradouro(cursor.getString(5));
            cliente.setBairro(cursor.getString(6));
            cliente.setCidade(cursor.getString(7));
            cliente.setEstado(cursor.getString(8));
            cliente.setCep(cursor.getString(9));
            cliente.setDddTelefone(cursor.getString(10));
            cliente.setTelefone(cursor.getString(11));
            cliente.setDddCelular(cursor.getString(12));
            cliente.setCelular(cursor.getString(13));
            cliente.setCpf_cnpj(cursor.getString(14));
            cliente.setRg_ie(cursor.getString(15));
            cliente.setCodigoSGV(cursor.getString(16));
            cliente.setAtivo(cursor.getString(17));
            cliente.setNumeroLogradouro(cursor.getString(18));
            cliente.setCodigoIntraFlex(cursor.getString(19));
            cliente.setCodigoeFresh(cursor.getString(20));
            cliente.setCodigoAplic(cursor.getString(21));
            cliente.setExibirCodigo(cursor.getString(22));
            cliente.setAtualizaLocal(cursor.getString(23));
            cliente.setSenha(cursor.getString(24));
            cliente.setPedeSenha(cursor.getString(25));
            cliente.setAuditagem(cursor.getString(26));
            cliente.setStatuseFresh(cursor.getString(27));
            cliente.setStatusSGV(cursor.getString(28));
            cliente.setFechamentoFatura(cursor.getString(29));
            cliente.setAtualizaContato(cursor.getString(30));
            cliente.setEmailCliente(cursor.getString(31));
            cliente.setDiasCortes(cursor.getInt(32));
            cliente.setBloqueiaVendaVista(Util_IO.numberToBoolean(cursor.getInt(33)));
            cliente.setCurvaRecarga(cursor.getString(34));
            cliente.setAtualizaBinario(Util_IO.numberToBoolean(cursor.getInt(35)));
            cliente.setAdquirencia(Util_IO.numberToBoolean(cursor.getInt(36)));
            cliente.setEletronico(Util_IO.numberToBoolean(cursor.getInt(37)));
            cliente.setFisico(Util_IO.numberToBoolean(cursor.getInt(38)));
            cliente.setQtdBoletoPendente(cursor.getInt(39));
            cliente.setValorBoletoPendente(cursor.getDouble(40));
            cliente.setCurvaChip(cursor.getString(41));
            cliente.setCerca(cursor.getInt(42));
            cliente.setBloqueiaAtendimento(Util_IO.numberToBoolean(cursor.getInt(43)));
            cliente.setQtdLocReprovada(cursor.getInt(44));
            cliente.setRecadastro(Util_IO.numberToBoolean(cursor.getInt(45)));
            cliente.setCurvaAdquirencia(cursor.getString(46));
            cliente.setClienteFisico(Util_IO.numberToBoolean(cursor.getInt(47)));
            cliente.setClienteEletronico(Util_IO.numberToBoolean(cursor.getInt(48)));
            cliente.setClienteAdquirencia(Util_IO.numberToBoolean(cursor.getInt(49)));
            cliente.setClienteAppFlex(Util_IO.numberToBoolean(cursor.getInt(50)));
            cliente.setMerchandising(cursor.getString(51));
            cliente.setIdSegmentoSGV(cursor.getString(52));
            cliente.setPontoReferencia(cursor.getString(53));
            cliente.setContato(cursor.getString(54));
            cliente.setPossuiRecadastro(cursor.getString(55));
            cliente.setRetornoRecadastro(cursor.getString(56));
            cliente.setLatitude(cursor.getDouble(57));
            cliente.setLongitude(cursor.getDouble(58));
            cliente.setDddTelefone2(cursor.getString(59));
            cliente.setTelefone2(cursor.getString(60));
            cliente.setDddCelular2(cursor.getString(61));
            cliente.setCelular2(cursor.getString(62));
            cliente.setVencimentoFatura(cursor.getString(63));
            cliente.setLimitePrimeiraVenda(cursor.getDouble(64));
            cliente.setTelefoneSub(cursor.getString(65));
            cliente.setEmailSub(cursor.getString(66));
            cliente.setClienteTipoAdq(cursor.getInt(67));
            cliente.setClienteCorban(Util_IO.numberToBoolean(cursor.getInt(68)));
            cliente.setVendaConsignada(Util_IO.numberToBoolean(cursor.getInt(69)));
            cliente.setAuditagemConsignadaObriga(Util_IO.numberToBoolean(cursor.getInt(70)));
            clientes.add(cliente);
        }
        cursor.close();
        return clientes;
    }

    private List<Cliente> moveCursorMigracao(Cursor cursor) {
        List<Cliente> clientes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Cliente cliente = new Cliente();
            cliente.setId(cursor.getString(0));
            cliente.setNomeFantasia(cursor.getString(1));
            cliente.setRazaoSocial(cursor.getString(2));
            cliente.setTipoLogradouro(cursor.getString(3));
            cliente.setNomeLogradouro(cursor.getString(4));
            cliente.setComplementoLogradouro(cursor.getString(5));
            cliente.setBairro(cursor.getString(6));
            cliente.setCidade(cursor.getString(7));
            cliente.setEstado(cursor.getString(8));
            cliente.setCep(cursor.getString(9));
            cliente.setDddTelefone(cursor.getString(10));
            cliente.setTelefone(cursor.getString(11));
            cliente.setDddCelular(cursor.getString(12));
            cliente.setCelular(cursor.getString(13));
            cliente.setCpf_cnpj(cursor.getString(14));
            cliente.setRg_ie(cursor.getString(15));
            cliente.setCodigoSGV(cursor.getString(16));
            cliente.setAtivo(cursor.getString(17));
            cliente.setNumeroLogradouro(cursor.getString(18));
            cliente.setCodigoIntraFlex(cursor.getString(19));
            cliente.setCodigoeFresh(cursor.getString(20));
            cliente.setCodigoAplic(cursor.getString(21));
            cliente.setExibirCodigo(cursor.getString(22));
            cliente.setAtualizaLocal(cursor.getString(23));
            cliente.setSenha(cursor.getString(24));
            cliente.setPedeSenha(cursor.getString(25));
            cliente.setAuditagem(cursor.getString(26));
            cliente.setStatuseFresh(cursor.getString(27));
            cliente.setStatusSGV(cursor.getString(28));
            cliente.setFechamentoFatura(cursor.getString(29));
            cliente.setAtualizaContato(cursor.getString(30));
            cliente.setEmailCliente(cursor.getString(31));
            cliente.setDiasCortes(cursor.getInt(32));
            cliente.setBloqueiaVendaVista(Util_IO.numberToBoolean(cursor.getInt(33)));
            cliente.setCurvaRecarga(cursor.getString(34));
            cliente.setAtualizaBinario(Util_IO.numberToBoolean(cursor.getInt(35)));
            cliente.setAdquirencia(Util_IO.numberToBoolean(cursor.getInt(36)));
            cliente.setEletronico(Util_IO.numberToBoolean(cursor.getInt(37)));
            cliente.setFisico(Util_IO.numberToBoolean(cursor.getInt(38)));
            cliente.setQtdBoletoPendente(cursor.getInt(39));
            cliente.setValorBoletoPendente(cursor.getDouble(40));
            cliente.setCurvaChip(cursor.getString(41));
            cliente.setCerca(cursor.getInt(42));
            cliente.setBloqueiaAtendimento(Util_IO.numberToBoolean(cursor.getInt(43)));
            cliente.setQtdLocReprovada(cursor.getInt(44));
            cliente.setRecadastro(Util_IO.numberToBoolean(cursor.getInt(45)));
            cliente.setCurvaAdquirencia(cursor.getString(46));
            cliente.setClienteFisico(Util_IO.numberToBoolean(cursor.getInt(47)));
            cliente.setClienteEletronico(Util_IO.numberToBoolean(cursor.getInt(48)));
            cliente.setClienteAdquirencia(Util_IO.numberToBoolean(cursor.getInt(49)));
            cliente.setClienteAppFlex(Util_IO.numberToBoolean(cursor.getInt(50)));
            cliente.setMerchandising(cursor.getString(51));
            cliente.setIdSegmentoSGV(cursor.getString(52));
            cliente.setPontoReferencia(cursor.getString(53));
            cliente.setContato(cursor.getString(54));
            cliente.setPossuiRecadastro(cursor.getString(55));
            cliente.setRetornoRecadastro(cursor.getString(56));
            cliente.setLatitude(cursor.getDouble(57));
            cliente.setLongitude(cursor.getDouble(58));
            cliente.setDddTelefone2(cursor.getString(59));
            cliente.setTelefone2(cursor.getString(60));
            cliente.setDddCelular2(cursor.getString(61));
            cliente.setCelular2(cursor.getString(62));
            cliente.setVencimentoFatura(cursor.getString(63));
            cliente.setLimitePrimeiraVenda(cursor.getDouble(64));
            cliente.setClienteMigracaoSub(cursor.getInt(65) == 1);
            cliente.setAntecipacaoAutomatica(getBooleanOrNull(cursor, 66));
            cliente.setPrazoDeNegociacao(cursor.getString(67));
            cliente.setIdDomicilio(cursor.getInt(68));
            cliente.setNomeBanco(cursor.getString(69));
            cliente.setTipoConta(cursor.getString(70));
            cliente.setAgencia(cursor.getString(71));
            cliente.setDigitoAgencia(cursor.getString(72));
            cliente.setConta(cursor.getString(73));
            cliente.setDigitoConta(cursor.getString(74));
            cliente.setNegociadoMigracaoSub(cursor.getInt(75) == 1);
            cliente.setIdBanco(cursor.getInt(76));
            cliente.setIdMcc(cursor.getInt(77));
            cliente.setTelefoneSub(cursor.getString(78));
            cliente.setEmailSub(cursor.getString(79));
            cliente.setClienteTipoAdq(cursor.getInt(80));
            cliente.setClienteCorban(Util_IO.numberToBoolean(cursor.getInt(81)));
            cliente.setVendaConsignada(Util_IO.numberToBoolean(cursor.getInt(82)));
            cliente.setAuditagemConsignadaObriga(Util_IO.numberToBoolean(cursor.getInt(83)));
            cliente.setClienteMigracaoAdq(cursor.getInt(84) == 1);
            clientes.add(cliente);
        }
        cursor.close();
        return clientes;
    }

    private Boolean getBooleanOrNull(Cursor cursor, int columnIndex) {
        if (cursor.isNull(columnIndex)) return null;
        return cursor.getInt(columnIndex) == 1;
    }

    public Observable<List<Cliente>> obterClientesPendentesMigracao() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            String sql = "SELECT C.id" +
                    ",C.nomeFantasia" +
                    ",C.razaoSocial" +
                    ",C.TipoLogradouro" +
                    ",C.nomeLogradouro" +
                    ",C.complementoLogradouro" +
                    ",C.bairro" +
                    ",C.cidade" +
                    ",C.estado" +
                    ",C.cep" +
                    ",C.dddTelefone" +
                    ",C.telefone" +
                    ",C.dddCelular" +
                    ",C.celular" +
                    ",C.cpf_cnpj" +
                    ",C.rg_ie" +
                    ",IFNULL(C.codigoSGV,'')" +
                    ",C.ativo" +
                    ",C.numeroLogradouro" +
                    ",C.idclienteintraflex" +
                    ",IFNULL(C.codigoeFresh,'')" +
                    ",IFNULL(C.codigoAplic,'')" +
                    ",IFNULL(C.exibirCodigo,'')" +
                    ",IFNULL(C.atualizarLocal,'N')" +
                    ",IFNULL(C.senha,'')" +
                    ",IFNULL(C.pedeSenha,'N')" +
                    ",IFNULL(C.auditagem,'N')" +
                    ",IFNULL(C.statuseFresh,'')" +
                    ",IFNULL(C.statusSGV,'')" +
                    ",IFNULL(C.fechamento,'')" +
                    ",IFNULL(C.atualizarContato,'N')" +
                    ",IFNULL(C.email,'')" +
                    ",IFNULL(C.diasCortes,15)" +
                    ",IFNULL(C.bloqueiaVendaVista,0)" +
                    ",IFNULL(C.curva,'')" +
                    ",IFNULL(C.atualizaBinario,0)" +
                    ",IFNULL(C.adquirencia,0)" +
                    ",IFNULL(C.eletronico,0)" +
                    ",IFNULL(C.fisico,0)" +
                    ",IFNULL(C.qtdBoletoPendente,0)" +
                    ",IFNULL(C.valorBoletoPendente,0)" +
                    ",IFNULL(C.curvaChip,'')" +
                    ",IFNULL(C.cerca,0)" +
                    ",IFNULL(C.bloqueiaAtendimento,0)" +
                    ",IFNULL(C.qtdReprovacaoImg,0)" +
                    ",IFNULL(C.recadastro,0)" +
                    ",IFNULL(C.curvaAdquirencia,'')" +
                    ",IFNULL(C.clienteFisico,'')" +
                    ",IFNULL(C.clienteEletronico,'')" +
                    ",IFNULL(C.clienteAdquirencia,'')" +
                    ",IFNULL(C.clienteAppFlex,'')" +
                    ",IFNULL(C.merchandising,'')" +
                    ",IFNULL(C.idSegmentoSGV,0)" +
                    ",IFNULL(C.pontoReferencia,'')" +
                    ",IFNULL(C.contato,'')" +
                    ",IFNULL(C.possuiRecadastro,'N')" +
                    ",IFNULL(C.retornoRecadastro,'')" +
                    ",C.latitude" +
                    ",C.longitude" +
                    ",C.dddTelefone2" +
                    ",C.telefone2" +
                    ",C.dddCelular2" +
                    ",C.celular2" +
                    ",C.vencimentoFatura" +
                    ",C.limitePrimeiraVenda " +
                    ",C.clienteMigracaoSub " +
                    ",C.antecipacaoAutomatica " +
                    ",C.prazoDeNegociacao " +
                    ",C.idDomicilio " +
                    ",C.nomeBanco " +
                    ",C.tipoConta " +
                    ",C.agencia " +
                    ",C.digitoAgencia " +
                    ",C.conta " +
                    ",C.digitoConta " +
                    ",C.negociadoMigracaoSub " +
                    ",C.idBanco " +
                    ",C.idMcc " +
                    ",C.telefoneSub " +
                    ",C.emailSub " +
                    ",C.clienteTipoAdq " +
                    ",IFNULL(C.clienteCorban,'')" +
                    ",C.VendaConsignada " +
                    ",C.AuditagemConsignadaObriga " +
                    ",C.clienteMigracaoAdq " +
                    "FROM Cliente AS C " +
                    "LEFT JOIN CadastroMigracaoSub AS CMB ON (CMB.idCliente = C.id) " +
                    "WHERE CMB.idAppMobile is null and (C.clienteMigracaoAdq = 1 or C.clienteMigracaoSub = 1) " +
                    "GROUP BY C.id " +
                    "ORDER BY C.nomeFantasia ASC";

            SQLiteDatabase db = SimpleDbHelper.INSTANCE.open(context);
            List<Cliente> clientes = moveCursorMigracao(db.rawQuery(sql, null));
            emitter.onNext(clientes);
            emitter.onComplete();
        });
    }
}
