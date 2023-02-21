package io.ingestor;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;

import java.net.URI;

public class opensearchConsumer {

    public static RestHighLevelClient createOpenSearchClient() {
        String connString = "http://localhost:9200";

        RestHighLevelClient restHighLevelClient;
        URI connURI = URI.create(connString);
        String userInfo = connURI.getUserInfo();

        if (userInfo) == null) {

            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(connURI.getHost)));


        }


    }

    public static void main(String[] args){


    }
}
