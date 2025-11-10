package server;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler {
    private final InMemoryTaskManager managers;

    public PrioritizedHandler(InMemoryTaskManager managers) {

        this.managers = managers;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Получен запрос: " + exchange.getRequestMethod());
        if (exchange.getRequestMethod().equals("GET")) {
            Set<Task> prioritizedTasks = managers.getPrioritizedTasks();
            System.out.println("Приоритетные задачи: " + prioritizedTasks);
            String json = convertSetToJson(prioritizedTasks);
            sendText(exchange, json, 200);
        }
    }

    private String convertSetToJson(Set<Task> map) {
        System.out.println("Полученная карта задач: " + map);
        String json = gson.toJson(map);
        System.out.println("после json");
        System.out.println("JSON: " + json);
        return json.toString();
    }
}
