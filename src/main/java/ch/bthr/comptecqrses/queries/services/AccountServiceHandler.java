package ch.bthr.comptecqrses.queries.services;

import ch.bthr.comptecqrses.commonapi.enums.OperationType;
import ch.bthr.comptecqrses.commonapi.events.AccountActivatedEvent;
import ch.bthr.comptecqrses.commonapi.events.AccountCreatedEvent;
import ch.bthr.comptecqrses.commonapi.events.AccountCreditedEvent;
import ch.bthr.comptecqrses.commonapi.events.AccountDebitedEvent;
import ch.bthr.comptecqrses.commonapi.queries.GetAccountQueryById;
import ch.bthr.comptecqrses.commonapi.queries.GetAllAccountQuery;
import ch.bthr.comptecqrses.queries.entities.Account;
import ch.bthr.comptecqrses.queries.entities.Operation;
import ch.bthr.comptecqrses.queries.repositories.AccountRepository;
import ch.bthr.comptecqrses.queries.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent accountCreatedEvent){
        // à chaque fois que cet evenement est émis
        log.info("***************************");
        log.info("Account created event received");
        Account account =new Account();
        account.setId(accountCreatedEvent.getId());
        account.setBalance(accountCreatedEvent.getInitialBalance());
        account.setStatus(accountCreatedEvent.getStatus());
        account.setCurrency(accountCreatedEvent.getCurrency() );

        accountRepository.save(account) ;
    }


    @EventHandler
    public void on(AccountActivatedEvent event){
        // à chaque fois que cet evenement est émis
        log.info("***************************");
        log.info("Account activated event received");
        Account account=accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());

        accountRepository.save(account) ;
    }

    @EventHandler
    public void on(AccountDebitedEvent event){
        // à chaque fois que cet evenement est émis
        log.info("***************************");
        log.info("Account Debited event received");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation =new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());// normalement la date doit parvenir de la partie ecriture
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepository.save(operation);

        account.setBalance(account.getBalance()-event.getAmount());

        accountRepository.save(account) ;
    }

    @EventHandler
    public void on(AccountCreditedEvent event){
        // à chaque fois que cet evenement est émis
        log.info("***************************");
        log.info("Account Credited event received");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation =new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());// normalement la date doit parvenir de la partie ecriture
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepository.save(operation);

        account.setBalance(account.getBalance() + event.getAmount());

        accountRepository.save(account) ;
    }

    @QueryHandler
    public List<Account> on(GetAllAccountQuery query){
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountQueryById query){
        return accountRepository.findById(query.getId()).get();
    }
}
