package server;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import manager.*;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TasksHandler extends BaseHttpHandler {

    private final InMemoryTaskManager managers;

    public TasksHandler(InMemoryTaskManager managers) {
        this.managers = managers;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Получен запрос: " + exchange.getRequestMethod());
        String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    String json = convertHashMapToJson(managers.getTasks());
                    sendText(exchange, json, 200);
                    break;
                } else {
                    String[] params = query.split("=");
                    int param = Integer.parseInt(params[1]);
                    if (managers.getTaskById(param).equals("Задачи с таким id нет")) {
                        sendNotFound(exchange);
                        break;
                    } else {
                        String json = managers.getTaskById(param);
                        sendText(exchange, json, 200);
                        break;
                    }
                }

            case "POST":
                try {
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Task task = gson.fromJson(body, Task.class);

                    if (query != null) { // Проверяем, есть ли параметр id в запросе
                        String[] params = query.split("=");
                        int param = Integer.parseInt(params[1]);
                        managers.updateTask(task, param); // Метод для обновления задачи по id
                        sendText(exchange, "Задача обновлена", 201);
                    } else {
                        int tasksLengthAfter = managers.getTasks().size();
                        managers.addTask(task);
                        int tasksLengthBefore = managers.getTasks().size();
                        if (tasksLengthBefore > tasksLengthAfter) {
                            sendText(exchange, "Задача добавлена " + tasksLengthAfter + tasksLengthBefore, 201);
                        } else {
                            sendHasOverlaps(exchange, "Время задачи пересекается с другой задачей");
                        }
                    }
                } catch (TimeOverlapException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "DELETE":
                String[] params = query.split("=");
                int param = Integer.parseInt(params[1]);
                managers.deleteTask(param);
                sendText(exchange, "", 200);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private String convertHashMapToJson(Map<Integer, Task> map) {
        System.out.println("Полученная карта задач: " + map);
        String json = gson.toJson(map);
        System.out.println("после json");
        System.out.println("JSON: " + json);
        return json.toString();
    }
}
