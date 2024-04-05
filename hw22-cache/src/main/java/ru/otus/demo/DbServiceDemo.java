package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Address.class, Client.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        // Сначала замеряем время на отключенном Кеше
        var dbServiceClientNoCache = new DbServiceClientImpl(transactionManager, clientTemplate, null);

        long startTime = System.currentTimeMillis();

        List<Long> ids = new ArrayList<>();

        // Добавляем 100 полных структур клиентов в базу
        for(int i=0; i<100; i++){
            var client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                    new Phone(null, "14-666-333")));
            ids.add(dbServiceClientNoCache.saveClient(client).getId());
        }

        // Затем извлекаем их из базы в обратном порядке
        Collections.reverse(ids);
        log.info("Извлекаем клиентов");
        for (var id : ids){
            log.info(dbServiceClientNoCache.getClient(id).map(c -> c.toString()+c.getAddress()+c.getPhones()).orElse("Пусто"));
        }
        long deltaNoCache = System.currentTimeMillis() - startTime;


        // Теперь выполняем тоже самое при включенном Кеше
        HwCache<String, Client> hwCache = new MyCache<>();

        var dbServiceClientCache = new DbServiceClientImpl(transactionManager, clientTemplate, hwCache);

        long cacheTime = System.currentTimeMillis();

        List<Long> idCaches = new ArrayList<>();

        // Добавляем 100 полных структур клиентов в базу
        for(int i=0; i<100; i++){
            var client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                    new Phone(null, "14-666-333")));
            idCaches.add(dbServiceClientCache.saveClient(client).getId());
        }

        // Затем извлекаем их в обратном порядке
        Collections.reverse(idCaches);
        log.info("Извлекаем клиентов");
        for (var id : idCaches){
            log.info(dbServiceClientCache.getClient(id).map(c -> c.toString()+c.getAddress()+c.getPhones()).orElse("Пусто"));
        }
        long deltaCache = System.currentTimeMillis() - cacheTime;

        log.info("Время выполнения без кеша, mSec: {}", deltaNoCache);
        log.info("Время выполнения с включенным кешем, mSec: {}", deltaCache);
    }
}
