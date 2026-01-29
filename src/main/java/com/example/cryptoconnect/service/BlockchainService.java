package com.example.cryptoconnect.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

@Service
public class BlockchainService {

    
    private static final String CONTRACT_ADDRESS =
            "0xd9145CCE52D386f254917e481eB44e9943F39138";

    @Autowired
    private Web3j web3j;

    @PostConstruct
    public void init() {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            System.out.println("Blockchain client connected: " + clientVersion);
        } catch (Exception e) {
            System.out.println("Blockchain connection running in simulated mode");
        }
    }
    public String savePostHashToBlockchain(String postHash) {

        System.out.println("=================================");
        System.out.println("Storing post hash on blockchain");
        System.out.println("Contract Address: " + CONTRACT_ADDRESS);
        System.out.println("Post Hash: " + postHash);
        System.out.println("Status: Stored as blockchain proof (simulated)");
        System.out.println("=================================");

        return postHash;
    }
}
