package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> hwCache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, HwCache<String, Client> hwCache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.hwCache = hwCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("Добавили в Базу: {}", savedClient.getId());

                if(hwCache != null) {
                    hwCache.put(savedClient.getId().toString(), savedClient);
                    log.info("Положили в Кэш: {}", savedClient.getId());
                }
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            log.info("Обновили в Базе: {}", savedClient.getId());

            if(hwCache != null) {
                hwCache.remove(savedClient.getId().toString());
                hwCache.put(savedClient.getId().toString(), savedClient);
                log.info("Обновили в Кэше: {}", savedClient.getId());
            }
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            Client getClient = null;
            Optional<Client> clientOptional;

            if(hwCache != null) {
                getClient = hwCache.get(Long.toString(id));
            }

            if(getClient == null){
                clientOptional = clientDataTemplate.findById(session, id);
                if (clientOptional.isPresent()&&(hwCache != null)) {
                    hwCache.put(clientOptional.get().getId().toString(), clientOptional.get());
                }
                log.info("Достали из БАЗЫ: {}", clientOptional.map(Client::getId).orElse(0L));
            } else {
                clientOptional = Optional.of(getClient);
                log.info("Получили из КЭША: {}", clientOptional.map(Client::getId).orElse(0L));
            }

            return clientOptional.map(Client::clone);
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            List<Client> clonedClients = clientList.stream()
                            .map(Client::clone).toList();
            log.info("Достали из БАЗЫ: {}", clonedClients.stream().map(Client::getId).collect(Collectors.toList()));
            return clonedClients;
        });
    }
}
