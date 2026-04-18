package com.axys.redeflexmobile.shared.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.DBCliente;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBDataHoraServidor;
import com.axys.redeflexmobile.shared.bd.DBEstoque;
import com.axys.redeflexmobile.shared.bd.DBHorarioNotificacao;
import com.axys.redeflexmobile.shared.bd.DBLimite;
import com.axys.redeflexmobile.shared.bd.DBOs;
import com.axys.redeflexmobile.shared.bd.DBPOS;
import com.axys.redeflexmobile.shared.bd.DBPreco;
import com.axys.redeflexmobile.shared.bd.DBVenda;
import com.axys.redeflexmobile.shared.dao.EstoqueDaoImpl;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.CodBarra;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.ComboVenda;
import com.axys.redeflexmobile.shared.models.DataHoraServidor;
import com.axys.redeflexmobile.shared.models.EstruturaProd;
import com.axys.redeflexmobile.shared.models.FormaPagamentoVenc;
import com.axys.redeflexmobile.shared.models.HorarioNotificacao;
import com.axys.redeflexmobile.shared.models.InformacaoGeralPOS;
import com.axys.redeflexmobile.shared.models.ItemVenda;
import com.axys.redeflexmobile.shared.models.ItemVendaCombo;
import com.axys.redeflexmobile.shared.models.LimiteCliente;
import com.axys.redeflexmobile.shared.models.PrecoDiferenciado;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.ProdutoCombo;
import com.axys.redeflexmobile.shared.models.UsoCodBarra;
import com.axys.redeflexmobile.shared.models.Venda;
import com.axys.redeflexmobile.ui.redeflex.Config;
import com.axys.redeflexmobile.ui.redeflex.NovoAtendimentoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axys.redeflexmobile.shared.util.DateUtils.DATA_MINIMA_BR;
import static com.axys.redeflexmobile.shared.util.DateUtils.DATA_SEPARATOR_BAR;

/**
 * Created by joao.viana on 20/07/2016.
 */
public class Validacoes {

    private static final String EMPTY_STRING = "";

    public static Boolean textoVazio(String text) {
        if (text == null) {
            return true;
        }
        String newText = text.trim();
        return newText.length() == 0;
    }

    private static Boolean possuiTexto(String text) {
        return !textoVazio(text);
    }

    private static String apenasNumero(String text) {
        return text.replaceAll("\\D+", "");
    }

    public static boolean cnpjValido(String cnpjParam) {
        String cnpj = apenasNumero(cnpjParam);
        List<Integer> numbers = Stream.of(cnpj.split(EMPTY_STRING))
                .filter(Validacoes::possuiTexto)
                .map(Integer::parseInt)
                .toList();
        Set<Integer> temp = new HashSet<>(numbers);
        if (temp.size() <= 1 || numbers.size() != 14) {
            return false;
        }

        int sum1 = 11 - (numbers.get(11) * 2 +
                numbers.get(10) * 3 +
                numbers.get(9) * 4 +
                numbers.get(8) * 5 +
                numbers.get(7) * 6 +
                numbers.get(6) * 7 +
                numbers.get(5) * 8 +
                numbers.get(4) * 9 +
                numbers.get(3) * 2 +
                numbers.get(2) * 3 +
                numbers.get(1) * 4 +
                numbers.get(0) * 5) % 11;
        int firstDigit = sum1 > 9 ? 0 : sum1;

        int sum2 = 11 - (numbers.get(12) * 2 +
                numbers.get(11) * 3 +
                numbers.get(10) * 4 +
                numbers.get(9) * 5 +
                numbers.get(8) * 6 +
                numbers.get(7) * 7 +
                numbers.get(6) * 8 +
                numbers.get(5) * 9 +
                numbers.get(4) * 2 +
                numbers.get(3) * 3 +
                numbers.get(2) * 4 +
                numbers.get(1) * 5 +
                numbers.get(0) * 6) % 11;
        int secondDigit = sum2 > 9 ? 0 : sum2;

        String digitos = String.valueOf(firstDigit) + secondDigit;

        return cnpj.endsWith(digitos);
    }

    public static boolean cpfValido(String cpfParam) {
        String cpf = apenasNumero(cpfParam);
        List<Integer> numbers = Stream.of(cpf.split(EMPTY_STRING))
                .filter(Validacoes::possuiTexto)
                .map(Integer::parseInt)
                .toList();
        Set<Integer> temp = new HashSet<>(numbers);
        if (temp.size() <= 1 || numbers.size() != 11) {
            return false;
        }

        int sum1 = 11 - (numbers.get(0) * 10 +
                numbers.get(1) * 9 +
                numbers.get(2) * 8 +
                numbers.get(3) * 7 +
                numbers.get(4) * 6 +
                numbers.get(5) * 5 +
                numbers.get(6) * 4 +
                numbers.get(7) * 3 +
                numbers.get(8) * 2) % 11;
        int firstDigit = sum1 > 9 ? 0 : sum1;

        int sum2 = 11 - (numbers.get(0) * 11 +
                numbers.get(1) * 10 +
                numbers.get(2) * 9 +
                numbers.get(3) * 8 +
                numbers.get(4) * 7 +
                numbers.get(5) * 6 +
                numbers.get(6) * 5 +
                numbers.get(7) * 4 +
                numbers.get(8) * 3 +
                numbers.get(9) * 2) % 11;
        int secondDigit = sum2 > 9 ? 0 : sum2;

        String digitos = String.valueOf(firstDigit) + secondDigit;

        return cpf.endsWith(digitos);
    }

    public static boolean validaCnpj(String pCnpj) {
        int[] multiplicador1 = new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] multiplicador2 = new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma;
        int resto;
        String digito;
        String tempCnpj;
        pCnpj = pCnpj.trim();
        pCnpj = pCnpj.replace(".", "").replace("-", "").replace("/", "");
        if (pCnpj.length() != 14)
            return false;
        tempCnpj = pCnpj.substring(0, 12);
        soma = 0;
        for (int i = 0; i < 12; i++)
            soma += Integer.parseInt(String.valueOf(tempCnpj.charAt(i))) * multiplicador1[i];
        resto = (soma % 11);
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = String.valueOf(resto);
        tempCnpj = tempCnpj + digito;
        soma = 0;
        for (int i = 0; i < 13; i++)
            soma += Integer.parseInt(String.valueOf(tempCnpj.charAt(i))) * multiplicador2[i];
        resto = (soma % 11);
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = digito + resto;
        return pCnpj.endsWith(digito);
    }

    public static boolean validaCPF(String pCpf) {
        int[] multiplicador1 = new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] multiplicador2 = new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        String tempCpf;
        String digito;
        int soma;
        int resto;
        pCpf = pCpf.trim();
        pCpf = pCpf.replace(".", "").replace("-", "");
        if (pCpf.length() != 11)
            return false;
        tempCpf = pCpf.substring(0, 9);
        soma = 0;
        for (int i = 0; i < 9; i++)
            soma += Integer.parseInt(String.valueOf(tempCpf.charAt(i))) * multiplicador1[i];
        resto = soma % 11;
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = String.valueOf(resto);
        tempCpf = tempCpf + digito;
        soma = 0;

        for (int i = 0; i < 10; i++)
            soma += Integer.parseInt(String.valueOf(tempCpf.charAt(i))) * multiplicador2[i];
        resto = soma % 11;
        if (resto < 2)
            resto = 0;
        else
            resto = 11 - resto;
        digito = digito + resto;
        return pCpf.endsWith(digito);
    }

    public static boolean validaEmail(String pEmail) {
        Boolean isEmailIdValid = false;
        if (pEmail != null && pEmail.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(pEmail);
            if (matcher.matches())
                isEmailIdValid = true;
        }
        return isEmailIdValid;
    }

    public static boolean validaData(String pDate) {
        if (pDate == null)
            return false;

        boolean dataValidaTamanhoInvalido = pDate.trim().length() < 10 && pDate.contains(DATA_SEPARATOR_BAR);
        boolean dataInvalidaTamanhoInvalido = pDate.trim().length() < 8 && !pDate.contains(DATA_SEPARATOR_BAR);

        if (dataValidaTamanhoInvalido || dataInvalidaTamanhoInvalido) {
            return false;
        }

        if (pDate.trim().length() == 8 && !pDate.contains(DATA_SEPARATOR_BAR)) {
            pDate = DateUtils.insereBarraData(pDate);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(Config.FormatDateStringBr, Locale.getDefault());
        try {
            sdf.setLenient(false);
            sdf.parse(pDate);
            Date minhaData = sdf.parse(pDate);

            Date dataMinima = sdf.parse(DATA_MINIMA_BR);

            return !minhaData.before(dataMinima);
        } catch (ParseException ex) {
            return false;
        }
    }

    public static boolean validaTelCelular(String pTelefone) {
        try {
            if (pTelefone.replace("(", "").replace(")", "").replace("-", "").length() != 11)
                return false;

            String expression = "[^0]{2}[7-9]{2}";
            CharSequence inputString = pTelefone.replace("(", "").replace(")", "").replace("-", "").substring(0, 4);
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputString);
            return matcher.matches();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isNumeric(String pValor) {
        pValor = pValor.replaceAll("[^0-9]*", "");
        return (pValor.length() > 0);
    }

    //Valida a Data e Hora do Aparelho se são validas
    public static boolean validacaoDataAparelho(Context pContext) {
        Colaborador colaborador = new DBColaborador(pContext).get();
        DataHoraServidor dataHoraServidor = new DBDataHoraServidor(pContext).get();

        GPSTracker gpsTracker = new GPSTracker(pContext);
        if (!gpsTracker.isGPSEnabled) {
            gpsTracker.showSettingsAlert();
            return false;
        }

        if (gpsTracker.isMockLocationON) {
            gpsTracker.showMockAlert();
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String dataAparelho = Util_IO.dateTimeToString(calendar.getTime(), Config.FormatDateStringBr);
        String dataGps = Util_IO.dateTimeToString(gpsTracker.getDataGps(), Config.FormatDateStringBr);
        String dataUltimaVenda = new DBVenda(pContext).retornaUltimaVenda();

        String dataServidor = "";
        if (dataHoraServidor != null) {
            dataServidor = Util_IO.dateTimeToString(dataHoraServidor.getDate(), Config.FormatDateStringBr);
        }

        if (colaborador.isValidaDataGps() && !dataAparelho.equals(dataGps)) {
            if (!dataServidor.equals("") && dataGps.equals("") && !dataAparelho.equals(dataServidor)) {
                String mensagem = "A data do aparelho não está correta, Por favor ajuste a data do aparelho. \n Data atual " + dataServidor + " Data aparelho " + dataAparelho;
                Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), mensagem);
                alerta.show();
                return false;
            } else if (dataAparelho.equals(dataServidor)) {
                return true;
            }

            String mensagem = "Data do aparelho não está correta, Por favor ajustar. Data atual " + dataGps + " Data aparelho " + dataAparelho;
            Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), mensagem);
            alerta.show();
            return false;
        }

        Date dataAp = Util_IO.stringToDate(dataAparelho);
        Date dataVend = Util_IO.stringToDate(dataUltimaVenda);
        Date datagp = Util_IO.stringToDate(dataGps);
        if (dataUltimaVenda != null && dataAp.before(dataVend) && dataAp.after(datagp)) {
            Mensagens.erroDataInvalida(pContext);
            return false;
        }

        return true;
    }

    public static boolean validacaoDataAparelhoAdquirencia(Context pContext) {
        Colaborador colaborador = new DBColaborador(pContext).get();
        DataHoraServidor dataHoraServidor = new DBDataHoraServidor(pContext).get();

        GPSTracker gpsTracker = new GPSTracker(pContext);
        if (!gpsTracker.isGPSEnabled) {
            gpsTracker.showSettingsAlert();
            return false;
        }

        if (gpsTracker.isMockLocationON || gpsTracker.areThereMockPermissionApps()) {
            gpsTracker.showMockAlert();
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String dataAparelho = Util_IO.dateTimeToString(calendar.getTime(), Config.FormatDateStringBr);
        String dataGps = Util_IO.dateTimeToString(gpsTracker.getDataGps(), Config.FormatDateStringBr);

        String dataServidor = "";
        if (dataHoraServidor != null) {
            dataServidor = Util_IO.dateTimeToString(dataHoraServidor.getDate(), Config.FormatDateStringBr);
        }

        if (colaborador.isValidaDataGps() && !dataAparelho.equals(dataGps)) {
            if (!dataServidor.equals("") && dataGps.equals("") && !dataAparelho.equals(dataServidor)) {
                String mensagem = "A data do aparelho não está correta, Por favor ajuste a data do aparelho. \n Data atual " + dataServidor + " Data aparelho " + dataAparelho;
                Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), mensagem);
                alerta.show();
                return false;
            } else if (dataAparelho.equals(dataServidor)) {
                return true;
            }

            String mensagem = "Data do aparelho não está correta, Por favor ajustar. Data atual " + dataGps + " Data aparelho " + dataAparelho;
            Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), mensagem);
            alerta.show();
            return false;
        }
        return true;
    }

    public static boolean validacoesPreAtendimento(Context pContext, Colaborador pColaborador, Cliente pCliente) throws Exception {
        if (pColaborador.getDesbloqueiaVenda() != null && pColaborador.getDesbloqueiaVenda().equalsIgnoreCase("N")) {
            Mensagens.boletosPendentes(pContext);
            return false;
        }

        if (!validacaoDataAparelho(pContext)) {
            return false;
        }
        boolean verificacao;
        if (pColaborador.isObrigaAuditagem()) {
            verificacao = new DBEstoque(pContext).auditagensRealizadasHoje();
            if (!verificacao) {
                Mensagens.auditagemNaoRealizada(pContext);
                return false;
            }
        }

        verificacao = new DBOs(pContext).existeOsPendenteAgendamento();
        if (verificacao) {
            Mensagens.osSemAgendamento(pContext);
            return false;
        }

        if (pCliente == null) {
            Mensagens.clienteNaoEncontrado(pContext, false);
            return false;
        }

        return true;
    }

    public static ComboVenda criarNovoCombo(ComboVenda comboVenda) {
        ComboVenda temp = new ComboVenda();
        temp.setQuantidade(comboVenda.getQuantidade());
        temp.setIdProduto(comboVenda.getIdProduto());
        temp.setId(comboVenda.getId());

        return temp;
    }

    public static boolean validacoesPreAddICCID(Context pContext,
                                                Produto pProduto,
                                                PrecoDiferenciado pPrecoDiferenciado,
                                                ArrayList<CodBarra> pItensCod,
                                                boolean itemVendaCombo,
                                                ArrayList<ComboVenda> pItensCombo,
                                                UsoCodBarra usoCodBarra) {
        return validacoesPreAddICCIDValidacoes(pContext, pProduto, pPrecoDiferenciado, pItensCod, itemVendaCombo, pItensCombo, usoCodBarra, 0, null, 0);
    }

    public static boolean validacoesPreAddICCID(Context pContext,
                                                Produto pProduto,
                                                PrecoDiferenciado pPrecoDiferenciado,
                                                ArrayList<CodBarra> pItensCod,
                                                boolean itemVendaCombo,
                                                ArrayList<ComboVenda> pItensCombo,
                                                UsoCodBarra usoCodBarra,
                                                int qtdBipadaExterna,
                                                ItemVendaCombo itemVenda,
                                                int qtdExternaParaCombo) {
        return validacoesPreAddICCIDValidacoes(pContext, pProduto, pPrecoDiferenciado, pItensCod, itemVendaCombo, pItensCombo, usoCodBarra, qtdBipadaExterna, itemVenda, qtdExternaParaCombo);
    }

    public static boolean validacoesPreAddICCIDValidacoes(Context pContext,
                                                          Produto pProduto,
                                                          PrecoDiferenciado pPrecoDiferenciado,
                                                          ArrayList<CodBarra> pItensCod,
                                                          boolean itemVendaCombo,
                                                          ArrayList<ComboVenda> pItensCombo,
                                                          UsoCodBarra usoCodBarra,
                                                          int qtdBipadaExterna,
                                                          ItemVendaCombo itemVenda,
                                                          int qtdExternaParaCombo) {

        if (pProduto.getPrecovenda() <= 0 && pProduto.getPermiteVendaSemValor().equalsIgnoreCase("N")) {
            ((Activity) pContext).runOnUiThread(() -> Mensagens.produtoComValorZerado(pContext));
            return false;
        }

        DBEstoque dbEstoque = new DBEstoque(pContext);
        if (itemVendaCombo) {
            int qtdCombo = CodigoBarra.retornaQtCombo(pProduto.getId(), pContext);
            pProduto.setQtdCombo(qtdCombo);
        }

        ProdutoCombo produtoCombo = null;
        if (pProduto.getQtdCombo() > 0 && itemVenda != null) {
            int limit = itemVenda.getQtdCombo() * qtdExternaParaCombo;
            int quantidadeJaBipada = itemVenda.getCodigosList() != null ? itemVenda.getCodigosList().size() : 0;
            if (limit != quantidadeJaBipada) {
                ((Activity) pContext).runOnUiThread(() -> Mensagens.faltaEscanearCodBarra(pContext, limit - quantidadeJaBipada));
                return false;
            }
        } else if (pProduto.getQtdCombo() > 0) {
            produtoCombo = CodigoBarra.retornaCombo(pProduto.getQtdCombo(), pItensCod, usoCodBarra);
            if (produtoCombo.getQtdFalta() > 0) {
                ProdutoCombo finalProdutoCombo = produtoCombo;
                ((Activity) pContext).runOnUiThread(() -> Mensagens.faltaEscanearCodBarra(pContext, finalProdutoCombo.getQtdFalta()));
                return false;
            }
        }

        /*
            19/12/2017
            Verificação da quantidade bipada com a estrutura
         */
        if (itemVendaCombo) {
            ArrayList<EstruturaProd> itensEstruturaProd = dbEstoque.getEstruturaByItemPai(pProduto.getId());
            for (EstruturaProd estruturaProd : itensEstruturaProd) {
                int quantidade = 0;
                for (ComboVenda comboVenda : pItensCombo) {
                    if (comboVenda.getIdProduto().equalsIgnoreCase(estruturaProd.getItemFilho()))
                        quantidade += comboVenda.getQuantidade();
                }

                int quantidadeEstrutura = estruturaProd.getQtd() * produtoCombo.getQtdTotal();
                if (quantidade != quantidadeEstrutura) {
                    int quantidadefalta = quantidade - quantidadeEstrutura;
                    if (quantidadefalta < 0)
                        quantidadefalta = quantidadefalta * -1;
                    Produto produto = dbEstoque.getProdutoById(estruturaProd.getItemFilho());
                    int finalQuantidadefalta = quantidadefalta;
                    ((Activity) pContext).runOnUiThread(() -> {
                        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name)
                                , "Falta escanear " + finalQuantidadefalta + " do produto " + produto.getNome());
                        alerta.show();
                    });
                    return false;
                }
            }
        }

        int quantidadebipada;
        if (pContext instanceof NovoAtendimentoActivity) {
            quantidadebipada = pProduto.getQtde();
        } else {
            quantidadebipada = Utilidades.getQtdBipadaICCID(pProduto, produtoCombo, pItensCod, usoCodBarra);
        }

        if (quantidadebipada <= 0) {
            ((Activity) pContext).runOnUiThread(() -> Mensagens.nenhumItemIncluido(pContext));
            return false;
        }

        EstoqueDaoImpl estoqueDao = new EstoqueDaoImpl(pContext);
        if (!estoqueDao.verificaEstoque(pProduto.getId(), quantidadebipada, qtdBipadaExterna)) {
            ((Activity) pContext).runOnUiThread(() -> Mensagens.semEstoque(pContext));
            return false;
        }

        if (pItensCombo != null) {
            List<ComboVenda> pItensComboUnique = new ArrayList<>();
            for (ComboVenda comboVenda : pItensCombo) {
                if (pItensComboUnique.isEmpty()) {
                    ComboVenda temp = criarNovoCombo(comboVenda);
                    pItensComboUnique.add(temp);
                    continue;
                }

                boolean contains = false;
                for (ComboVenda comboVenda1 : pItensComboUnique) {
                    if (comboVenda1.getIdProduto().equals(comboVenda.getIdProduto())) {
                        comboVenda1.setQuantidade(comboVenda1.getQuantidade() + comboVenda.getQuantidade());
                        contains = true;
                    }
                }

                if (!contains) {
                    ComboVenda temp = criarNovoCombo(comboVenda);
                    pItensComboUnique.add(temp);
                }
            }

            for (ComboVenda comboVenda : pItensComboUnique) {
                if (!dbEstoque.verificaEstoque(comboVenda.getIdProduto(), comboVenda.getQuantidade())) {
                    ((Activity) pContext).runOnUiThread(() -> Mensagens.semEstoque(pContext));
                    return false;
                }
            }
        }

        if (pPrecoDiferenciado != null && pPrecoDiferenciado.getQtdPreco() > 0 && quantidadebipada > pPrecoDiferenciado.getQtdPreco()) {
            ((Activity) pContext).runOnUiThread(() -> Mensagens.promocaoAteTantasUnds(pContext, pPrecoDiferenciado.getQtdPreco()));
            return false;
        }
        return true;
    }

    public static boolean validacoesAtualizaFormaPagamento(Context pContext, FormaPagamentoVenc pFormaPagamentoVenc, Venda pVenda) throws Exception {
        if (pFormaPagamentoVenc == null)
            throw new Exception("Forma de pagamento não selecionada, Verifique!");

        Cliente cliente = new DBCliente(pContext).getById(pVenda.getIdCliente());
        DBEstoque dbEstoque = new DBEstoque(pContext);
        DBVenda dbVenda = new DBVenda(pContext);
        ArrayList<ItemVenda> listaItens = dbVenda.getItensVendabyIdVenda(pVenda.getId());
        if (pFormaPagamentoVenc.getFormapgto().getId() == 2) {
            if (listaItens != null && listaItens.size() > 0) {
                for (ItemVenda item : listaItens) {
                    Produto prodVerifica = dbEstoque.getProdutoById(item.getIdProduto());
                    if (!Util_IO.isNullOrEmpty(prodVerifica.getPermiteVendaPrazo()) && prodVerifica.getPermiteVendaPrazo().equalsIgnoreCase("N")) {
                        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "O produto " + prodVerifica.getNome() + " só pode ser vendido à vista, Verifique!");
                        alerta.show();
                        return false;
                    }
                }
            }

            if (!cliente.getExibirCodigo().equalsIgnoreCase("EFRESH") && !cliente.getExibirCodigo().equalsIgnoreCase("SGV")) {
                Mensagens.formaPagamentoNaoPermitida(pContext);
                return false;
            }

            boolean bVendaPermitida;
            if (cliente.getExibirCodigo().equalsIgnoreCase("EFRESH")) {
                if (cliente.getCodigoeFresh().isEmpty()) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente sem código eFresh, Verificar com departamento de TI");
                    alerta.show();
                    return false;
                }

                if (cliente.getStatuseFresh().equalsIgnoreCase("T")) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente com Pendência Comercial, Verifique");
                    alerta.show();
                    return false;
                } else if (cliente.getStatuseFresh().equalsIgnoreCase("F")) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente com Pendência Financeira, Verifique");
                    alerta.show();
                    return false;
                }

                bVendaPermitida = cliente.getStatuseFresh().equalsIgnoreCase("Y");
            } else {
                if (cliente.getCodigoSGV().isEmpty()) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente sem código SGV, Verificar com departamento de TI");
                    alerta.show();
                    return false;
                }

                if (cliente.getStatusSGV().equalsIgnoreCase("Bloqueado")) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente com Pendência Financeira, o mesmo encontra-se bloqueado no SGV, Verifique");
                    alerta.show();
                    return false;
                } else if (cliente.getStatusSGV().equalsIgnoreCase("Descredenciado")) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente descredenciado no SGV, Verifique com departamento de Cadastro");
                    alerta.show();
                    return false;
                } else if (cliente.getStatusSGV().equalsIgnoreCase("Inativo")) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente inativo no SGV, Verifique com departamento de Cadastro");
                    alerta.show();
                    return false;
                } else if (cliente.getStatusSGV().equalsIgnoreCase("Validado")) {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente sem POS no SGV, Verifique com departamento de Cadastro");
                    alerta.show();
                    return false;
                }

//                if (cliente.getClienteFisico() && !cliente.getClienteEletronico() && !cliente.getClienteAdquirencia()) {
//                    List<InformacaoGeralPOS> listPos = new DBPOS(pContext).obterInformacoesGeraisPOSPorId(cliente.getId());
//                    if (listPos.size() <= 0) {
//                        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Venda à Prazo não permitida para Cliente tipo físico e sem POS.");
//                        alerta.show();
//                        return false;
//                    }
//                }

                bVendaPermitida = (cliente.getStatusSGV().equalsIgnoreCase("Credenciado") || cliente.getStatusSGV().equalsIgnoreCase("Ativo"));
            }

            if (!bVendaPermitida) {
                Mensagens.formaPagamentoNaoPermitida(pContext);
                return false;
            }

            //Limite Cliente
            if (cliente.getExibirCodigo().equalsIgnoreCase("SGV") && cliente.getStatusSGV().equalsIgnoreCase("Credenciado")) {
                double valorJaVendido = dbVenda.retornaValorVendidoByCliente(cliente.getId());
                double valorLimiteDisponivel = cliente.getLimitePrimeiraVenda() - valorJaVendido;
                if (valorLimiteDisponivel < 0)
                    valorLimiteDisponivel = 0;
                double valorVenda = dbVenda.retornaValorTotalVenda(pVenda.getId());
                if (valorVenda > valorLimiteDisponivel) {
                    Mensagens.valorUltrapassaLimiteCliente(pContext, valorLimiteDisponivel);
                    return false;
                }
            } else {
                LimiteCliente limite = new DBLimite(pContext).getByIdCliente(cliente.getId());
                if (limite != null) {
                    if (limite.getSaldo() <= 0) {
                        Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente sem limite de crédito disponível");
                        alerta.show();
                        return false;
                    }

                    double saldoFinal = limite.getSaldo() - dbVenda.retornaValorTotalVenda(pVenda.getId());
                    if (saldoFinal < 0) {
                        Mensagens.valorUltrapassaLimiteCliente(pContext, limite.getSaldo());
                        return false;
                    }
                }
                else
                {
                    Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente sem limite de crédito cadastrado!");
                    alerta.show();
                    return false;
                }
            }
            return true;
        } else {
            if (cliente.isBloqueiaVendaVista()) {
                if (listaItens != null && listaItens.size() > 0) {
                    for (ItemVenda item : listaItens) {
                        Produto prodVerifica = dbEstoque.getProdutoById(item.getIdProduto());
                        if (!Util_IO.isNullOrEmpty(prodVerifica.getPermiteVendaPrazo()) && prodVerifica.getPermiteVendaPrazo().equalsIgnoreCase("S")) {
                            Alerta alerta = new Alerta(pContext, pContext.getResources().getString(R.string.app_name), "Cliente com pendencias financeiras. A venda está desabilitada para esse PDV até sua regularização");
                            alerta.show();
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    public static boolean validacoesPrecoBonificado(Context pContext, ArrayList<ItemVendaCombo> pListFinal, Venda pVenda) {
        DBPreco dbPreco = new DBPreco(pContext);

        boolean existeDiferenciado = false;
        int limiteVenda = 0;
        int quantidadeItemDiferenciado = 0;
        String idProduto = "";

        pListFinal = (ArrayList<ItemVendaCombo>) Stream.ofNullable(pListFinal).sortBy(ItemVenda::getIdProduto).toList();

        int count = 0;
        for (ItemVendaCombo item : pListFinal) {
            PrecoDiferenciado precoDiferenciado = (item.getIdPreco() > 0) ? dbPreco.getPrecoById(String.valueOf(item.getIdPreco())) : null;

            ArrayList<ItemVendaCombo> itemsJaAdicionados = new DBVenda(pContext).getItensComboVendabyIdVenda(pVenda.getId());

            ArrayList<ItemVendaCombo> itemsIguais = (ArrayList<ItemVendaCombo>) Stream.ofNullable(itemsJaAdicionados)
                    .filter(value -> value.getIdProduto().equals(item.getIdProduto())
                            && value.getIdPreco() == item.getIdPreco())
                    .toList();

            if (idProduto.isEmpty()) {
                idProduto = item.getIdProduto();
            } else if (!idProduto.equals(item.getIdProduto())) {
                existeDiferenciado = false;
                limiteVenda = 0;
                quantidadeItemDiferenciado = 0;
                idProduto = item.getIdProduto();
                count = 0;
            }

            if (precoDiferenciado != null) {
                existeDiferenciado = true;
                limiteVenda = precoDiferenciado.getQtdPreco();
            }

            if (!itemsIguais.isEmpty()) {
                int qtdTotalPrecoDiferenciado = 0;
                for (ItemVendaCombo igual : itemsIguais) {
                    qtdTotalPrecoDiferenciado += igual.getQtde();
                }

                if (qtdTotalPrecoDiferenciado > limiteVenda) {
                    existeDiferenciado = false;
                }

                if (count < 1) {
                    quantidadeItemDiferenciado += item.getQtde() + qtdTotalPrecoDiferenciado;
                } else {
                    quantidadeItemDiferenciado += item.getQtde();
                }
            } else {
                quantidadeItemDiferenciado += item.getQtde();
            }

            if (existeDiferenciado && limiteVenda < quantidadeItemDiferenciado) {
                String prec = "";
                if (precoDiferenciado != null) {
                    prec = "R$" + Util_IO.formataValor(precoDiferenciado.getValor());
                }

                String finalPrec = prec;
                int finalLimiteVenda = limiteVenda;
                int finalQuantidadeItemDiferenciado = quantidadeItemDiferenciado;
                ((Activity) pContext).runOnUiThread(() -> Mensagens.bonificadoAteTantasUnds(pContext,
                        item.getNomeProduto(),
                        finalPrec,
                        finalLimiteVenda,
                        finalQuantidadeItemDiferenciado));
                return false;
            }

            count++;
        }

        return true;
    }

    public static boolean validacoesHorarioInicioAplicativo(Context context, boolean validaDomingo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        LocalTime agora = LocalTime.now();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY && validaDomingo) {
            return false;
        }

        DBColaborador dbColaborador = new DBColaborador(context);
        Colaborador colaborador = dbColaborador.get();

        DBHorarioNotificacao dbHorarioNotificacao = new DBHorarioNotificacao(context);
        HorarioNotificacao horarios = dbHorarioNotificacao
                .getHorarioNotificacaoPorDiaSemana(day);

        LocalTime limitHour = LocalTime.of(7, 0);
        if (horarios != null && StringUtils.isNotEmpty(horarios.getHoraUm()) && StringUtils.isNotEmpty(horarios.getHoraQuatro())) {
            limitHour = toLocalTime(horarios.getHoraUm());
        }

        if (validaDomingo) {
            return colaborador.isCartaoPonto()
                    && colaborador.checkIfBloqueiaHorario()
                    && agora.isBefore(limitHour);
        }

        return colaborador.isCartaoPonto()
                && colaborador.checkIfBloqueiaHorario()
                && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                && agora.isBefore(limitHour);
    }

    private static LocalTime toLocalTime(String hora) {
        String[] tempo = hora.split(":");
        return LocalTime.of(Integer.parseInt(tempo[0]), Integer.parseInt(tempo[1]));
    }
}
