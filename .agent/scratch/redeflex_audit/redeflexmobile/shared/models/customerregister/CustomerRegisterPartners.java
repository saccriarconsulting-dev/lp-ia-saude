package com.axys.redeflexmobile.shared.models.customerregister;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CustomerRegisterPartners {
    @SerializedName("IdAppMobileId") private int Id;
    @SerializedName("IdCadastro") private int IdCadastro;
    @SerializedName("Nome") private String Nome;
    @SerializedName("DataNascimento") private Date DataNascimento;
    @SerializedName("CPF") private String CPF;
    @SerializedName("IdProfissao") private Integer IdProfissao;
    @SerializedName("IdRenda") private Integer IdRenda;
    @SerializedName("IdPatrimonio") private Integer IdPatrimonio;
    @SerializedName("Email") private String Email;
    @SerializedName("Telefone") private String Telefone;
    @SerializedName("Celular") private String Celular;
    @SerializedName("TipoSocio") private int TipoSocio;

    public CustomerRegisterPartners() {

    }

    public CustomerRegisterPartners(CustomerRegisterPartners customerRegisterPartners) {
        this.Id = customerRegisterPartners.getId();
        this.IdCadastro= customerRegisterPartners.getIdCadastro();
        this.Nome= customerRegisterPartners.getNome();
        this.DataNascimento= customerRegisterPartners.getDataNascimento();
        this.CPF= customerRegisterPartners.getCPF();
        this.IdProfissao= customerRegisterPartners.getIdProfissao();
        this.IdRenda= customerRegisterPartners.getIdRenda();
        this.IdPatrimonio= customerRegisterPartners.getIdPatrimonio();
        this.Email= customerRegisterPartners.getEmail();
        this.Telefone= customerRegisterPartners.getTelefone();
        this.Celular= customerRegisterPartners.getCelular();
        this.TipoSocio= customerRegisterPartners.getTipoSocio();
    }

    public CustomerRegisterPartners(int id, int idCadastro, String nome, Date dataNascimento, String CPF, Integer idProfissao, Integer idRenda, Integer idPatrimonio, String email, String telefone, String celular, int tipoSocio) {
        this.Id = id;
        this.IdCadastro = idCadastro;
        this.Nome = nome;
        this.DataNascimento = dataNascimento;
        this.CPF = CPF;
        this.IdProfissao = idProfissao;
        this.IdRenda = idRenda;
        this.IdPatrimonio = idPatrimonio;
        this.Email = email;
        this.Telefone = telefone;
        this.Celular = celular;
        this.TipoSocio = tipoSocio;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdCadastro() {
        return IdCadastro;
    }

    public void setIdCadastro(int idCadastro) {
        IdCadastro = idCadastro;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Date getDataNascimento() {
        return DataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        DataNascimento = dataNascimento;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public Integer getIdProfissao() {
        return IdProfissao;
    }

    public void setIdProfissao(Integer idProfissao) {
        IdProfissao = idProfissao;
    }

    public Integer getIdRenda() {
        return IdRenda;
    }

    public void setIdRenda(Integer idRenda) {
        IdRenda = idRenda;
    }

    public Integer getIdPatrimonio() {
        return IdPatrimonio;
    }

    public void setIdPatrimonio(Integer idPatrimonio) {
        IdPatrimonio = idPatrimonio;
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

    public int getTipoSocio() {
        return TipoSocio;
    }

    public void setTipoSocio(int tipoSocio) {
        TipoSocio = tipoSocio;
    }
}
