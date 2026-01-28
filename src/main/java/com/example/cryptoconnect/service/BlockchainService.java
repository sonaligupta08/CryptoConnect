package com.example.cryptoconnect.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Service
public class BlockchainService {

    private static final String NODE_URL = "http://127.0.0.1:7545";

    private static final String CONTRACT_ADDRESS =
        "0xd9145CCE52D386f254917e481eB44e9943F39138";

    private Web3j web3j;

    @PostConstruct
    public void init() {
        this.web3j = Web3j.build(new HttpService(NODE_URL));
        System.out.println("Blockchain connected");
    }

    // Temporary method
    public String savePostHashToBlockchain(String postHash) {
        System.out.println("Post hash stored on blockchain: " + postHash);
        return postHash;
    }
}