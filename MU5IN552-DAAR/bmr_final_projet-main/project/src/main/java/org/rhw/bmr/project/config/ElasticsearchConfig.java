package org.rhw.bmr.project.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.username}")
    String username;

    @Value("${elasticsearch.password}")
    String password;

    @Value("${elasticsearch.host}")
    String host;

    @Value("${elasticsearch.port}")
    int port;

    @Value("${elasticsearch.protocol}")
    String protocol;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        try {

            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            TrustStrategy trustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(trustStrategy)
                    .build();


            RestClientBuilder builder = RestClient.builder(
                    new HttpHost(host, port, protocol)
            ).setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier((hostname, session) -> true)
                    .setDefaultIOReactorConfig(IOReactorConfig.custom()
                            .setIoThreadCount(4)
                            .build())
                    .setMaxConnTotal(100)
                    .setMaxConnPerRoute(100)
            ).setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                    .setConnectTimeout(10000)
                    .setSocketTimeout(30000)
            );

            // 创建 ElasticsearchClient
            RestClient restClient = builder.build();
            RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            return new ElasticsearchClient(transport);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Elasticsearch client", e);
        }
    }
}
