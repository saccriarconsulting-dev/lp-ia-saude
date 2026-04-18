package com.axys.redeflexmobile.shared.models.customerregister;

import com.google.gson.annotations.SerializedName;

public class CustomerRegisterContatoPrincipal {
    @SerializedName("IdAppMobileId") private Integer id;
    @SerializedName("idCadastro") private Integer idCadastro;
    @SerializedName("nome") private String nome;
    @SerializedName("email") private String email;
    @SerializedName("telefone") private String telefone;
    @SerializedName("celular") private String celular;

    public CustomerRegisterContatoPrincipal() {

    }

    public CustomerRegisterContatoPrincipal(CustomerRegisterContatoPrincipal customerRegisterContatoPrincipal) {
        this.id = customerRegisterContatoPrincipal.getId();
        this.idCadastro = customerRegisterContatoPrincipal.getIdCadastro();
        this.nome = customerRegisterContatoPrincipal.getNome();
        this.email = customerRegisterContatoPrincipal.getEmail();
        this.telefone = customerRegisterContatoPrincipal.getTelefone();
        this.celular = customerRegisterContatoPrincipal.getCelular();
    }

    public CustomerRegisterContatoPrincipal(Integer id, Integer idCadastro, String nome, String email, String telefone, String celular) {
        this.id = id;
        this.idCadastro = idCadastro;
        this.nome = nome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
