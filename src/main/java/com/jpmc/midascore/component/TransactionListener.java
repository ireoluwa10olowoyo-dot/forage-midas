package com.jpmc.midascore.component;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.Service.TransactionService;
import com.jpmc.midascore.foundation.Transaction;


@Component
public class TransactionListener{
    private TransactionService transactionService;
    public TransactionListener(TransactionService transactionService){
        this .transactionService = transactionService;
    }

    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "midas-core"
    )
    public void receiveTransaction(Transaction transaction){
        transactionService.processTransaction(transaction);
    }

}