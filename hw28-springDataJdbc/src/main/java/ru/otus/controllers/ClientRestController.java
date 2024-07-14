package ru.otus.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.ClientDto;
import ru.otus.dto.ResponceMessage;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.DBServiceClient;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toSet;

@RestController
public class ClientRestController {
    private final DBServiceClient dbServiceClient;

    public ClientRestController (DBServiceClient dbServiceClient){
        this.dbServiceClient = dbServiceClient;
    }

    @PostMapping("api/clients")
    public ResponceMessage saveClient(@RequestBody ClientDto clientDto){

        ResponceMessage msg;
        try{

            Client client = toEntity(clientDto);

            Client newClient = dbServiceClient.saveClient(client);

            msg = new ResponceMessage("success", "Успешно добавлен ID = " + newClient.getId());
        }
        catch (Exception e) {
            msg = new ResponceMessage("Error", "Новый клиент не создан - " + e.getMessage());
        }
        return msg;
    }

    private Client toEntity(ClientDto clientDto) {
        Address address = null;
        Set<Phone> phones = null;

        if (!clientDto.getName().isBlank()) {

            if (nonNull(clientDto.getAddress()) && !clientDto.getAddress().isBlank()) {
                address = new Address(null, clientDto.getAddress());
            }

            if (nonNull(clientDto.getPhones()) && !clientDto.getPhones().isBlank()) {
               phones = Arrays.stream(clientDto.getPhones().split(","))
                       .map(Phone::new)
                       .collect(toSet());
            }

            return new Client(
                    clientDto.getName(),
                    address,
                    phones);

        }else throw new NoSuchElementException("Имя клиента не может быть пустым!");
    }
}
