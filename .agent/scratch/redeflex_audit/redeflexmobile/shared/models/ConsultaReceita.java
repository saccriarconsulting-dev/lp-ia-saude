package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by joao.viana on 31/05/2017.
 */

public class ConsultaReceita {

    private String Cnae;
    private String RazaoSocial;
    private String Fantasia;
    private String NomeSocio;
    private String Bairro;
    private String Logradouro;
    private String Numero;
    private String Cidade;
    private Date InicioAtividade;
    private String Cnpj;
    private String Cep;
    private String CapitalSocial;
    private String Email;
    private String Telefone;
    private String Celular;
    private String Complemento;
    @SerializedName(value = "MCC", alternate = {"mcc"}) private String mcc;
    @SerializedName(value = "UF", alternate = {"uf"}) private String uf;
    private String CodigoSgv;
    private String Situacao;
    public String getCnae() {
        return Cnae;
    }

    public void setCnae(String cnae) {
        Cnae = cnae;
    }

    public String getRazaoSocial() {
        return RazaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        RazaoSocial = razaoSocial;
    }

    public String getFantasia() {
        return Fantasia;
    }

    public void setFantasia(String fantasia) {
        Fantasia = fantasia;
    }

    public String getNomeSocio() {
        return NomeSocio;
    }

    public void setNomeSocio(String nomeSocio) {
        NomeSocio = nomeSocio;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getLogradouro() {
        return Logradouro;
    }

    public void setLogradouro(String logradouro) {
        Logradouro = logradouro;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public Date getInicioAtividade() {
        return InicioAtividade;
    }

    public void setInicioAtividade(Date inicioAtividade) {
        InicioAtividade = inicioAtividade;
    }

    public String getCnpj() {
        return Cnpj;
    }

    public void setCnpj(String cnpj) {
        Cnpj = cnpj;
    }

    public String getCep() {
        return Cep;
    }

    public void setCep(String cep) {
        Cep = cep;
    }

    public String getCapitalSocial() {
        return CapitalSocial;
    }

    public void setCapitalSocial(String capitalSocial) {
        CapitalSocial = capitalSocial;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String complemento) {
        Complemento = complemento;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCodigoSgv() {
        return CodigoSgv;
    }

    public void setCodigoSgv(String codigoSgv) {
        CodigoSgv = codigoSgv;
    }
    
    public String getSituacao() {
        return Situacao;
    }

    public void setSituacao(String situacao) {
        Situacao = situacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsultaReceita that = (ConsultaReceita) o;

        if (Cnae != null ? !Cnae.equals(that.Cnae) : that.Cnae != null) return false;
        if (RazaoSocial != null ? !RazaoSocial.equals(that.RazaoSocial) : that.RazaoSocial != null)
            return false;
        if (Fantasia != null ? !Fantasia.equals(that.Fantasia) : that.Fantasia != null)
            return false;
        if (NomeSocio != null ? !NomeSocio.equals(that.NomeSocio) : that.NomeSocio != null)
            return false;
        if (Bairro != null ? !Bairro.equals(that.Bairro) : that.Bairro != null) return false;
        if (Logradouro != null ? !Logradouro.equals(that.Logradouro) : that.Logradouro != null)
            return false;
        if (Numero != null ? !Numero.equals(that.Numero) : that.Numero != null) return false;
        if (Cidade != null ? !Cidade.equals(that.Cidade) : that.Cidade != null) return false;
        if (InicioAtividade != null ? !InicioAtividade.equals(that.InicioAtividade) : that.InicioAtividade != null)
            return false;
        if (Cnpj != null ? !Cnpj.equals(that.Cnpj) : that.Cnpj != null) return false;
        if (Cep != null ? !Cep.equals(that.Cep) : that.Cep != null) return false;
        if (CapitalSocial != null ? !CapitalSocial.equals(that.CapitalSocial) : that.CapitalSocial != null)
            return false;
        if (Email != null ? !Email.equals(that.Email) : that.Email != null) return false;
        if (Telefone != null ? !Telefone.equals(that.Telefone) : that.Telefone != null)
            return false;
        if (Celular != null ? !Celular.equals(that.Celular) : that.Celular != null) return false;
        if (Complemento != null ? !Complemento.equals(that.Complemento) : that.Complemento != null)
            return false;
        if (mcc != null ? !mcc.equals(that.mcc) : that.mcc != null) return false;
        return uf != null ? uf.equals(that.uf) : that.uf == null;
    }

    @Override
    public int hashCode() {
        int result = Cnae != null ? Cnae.hashCode() : 0;
        result = 31 * result + (RazaoSocial != null ? RazaoSocial.hashCode() : 0);
        result = 31 * result + (Fantasia != null ? Fantasia.hashCode() : 0);
        result = 31 * result + (NomeSocio != null ? NomeSocio.hashCode() : 0);
        result = 31 * result + (Bairro != null ? Bairro.hashCode() : 0);
        result = 31 * result + (Logradouro != null ? Logradouro.hashCode() : 0);
        result = 31 * result + (Numero != null ? Numero.hashCode() : 0);
        result = 31 * result + (Cidade != null ? Cidade.hashCode() : 0);
        result = 31 * result + (InicioAtividade != null ? InicioAtividade.hashCode() : 0);
        result = 31 * result + (Cnpj != null ? Cnpj.hashCode() : 0);
        result = 31 * result + (Cep != null ? Cep.hashCode() : 0);
        result = 31 * result + (CapitalSocial != null ? CapitalSocial.hashCode() : 0);
        result = 31 * result + (Email != null ? Email.hashCode() : 0);
        result = 31 * result + (Telefone != null ? Telefone.hashCode() : 0);
        result = 31 * result + (Celular != null ? Celular.hashCode() : 0);
        result = 31 * result + (Complemento != null ? Complemento.hashCode() : 0);
        result = 31 * result + (mcc != null ? mcc.hashCode() : 0);
        result = 31 * result + (uf != null ? uf.hashCode() : 0);
        return result;
    }
}