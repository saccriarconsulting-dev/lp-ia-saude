package com.axys.redeflexmobile.shared.models;

import com.google.gson.annotations.SerializedName;

public class ConsultaPessoaFisica {
    @SerializedName("Data") private Dados RetornoDados;
    @SerializedName("Error") private RetornoErro RetornoErro;

    public Dados getRetornoDados() {
        return RetornoDados;
    }

    public void setRetornoDados(Dados retornoDados) {
        RetornoDados = retornoDados;
    }

    public ConsultaPessoaFisica.RetornoErro getRetornoErro() {
        return RetornoErro;
    }

    public void setRetornoErro(ConsultaPessoaFisica.RetornoErro retornoErro) {
        RetornoErro = retornoErro;
    }

    public class Dados {
        @SerializedName("Nome") private String Nome;
        @SerializedName("CPF") private String CPF;
        @SerializedName("DataNascimento") private String DataNascimento;
        @SerializedName("Idade") private int Idade;
        @SerializedName("Sexo") private String Sexo;
        @SerializedName("NomeMae") private String NomeMae;
        @SerializedName("NomePai") private String NomePai;
        @SerializedName("StatusReceitaFederal") private String StatusReceitaFederal;
        @SerializedName("Obito") private boolean Obito;

        public String getNome() {
            return Nome;
        }

        public void setNome(String nome) {
            Nome = nome;
        }

        public String getCPF() {
            return CPF;
        }

        public void setCPF(String CPF) {
            this.CPF = CPF;
        }

        public String getDataNascimento() {
            return DataNascimento;
        }

        public void setDataNascimento(String dataNascimento) {
            DataNascimento = dataNascimento;
        }

        public int getIdade() {
            return Idade;
        }

        public void setIdade(int idade) {
            Idade = idade;
        }

        public String getSexo() {
            return Sexo;
        }

        public void setSexo(String sexo) {
            Sexo = sexo;
        }

        public String getNomeMae() {
            return NomeMae;
        }

        public void setNomeMae(String nomeMae) {
            NomeMae = nomeMae;
        }

        public String getNomePai() {
            return NomePai;
        }

        public void setNomePai(String nomePai) {
            NomePai = nomePai;
        }

        public String getStatusReceitaFederal() {
            return StatusReceitaFederal;
        }

        public void setStatusReceitaFederal(String statusReceitaFederal) {
            StatusReceitaFederal = statusReceitaFederal;
        }

        public boolean isObito() {
            return Obito;
        }

        public void setObito(boolean obito) {
            Obito = obito;
        }

    }

    public class RetornoErro {
        @SerializedName("StatusApiResponse") private String StatusApiResponse;
        @SerializedName("StatusCode") private String StatusCode;
        @SerializedName("ErroMensagem") private String ErroMensagem;
        @SerializedName("ErroCampo") private String ErroCampo;
        @SerializedName("ErroDescricao") private String ErroDescricao;
        @SerializedName("RequestId") private String RequestId;

        public String getStatusApiResponse() {
            return StatusApiResponse;
        }

        public void setStatusApiResponse(String statusApiResponse) {
            StatusApiResponse = statusApiResponse;
        }

        public String getStatusCode() {
            return StatusCode;
        }

        public void setStatusCode(String statusCode) {
            StatusCode = statusCode;
        }

        public String getErroMensagem() {
            return ErroMensagem;
        }

        public void setErroMensagem(String erroMensagem) {
            ErroMensagem = erroMensagem;
        }

        public String getErroCampo() {
            return ErroCampo;
        }

        public void setErroCampo(String erroCampo) {
            ErroCampo = erroCampo;
        }

        public String getErroDescricao() {
            return ErroDescricao;
        }

        public void setErroDescricao(String erroDescricao) {
            ErroDescricao = erroDescricao;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String requestId) {
            RequestId = requestId;
        }
    }

}
