package com.axys.redeflexmobile.shared.util.exception;

import android.content.Context;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.GenericResponse;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class AppExceptionUtils implements ExceptionUtils {

    private static final String TAG = AppExceptionUtils.class.getSimpleName();
    private static final String DEFAULT_MESSAGE_CODE = "-1";
    private Retrofit retrofit;
    private Context context;

    public AppExceptionUtils(Retrofit retrofit, Context context) {
        this.retrofit = retrofit;
        this.context = context;
    }

    public MessageException getExceptionsMessage(Throwable throwable) {
        MessageException messageException;
        GenericResponse messageWs;

        if (throwable instanceof HttpException) {
            messageWs = converterResponse(((HttpException) throwable).response());
            messageException = new MessageException(String.valueOf(((HttpException) throwable).code()), messageWs.getMessage(), messageWs.getTitle());
        } else if (throwable instanceof IOException) {
            messageException = new MessageException(DEFAULT_MESSAGE_CODE, R.string.exception_utils_error_no_network);
        } else if (throwable instanceof RealmOwnException) {
            messageException = new MessageException(DEFAULT_MESSAGE_CODE, ((RealmOwnException) throwable).getStringId());
        } else if (throwable instanceof NullPointerException) {
            messageException = new MessageException(DEFAULT_MESSAGE_CODE, throwable.getMessage());
        } else if (throwable instanceof IllegalAccessException) {
            messageException = new MessageException(DEFAULT_MESSAGE_CODE, throwable.getMessage());
        } else {
            messageException = new MessageException(DEFAULT_MESSAGE_CODE, R.string.exception_utils_error_generic);
        }
        return messageException;
    }

    private GenericResponse converterResponse(Response<?> response) {
        Converter<ResponseBody, GenericResponse> converter =
                retrofit.responseBodyConverter(GenericResponse.class, new Annotation[0]);

        String defaultCode = String.valueOf(response.code());
        try {
            return converter.convert(response.errorBody());
        } catch (IOException | NullPointerException | IllegalStateException | JsonSyntaxException e) {
            Timber.d(e, "%s: converterResponse", TAG);
        }

        return new GenericResponse(defaultCode,
                context.getString(R.string.exception_utils_error_generic));
    }
}
