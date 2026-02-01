package com.example.cryptoconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;


@Configuration
public class BlockchainConfig {

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService("http://127.0.0.1:7545"));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(
            "41b8e0b2af771c520808eb46c04ce27bb3f2bfd6766c74060f61530ebd8d1359"
        );
    }
}

