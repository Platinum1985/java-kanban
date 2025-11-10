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

public class HistoryHandler extends BaseHttpHandler{
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HistoryHandler.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new HistoryHandler.DurationAdapter())
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

