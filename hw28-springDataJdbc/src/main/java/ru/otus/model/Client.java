package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Table("client")
public class Client implements Cloneable {
    @Id
    private final Long id;

    @Nonnull
    private final String name;

    @MappedCollection(idColumn = "client_id")
    private final Address address;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
        this.address = null;
        this.phones = null;
    }

    public Client(String name, Address address, Set<Phone> phones) {
        this.id = null;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Client(String name, String address, String phone) {
        this.id = null;
        this.name = name;

        if (address != null) {
            this.address = new Address(null, address);
        } else
            this.address = null;

        if (phone != null) {
            this.phones = Set.of(new Phone(null, phone, null));
        } else this.phones = null;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public Client clone() {
        Address copyaddress = null;
        if (this.address != null){
            copyaddress = new Address(this.address.getClientId(), this.address.getStreet());
        }

        Set<Phone> copyPhones = new HashSet<>();
        if (this.phones != null && this.phones.size() > 0) {
            copyPhones = this.phones.stream()
                    .map(phone -> new Phone(phone.getId(), phone.getNumber(), this.id)).collect(Collectors.toSet());
        }

        return new Client(this.id, this.name, copyaddress, copyPhones);
    }

    @Override
    public String toString() {
        return "Client{" + "id="
                + id + ", name='"
                + name + "\nAddress = "
                + address + "\nPhones = "
                + (phones != null ? phones.toString() : "empty phones") +'}';
    }
}
