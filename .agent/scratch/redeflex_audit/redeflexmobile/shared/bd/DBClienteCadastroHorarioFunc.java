package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterHorarioFunc;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;

import java.util.ArrayList;

public class DBClienteCadastroHorarioFunc {
    private Context context;

    public DBClienteCadastroHorarioFunc(Context context) {
        this.context = context;
    }

    public void addClienteCadastroHoarioFunc(CustomerRegisterHorarioFunc pCustomerRegisterHorarioFunc) {
        try {
            ContentValues values = new ContentValues();
            values.put("idCadastro", pCustomerRegisterHorarioFunc.getIdCadastro());
            values.put("diaAtendimentoId", pCustomerRegisterHorarioFunc.getDiaAtendimentoId());
            values.put("aberto", pCustomerRegisterHorarioFunc.getAberto());
            values.put("horaInicio", pCustomerRegisterHorarioFunc.getHorarioInicio());
            values.put("horaFim", pCustomerRegisterHorarioFunc.getHorarioFim());
            SimpleDbHelper.INSTANCE.open(context).insert("ClienteCadastroHorarioFuncionamento", null, values);
        }
        catch (Exception ex)
        {
            Log.d("Roni", "addClienteCadastroHoarioFunc: " + ex.getMessage());
        }
    }

    public CustomerRegisterHorarioFunc getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getCustomerRegisterHoarioFunc(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public ArrayList<CustomerRegisterHorarioFunc> getByIdCadastro(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [idCadastro] = ?");
        return getCustomerRegisterHoarioFunc(sb.toString(), new String[]{String.valueOf(pId)});
    }

    private ArrayList<CustomerRegisterHorarioFunc> getCustomerRegisterHoarioFunc(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, idCadastro, diaAtendimentoId, aberto, horaInicio, horaFim");
        sb.appendLine("FROM ClienteCadastroHorarioFuncionamento ");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<CustomerRegisterHorarioFunc> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos);
            CustomerRegisterHorarioFunc customerRegisterHorarioFunc;
            if (cursor.moveToFirst()) {
                do {
                    customerRegisterHorarioFunc = new CustomerRegisterHorarioFunc();
                    customerRegisterHorarioFunc.setId(cursor.getInt(0));
                    customerRegisterHorarioFunc.setIdCadastro(cursor.getInt(1));
                    customerRegisterHorarioFunc.setDiaAtendimentoId(cursor.getInt(2));
                    customerRegisterHorarioFunc.setAberto(cursor.getInt(3));
                    customerRegisterHorarioFunc.setHorarioInicio(cursor.getString(4));
                    customerRegisterHorarioFunc.setHorarioFim(cursor.getString(5));
                    lista.add(customerRegisterHorarioFunc);
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
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroHorarioFuncionamento", "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteByIdCadastro(int pIdCadastro) {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroHorarioFuncionamento", "[idCadastro]=?", new String[]{String.valueOf(pIdCadastro)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroHorarioFuncionamento", null, null);
    }
}
