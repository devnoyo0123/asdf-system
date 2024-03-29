package com.example.modulecommon.domain.valueobject;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
public class Money {
    private final BigDecimal amount;

    public static final Money ZERO = Money.of(BigDecimal.ZERO);

    protected Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public boolean isGreaterThanZero() {
        // do not use equals
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money add(Money money) {
        return Money.of(this.amount.add(money.getAmount()));
    }

    public Money substract(Money money) {
        return Money.of(this.amount.subtract(money.getAmount()));
    }

    public Money multiply(int multiplier) {
        return Money.of(this.amount.multiply(new BigDecimal(multiplier)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return this.amount != null && money.amount != null &&
                this.amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

}
