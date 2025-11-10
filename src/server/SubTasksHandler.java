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

public class SubTasksHandler extends BaseHttpHandler{
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
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDateTime.class, new PrioritizedHandler.LocalDateTimeAdapter())
                            .registerTypeAdapter(Duration.class, new PrioritizedHandler.DurationAdapter())
                            .excludeFieldsWithoutExposeAnnotation()
                            .create();
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new SubTasksHandler.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new SubTasksHandler.DurationAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String json = gson.toJson(map);
        System.out.println("после json");
        System.out.println("JSON: " + json);
        return json.toString();
    }
    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            if (localDateTime == null) {//Ты меня так замучил!!! проверка на null
                /*
                Если у вас есть поля, которые не должны быть сериализованы,
                 вы можете использовать аннотации, например @SerializedName
                 для переименования полей или @Expose для контроля, какие поля включать в сериализацию.
                 еще добавить в Builder .excludeFieldsWithoutExposeAnnotation()
                 */
                jsonWriter.value(String.valueOf(JsonNull.INSTANCE));// устанавливаем null если время не задано
            } else {
                jsonWriter.value(localDateTime.format(dtf));
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }

    public static class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
        @Override
        public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toMinutes());
        }

        @Override
        public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            long minutes = json.getAsLong();
            return Duration.ofMinutes(minutes);
        }
    }

}
