//package com.example.mall_study.config;
//
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//
//import java.time.Duration;
//
//@Configuration
//public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
//    @Value("${elasticsearch.node_name}")
//    private String node_name;
//
//    @Bean
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        //标题
////        HttpHeaders defaultHeaders = new HttpHeaders();
////        defaultHeaders.setBasicAuth("zwk","123");
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(node_name)
//                //连接超时
//                .withConnectTimeout(Duration.ofSeconds(5))
//                //套接字超时
//                //.withSocketTimeout(Duration.ofSeconds(3))
//                //启用SSL
//                //.useSsl()
//                //设置标题
////                .withDefaultHeaders(defaultHeaders)
//                //添加基本身份验证
//                //.withBasicAuth(username, password)
//                .build();
//        return RestClients.create(clientConfiguration).rest();
//    }
////    @Bean(name = { "elasticsearchOperations", "elasticsearchRestTemplate" })
////    public ElasticsearchRestTemplate elasticsearchTemplate() {
////        return new ElasticsearchRestTemplate(elasticsearchClient());
////    }
//}
