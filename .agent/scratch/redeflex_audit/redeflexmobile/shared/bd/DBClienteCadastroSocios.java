package com.axys.redeflexmobile.shared.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterPartners;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.util.ArrayList;

public class DBClienteCadastroSocios {
    private Context context;

    public DBClienteCadastroSocios(Context context) {
        this.context = context;
    }

    public void addClienteCadastroSocios(CustomerRegisterPartners pCustomerRegisterPartners) throws Exception {
        ContentValues values = new ContentValues();
        values.put("idCadastro", pCustomerRegisterPartners.getIdCadastro());
        values.put("nome", pCustomerRegisterPartners.getNome());
        values.put("dataNascimento", Util_IO.dateTimeToString(pCustomerRegisterPartners.getDataNascimento(), Config.FormatDateTimeStringBanco));
        values.put("cpf", pCustomerRegisterPartners.getCPF());
        values.put("idProfissao", pCustomerRegisterPartners.getIdProfissao());
        values.put("idRenda", pCustomerRegisterPartners.getIdRenda());
        values.put("idPatrimonio", pCustomerRegisterPartners.getIdPatrimonio());
        values.put("email", pCustomerRegisterPartners.getEmail());
        values.put("telefone", pCustomerRegisterPartners.getTelefone());
        values.put("celular", pCustomerRegisterPartners.getCelular());
        values.put("tipoSocio", 0);
        SimpleDbHelper.INSTANCE.open(context).insert("ClienteCadastroSocios", null, values);
    }

    public CustomerRegisterPartners getByIdCadastro(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [idCadastro] = ?");
        return Utilidades.firstOrDefault(getCustomerRegisterPartners(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    public CustomerRegisterPartners getById(int pId) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("AND [id] = ?");
        return Utilidades.firstOrDefault(getCustomerRegisterPartners(sb.toString(), new String[]{String.valueOf(pId)}));
    }

    private ArrayList<CustomerRegisterPartners> getCustomerRegisterPartners(String pCondicao, String[] pCampos) {
        Util_IO.StringBuilder sb = new Util_IO.StringBuilder();
        sb.appendLine("SELECT id, idCadastro, nome, dataNascimento, cpf, idProfissao, idRenda, idPatrimonio,");
        sb.appendLine("       email, telefone, celular, tipoSocio ");
        sb.appendLine("FROM ClienteCadastroSocios ");
        sb.appendLine("WHERE 1 = 1 ");
        if (pCondicao != null)
            sb.append(pCondicao);

        Cursor cursor = null;
        ArrayList<CustomerRegisterPartners> lista = new ArrayList<>();
        try {
            cursor = SimpleDbHelper.INSTANCE.open(context).rawQuery(sb.toString(), pCampos);
            CustomerRegisterPartners customerRegisterPartners;
            if (cursor.moveToFirst()) {
                do {
                    customerRegisterPartners = new CustomerRegisterPartners();

                    customerRegisterPartners.setId(cursor.getInt(0));
                    customerRegisterPartners.setIdCadastro(cursor.getInt(1));
                    customerRegisterPartners.setNome(cursor.getString(2));
                    customerRegisterPartners.setDataNascimento(Util_IO.stringToDate(cursor.getString(3), Config.FormatDateStringBanco));
                    customerRegisterPartners.setCPF(cursor.getString(4));
                    customerRegisterPartners.setIdProfissao(cursor.getInt(5));
                    customerRegisterPartners.setIdRenda(cursor.getInt(6));
                    customerRegisterPartners.setIdPatrimonio(cursor.getInt(7));
                    customerRegisterPartners.setEmail(cursor.getString(8));
                    customerRegisterPartners.setTelefone(cursor.getString(9));
                    customerRegisterPartners.setCelular(cursor.getString(10));
                    customerRegisterPartners.setTipoSocio(cursor.getInt(11));
                    lista.add(customerRegisterPartners);
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
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroSocios", "[id]=?", new String[]{String.valueOf(pId)});
    }

    public void deleteByIdCadastro(int pIdCadastro) {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroSocios", "[idCadastro]=?", new String[]{String.valueOf(pIdCadastro)});
    }

    public void deleteAll() {
        SimpleDbHelper.INSTANCE.open(context).delete("ClienteCadastroSocios", null, null);
    }
}
