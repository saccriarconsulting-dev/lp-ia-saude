package com.axys.redeflexmobile.shared.services.bus;

import android.content.Context;

import androidx.annotation.NonNull;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.shared.bd.DBColaborador;
import com.axys.redeflexmobile.shared.bd.DBPendencia;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.Pendencia;
import com.axys.redeflexmobile.shared.models.PendenciaCliente;
import com.axys.redeflexmobile.shared.models.PendenciaMotivo;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.network.util.JsonUtils;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import timber.log.Timber;

import static com.axys.redeflexmobile.shared.services.bus.BaseBus.setSync;

/**
 * @author Lucas Marciano
 */
public class PendenciaBus {

    public static void getPendencias(int pTipoCarga, @NonNull Context pContext) {
        try {
            DBPendencia dbPendencia = new DBPendencia(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_PENDENCIA + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;

            Pendencia[] array = Utilidades.getArrayObject(urlfinal, Pendencia[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>(array.length);
                for (Pendencia pendencia : array) {
                    try {
                        if (pendencia.isAtivo()) {
                            dbPendencia.addPendencia(pendencia);
                        } else {
                            dbPendencia.removePendencia(pendencia);
                        }
                    } catch (Exception e) {
                        Timber.d(e, "getPendencias: erro ao gravar pendenciaId=%s id=%s",
                                String.valueOf(pendencia.getPendenciaId()), pendencia.getId());
                    }

                    try {
                        idList.add(Integer.parseInt(pendencia.getId()));
                    } catch (Exception nfe) {
                        Timber.d("getPendencias: id inválido recebido: %s", pendencia.getId());
                    }
                }
                setSync(URLs.URL_PENDENCIA, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            Timber.d(ex, "getPendencias: IOException");
        } catch (Exception ex) {
            Timber.d(ex, "getPendencias: Exception");
        }
    }

    public static void getPendenciasMotivo(int pTipoCarga, @NonNull Context pContext) {
        try {
            DBPendencia dbPendencia = new DBPendencia(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_PENDENCIA_MOTIVO + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;

            PendenciaMotivo[] array = Utilidades.getArrayObject(urlfinal, PendenciaMotivo[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>(array.length);
                for (PendenciaMotivo pendenciaMotivo : array) {
                    try {
                        if (pendenciaMotivo.isAtivo()) {
                            dbPendencia.addPendenciaMotivo(pendenciaMotivo);
                        } else {
                            dbPendencia.removePendenciaMotivo(pendenciaMotivo);
                        }
                    } catch (Exception e) {
                        Timber.d(e, "getPendenciasMotivo: erro ao gravar pendenciaMotivoId=%s",
                                String.valueOf(pendenciaMotivo.getPendenciaMotivoId()));
                    }

                    try {
                        idList.add(Integer.parseInt(pendenciaMotivo.getId()));
                    } catch (Exception nfe) {
                        Timber.d("getPendenciasMotivo: id inválido recebido: %s", pendenciaMotivo.getId());
                    }
                }
                setSync(URLs.URL_PENDENCIA_MOTIVO, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            Timber.d(ex, "getPendenciasMotivo: IOException");
        } catch (Exception ex) {
            Timber.d(ex, "getPendenciasMotivo: Exception");
        }
    }

    public static void getPendenciasCliente(int pTipoCarga, @NonNull Context pContext) {
        try {
            DBPendencia dbPendencia = new DBPendencia(pContext);
            DBColaborador dbColaborador = new DBColaborador(pContext);
            Colaborador colaborador = dbColaborador.get();
            String urlfinal = URLs.URL_PENDENCIA_CLIENTE + "?idVendedor=" + colaborador.getId() + "&tipoCarga=" + pTipoCarga;

            PendenciaCliente[] array = Utilidades.getArrayObject(urlfinal, PendenciaCliente[].class);
            if (array != null && array.length > 0) {
                ArrayList<Integer> idList = new ArrayList<>(array.length);
                for (PendenciaCliente pendenciaCliente : array) {
                    try {
                        if (pendenciaCliente.isAtivo()) {
                            dbPendencia.addPendenciaCliente(pendenciaCliente);
                        } else {
                            dbPendencia.removePendenciaCliente(pendenciaCliente);
                        }
                    } catch (Exception e) {
                        Timber.d(e, "getPendenciasCliente: erro ao gravar pendenciaId=%s clienteId=%s",
                                String.valueOf(pendenciaCliente.getPendenciaId()),
                                String.valueOf(pendenciaCliente.getClienteId()));
                    }

                    try {
                        idList.add(Integer.parseInt(pendenciaCliente.getId()));
                    } catch (Exception nfe) {
                        Timber.d("getPendenciasCliente: id inválido recebido: %s", pendenciaCliente.getId());
                    }
                }
                setSync(URLs.URL_PENDENCIA_CLIENTE, idList, colaborador.getId());
            }
        } catch (IOException ex) {
            Timber.d(ex, "getPendenciasCliente: IOException");
        } catch (Exception ex) {
            Timber.d(ex, "getPendenciasCliente: Exception");
        }
    }

    public static void enviarPendenciaCliente(@NonNull Context pContext) {
        DBPendencia dbPendencia = new DBPendencia(pContext);
        DBColaborador dbColaborador = new DBColaborador(pContext);
        Colaborador colaborador = dbColaborador.get();

        try {
            Stream.ofNullable(dbPendencia.getAllPendenciaCliente()).forEach(pc -> {
                try {
                    String request = JsonUtils.getGsonInstance().toJson(new PendenciaCliente(
                            pc.getId(),
                            pc.getClienteId(),
                            pc.getPendenciaId(),
                            pc.getPendenciaMotivoId(),
                            Util_IO.trataString(pc.getObservacao()),
                            pc.getLatitude(),
                            pc.getLongitude(),
                            pc.getPrecision(),
                            pc.getDataVisualizacao(),
                            pc.getDataResposta(),
                            colaborador.getId(),
                            pc.isExibeExplicacao(),
                            Util_IO.retiraAcento(pc.getExplicacao()),
                            pc.getOrdem()
                    ));
                    Utilidades.putRegistros(new URL(URLs.URL_PENDENCIA_CLIENTE), request);
                } catch (Exception ex) {
                    Timber.d(ex, "enviarPendenciaCliente: falha ao enviar pendenciaClienteId=%s", pc.getId());
                }
            });
        } catch (Exception e) {
            Timber.d(e, "enviarPendenciaCliente: Exception");
        }
    }
}