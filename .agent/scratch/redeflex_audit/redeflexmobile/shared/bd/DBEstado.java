package com.axys.redeflexmobile.shared.bd;

import android.content.Context;

import com.axys.redeflexmobile.shared.models.Estado;

import java.util.ArrayList;

/**
 * Created by joao.viana on 31/08/2016.
 */
public class DBEstado {
    public DBEstado(Context pContext) {

    }

    public ArrayList<Estado> getEstado() {
        ArrayList<Estado> lista = new ArrayList<>();
        try {
            Estado estado = new Estado();
            estado.setId("1");
            estado.setSigla("AC");
            estado.setDescricao("Acre");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("2");
            estado.setSigla("AL");
            estado.setDescricao("Alagoas");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("3");
            estado.setSigla("AM");
            estado.setDescricao("Amazonas");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("4");
            estado.setSigla("AP");
            estado.setDescricao("Amapá");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("5");
            estado.setSigla("BA");
            estado.setDescricao("Bahia");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("6");
            estado.setSigla("CE");
            estado.setDescricao("Ceará");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("7");
            estado.setSigla("DF");
            estado.setDescricao("Distrito Federal");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("8");
            estado.setSigla("ES");
            estado.setDescricao("Espirito Santo");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("9");
            estado.setSigla("GO");
            estado.setDescricao("Goiás");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("10");
            estado.setSigla("MA");
            estado.setDescricao("Maranhão");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("11");
            estado.setSigla("MG");
            estado.setDescricao("Minas Gerais");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("12");
            estado.setSigla("MS");
            estado.setDescricao("Mato Grosso do Sul");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("13");
            estado.setSigla("MT");
            estado.setDescricao("Mato Grosso");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("14");
            estado.setSigla("PA");
            estado.setDescricao("Pará");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("15");
            estado.setSigla("PB");
            estado.setDescricao("Paraíba");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("16");
            estado.setSigla("PE");
            estado.setDescricao("Pernambuco");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("17");
            estado.setSigla("PI");
            estado.setDescricao("Piauí");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("18");
            estado.setSigla("PR");
            estado.setDescricao("Paraná");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("19");
            estado.setSigla("RJ");
            estado.setDescricao("Rio de Janeiro");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("20");
            estado.setSigla("RN");
            estado.setDescricao("Rio Grande do Norte");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("21");
            estado.setSigla("RO");
            estado.setDescricao("Rondônia");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("22");
            estado.setSigla("RR");
            estado.setDescricao("Roraima");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("23");
            estado.setSigla("RS");
            estado.setDescricao("Rio Grande do Sul");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("24");
            estado.setSigla("SC");
            estado.setDescricao("Santa Catarina");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("25");
            estado.setSigla("SE");
            estado.setDescricao("Sergipe");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("26");
            estado.setSigla("SP");
            estado.setDescricao("São Paulo");
            estado.setSituacao("A");
            lista.add(estado);

            estado = new Estado();
            estado.setId("27");
            estado.setSigla("TO");
            estado.setDescricao("Tocantins");
            estado.setSituacao("A");
            lista.add(estado);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public Estado getEstadobySigla(String sigla) {
        ArrayList<Estado> listEstado = getEstado();
        if (listEstado != null && listEstado.size() > 0) {
            for (Estado item : listEstado) {
                if (item.getSigla().equalsIgnoreCase(sigla))
                    return item;
            }
        }

        return null;
    }
}