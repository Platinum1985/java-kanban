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
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class HistoryHandler extends BaseHttpHandler {
    private final InMemoryTaskManager managers;

    public HistoryHandler(InMemoryTaskManager managers) {
        this.managers = managers;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Получен запрос: " + exchange.getRequestMethod());
        if (exchange.getRequestMethod().equals("GET")) {
            Collection<Task> historyTasks = managers.getHistory();
            System.out.println("Приоритетные задачи: " + historyTasks);
            String json = convertHistoryCollection(historyTasks);
            sendText(exchange, json, 200);
        }
    }

    private String convertHistoryCollection(Collection<Task> map) {
        System.out.println("Полученная карта задач: " + map);
        String json = gson.toJson(map);
        System.out.println("после json");
        System.out.println("JSON: " + json);
        return json.toString();
    }
}

