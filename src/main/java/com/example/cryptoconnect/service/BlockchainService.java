package com.example.cryptoconnect.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.DefaultGasProvider;

import com.example.cryptoconnect.blockchain.PostRegistry;

@Service
public class BlockchainService {

    private PostRegistry postRegistry;

    @Autowired
    public BlockchainService(
            Web3j web3j,
            Credentials credentials,
            @Value("${blockchain.contract.address}") String contractAddress) {

        this.postRegistry = PostRegistry.load(
            contractAddress,
            web3j,
            credentials,
            new DefaultGasProvider()
        );
    }

    public String savePostHashToBlockchain(String hash) {
        try {
            return postRegistry
                    .storePost(hash)
                    .send()
                    .getTransactionHash();
        } catch (Exception e) {
            System.out.println("Blockchain save failed: " + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> verifyPost(String hash) {
        try {
            var result = postRegistry.verifyPost(hash).send();

            return Map.of(
                "owner", result.component1().getValue(),
                "timestamp", result.component2().getValue().longValue(),
                "verified", true
            );

        } catch (Exception e) {
            return Map.of(
                "verified", false,
                "error", e.getMessage()
            );
        }
    }
}

