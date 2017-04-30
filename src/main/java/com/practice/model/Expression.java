package com.practice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Expression implements Serializable{

    private LocalDateTime dateTime;

    private BigDecimal firstOperand;

    private BigDecimal secondOperand;

    private Operation operation;

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

    public BigDecimal getSecondOperand() {
        return secondOperand;
    }

    public Operation getOperation() {
        return operation;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return dateTime +
                " : " + firstOperand +
                " " + operation +
                " " + secondOperand +
                " = " + result;
    }
}
