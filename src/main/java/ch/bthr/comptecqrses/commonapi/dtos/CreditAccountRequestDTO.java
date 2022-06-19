package ch.bthr.comptecqrses.commonapi.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreditAccountRequestDTO {
    private String id;
    private double amount;
    private String currency;
}
