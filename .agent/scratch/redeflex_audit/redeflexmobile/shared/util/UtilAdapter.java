package com.axys.redeflexmobile.shared.util;

import android.content.Context;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.adapters.StringAdapter;
import com.axys.redeflexmobile.shared.models.Bancos;
import com.axys.redeflexmobile.shared.models.Cliente;
import com.axys.redeflexmobile.shared.models.Departamento;
import com.axys.redeflexmobile.shared.models.Estado;
import com.axys.redeflexmobile.shared.models.Filial;
import com.axys.redeflexmobile.shared.models.ModeloPOS;
import com.axys.redeflexmobile.shared.models.Preco;
import com.axys.redeflexmobile.shared.models.Produto;
import com.axys.redeflexmobile.shared.models.Segmento;
import com.axys.redeflexmobile.shared.models.TaxasAdquirencia;
import com.axys.redeflexmobile.shared.models.TokenCliente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao.viana on 25/09/2017.
 */

public class UtilAdapter {
    public static StringAdapter adapterFilial(Context mContext, ArrayList<Filial> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (Filial item : lista)
                retorno.add(item.getDescricao());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterDepartamento(Context mContext, ArrayList<Departamento> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (Departamento item : lista)
                retorno.add(item.getDescricao());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterBanco(Context mContext, ArrayList<Bancos> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (Bancos item : lista)
                retorno.add(item.getDescricao());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterPreco(Context mContext, ArrayList<Preco> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            if (lista != null && lista.size() > 0)
                for (Preco item : lista)
                    retorno.add("R$ " + Util_IO.formataValor(item.getValor()));

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterProduto(Context mContext, ArrayList<Produto> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();
            for (Produto item : lista) retorno.add(item.getNome());
            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterIdProduto(Context mContext, ArrayList<Produto> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();
            for (Produto item : lista) retorno.add(String.format("%s - %s", item.getId(), item.getNome()));
            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterTaxasPf(Context context, List<TaxasAdquirencia> taxas) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (TaxasAdquirencia item : taxas) {
                String temp = String.format("%s - %s", item.getMcc(), item.getRamoAtividade());
                retorno.add(temp);
            }

            return new StringAdapter(context, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterTipoAnexo(Context context) {
        try {
            List<String> retorno = new ArrayList<>();
            retorno.add("Merchan Sky");
            retorno.add("Comprovante T. A.");

            return new StringAdapter(context, R.layout.custom_spinner_title_bar, (ArrayList<String>) retorno);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterSegmento(Context mContext, ArrayList<Segmento> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (Segmento item : lista)
                retorno.add(item.getDescricao());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterEstado(Context mContext, ArrayList<Estado> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (Estado item : lista)
                retorno.add(item.getDescricao());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterToken(Context mContext, ArrayList<TokenCliente> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (TokenCliente item : lista)
                retorno.add(item.getToken());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterModeloPOS(Context mContext, ArrayList<ModeloPOS> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (ModeloPOS item : lista)
                retorno.add(item.getDescricao());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static StringAdapter adapterCliente(Context mContext, ArrayList<Cliente> lista) {
        try {
            ArrayList<String> retorno = new ArrayList<>();

            for (Cliente item : lista)
                retorno.add(item.getNomeFantasia());

            return new StringAdapter(mContext, R.layout.custom_spinner_title_bar, retorno);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}