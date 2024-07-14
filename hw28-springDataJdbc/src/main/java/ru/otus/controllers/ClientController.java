package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.service.DBServiceClient;

@Controller
public class ClientController {
    private final String osData;
    private final DBServiceClient dbServiceClient;

    public ClientController(
            @Value("OS: #{T(System).getProperty(\"os.name\")}, "
                    + "JDK: #{T(System).getProperty(\"java.runtime.version\")}")
            String osData, DBServiceClient dbServiceClient){
        this.osData = osData;
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping({"/", "/clients"})
    public String clientsView(Model model){
        var clients = dbServiceClient.findAll();
        model.addAttribute("allClients", clients);
        model.addAttribute("osData", osData);
        return "clients";
    }
}
