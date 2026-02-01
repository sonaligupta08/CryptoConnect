package com.example.cryptoconnect.blockchain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tuples.generated.Tuple2;

public class PostRegistry extends Contract {

    protected PostRegistry(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider gasProvider) {

        super("", contractAddress, web3j, credentials, gasProvider);
    }

    public static PostRegistry load(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider gasProvider) {

        return new PostRegistry(contractAddress, web3j, credentials, gasProvider);
    }

    // WRITE TRANSACTION
    public RemoteFunctionCall<TransactionReceipt> storePost(String hash) {

        final Function function = new Function(
                "storePost",
                Arrays.asList(new Utf8String(hash)),
                Collections.emptyList()
        );

        return executeRemoteCallTransaction(function);
    }

    // READ (VIEW) TRANSACTION
    public RemoteFunctionCall<Tuple2<Address, Uint256>> verifyPost(String hash) {

        final Function function = new Function(
                "verifyPost",
                Arrays.asList(new Utf8String(hash)),
                Arrays.asList(
                        new TypeReference<Address>() {},
                        new TypeReference<Uint256>() {}
                )
        );

        return new RemoteFunctionCall<>(
                function,
                () -> {
                    List<Type> values = executeCallMultipleValueReturn(function);

                    Address owner = (Address) values.get(0);
                    Uint256 timestamp = (Uint256) values.get(1);

                    return new Tuple2<>(owner, timestamp);
                }
        );
    }
}
