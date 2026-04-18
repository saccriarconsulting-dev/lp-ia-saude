package com.axys.redeflexmobile.shared.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Wrapper genérico para resultados de operações de rede.
 * @param <T> tipo de dado retornado em caso de sucesso.
 */
public final class Result<T> {

    @Nullable
    private final T data;

    @Nullable
    private final DomainError error;

    private Result(@Nullable T data, @Nullable DomainError error) {
        this.data = data;
        this.error = error;
    }

    /**
     * Cria um Result de sucesso com o dado fornecido.
     */
    @NonNull
    public static <T> Result<T> success(@Nullable T data) {
        return new Result<>(data, null);
    }

    /**
     * Cria um Result de erro com o DomainError fornecido.
     */
    @NonNull
    public static <T> Result<T> error(@NonNull DomainError error) {
        return new Result<>(null, error);
    }

    /**
     * Verifica se o resultado representa sucesso.
     */
    public boolean isSuccess() {
        return error == null;
    }

    /**
     * Verifica se o resultado representa erro.
     */
    public boolean isError() {
        return error != null;
    }

    /**
     * Retorna o dado em caso de sucesso, ou null.
     */
    @Nullable
    public T getData() {
        return data;
    }

    /**
     * Retorna o DomainError em caso de erro, ou null.
     */
    @Nullable
    public DomainError getError() {
        return error;
    }

    @NonNull
    @Override
    public String toString() {
        if (isSuccess()) {
            return "Result{success, data=" + data + '}';
        } else {
            return "Result{error, code=" + error.getCode()
                    + ", message='" + error.getMessage() + "'}";
        }
    }
}
