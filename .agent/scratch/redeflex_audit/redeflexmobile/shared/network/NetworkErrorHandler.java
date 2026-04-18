package com.axys.redeflexmobile.shared.network;

import androidx.annotation.NonNull;

import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import okhttp3.internal.http2.StreamResetException;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Converts any Throwable from the network layer into a DomainError
 * for uniform handling in the presentation layer.
 */
public final class NetworkErrorHandler implements Function<Throwable, DomainError> {

    @Inject
    public NetworkErrorHandler() { }

    @Override
    @NonNull
    public DomainError apply(@NonNull Throwable throwable) {
        // 1) HTTP errors via Retrofit (non-2xx)
        if (throwable instanceof retrofit2.HttpException) {
            retrofit2.HttpException httpEx = (retrofit2.HttpException) throwable;
            int code = httpEx.code();
            String defaultMsg = getDefaultMessage(code);
            String backendMsg = null;

            // Tenta extrair "message" do corpo JSON de erro
            try {
                Response<?> resp = httpEx.response();
                if (resp != null && resp.errorBody() != null) {
                    String json = resp.errorBody().string();
                    backendMsg = new JSONObject(json).optString("message", null);
                }
            } catch (Exception e) {
                Timber.w(e, "Failed extracting HTTP %d error message", code);
            }

            String finalMsg = (backendMsg != null && !backendMsg.isEmpty())
                    ? backendMsg
                    : defaultMsg;
            return DomainError.httpError(code, finalMsg);
        }

//        // 2) Authentication failure thrown by interceptor
//        if (throwable instanceof AuthenticationException) {
//            String msg = throwable.getMessage();
//            return DomainError.httpError(
//                    401,
//                    (msg != null && !msg.isEmpty()) ? msg : "Não autorizado"
//            );
//        }

//        // 3) Custom HttpException from our network layer
//        if (throwable instanceof HttpException) {
//            HttpException customEx = (HttpException) throwable;
//            int code = customEx.getStatusCode();
//            String msg = customEx.getMessage();
//            return DomainError.httpError(
//                    code,
//                    (msg != null && !msg.isEmpty()) ? msg : getDefaultMessage(code)
//            );
//        }

        // 4) Network connectivity issues
        if (throwable instanceof UnknownHostException) {
            return DomainError.noInternet();
        }
        if (throwable instanceof SocketTimeoutException || throwable instanceof StreamResetException) {
            return DomainError.timeout();
        }

        // 5) JSON parsing failures
        if (throwable instanceof JsonSyntaxException) {
            return DomainError.parseError();
        }

        // 6) Generic I/O fallback
        if (throwable instanceof IOException) {
            return DomainError.noInternet();
        }

        // 7) Unmapped errors
        Timber.e(throwable, "Unmapped error in NetworkErrorHandler");
        return DomainError.unknown();
    }

    @NonNull
    private String getDefaultMessage(int code) {
        switch (code) {
            case 401:
                return "Não autorizado";
            case 422:
                return "Dados inválidos";
            case 500:
                return "Erro interno do servidor";
            default:
                return "Erro HTTP: " + code;
        }
    }
}
