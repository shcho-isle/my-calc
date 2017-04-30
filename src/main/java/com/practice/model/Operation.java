package com.practice.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Operation {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private static final int SCALE = 20;

    private String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal calculate(BigDecimal firstOperand, BigDecimal secondOperand) {
        BigDecimal result;

        switch (this) {
            case PLUS:
                result = firstOperand.add(secondOperand);
                break;
            case MINUS:
                result = firstOperand.subtract(secondOperand);
                break;
            case MULTIPLY:
                result = firstOperand.multiply(secondOperand);
                break;
            case DIVIDE:
                result = firstOperand.divide(secondOperand, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
                break;
            default:
                throw new UnsupportedOperationException(this.name() + " is unsupported.");
        }

        return result;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
