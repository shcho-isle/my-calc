package com.practice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Expression implements Serializable{

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyy kk:mm:ss");

    private LocalDateTime dateTime;

    private BigDecimal firstOperand;

    private Operation operation;

    private BigDecimal secondOperand;

    private BigDecimal result;

    public Expression() {
        operation = Operation.PLUS;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public BigDecimal getFirstOperand() {
        return firstOperand;
    }

    public Operation getOperation() {
        return operation;
    }

    public BigDecimal getSecondOperand() {
        return secondOperand;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void updateDateTime() {
        dateTime = LocalDateTime.now();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setFirstOperand(BigDecimal firstOperand) {
        this.firstOperand = firstOperand;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setSecondOperand(BigDecimal secondOperand) {
        this.secondOperand = secondOperand;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "<" + dateTime.format(FORMATTER) +
                ">\t" + firstOperand.stripTrailingZeros() +
                " " + operation +
                " " + secondOperand.stripTrailingZeros() +
                " = " + result.stripTrailingZeros();
    }
}
