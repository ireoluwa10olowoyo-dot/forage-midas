package com.jpmc.midascore.Service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

import jakarta.transaction.Transactional;





@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private static final String INCENTIVE_URL = 
    "http://localhost:8080/incentive";
    private final RestTemplate restTemplate = new RestTemplate();


    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository){
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        
    }

    

    @Transactional
    public void processTransaction(Transaction transaction){
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if(sender == null || recipient == null){
            return;
        }
        if(sender.getBalance()<transaction.getAmount()){
            return;
        }
        
        Incentive incentiveResponse = restTemplate.postForObject(INCENTIVE_URL,transaction,Incentive.class);

        float incentiveAmount = 0;

        if(incentiveResponse !=null){
            incentiveAmount = incentiveResponse.getAmount();
        }

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance()+transaction.getAmount()+incentiveAmount);
        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord transactionRecord = new TransactionRecord(sender,recipient,transaction.getAmount(),incentiveAmount);


        transactionRepository.save(transactionRecord);

        



    }

   
    
}
