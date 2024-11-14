package org.obbs.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.obbs.util.AwsSignerUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwsOkHttpClientInterceptor implements Interceptor {

    private final AwsSignerUtility awsSignerUtility;

    public AwsOkHttpClientInterceptor(AwsSignerUtility awsSignerUtility) {
        this.awsSignerUtility = awsSignerUtility;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request originalRequest = chain.request();
        Map<String, String> headers = mapHeaders(originalRequest);

        Map<String, List<String>> signedHeaders = awsSignerUtility.signRequest(
                originalRequest.method(),
                originalRequest.url().uri(),
                headers
        );

        Request.Builder requestBuilder = originalRequest.newBuilder();
        signedHeaders.forEach((key, values) -> {
            requestBuilder.removeHeader(key);
            values.forEach(value -> requestBuilder.addHeader(key, value));
        });

        return chain.proceed(requestBuilder.build());
    }

    private Map<String, String> mapHeaders(Request httpRequest) {

        Map<String, String> headers = new HashMap<>();
        for (String name: httpRequest.headers().names()) {
            headers.put(name, httpRequest.header(name));
        }

        return headers;
    }

}
