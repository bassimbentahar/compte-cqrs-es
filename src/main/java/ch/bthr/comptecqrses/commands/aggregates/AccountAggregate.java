package ch.bthr.comptecqrses.commands.aggregates;

import ch.bthr.comptecqrses.commonapi.commands.CreateAccountCommand;
import ch.bthr.comptecqrses.commonapi.commands.CreditAccountCommand;
import ch.bthr.comptecqrses.commonapi.commands.DebitAccountCommand;
import ch.bthr.comptecqrses.commonapi.enums.AccountStatus;
import ch.bthr.comptecqrses.commonapi.exceptions.AmmountNegativeException;
import ch.bthr.comptecqrses.commonapi.exceptions.BalanceNotSufficientException;
import ch.bthr.comptecqrses.commonapi.events.AccountActivatedEvent;
import ch.bthr.comptecqrses.commonapi.events.AccountCreatedEvent;
import ch.bthr.comptecqrses.commonapi.events.AccountCreditedEvent;
import ch.bthr.comptecqrses.commonapi.events.AccountDebitedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier// grace à cette annotation il va savoir
    private String accountId;
    private double balance;
    private  String currency;
    private AccountStatus status;

    public AccountAggregate() {
        // required by AXON , quand il restitue l'aggregat à partir de even stor il utilise ce constructeur
    }

    @CommandHandler//on fait un subscribe sur le bus de commande, dès qu'il y'a une
    // commande de type CreateAccountCommand qui arrive,
    // AXON va instancier cet aggregat et executer la logique métier
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if(createAccountCommand.getInitialBalance()<0)throw new RuntimeException("impossible de créer le compte avec un solde négatif");
        //OK
        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED
                // c'est axon qui va se charger de persister
                ));
        // c'est la partie métier de la création de compte
        // représente le writer

    }

    @EventSourcingHandler//dès que cet evenement va etre emis , cette fonction va s'executer,
    // il va muter l'état de l'application
    public void on(AccountCreatedEvent event){
        // ici pas de logique métier , ici l'état de l'application va changer (l'état représenté par l'aggregat)
        // l'event est produit , il est dans l'event store
        this.accountId=event.getId();
        this.balance= event.getInitialBalance();
        this.currency=event.getCurrency();
        this.status= AccountStatus.CREATED;

        // j'émis un autre event pour activer le compte
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
                // il faut muter l'état de l'application encore
        ));
    }



    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        this.status=event.getStatus();
    }

    // handel
    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand){
        // logique métier
        if(creditAccountCommand.getAmount()<0) throw new AmmountNegativeException("Amount should not be negatif");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getAmount(),
                creditAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent accountCreditedEvent){
        this.balance+= accountCreditedEvent.getAmount();

    }

    @CommandHandler
    public void handle(DebitAccountCommand debitAccountCommand){
        // logique métier
        if(debitAccountCommand.getAmount()<0) throw new AmmountNegativeException("Amount should not be negatif");
        if(this.balance<debitAccountCommand.getAmount())throw new BalanceNotSufficientException("Balance not suficient Exception");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getAmount(),
                debitAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent accountDebitedEvent){
        this.balance -= accountDebitedEvent.getAmount();

    }


}
