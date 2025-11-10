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
import java.util.*;

public class EpicsHandler extends BaseHttpHandler {
    private final InMemoryTaskManager managers;

    public EpicsHandler(InMemoryTaskManager managers) {
        this.managers = managers;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Получен запрос: " + exchange.getRequestMethod());
        String query = exchange.getRequestURI().getQuery();
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    String json = convertHashMapToJson(managers.getEpics());
                    sendText(exchange, json, 200);
                    break;
                } else {
                    Map<String, String> params = parseQuery(query);
                    Integer paramId = Integer.parseInt(params.get("id"));
                    if (params.size() == 1) {
                        Epic epic = managers.getEpics().get(paramId);
                        if (epic == null) {
                            sendNotFound(exchange);
                            break;
                        } else {
                            String json = managers.getEpicById(paramId).toString();
                            sendText(exchange, json, 200);
                            break;
                        }
                    } else if (params.size() > 1) {
                        System.out.println("2 paramrtr");
                        List<Integer> ids = managers.getEpics().get(paramId).getSubTaskIds();
                        List<SubTask> subTasks = new ArrayList<>();
                        System.out.println("id subtasks=" + ids);
                        for (Integer i : ids) {
                            subTasks.add(managers.getSubTasks().get(i));
                        }
                        String json = convertListToJson(subTasks);
                        sendText(exchange, json, 200);
                        break;
                    }
                }
                break;
            case "POST":
                String body = new String(exchange.getRequestBody().readAllBytes());
              /*  Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new PrioritizedHandler.LocalDateTimeAdapter())
                        .registerTypeAdapter(Duration.class, new PrioritizedHandler.DurationAdapter())
                        .excludeFieldsWithoutExposeAnnotation()
                        .create();*/
                Epic epic = gson.fromJson(body, Epic.class);
                int tasksLengthAfter = managers.getEpics().size();
                managers.addEpics(epic);
                int tasksLengthBefore = managers.getEpics().size();
                if (tasksLengthBefore > tasksLengthAfter) {
                    sendText(exchange, "Задача добавлена " + tasksLengthAfter + tasksLengthBefore, 201);
                } else {
                    sendHasOverlaps(exchange, "Время задачи пересекается с другой задачей");
                }
                break;
            case "DELETE":
                String[] params = query.split("=");
                int param = Integer.parseInt(params[1]);
                managers.deleteEpic(param);
                sendText(exchange, "", 200);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            }
        }
        return result;
    }


    private <T extends Task> String convertHashMapToJson(Map<Integer, T> map) {
        System.out.println("Полученная карта задач: " + map);
        /*Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new EpicsHandler.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new EpicsHandler.DurationAdapter())
                .excludeFieldsWithoutExposeAnnotation()//для аннотаций
                .create();*/
        String json = gson.toJson(map);
        System.out.println("после json");
        System.out.println("JSON: " + json);
        return json.toString();
    }

    private String convertListToJson(List<SubTask> map) {
        System.out.println("Полученная карта задач: " + map);
        String json = gson.toJson(map);
        System.out.println("после json");
        System.out.println("JSON: " + json);
        return json.toString();
    }
}



