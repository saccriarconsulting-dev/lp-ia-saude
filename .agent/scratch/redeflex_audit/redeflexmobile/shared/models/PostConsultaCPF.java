package com.axys.redeflexmobile.shared.models;

public class PostConsultaCPF {
    private String CpfCnpj;
    private String birth_date;

    public PostConsultaCPF() {
    }

    public PostConsultaCPF(String cpfCnpj, String birth_date) {
        CpfCnpj = cpfCnpj;
        this.birth_date = birth_date;
    }

    public String getCpfCnpj() {
        return CpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        CpfCnpj = cpfCnpj;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}
