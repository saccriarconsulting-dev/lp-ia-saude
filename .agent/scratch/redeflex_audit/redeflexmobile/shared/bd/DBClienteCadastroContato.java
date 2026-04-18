package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContato;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBClienteCadastroContato {
    private Context context;

    public DBClienteCadastroContato(Context context) {
        this.context = context;
    }

    public void addDBClienteCadastroContato(CustomerRegisterContato pCustomerRegisterContato) {
        try {
            ContentValues values = new ContentValues();
            values.put("idCadastro", pCustomerRegisterContato.getIdCadastro());
            values.put("nome", pCustomerRegisterContato.getNome());
            values.put("telefone", pCustomerRegisterContato.getTelefone());
            values.put("celular", pCustomerRegisterContato.getCelular());
            SimpleDbHelper.INSTANCE.open(context).insert("ClienteCadastroContatos", null, values);
        }
        catch (Exception ex)
        {
            Log.d("Roni", "addDBClienteCadastroContato: " + ex.getMessage());
        }
    }

    public CustomerRegisterContato getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getCustomerRegisterContato(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public ArrayList<CustomerRegisterContato> getByIdCadastro(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [idCadastro] = ?");
        return getCustomerRegisterContato(sb.toString(), new String[]{String.valueOf(pId)});
    }

    private ArrayList<CustomerRegisterContato> getCustomerRegisterContato(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, idCadastro, nome, telefone, celular");
        sb.appendLine("FROM ClienteCadastroContatos ");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<CustomerRegisterContato> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos);
            CustomerRegisterContato customerRegisterContato;
            if (cursor.moveToFirst()) {
                do {
                    customerRegisterContato = new CustomerRegisterContato();
                    customerRegisterContato.setId(cursor.getInt(0));
                    customerRegisterContato.setIdCadastro(cursor.getInt(1));
                    customerRegisterContato.setNome(cursor.getString(2));
                    customerRegisterContato.setTelefone(cursor.getString(3));
                    customerRegisterContato.setCelular(cursor.getString(4));
                    lista.add(customerRegisterContato);
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

    public void deleteById(int pId) {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroContatos", "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteByIdCadastro(int pIdCadastro) {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroContatos", "[idCadastro]=?", new String[]{String.valueOf(pIdCadastro)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroContatos", null, null);
    }
}
