package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastro;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroEndereco;
import com.axys.redeflexmobile.shared.bd.DBClienteCadastroPOS;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBSuporte;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.models.AnexoRetorno;
import com.axys.redeflexmobile.shared.models.ClienteCadastroAnexo;
import com.axys.redeflexmobile.shared.models.ClienteCadastroPOS;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.RetCadCliente;
import com.axys.redeflexmobile.shared.models.TipoConta;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterConfirm;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by joao.viana on 09/09/2016.
 */
public class ClienteCadastroBus extends BaseBus {

    public static void enviarCadastroCliente(Context pContext) {
        DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(pContext);
        DBClienteCadastroEndereco dbClienteCadastroEndereco = new DBClienteCadastroEndereco(pContext);
        Stream.ofNullable(dbClienteCadastro.getClientesPendentes()).forEach(clienteCadastro -> {
            try {
                Log.d("Roni", "Enviando Cadastro: ");

                URL url = new URL(URLs.CADASTRO_CLIENTE);
                if (EnumRegisterCustomerType.ACQUISITION.equals(clienteCadastro.getCustomerType()) || EnumRegisterCustomerType.SUBADQUIRENCIA.equals(clienteCadastro.getCustomerType())) {
                    url = new URL(URLs.CADASTRO_ADQUIRENCIA);
                }
                String customer = JsonUtils.getGsonInstance().toJson(removeSpecialCharacters(clienteCadastro));
                String response = Utilidades.putRegistros(url, customer);
                if (response != null && !response.contains("-1")) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    clienteCadastro.setDateUpdate(cal.getTime());
                    dbClienteCadastro.updateCadastro(clienteCadastro, response);

                    Stream.ofNullable(clienteCadastro.getAddresses())
                            .forEach(customerRegisterAddress ->
                                    dbClienteCadastroEndereco.atualizarSync(customerRegisterAddress.getId()));

                } else if (response != null) {
                    new DBSuporte(pContext).addErroSync(clienteCadastro.getIdSalesman(), "ClienteCadastro", Integer.valueOf(clienteCadastro.getId()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("Roni", "Erro ao Enviar: " + ex.getMessage());
            }
        });
    }

    public static void enviarRecadastroCliente(Context pContext) {
        DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(pContext);
        DBClienteCadastroEndereco dbClienteCadastroEndereco = new DBClienteCadastroEndereco(pContext);
        Stream.ofNullable(dbClienteCadastro.getClientesRecadastroPendentes()).forEach(clienteCadastro -> {
            try {

                URL url = new URL(URLs.CADASTRO_CLIENTE);
                if (EnumRegisterCustomerType.ACQUISITION.equals(clienteCadastro.getCustomerType()) || EnumRegisterCustomerType.SUBADQUIRENCIA.equals(clienteCadastro.getCustomerType())) {
                    url = new URL(URLs.CADASTRO_ADQUIRENCIA);
                }

                String customer = JsonUtils.getGsonInstance().toJson(removeSpecialCharacters(clienteCadastro));
                String response = Utilidades.putRegistros(url, customer);
                if (response != null && !response.equals("-1")) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    clienteCadastro.setDateUpdate(cal.getTime());
                    dbClienteCadastro.updateCadastro(clienteCadastro, response);

                    Stream.ofNullable(clienteCadastro.getAddresses())
                            .forEach(customerRegisterAddress ->
                                    dbClienteCadastroEndereco.atualizarSync(customerRegisterAddress.getId()));

                } else if (response != null) {
                    new DBSuporte(pContext).addErroSync(clienteCadastro.getIdSalesman(), "ClienteCadastro", Integer.valueOf(clienteCadastro.getId()));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void enviarAnexos(Context pContext) {
        try {

            DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(pContext);
            ArrayList<ClienteCadastroAnexo> list = dbClienteCadastro.getAnexosPendentes();

            for (ClienteCadastroAnexo clienteCadastroAnexo : list) {

                Bitmap bImagem;
                if (clienteCadastroAnexo.getImagem().contains("/Files/Compressed")) {
                    bImagem = BitmapFactory.decodeFile(clienteCadastroAnexo.getImagem());
                } else {
                    bImagem = Utilidades.decodeBase64(clienteCadastroAnexo.getImagem());
                }

                if (bImagem != null) {
                    try {
                        URL url = new URL(URLs.CADASTRO_CLIENTE_ANEXO);
                        JSONObject main = new JSONObject();
                        try {
                            main.put("Cpfcnpj", clienteCadastroAnexo.getCpfcnpj());
                            main.put("Imagem", Utilidades.encodeToBase64(bImagem, Bitmap.CompressFormat.JPEG, 100));
                            main.put("Tipo", clienteCadastroAnexo.getTipo());
                            main.put("IdVendedor", clienteCadastroAnexo.getIdVendedor());
                            main.put("Longitude", clienteCadastroAnexo.getLongitude());
                            main.put("Latitude", clienteCadastroAnexo.getLatitude());
                            main.put("Precisao", clienteCadastroAnexo.getPrecisao());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            continue;
                        } catch (OutOfMemoryError ex) {
                            ex.printStackTrace();
                            continue;
                        }

                        String json = main.toString();
                        String response = Utilidades.putRegistros(url, json);
                        if (response != null && response.equals("1")) {
                            dbClienteCadastro.updateAnexo(clienteCadastroAnexo.getId());
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    dbClienteCadastro.updateSync(clienteCadastroAnexo.getIdCadastro(), 8);
                    dbClienteCadastro.deletarAnexoById(clienteCadastroAnexo.getId());
                    Notificacoes.Cadastro("Erro ao sincronizar Cadastro",
                            Integer.parseInt(clienteCadastroAnexo.getIdCadastro()), pContext,
                            "É necessario anexar novamente os arquivos pendentes", true);
                }
            }
        } catch (Exception ex) {
            Log.d("Roni", "enviarAnexos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void enviarSituacao99(Context pContext) {
        DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(pContext);
        Stream.ofNullable(dbClienteCadastro.getClientesPendentesSituacao99()).forEach(clienteCadastro -> {
            try {
                String pTipoCliente = "";
                if (clienteCadastro.getCustomerType() == EnumRegisterCustomerType.PHYSICAL)
                    pTipoCliente = "Fisico";
                else if (clienteCadastro.getCustomerType() == EnumRegisterCustomerType.ELECTRONIC)
                    pTipoCliente = "Eletronico";
                else if (clienteCadastro.getCustomerType() == EnumRegisterCustomerType.ACQUISITION)
                    pTipoCliente = "Solver";
                else if (clienteCadastro.getCustomerType() == EnumRegisterCustomerType.SUBADQUIRENCIA)
                    pTipoCliente = "Adquirencia";
                else if (clienteCadastro.getCustomerType() == EnumRegisterCustomerType.MIGRATION)
                    pTipoCliente = "Migracao";

                String request = JsonUtils.getGsonInstance().toJson(new CustomerRegisterConfirm(
                        Integer.valueOf(clienteCadastro.getIdSalesman()),
                        pTipoCliente,
                        clienteCadastro.getCpfCnpj(),
                        Integer.valueOf(clienteCadastro.getId())
                ));

                String response = Utilidades.postRegistros(new URL(URLs.CONFIRMA_CADASTRO_CLIENTE), request);
                if (response != null && response.equals("1")) {
                    dbClienteCadastro.updateSync(clienteCadastro.getId(), Integer.valueOf(response));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void getRetorno(Context pContext, int pIipoCarga) {
        try {
            DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(pContext);
            DBClienteCadastroEndereco dbClienteCadastroEndereco = new DBClienteCadastroEndereco(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();

            String urlfinal = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.CADASTRO_CLIENTE,
                    colaborador.getId(),
                    pIipoCarga);

            RetCadCliente[] array = Utilidades.getArrayObject(urlfinal, RetCadCliente[].class);
            if (array != null && array.length > 0) {

                ArrayList<String> idList = new ArrayList<>();
                for (RetCadCliente retCadCliente : array) {

                    String cpfCnpj = dbClienteCadastro.updateRetorno(retCadCliente);
                    if (cpfCnpj != null) {
                        idList.add(cpfCnpj);
                    }

                    Stream.ofNullable(retCadCliente.getRetCadClienteEnd())
                            .forEach(retCadClienteEnd ->
                                    dbClienteCadastroEndereco.atualizarIdServer(
                                            retCadClienteEnd.getIdAppMobile(),
                                            retCadClienteEnd.getId())
                            );
                }

                if (!idList.isEmpty()) {
                    setSyncString(URLs.CADASTRO_CLIENTE, idList, colaborador.getId());
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getRetornoDoc(Context pContext) {
        try {
            DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();
            String urlfinal = URLs.CADASTRO_CLIENTE_ANEXO + "?idVendedor=" + String.valueOf(colaborador.getId());
            AnexoRetorno[] array = Utilidades.getArrayObject(urlfinal, AnexoRetorno[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>();
                for (AnexoRetorno item : array) {
                    int idCodigo = dbClienteCadastro.retornaCodigoCadastro(item.getCpfCnpj());
                    if (idCodigo > 0) {
                        item.setIdCadastro(String.valueOf(idCodigo));
                        if (dbClienteCadastro.verificaAnexo(item)) {
                            dbClienteCadastro.updateRetornoAnexo(item);
                        } else {
                            getAnexo(item, pContext);
                        }
                    }

                    idList.add(Integer.valueOf(item.getId()));
                }

                setSync(URLs.CADASTRO_CLIENTE_ANEXO, idList, colaborador.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void getAnexo(AnexoRetorno pAnexoRetorno, Context pContext) {
        try {
            DBClienteCadastro dbClienteCadastro = new DBClienteCadastro(pContext);
            String urlFinal = URLs.CADASTRO_CLIENTE_ANEXO + "?idAnexo=" + pAnexoRetorno.getId();
            URL pUrl = new URL(urlFinal);
            HttpURLConnection urlConnection = (HttpURLConnection) pUrl.openConnection();
            String resultString = Utilidades.convertStreamToString(new BufferedInputStream(urlConnection.getInputStream()), false);
            if (!resultString.equalsIgnoreCase("null")) {
                Bitmap bImagem = Utilidades.decodeBase64(resultString);
                if (bImagem != null) {
                    String localFinal = Utilidades.getFilename(pContext);
                    try {
                        FileOutputStream fos = new FileOutputStream(localFinal);
                        bImagem.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        fos.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        localFinal = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        localFinal = null;
                    }
                    if (localFinal != null)
                        dbClienteCadastro.addAnexo(pAnexoRetorno, localFinal);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getModelosPOS(int tipoCarga, Context pContext) {
        try {
            DBClienteCadastroPOS dbClienteCadastroPOS = new DBClienteCadastroPOS(pContext);
            Colaborador colaborador = new DBColaborador(pContext).get();
            String url = String.format("%s?idVendedor=%s&tipoCarga=%s",
                    URLs.CADASTRO_CLIENTE_POS,
                    colaborador.getId(),
                    tipoCarga
            );

            ArrayList<Integer> ids = new ArrayList<>();
            ClienteCadastroPOS[] responses = Utilidades.getArrayObject(url, ClienteCadastroPOS[].class);
            Stream.ofNullable(responses).forEach(clienteCadastroPOS -> {
                dbClienteCadastroPOS.addPOSSync(clienteCadastroPOS);
                ids.add(clienteCadastroPOS.getId());
            });

            if (!ids.isEmpty()) {
                setSync(URLs.CADASTRO_CLIENTE_POS, ids, colaborador.getId());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static CustomerRegister removeSpecialCharacters(CustomerRegister customerRegister) {
        TipoConta tipoConta = customerRegister.getTipoConta();
        if (tipoConta != null) {
            tipoConta.setDescription(Util_IO.retiraAcento(customerRegister.getTipoConta().getDescription()));
        }
        customerRegister.setTipoConta(tipoConta);

        // Teve que criar para remover acentuação do campo nome do contato, pois estava dando problema na inserção.
        for (int aa=0; aa<customerRegister.getContatos().size();aa++)
        {
            customerRegister.getContatos().get(aa).setNome(Util_IO.retiraAcento(customerRegister.getContatos().get(aa).getNome()));
        }

        if (!Util_IO.isNullOrEmpty(customerRegister.getPartnerName()))
            customerRegister.setPartnerName(Util_IO.retiraAcento(customerRegister.getPartnerName()));

        if (customerRegister.getPartners() != null) {
            if (!Util_IO.isNullOrEmpty(customerRegister.getPartners().getNome()))
                customerRegister.getPartners().setNome(Util_IO.retiraAcento(customerRegister.getPartners().getNome()));
        }

        if (customerRegister.getContatoPrincipal() != null)
        {
            if (!Util_IO.isNullOrEmpty(customerRegister.getContatoPrincipal().getNome()))
                customerRegister.getContatoPrincipal().setNome(Util_IO.retiraAcento(customerRegister.getContatoPrincipal().getNome()));
        }
        return customerRegister;
    }
}
