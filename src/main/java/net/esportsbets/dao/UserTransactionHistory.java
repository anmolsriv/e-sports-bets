package net.esportsbets.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_transaction_history")
public class UserTransactionHistory {

    public enum TransactionType{
        CREDIT, DEBIT;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_credit_id")
    private Long userCreditId;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "bet_id")
    private Integer betId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "time", insertable = false)
    private java.sql.Timestamp time;
}
