package com.jpmc.midascore.component;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;


@Component
public class TransactionListener{

    public TransactionListener(){

    }

    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "midas-core"
    )
    public void receiveTransaction(Transaction transaction){
        System.out.println(transaction);
    }

}