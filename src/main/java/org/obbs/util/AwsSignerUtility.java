package org.obbs.util;

import org.obbs.enums.AwsRegions;
import org.obbs.enums.AwsServices;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.auth.aws.signer.AwsV4HttpSigner;
import software.amazon.awssdk.http.auth.spi.signer.HttpSigner;
import software.amazon.awssdk.http.auth.spi.signer.SignRequest;
import software.amazon.awssdk.http.auth.spi.signer.SignedRequest;

import java.net.URI;
import java.time.Clock;
import java.util.List;
import java.util.Map;

public class AwsSignerUtility {

    // Singleton Instance
    private static AwsSignerUtility instance;

    private final AwsCredentialsProvider awsCredentialsProvider;
    private final String defaultAwsServiceName;
    private final String defaultAwsRegion;

    public AwsSignerUtility(
            AwsCredentialsProvider awsCredentialsProvider,
            String defaultAwsServiceName,
            String defaultAwsRegion
    ) {
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.defaultAwsServiceName = defaultAwsServiceName;
        this.defaultAwsRegion = defaultAwsRegion;
    }

    /*
        Checks environment variables, if none are configured:
        Service defaults to EC2
        Region defaults to us-east-2
    */
    public AwsSignerUtility(AwsCredentialsProvider awsCredentialsProvider) {
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.defaultAwsServiceName =
                System.getenv().getOrDefault("AWS_DEFAULT_SERVICE_NAME", AwsServices.EC2.getServiceCode());
        this.defaultAwsRegion =
                System.getenv().getOrDefault("AWS_DEFAULT_REGION", AwsRegions.US_EAST_2.getRegionCode());
    }

    public static AwsSignerUtility getInstance(
            AwsCredentialsProvider awsCredentialsProvider,
            String defaultAwsServiceName,
            String defaultAwsRegion
    ) {
        if (instance == null) {
            synchronized (AwsSignerUtility.class) {
                instance = new AwsSignerUtility(awsCredentialsProvider, defaultAwsServiceName, defaultAwsRegion);
            }
        }
        return instance;
    }

    public static AwsSignerUtility getInstance(AwsCredentialsProvider awsCredentialsProvider) {
        if (instance == null) {
            synchronized (AwsSignerUtility.class) {
                instance = new AwsSignerUtility(awsCredentialsProvider);
            }
        }
        return instance;
    }

    public Map<String, List<String>> signRequest(
            String httpMethod,
            URI uri,
            Map<String, String> httpHeaders
    ) {

        SdkHttpFullRequest fullRequest = createSdkFullRequest(httpMethod, uri, httpHeaders);
        SignRequest signRequest = SignRequest
                .builder(awsCredentialsProvider.resolveCredentials())
                .request(fullRequest)
                .putProperty(HttpSigner.SIGNING_CLOCK, Clock.systemUTC())
                .putProperty(AwsV4HttpSigner.SERVICE_SIGNING_NAME, defaultAwsServiceName)
                .putProperty(AwsV4HttpSigner.REGION_NAME, defaultAwsRegion)
                .build();

        AwsV4HttpSigner signer = AwsV4HttpSigner.create();
        SignedRequest signedRequest = signer.sign(signRequest);

        return signedRequest.request().headers();
    }

    private SdkHttpFullRequest createSdkFullRequest(String httpMethod, URI uri, Map<String, String> httpHeaders) {

        SdkHttpMethod sdkHttpMethod = SdkHttpMethod.fromValue(httpMethod);

        SdkHttpFullRequest.Builder fullRequestBuilder = SdkHttpFullRequest
                .builder()
                .method(sdkHttpMethod)
                .uri(uri);

        httpHeaders.forEach((httpHeaderKey, httpHeaderValue) -> {
            fullRequestBuilder.appendHeader(httpHeaderKey, httpHeaderValue);
        });

        return fullRequestBuilder.build();
    }

}
