package server;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import manager.TimeOverlapException;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SubTasksHandler extends BaseHttpHandler {
    private final InMemoryTaskManager managers;

    public SubTasksHandler(InMemoryTaskManager managers) {

        this.managers = managers;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Получен запрос: " + exchange.getRequestMethod());
        String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET":
                String json = convertHashMapToJson(managers.getSubTasks());
                System.out.println("JSON для отправки: " + json);
                sendText(exchange, json, 200);
                break;
            case "POST":
                try {
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    SubTask subTask = gson.fromJson(body, SubTask.class);

                    if (query != null) { // Проверяем, есть ли параметр id в запросе
                        String[] params = query.split("=");
                        int param = Integer.parseInt(params[1]);
                        managers.updateSubTask(subTask, param); // Метод для обновления задачи по id
                        sendText(exchange, "Задача обновлена", 201);
                    } else {
                        int tasksLengthAfter = managers.getSubTasks().size();
                        managers.addTask(subTask);
                        int tasksLengthBefore = managers.getSubTasks().size();
                        if (tasksLengthBefore > tasksLengthAfter) {
                            sendText(exchange, "Задача добавлена " + tasksLengthAfter + tasksLengthBefore, 201);
                        } else {
                            sendHasOverlaps(exchange, "Время задачи пересекается с другой задачей");
                        }
                    }
                } catch (TimeOverlapException e) {
                    throw new RuntimeException(e);
                }
            case "DELETE":
                String[] params = query.split("=");
                int param = Integer.parseInt(params[1]);
                managers.deleteSubTask(param);
                sendText(exchange, "", 200);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private String convertHashMapToJson(Map<Integer, SubTask> map) {
        System.out.println("Полученная карта задач: " + map);
        String json = gson.toJson(map);
        System.out.println("после json");
        System.out.println("JSON: " + json);
        return json.toString();
    }
}
