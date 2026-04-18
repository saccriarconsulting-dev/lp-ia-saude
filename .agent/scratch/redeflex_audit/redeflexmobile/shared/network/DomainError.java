package com.axys.redeflexmobile.shared.network;

import androidx.annotation.NonNull;

import java.util.Objects;

public final class DomainError {

    private final int code;
    @NonNull private final String message;

    private DomainError(int code, @NonNull String message) {
        this.code = code;
        this.message = message;
    }

    /** HTTP 401 – Unauthorized (default). */
    @NonNull
    public static DomainError unauthorized() {
        return new DomainError(401, "Não autorizado");
    }

    /** HTTP 422 – Dados inválidos (genérico). */
    @NonNull
    public static DomainError invalidData() {
        return new DomainError(422, "Dados inválidos");
    }

    /** HTTP 422 – Dados inválidos com mensagem personalizada do servidor. */
    @NonNull
    public static DomainError invalidData(@NonNull String message) {
        return new DomainError(422, message);
    }

    /** HTTP 500 – Erro interno do servidor. */
    @NonNull
    public static DomainError serverError() {
        return new DomainError(500, "Erro interno do servidor");
    }

    /** HTTP 422 – Cadastro em análise (usuário em fila). */
    @NonNull
    public static DomainError userInQueue() {
        return new DomainError(422, "Cadastro em análise");
    }

    @NonNull
    public static DomainError httpError(int httpCode, @NonNull String message) {
        return new DomainError(httpCode, message);
    }

    @NonNull
    public static DomainError httpError(int httpCode) {
        return httpError(httpCode, "Erro HTTP: " + httpCode);
    }

    /** Sem conexão com a internet. */
    @NonNull
    public static DomainError noInternet() {
        return new DomainError(-1, "Sem conexão com a internet");
    }

    /** Tempo de conexão esgotado. */
    @NonNull
    public static DomainError timeout() {
        return new DomainError(-2, "Tempo de conexão esgotado");
    }

    /** Erro no processamento de dados (parsing). */
    @NonNull
    public static DomainError parseError() {
        return new DomainError(-4, "Erro no processamento de dados");
    }

    /** Erro genérico ou não categorizado. */
    @NonNull
    public static DomainError unknown() {
        return new DomainError(-3, "Erro desconhecido");
    }

    /** Retorna o código HTTP ou customizado. */
    public int getCode() {
        return code;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @Override
    @NonNull
    public String toString() {
        return "DomainError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainError)) return false;
        DomainError that = (DomainError) o;
        return code == that.code &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }
}
