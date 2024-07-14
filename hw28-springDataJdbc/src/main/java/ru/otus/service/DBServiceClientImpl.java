package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DBServiceClientImpl implements DBServiceClient{
    private static final Logger log = LoggerFactory.getLogger(DBServiceClientImpl.class);
    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public DBServiceClientImpl(TransactionManager transactionManager, ClientRepository clientRepository){
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client).clone();
            log.info("\nsaved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        var clients = new ArrayList<Client>(clientRepository.findAll());
        log.info("clientList:{}", clients);
        return clients;
    }
}