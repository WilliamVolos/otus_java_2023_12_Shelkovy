package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @SequenceGenerator(name = "address_gen", sequenceName = "address_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne
    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
        this.client = null;
    }
    public Address(String street) {
        this.id = null;
        this.street = street;
        this.client = null;
    }
    public Address(Long id, String street, Client client) {
        this.id = id;
        this.street = street;
        this.client = client;
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", street='" + street + '\'' + '}';
    }
}
