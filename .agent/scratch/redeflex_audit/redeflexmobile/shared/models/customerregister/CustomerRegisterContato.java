package com.axys.redeflexmobile.shared.models.customerregister;

import com.google.gson.annotations.SerializedName;

public class CustomerRegisterContato {
    @SerializedName("IdAppMobileId") private Integer id;
    @SerializedName("idCadastro") private Integer idCadastro;
    @SerializedName("nome") private String nome;
    @SerializedName("telefone") private String telefone;
    @SerializedName("celular") private String celular;

    public CustomerRegisterContato() {

    }

    public CustomerRegisterContato(CustomerRegisterContato customerRegisterContato) {
        this.id = customerRegisterContato.getId();
        this.idCadastro = customerRegisterContato.getIdCadastro();
        this.nome = customerRegisterContato.getNome();
        this.telefone = customerRegisterContato.getTelefone();
        this.celular = customerRegisterContato.getCelular();
    }

    public CustomerRegisterContato(Integer id, Integer idCadastro, String nome, String telefone, String celular) {
        this.id = id;
        this.idCadastro = idCadastro;
        this.nome = nome;
        this.telefone = telefone;
        this.celular = celular;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCadastro() {
        return idCadastro;
    }

    public void setIdCadastro(Integer idCadastro) {
        this.idCadastro = idCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
