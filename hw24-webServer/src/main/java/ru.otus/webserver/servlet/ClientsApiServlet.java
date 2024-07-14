package ru.otus.webserver.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.service.DBServiceClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"squid:S1948"})
public class ClientsApiServlet extends HttpServlet {
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";
    private final Gson gson;
    private final DBServiceClient dbServiceClient;

    public ClientsApiServlet(Gson gson, DBServiceClient dbServiceClient) {
        this.gson = gson;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        JsonMessage msg;
        try{

            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            Type empMapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> clientMap = gson.fromJson(requestBody, empMapType);

            Address address = null;
            Phone phone = null;

            String name = clientMap.get(PARAM_NAME);
            if (name != null && !name.isEmpty()){

                String addressText = clientMap.get(PARAM_ADDRESS);
                if(addressText != null && !addressText.isEmpty()){
                    address = new Address(null, addressText);
                }

                String phoneText = clientMap.get(PARAM_PHONE);
                if(phoneText != null && !phoneText.isEmpty()){
                    phone = new Phone(null, phoneText);
                }
            } else {
                throw new JsonParseException("Error");
            }

            Client client = new Client(null, name, address, phone != null ? List.of(phone) : null);

            Client newClient = dbServiceClient.saveClient(client);

            msg = new JsonMessage("success", "Успешно добавлен ID = " + newClient.getId());
        }
        catch (JsonParseException e){
            response.setStatus(200);
            msg = new JsonMessage("Error", "Неправильно введенные данные (Имя не может быть пустым)");
        } catch (Exception e) {
            response.setStatus(200);
            msg = new JsonMessage("Error", "Новый клиент не создан - " + e.getMessage());
        }
        out.print(gson.toJson(msg));
    }

    private class JsonMessage {
        private final String status;
        private final String message;

        public JsonMessage(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
