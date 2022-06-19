package ch.bthr.comptecqrses.commonapi.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public abstract class BaseCommand <T>{
    @TargetAggregateIdentifier// l'id de la commande represente l'identifiant de l'agregat sur lequel on va effectuer la commande(ici le compte)
     @Getter private T id;

    public BaseCommand(T id) {
        this.id = id;
    }
}
