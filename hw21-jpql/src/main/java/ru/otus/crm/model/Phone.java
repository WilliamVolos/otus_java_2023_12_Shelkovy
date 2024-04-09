package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @SequenceGenerator(name = "phones_gen", sequenceName = "phones_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phones_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    private Client client;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
    public Phone(Long id, String number, Client client) {
        this.id = id;
        this.number = number;
        this.client = client;
    }
    public Phone(String number) {
        this.id = null;
        this.number = number;
    }
}
