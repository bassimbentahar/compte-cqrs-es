package ch.bthr.comptecqrses.commonapi.commands;


import lombok.Getter;

public class CreateAccountCommand extends BaseCommand<String>{

    @Getter private double initialBalance;// nb pas de setter sinon on viole le pattern
    @Getter private String currency;

    public CreateAccountCommand(String id, double initialBalance, String currency) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
