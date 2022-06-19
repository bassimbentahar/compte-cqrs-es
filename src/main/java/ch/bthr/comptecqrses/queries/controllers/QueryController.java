package ch.bthr.comptecqrses.queries.controllers;

import ch.bthr.comptecqrses.commonapi.queries.GetAccountQueryById;
import ch.bthr.comptecqrses.commonapi.queries.GetAllAccountQuery;
import ch.bthr.comptecqrses.queries.entities.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/query/accounts")
@AllArgsConstructor
@Slf4j
public class QueryController {

    private QueryGateway queryGateway;

    @GetMapping(path = "/allAccounts")
    public List<Account> accountsList(){
        return queryGateway.query(new GetAllAccountQuery(), ResponseTypes.multipleInstancesOf(Account.class)).join();
    }
    @GetMapping(path = "/byId/{id}")
    public Account accountsList(@PathVariable String id){
        return queryGateway.query(new GetAccountQueryById(id), ResponseTypes.instanceOf(Account.class)).join();
    }

}
