package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("address")
public class Address {
    @Id
    private final Long clientId;

    @Nonnull
    private final String street;

    @PersistenceCreator
    public Address(Long clientId, String street) {
        this.clientId = clientId;
        this.street = street;
    }

    @Override
    public String toString() {
//        return "Address{" + " clientId = "
//                +this.clientId+" street = "
//                +this.street+"}";
        return this.street;
    }
}
