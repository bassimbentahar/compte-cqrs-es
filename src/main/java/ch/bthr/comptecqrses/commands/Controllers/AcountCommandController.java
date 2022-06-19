package ch.bthr.comptecqrses.commands.Controllers;


import ch.bthr.comptecqrses.commonapi.commands.CreateAccountCommand;
import ch.bthr.comptecqrses.commonapi.commands.CreditAccountCommand;
import ch.bthr.comptecqrses.commonapi.commands.DebitAccountCommand;
import ch.bthr.comptecqrses.commonapi.dtos.CreateAccountRequestDTO;
import ch.bthr.comptecqrses.commonapi.dtos.CreditAccountRequestDTO;
import ch.bthr.comptecqrses.commonapi.dtos.DebitAccountRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/commands/account")
public class AcountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;

    public AcountCommandController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping(path = "/create")
    public CompletableFuture<String>  createAccountDto( @RequestBody  CreateAccountRequestDTO createAccountDTO){

        CompletableFuture<String> commandResponse = commandGateway.send(
                new CreateAccountCommand(
                        UUID.randomUUID().toString(),
                        createAccountDTO.getInitialBalnce(),
                        createAccountDTO.getCurrency()
                )
        );

        return commandResponse;
    }

    @PutMapping(path = "/credit")
    public CompletableFuture<String>  creditAccountDto( @RequestBody CreditAccountRequestDTO creditAccountDTO){

        CompletableFuture<String> commandResponse = commandGateway.send(
                new CreditAccountCommand(
                        creditAccountDTO.getId(),
                        creditAccountDTO.getAmount(),
                        creditAccountDTO.getCurrency()
                )
        );

        return commandResponse;
    }

    @PutMapping(path = "/debit")
    public CompletableFuture<String>  debitAccountDto( @RequestBody DebitAccountRequestDTO debitAccountDTO){

        CompletableFuture<String> commandResponse = commandGateway.send(
                new DebitAccountCommand(
                        debitAccountDTO.getId(),
                        debitAccountDTO.getAmount(),
                        debitAccountDTO.getCurrency()
                )
        );

        return commandResponse;
    }
    @GetMapping(path = "/eventStore/{id}")
    public Stream eventStore(@PathVariable String id){
        return eventStore.readEvents(id).asStream();
    }
    // à chaque fois que j'ai une erreur je la capte
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exeptionHandler(Exception e){
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
