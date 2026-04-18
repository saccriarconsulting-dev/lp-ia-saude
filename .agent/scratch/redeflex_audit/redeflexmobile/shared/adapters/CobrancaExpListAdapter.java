package com.axys.redeflexmobile.shared.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.bd.SimpleDbHelper;
import com.axys.redeflexmobile.shared.models.Cobranca;
import com.axys.redeflexmobile.shared.services.URLs;
import com.axys.redeflexmobile.shared.services.bluetooth.Command;
import com.axys.redeflexmobile.shared.services.bluetooth.DeviceListActivity;
import com.axys.redeflexmobile.shared.services.bluetooth.Print;
import com.axys.redeflexmobile.shared.services.bluetooth.PrintPicture;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by joao.viana on 14/07/2016.
 */
public class CobrancaExpListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<Cobranca> listaCobrancas;
    private Print mPrint;

    public CobrancaExpListAdapter(Context context, ArrayList<Cobranca> cobranca, Print _Print) {
        this.mContext = context;
        this.listaCobrancas = cobranca;
        this.mPrint = _Print;
    }

    @Override
    public int getGroupCount() {
        return listaCobrancas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listaCobrancas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Cobranca cobranca = listaCobrancas.get(groupPosition);
        return cobranca.getLinhaDigitavel();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Cobranca cobranca = listaCobrancas.get(childPosition);
        return cobranca.getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Cobranca cobranca = listaCobrancas.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_cobranca, parent, false);
        }

        DecimalFormat precision = new DecimalFormat("0.00");

        ((TextView) convertView.findViewById(R.id.txtDataVencimentoCob)).setText(Util_IO.dateToStringBr(cobranca.getDataVencimento()));
        ((TextView) convertView.findViewById(R.id.txtValorCobranca)).setText("R$ " + precision.format(cobranca.getValor()));

        String situacao = "";

        switch (cobranca.getSituacao()) {
            case 1:
                situacao = "À Vencer";
                break;
            case 0:
                situacao = "Vencido";
                break;
            case 6:
                situacao = "Cancelado";
                break;
            case 5:
                situacao = "Cancelado";
                break;
            default:
                situacao = "Pago";
                break;
        }

        ((TextView) convertView.findViewById(R.id.txtSituacaoCobranca)).setText(situacao);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Cobranca cobranca = listaCobrancas.get(groupPosition);
        final String codigo = cobranca.getLinhaDigitavel();

        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.item_cobranca_detalhes, null);

        ((TextView) convertView.findViewById(R.id.txtCodigoBarra)).setText(codigo);
        Button buttonImprimir = (Button) convertView.findViewById(R.id.btnImprimirBoleto);

        switch (cobranca.getSituacao()) {
            case 1:
                buttonImprimir.setVisibility(View.VISIBLE);
                break;
            default:
                buttonImprimir.setVisibility(View.GONE);
                break;
        }
        buttonImprimir.setVisibility(View.GONE);

        buttonImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPrint.isConnect()) {
                    if (!mPrint.isBluetoothEnable()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        ((Activity) mContext).startActivityForResult(enableIntent, 2);
                    }
                    Alerta alerta = new Alerta(mContext, mContext.getResources().getString(R.string.app_name), "Impressora não conectada, Deseja conectar?");
                    alerta.showConfirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                            ((Activity) mContext).startActivityForResult(serverIntent, 1);
                        }
                    }, null);
                } else {
                    new BuscarBoletoTask().execute(String.valueOf(cobranca.getNumBoleto()));
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utilidades.getCtrlC(mContext, codigo);
                Utilidades.retornaMensagem(mContext, "Código de barras copiado", false);
                return true;
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class BuscarBoletoTask extends AsyncTask<String, Void, String> {
        private ProgressDialog mDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, mContext.getResources().getString(R.string.app_name), "Aguarde, Gerando Boleto(s)!!", false, false);
            mDialog.setIcon(R.mipmap.ic_icone_new);
        }

        protected String doInBackground(String... params) {
            try {
                return buscarBoleto(params[0]);
            } catch (Exception ex) {
                return ex.getMessage();
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                mDialog.dismiss();
                if (result.length() > 0)
                    Utilidades.retornaMensagem(mContext, result, false);
            } catch (Exception ex) {
                Mensagens.mensagemErro(mContext, ex.getMessage(), false);
            } finally {
                SimpleDbHelper.INSTANCE.close();
            }
        }

        @SuppressWarnings("TryWithIdenticalCatches")
        private String buscarBoleto(String Numboleto) {
            try {
                String urlFinal = URLs.BOLETO + "?NumBoleto=" + Numboleto;
                String retorno = "";
                String resultString = Utilidades.getRegistros(urlFinal, false);
                if (resultString != null && !resultString.equalsIgnoreCase("null")) {
                    int nMode = 0;
                    int nPaperWidth = 384;
                    Bitmap bImagem = Utilidades.decodeBase64(resultString);
                    if (bImagem != null) {
                        byte[] data = PrintPicture.POS_PrintBMP(bImagem, nPaperWidth, nMode);
                        mPrint.sendDataByte(data);
                        mPrint.sendDataByte(Command.ESC_Init);
                    } else
                        retorno = "Boleto não encontrado";
                } else
                    retorno = "Boleto não encontrado";

                return retorno;
            } catch (Exception ex) {
                return "Erro: " + ex.getMessage();
            }
        }
    }
}