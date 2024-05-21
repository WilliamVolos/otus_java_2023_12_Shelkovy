package ru.otus.webserver.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.webserver.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"squid:S1948"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_ALL_CLIENTS = "allClients";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put(TEMPLATE_ATTR_ALL_CLIENTS, dbServiceClient.findAll());

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}
