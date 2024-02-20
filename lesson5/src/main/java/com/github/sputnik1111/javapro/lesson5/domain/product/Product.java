package com.github.sputnik1111.javapro.lesson5.domain.product;

public class Product {
    private Long id;

    private Long userId;

    private String account;

    private Long balance;

    private TypeProduct typeProduct;

    public Product(Long id, long userId, String account, long balance, TypeProduct typeProduct) {

        if (account == null)
            throw new IllegalArgumentException(" account is null ");

        if (typeProduct == null)
            throw new IllegalArgumentException(" typeProduct is null ");

        this.id = id;
        this.userId = userId;
        this.account = account;
        this.balance = balance;
        this.typeProduct = typeProduct;
    }

    public enum TypeProduct {
        CARD, BILL
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccount() {
        return account;
    }

    public Long getBalance() {
        return balance;
    }

    public TypeProduct getTypeProduct() {
        return typeProduct;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", userId=" + userId +
                ", account='" + account + '\'' +
                ", balance=" + balance +
                ", typeProduct=" + typeProduct +
                '}';
    }
}
