package ch.bthr.comptecqrses.commonapi.events;

import ch.bthr.comptecqrses.commonapi.enums.AccountStatus;
import lombok.Getter;

public class AccountCreatedEvent extends BaseEvent<String>{

    @Getter private double initialBalance;
    @Getter private String currency;
    @Getter private AccountStatus status;

    public  AccountCreatedEvent(String id, double initialBalance, String currency, AccountStatus status) {
        super(id);
        this.status = status;
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
