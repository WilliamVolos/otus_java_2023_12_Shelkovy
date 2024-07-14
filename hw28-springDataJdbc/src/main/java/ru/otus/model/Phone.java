package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("phones")
public class Phone {
    @Id
    private final Long id;

    @Nonnull
    private final String number;

    @Nonnull
    private final Long clientId;

    public Phone(String number) {
        this.id = null;
        this.number = number;
        this.clientId = null;
    }

    @PersistenceCreator
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    @Override
    public String toString() {
//        return "Phone{" + " Id = "
//                +this.id+" number = "
//                +this.number+" clientId = "
//                +this.clientId+"}";
        return this.number;
    }
}
