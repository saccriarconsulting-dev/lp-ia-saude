package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterContatoPrincipal;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBClienteCadastroContatoPrincipal {
    private Context context;

    public DBClienteCadastroContatoPrincipal(Context context) {
        this.context = context;
    }

    public void addDBClienteCadastroContatoPrincipal(CustomerRegisterContatoPrincipal pCustomerRegisterContatoPrincipal) {
        try {
            ContentValues values = new ContentValues();
            values.put("idCadastro", pCustomerRegisterContatoPrincipal.getIdCadastro());
            values.put("nome", pCustomerRegisterContatoPrincipal.getNome());
            values.put("email", pCustomerRegisterContatoPrincipal.getEmail());
            values.put("telefone", pCustomerRegisterContatoPrincipal.getTelefone());
            values.put("celular", pCustomerRegisterContatoPrincipal.getCelular());
            SimpleDbHelper.INSTANCE.open(context).insert("ClienteCadastroContatoPrincipal", null, values);
        }
        catch (Exception ex)
        {
            Log.d("Roni", "addDBClienteCadastroContatoPrincipal: " + ex.getMessage());
        }
    }

    public CustomerRegisterContatoPrincipal getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getCustomerRegisterContatoPrincipal(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public CustomerRegisterContatoPrincipal getByIdCadastro(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [idCadastro] = ?");
        return Utilidades.firstOrDefault(getCustomerRegisterContatoPrincipal(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    private ArrayList<CustomerRegisterContatoPrincipal> getCustomerRegisterContatoPrincipal(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, idCadastro, nome, email, telefone, celular");
        sb.appendLine("FROM ClienteCadastroContatoPrincipal ");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<CustomerRegisterContatoPrincipal> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos);
            CustomerRegisterContatoPrincipal customerRegisterContatoPrincipal;
            if (cursor.moveToFirst()) {
                do {
                    customerRegisterContatoPrincipal = new CustomerRegisterContatoPrincipal();
                    customerRegisterContatoPrincipal.setId(cursor.getInt(0));
                    customerRegisterContatoPrincipal.setIdCadastro(cursor.getInt(1));
                    customerRegisterContatoPrincipal.setNome(cursor.getString(2));
                    customerRegisterContatoPrincipal.setEmail(cursor.getString(3));
                    customerRegisterContatoPrincipal.setTelefone(cursor.getString(4));
                    customerRegisterContatoPrincipal.setCelular(cursor.getString(5));
                    lista.add(customerRegisterContatoPrincipal);
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
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroContatoPrincipal", "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteByIdCadastro(int pIdCadastro) {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroContatoPrincipal", "[idCadastro]=?", new String[]{String.valueOf(pIdCadastro)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroContatoPrincipal", null, null);
    }
}
