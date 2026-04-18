package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_DB;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desenvolvimento on 24/06/2016.
 */
public class DBCliente {
    private final Context mContext;
    private final String mTabela = "Cliente";

    public DBCliente(Context pContext) {
        mContext = pContext;
    }

    public void addCliente(Cliente pCliente) throws Exception {
        if (pCliente.isIncluir()) {
            ContentValues values = new ContentValues();
            values.put("id", pCliente.getId());
            values.put("nomeFantasia", pCliente.getNomeFantasia());
            values.put("razaoSocial", pCliente.getRazaoSocial());
            values.put("tipoLogradouro", pCliente.getTipoLogradouro());
            values.put("nomeLogradouro", pCliente.getNomeLogradouro());
            values.put("numeroLogradouro", pCliente.getNumeroLogradouro());
            values.put("complementoLogradouro", pCliente.getComplementoLogradouro());
            values.put("bairro", pCliente.getBairro());
            values.put("cidade", pCliente.getCidade());
            values.put("estado", pCliente.getEstado());
            values.put("cep", pCliente.getCep());
            values.put("dddTelefone", pCliente.getDddTelefone());
            values.put("telefone", pCliente.getTelefone());
            values.put("dddCelular", pCliente.getDddCelular());
            values.put("cpf_cnpj", pCliente.getCpf_cnpj());
            values.put("rg_ie", pCliente.getRg_ie());
            values.put("celular", pCliente.getCelular());
            values.put("codigoSGV", pCliente.getCodigoSGV());
            values.put("ativo", pCliente.getAtivo());
            values.put("idclienteintraflex", pCliente.getCodigoIntraFlex());
            values.put("codigoeFresh", pCliente.getCodigoeFresh());
            values.put("exibirCodigo", pCliente.getExibirCodigo());
            values.put("codigoAplic", pCliente.getCodigoAplic());
            values.put("atualizarLocal", pCliente.getAtualizaLocal());
            values.put("sync", 1);
            if (pCliente.getSenha().toUpperCase().contains("ERRO")) {
                values.put("senha", "");
                Notificacoes.Cliente(mContext, "Erro ao gerar senha", "Verifique número do celular informado " + pCliente.getNomeFantasia(), pCliente);
            } else
                values.put("senha", pCliente.getSenha());
            values.put("pedeSenha", pCliente.getPedeSenha());
            values.put("auditagem", pCliente.getAuditagem());
            values.put("statuseFresh", pCliente.getStatuseFresh());
            values.put("statusSGV", pCliente.getStatusSGV());
            values.put("fechamento", pCliente.getFechamentoFatura());
            values.put("atualizarContato", pCliente.getAtualizaContato());
            values.put("email", pCliente.getEmailCliente());
            values.put("latitude", pCliente.getLatitude());
            values.put("longitude", pCliente.getLongitude());
            values.put("diasCortes", pCliente.getDiasCortes());
            values.put("curva", pCliente.getCurvaRecarga());
            values.put("bloqueiaVendaVista", Util_IO.booleanToNumber(pCliente.isBloqueiaVendaVista()));
            values.put("atualizaBinario", Util_IO.booleanToNumber(pCliente.isAtualizaBinario()));
            values.put("curvaChip", pCliente.getCurvaChip());
            values.put("adquirencia", Util_IO.booleanToNumber(pCliente.isAdquirencia()));
            values.put("eletronico", Util_IO.booleanToNumber(pCliente.isEletronico()));
            values.put("fisico", Util_IO.booleanToNumber(pCliente.isFisico()));
            values.put("qtdBoletoPendente", pCliente.getQtdBoletoPendente());
            values.put("valorBoletoPendente", pCliente.getValorBoletoPendente());
            values.put("cerca", pCliente.getCerca());
            values.put("bloqueiaAtendimento", Util_IO.booleanToNumber(pCliente.isBloqueiaAtendimento()));
            values.put("qtdReprovacaoImg", pCliente.getQtdLocReprovada());
            values.put("curvaAdquirencia", pCliente.getCurvaAdquirencia());
            values.put("recadastro", Util_IO.booleanToNumber(pCliente.isRecadastro()));
            values.put("clienteFisico", Util_IO.booleanToNumber(pCliente.getClienteFisico()));
            values.put("clienteEletronico", Util_IO.booleanToNumber(pCliente.getClienteEletronico()));
            values.put("clienteAdquirencia", Util_IO.booleanToNumber(pCliente.getClienteAdquirencia()));
            values.put("clienteAppFlex", Util_IO.booleanToNumber(pCliente.getClienteAppFlex()));
            values.put("idSegmentoSGV", pCliente.getIdSegmentoSGV());
            values.put("pontoReferencia", pCliente.getPontoReferencia());
            values.put("contato", pCliente.getContato());
            values.put("possuiRecadastro", pCliente.getPossuiRecadastro());
            values.put("retornoRecadastro", pCliente.getRetornoRecadastro());
            values.put("dddTelefone2", pCliente.getDddTelefone2());
            values.put("telefone2", pCliente.getTelefone2());
            values.put("dddCelular2", pCliente.getDddCelular2());
            values.put("celular2", pCliente.getCelular2());
            values.put("vencimentoFatura", pCliente.getVencimentoFatura());
            values.put("limitePrimeiraVenda", pCliente.getLimitePrimeiraVenda());
            values.put("clienteMigracaoSub", pCliente.isClienteMigracaoSub() ? 1 : 0);
            values.put("antecipacaoAutomatica", pCliente.isAntecipacaoAutomatica());
            values.put("prazoDeNegociacao", pCliente.getPrazoDeNegociacao());
            values.put("idDomicilio", pCliente.getIdDomicilio());
            values.put("nomeBanco", pCliente.getNomeBanco());
            values.put("tipoConta", pCliente.getTipoConta());
            values.put("agencia", pCliente.getAgencia());
            values.put("digitoAgencia", pCliente.getDigitoAgencia());
            values.put("conta", pCliente.getConta());
            values.put("digitoConta", pCliente.getDigitoConta());
            values.put("idBanco", pCliente.getIdBanco());
            values.put("idMcc", pCliente.getIdMcc());
            values.put("negociadoMigracaoSub", pCliente.isNegociadoMigracaoSub());
            values.put("telefoneSub", pCliente.getTelefoneSub());
            values.put("emailSub", pCliente.getEmailSub());
            values.put("clienteTipoAdq", pCliente.getClienteTipoAdq());
            values.put("clienteCorban", pCliente.getClienteCorban());
            values.put("Premium", pCliente.getPremium());
            values.put("VendaConsignada", pCliente.isVendaConsignada());
            values.put("AuditagemConsignadaObriga", pCliente.isAuditagemConsignadaObriga());
            values.put("clienteMigracaoAdq", pCliente.isClienteMigracaoAdq() ? 1 : 0);
            values.put("idVendedor", pCliente.getIdVendedor());

            if (!Util_DB.isCadastrado(mContext, mTabela, "id", pCliente.getId()))
                SimpleDbHelper.INSTANCE.open(mContext).insert(mTabela, null, values);
            else
                SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pCliente.getId()});
        } else {
            SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=? ", new String[]{pCliente.getId()});
            new DBSenhaCliente(mContext).deleteSenhasByIdCliente(pCliente.getId());
            new DBRota(mContext).deleteRotaByIdCliente(pCliente.getId());
            new DBTokenCliente(mContext).deleteTokenByIdCliente(pCliente.getId());
            new DBAudOpe(mContext).deleteByIdCliente(pCliente.getId());
        }
    }

    public Cliente getById(String pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getClientes(sb.toString(), new String[]{pId}));
    }

    public void deleteById(String pId) {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, "[id]=?", new String[]{pId});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(mContext).delete(mTabela, null, null);
    }

    private ArrayList<Cliente> getClientes(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id");
        sb.appendLine(",nomeFantasia");
        sb.appendLine(",razaoSocial");
        sb.appendLine(",TipoLogradouro");
        sb.appendLine(",nomeLogradouro");
        sb.appendLine(",complementoLogradouro");
        sb.appendLine(",bairro");
        sb.appendLine(",cidade");
        sb.appendLine(",estado");
        sb.appendLine(",cep");
        sb.appendLine(",dddTelefone");
        sb.appendLine(",telefone");
        sb.appendLine(",dddCelular");
        sb.appendLine(",celular");
        sb.appendLine(",cpf_cnpj");
        sb.appendLine(",rg_ie");
        sb.appendLine(",IFNULL(codigoSGV,'')");
        sb.appendLine(",ativo");
        sb.appendLine(",numeroLogradouro");
        sb.appendLine(",idclienteintraflex");
        sb.appendLine(",IFNULL(codigoeFresh,'')");
        sb.appendLine(",IFNULL(codigoAplic,'')");
        sb.appendLine(",IFNULL(exibirCodigo,'')");
        sb.appendLine(",IFNULL(atualizarLocal,'N')");
        sb.appendLine(",IFNULL(senha,'')");
        sb.appendLine(",IFNULL(pedeSenha,'N')");
        sb.appendLine(",IFNULL(auditagem,'N')");
        sb.appendLine(",IFNULL(statuseFresh,'')");
        sb.appendLine(",IFNULL(statusSGV,'')");
        sb.appendLine(",IFNULL(fechamento,'')");
        sb.appendLine(",IFNULL(atualizarContato,'N')");
        sb.appendLine(",IFNULL(email,'')");
        sb.appendLine(",IFNULL(diasCortes,15)");
        sb.appendLine(",IFNULL(bloqueiaVendaVista,0)");
        sb.appendLine(",IFNULL(curva,'')");
        sb.appendLine(",IFNULL(atualizaBinario,0)");
        sb.appendLine(",IFNULL(adquirencia,0)");
        sb.appendLine(",IFNULL(eletronico,0)");
        sb.appendLine(",IFNULL(fisico,0)");
        sb.appendLine(",IFNULL(qtdBoletoPendente,0)");
        sb.appendLine(",IFNULL(valorBoletoPendente,0)");
        sb.appendLine(",IFNULL(curvaChip,'')");
        sb.appendLine(",IFNULL(cerca,0)");
        sb.appendLine(",IFNULL(bloqueiaAtendimento,0)");
        sb.appendLine(",IFNULL(qtdReprovacaoImg,0)");
        sb.appendLine(",IFNULL(recadastro,0)");
        sb.appendLine(",IFNULL(curvaAdquirencia,'')");
        sb.appendLine(",IFNULL(clienteFisico,'')");
        sb.appendLine(",IFNULL(clienteEletronico,'')");
        sb.appendLine(",IFNULL(clienteAdquirencia,'')");
        sb.appendLine(",IFNULL(clienteAppFlex,'')");
        sb.appendLine(",IFNULL(merchandising,'')");
        sb.appendLine(",IFNULL(idSegmentoSGV,0)");
        sb.appendLine(",IFNULL(pontoReferencia,'')");
        sb.appendLine(",IFNULL(contato,'')");
        sb.appendLine(",IFNULL(possuiRecadastro,'N')");
        sb.appendLine(",IFNULL(retornoRecadastro,'')");
        sb.appendLine(",latitude");
        sb.appendLine(",longitude");
        sb.appendLine(",dddTelefone2");
        sb.appendLine(",telefone2");
        sb.appendLine(",dddCelular2");
        sb.appendLine(",celular2");
        sb.appendLine(",vencimentoFatura");
        sb.appendLine(",limitePrimeiraVenda ");
        sb.appendLine(",clienteMigracaoSub ");
        sb.appendLine(",antecipacaoAutomatica ");
        sb.appendLine(",prazoDeNegociacao ");
        sb.appendLine(",idDomicilio ");
        sb.appendLine(",nomeBanco ");
        sb.appendLine(",tipoConta ");
        sb.appendLine(",agencia ");
        sb.appendLine(",digitoAgencia ");
        sb.appendLine(",conta ");
        sb.appendLine(",digitoConta ");
        sb.appendLine(",idBanco ");
        sb.appendLine(",idMcc ");
        sb.appendLine(",telefoneSub ");
        sb.appendLine(",emailSub ");
        sb.appendLine(",clienteTipoAdq ");
        sb.appendLine(",IFNULL(clienteCorban,'')");
        sb.appendLine(",Premium ");
        sb.appendLine(",VendaConsignada");
        sb.appendLine(",AuditagemConsignadaObriga");
        sb.appendLine(",clienteMigracaoAdq ");
        sb.appendLine(",IFNULL(idVendedor,0)");
        sb.appendLine("FROM [Cliente]");
        sb.appendLine("WHERE 1=1");
        if (pCondicao != null)
            sb.append(pCondicao);

        return moverCursorDb(sb, pCampos);
    }

    public List<Cliente> obterClientesComPendenciaNaoRespondido() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
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
                ",C.idBanco " +
                ",C.idMcc " +
                ",C.telefoneSub " +
                ",C.emailSub " +
                ",C.clienteTipoAdq " +
                ",C.Premium " +
                ",C.VendaConsignada " +
                ",C.AuditagemConsignadaObriga " +
                ",C.clienteMigracaoAdq " +
                ",IFNULL(C.idVendedor,0)" +
                "FROM PendenciaCliente AS PC " +
                "INNER JOIN Cliente AS C ON (PC.clienteId = C.id) " +
                "WHERE PC.pendenciaMotivoId == 0 " +
                "GROUP BY C.id " +
                "ORDER BY PC.ordem ASC";
        sb.appendLine(sql);

        return moverCursorDb(sb, null);
    }

    public void updateAtualizaLocal(String pId, String pValor) {
        ContentValues values = new ContentValues();
        values.put("atualizarLocal", pValor);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pId});
    }

    public void updateCadastro(Cliente pCliente, boolean pAtualizaContato) {
        ContentValues values = new ContentValues();
        values.put("dddCelular", pCliente.getDddCelular());
        values.put("celular", pCliente.getCelular());
        values.put("sync", 0);
        if (pAtualizaContato) {
            values.put("email", pCliente.getEmailCliente());
            values.put("atualizarContato", pCliente.getAtualizaContato());
        }
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pCliente.getId()});
    }

    public void updateSenha(String pIdCliente, String pSenha) {
        ContentValues values = new ContentValues();
        values.put("senha", pSenha);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pIdCliente});
    }

    public void updateSync(String pCodigo) {
        ContentValues values = new ContentValues();
        values.put("sync", 1);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pCodigo});
    }

    public ArrayList<Cliente> getPendentes() {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND sync = 0");
        return getClientes(sb.toString(), null);
    }

    public void confirmaAuditagem(String pIdCliente) {
        ContentValues values = new ContentValues();
        values.put("auditagem", "N");
        values.put("sync", 0);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pIdCliente});
    }

    public void confirmaAtualizacaoBinario(String pIdCliente) {
        ContentValues values = new ContentValues();
        values.put("atualizaBinario", 0);
        values.put("sync", 0);
        SimpleDbHelper.INSTANCE.open(mContext).update(mTabela, values, "[id]=?", new String[]{pIdCliente});
    }

    public ArrayList<Cliente> getCredenciados(String pFantasia, int pVendedor) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND IFNULL(statusSGV,'') IN ('Credenciado')");
        sb.appendLine("AND IFNULL(exibirCodigo,'') = 'SGV'");
        if (!pFantasia.isEmpty())
            sb.appendLine("AND (nomeFantasia LIKE '%" + pFantasia + "%' OR razaoSocial LIKE '%" + pFantasia + "%')");
        if (pVendedor > 0)
            sb.appendLine("AND (idVendedor = " + pVendedor + ")");
        sb.appendLine("ORDER BY nomeFantasia");


        return getClientes(sb.toString(), null);
    }

    public ArrayList<Cliente> getClientes(String pFantasia) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND ativo = 'S'");
        if (!pFantasia.isEmpty())
            sb.appendLine("AND (nomeFantasia LIKE '%" + pFantasia + "%' OR razaoSocial LIKE '%" + pFantasia + "%')");
        sb.appendLine("Order by nomeFantasia");

        return getClientes(sb.toString(), null);
    }

    public ArrayList<Cliente> getAll() {
        return getClientes(null, null);
    }

    public List<Integer> obterTodosClientesAprovadosRecadastro() {
        List<Cliente> listaClientes = getAll();
        List<Integer> clientesAprovados = new ArrayList<>();

        for (Cliente item : listaClientes) {
            if (!item.isRecadastro() && item.getPossuiRecadastro().equalsIgnoreCase("N") &&
                    Util_IO.isNullOrEmpty(item.getRetornoRecadastro())) {
                clientesAprovados.add(Integer.parseInt(item.getId()));
            }
        }

        return clientesAprovados;
    }

    private ArrayList<Cliente> moverCursorDb(Util_IO.StringBuilder sb, String[] args) {
        Cursor cursor = null;
        ArrayList<Cliente> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(mContext).rawQuery(sb.toString(), args);
            Cliente cliente;
            if (cursor.moveToFirst()) {
                do {
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
                    cliente.setIdBanco(cursor.getInt(75));
                    cliente.setIdMcc(cursor.getInt(76));
                    cliente.setTelefoneSub(cursor.getString(77));
                    cliente.setEmailSub(cursor.getString(78));
                    cliente.setClienteTipoAdq(cursor.getInt(79));
                    cliente.setClienteCorban(Util_IO.numberToBoolean(cursor.getInt(80)));
                    cliente.setPremium(Util_IO.numberToBoolean(cursor.getInt(81)));
                    cliente.setVendaConsignada(Util_IO.numberToBoolean(cursor.getInt(82)));
                    cliente.setAuditagemConsignadaObriga(Util_IO.numberToBoolean(cursor.getInt(83)));
                    cliente.setClienteMigracaoAdq(cursor.getInt(84) == 1);
                    cliente.setIdVendedor(cursor.getInt(85));
                    lista.add(cliente);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return lista;
    }

    private Boolean getBooleanOrNull(Cursor cursor, int columnIndex) {
        if (cursor.isNull(columnIndex)) return null;
        return cursor.getInt(columnIndex) == 1;
    }

    public ArrayList<Cliente> getClientesSituacao(String situacaoCliente, String idOperadora ) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT T0.id");
        sb.appendLine(",T0.nomeFantasia");
        sb.appendLine(",T0.razaoSocial");
        sb.appendLine(",T0.TipoLogradouro");
        sb.appendLine(",T0.nomeLogradouro");
        sb.appendLine(",T0.complementoLogradouro");
        sb.appendLine(",T0.bairro");
        sb.appendLine(",T0.cidade");
        sb.appendLine(",T0.estado");
        sb.appendLine(",T0.cep");
        sb.appendLine(",T0.dddTelefone");
        sb.appendLine(",T0.telefone");
        sb.appendLine(",T0.dddCelular");
        sb.appendLine(",T0.celular");
        sb.appendLine(",T0.cpf_cnpj");
        sb.appendLine(",T0.rg_ie");
        sb.appendLine(",IFNULL(T0.codigoSGV,'')");
        sb.appendLine(",T0.ativo");
        sb.appendLine(",T0.numeroLogradouro");
        sb.appendLine(",T0.idclienteintraflex");
        sb.appendLine(",IFNULL(T0.codigoeFresh,'')");
        sb.appendLine(",IFNULL(T0.codigoAplic,'')");
        sb.appendLine(",IFNULL(T0.exibirCodigo,'')");
        sb.appendLine(",IFNULL(T0.atualizarLocal,'N')");
        sb.appendLine(",IFNULL(T0.senha,'')");
        sb.appendLine(",IFNULL(T0.pedeSenha,'N')");
        sb.appendLine(",IFNULL(T0.auditagem,'N')");
        sb.appendLine(",IFNULL(T0.statuseFresh,'')");
        sb.appendLine(",IFNULL(T0.statusSGV,'')");
        sb.appendLine(",IFNULL(T0.fechamento,'')");
        sb.appendLine(",IFNULL(T0.atualizarContato,'N')");
        sb.appendLine(",IFNULL(T0.email,'')");
        sb.appendLine(",IFNULL(T0.diasCortes,15)");
        sb.appendLine(",IFNULL(T0.bloqueiaVendaVista,0)");
        sb.appendLine(",IFNULL(T0.curva,'')");
        sb.appendLine(",IFNULL(T0.atualizaBinario,0)");
        sb.appendLine(",IFNULL(T0.adquirencia,0)");
        sb.appendLine(",IFNULL(T0.eletronico,0)");
        sb.appendLine(",IFNULL(T0.fisico,0)");
        sb.appendLine(",IFNULL(T0.qtdBoletoPendente,0)");
        sb.appendLine(",IFNULL(T0.valorBoletoPendente,0)");
        sb.appendLine(",IFNULL(T0.curvaChip,'')");
        sb.appendLine(",IFNULL(T0.cerca,0)");
        sb.appendLine(",IFNULL(T0.bloqueiaAtendimento,0)");
        sb.appendLine(",IFNULL(T0.qtdReprovacaoImg,0)");
        sb.appendLine(",IFNULL(T0.recadastro,0)");
        sb.appendLine(",IFNULL(T0.curvaAdquirencia,'')");
        sb.appendLine(",IFNULL(T0.clienteFisico,'')");
        sb.appendLine(",IFNULL(T0.clienteEletronico,'')");
        sb.appendLine(",IFNULL(T0.clienteAdquirencia,'')");
        sb.appendLine(",IFNULL(T0.clienteAppFlex,'')");
        sb.appendLine(",IFNULL(T0.merchandising,'')");
        sb.appendLine(",IFNULL(T0.idSegmentoSGV,0)");
        sb.appendLine(",IFNULL(T0.pontoReferencia,'')");
        sb.appendLine(",IFNULL(T0.contato,'')");
        sb.appendLine(",IFNULL(T0.possuiRecadastro,'N')");
        sb.appendLine(",IFNULL(T0.retornoRecadastro,'')");
        sb.appendLine(",T0.latitude");
        sb.appendLine(",T0.longitude");
        sb.appendLine(",T0.dddTelefone2");
        sb.appendLine(",T0.telefone2");
        sb.appendLine(",T0.dddCelular2");
        sb.appendLine(",T0.celular2");
        sb.appendLine(",T0.vencimentoFatura");
        sb.appendLine(",T0.limitePrimeiraVenda ");
        sb.appendLine(",T0.clienteMigracaoSub ");
        sb.appendLine(",T0.antecipacaoAutomatica ");
        sb.appendLine(",T0.prazoDeNegociacao ");
        sb.appendLine(",T0.idDomicilio ");
        sb.appendLine(",T0.nomeBanco ");
        sb.appendLine(",T0.tipoConta ");
        sb.appendLine(",T0.agencia ");
        sb.appendLine(",T0.digitoAgencia ");
        sb.appendLine(",T0.conta ");
        sb.appendLine(",T0.digitoConta ");
        sb.appendLine(",T0.idBanco ");
        sb.appendLine(",T0.idMcc ");
        sb.appendLine(",T0.telefoneSub ");
        sb.appendLine(",T0.emailSub ");
        sb.appendLine(",T0.clienteTipoAdq ");
        sb.appendLine(",IFNULL(T0.clienteCorban,'')");
        sb.appendLine(",T0.Premium ");
        sb.appendLine(",T0.VendaConsignada ");
        sb.appendLine(",T0.AuditagemConsignadaObriga ");
        sb.appendLine(",T0.clienteMigracaoAdq ");
        sb.appendLine(",IFNULL(T0.idVendedor,0)");
        sb.appendLine("FROM [Cliente] T0");
        sb.appendLine("LEFT JOIN SUGESTAOVENDA T1 ON T1.CLIENTEID = T0.ID");
        sb.appendLine("WHERE T1.OPERADORAID = " + idOperadora + " AND T1.SITUACAOCLIENTE = '" + situacaoCliente + "'");

        return moverCursorDb(sb, null);
    }
}
