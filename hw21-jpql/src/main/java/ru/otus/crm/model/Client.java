package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "address_id", nullable = true)
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

//    @Fetch(FetchMode.SUBSELECT)
//    @JoinColumn(name = "client_id", nullable = false)
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
        this.address = null;
        this.phones = new ArrayList<>();
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
        this.address = null;
        this.phones = new ArrayList<>();
    }

    public <E> Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        if(phones == null){
            this.phones = new ArrayList<>();
        }else
            this.phones = phones;
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Address copyaddress = null;

        Client copyClient = new Client( this.id,
                                        this.name,
                                        null,
                                        null);

        if (this.getAddress() != null){
            copyaddress = new Address(this.address.getId(), this.address.getStreet(), copyClient);
            copyClient.setAddress(copyaddress);
        }

        List<Phone> copyPhones;
        if (this.getPhones().size() > 0) {
            copyPhones = this.phones.stream()
                    .map(phone -> new Phone(phone.getId(), phone.getNumber(), copyClient)).toList();
        } else {
            copyPhones = new ArrayList<>();
        }
        copyClient.setPhones(copyPhones);
        return copyClient;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
