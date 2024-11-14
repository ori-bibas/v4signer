package org.obbs.interceptors;

import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.obbs.util.AwsSignerUtility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwsApacheHttpClientInterceptor implements HttpRequestInterceptor {

    private final AwsSignerUtility awsSignerUtility;

    public AwsApacheHttpClientInterceptor(AwsSignerUtility awsSignerUtility) {
        this.awsSignerUtility = awsSignerUtility;
    }

    @Override
    public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {

        URI uri = null;
        try {
            uri = request.getUri();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String httpMethod = request.getMethod();
        Map<String, String> headers = mapHeaders(request);

        Map<String, List<String>> signedHeaders =
                awsSignerUtility.signRequest(httpMethod, uri, headers);

        signedHeaders.forEach((key, values) -> {
            request.removeHeaders(key);
            values.forEach(value -> request.addHeader(key, value));
        });

    }

    private Map<String, String> mapHeaders(HttpRequest httpRequest) {

        Map<String, String> headers = new HashMap<>();
        for (Header header : httpRequest.getHeaders()) {
            headers.put(header.getName(), header.getValue());
        }

        return headers;
    }

}
