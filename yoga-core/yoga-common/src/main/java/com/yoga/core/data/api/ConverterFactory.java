package com.yoga.core.data.api;

import io.reactivex.annotations.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jaxb.JaxbConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class ConverterFactory extends Converter.Factory {
    private final Converter.Factory xmlFactory = JaxbConverterFactory.create();
    private final Converter.Factory jsonFactory = GsonConverterFactory.create();
    private final Converter.Factory rawFactory = ScalarsConverterFactory.create();

    public static ConverterFactory create() {
        return new ConverterFactory();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        for (Annotation annotation : methodAnnotations) {
            if (!(annotation instanceof RequestFormat)) continue;
            String value = ((RequestFormat) annotation).value();
            if (RequestFormat.JSON.equals(value)) {
                return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
            } else if (RequestFormat.XML.equals(value)) {
                return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
            } else if (RequestFormat.RAW.equals(value)) {
                return rawFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (!(annotation instanceof ResponseFormat)) continue;
            String value = ((ResponseFormat) annotation).value();
            if (ResponseFormat.JSON.equals(value)) {
                return jsonFactory.responseBodyConverter(type, annotations, retrofit);
            } else if (ResponseFormat.XML.equals(value)) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit);
            } else if (ResponseFormat.RAW.equals(value)) {
                return rawFactory.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return null;
    }
}
