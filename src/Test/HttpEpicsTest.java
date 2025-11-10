package Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TimeOverlapException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.EpicsHandler;
import server.PrioritizedHandler;
import server.TasksHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpEpicsTest {
    // создаём экземпляр InMemoryTaskManager
    InMemoryTaskManager manager = new FileBackedTaskManager("C:\\Users\\1\\Desktop\\AllTasks.csv");
    // передаём его в качестве аргумента в конструктор server.HttpTaskServer
    server.HttpTaskServer taskServer = new server.HttpTaskServer(manager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new EpicsHandler.LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new EpicsHandler.DurationAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public HttpEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        manager.clearTask();
        manager.clearSubTasks();
        manager.clearEpics();
        taskServer.start(8080);
        Thread.sleep(1000); // добавляем задержку в 1 секунду
    }

    @AfterEach
    public void shutDown() throws IOException {
        taskServer.stop();
    }

    @Test
    public void getSubtasksByEpicId() throws TimeOverlapException, IOException, InterruptedException {
        manager.addTask(new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L));
        manager.addTask(new Task("2 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 6L));
        manager.addEpics(new Epic("3 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L));
        manager.addEpics(new Epic("4 Епотека", Status.NEW, "Особняк на берегу моря", "2025.10.27 00:06", 0L));
        manager.addEpics(new Epic("5 вернуть", Status.NEW, "hfjdk", "2025.12.27 01:03", 0L));
        manager.addSubTask(new SubTask("6 вернуть", Status.NEW, "hfdk", "2025.01.27 01:03", 75L), 4);
        manager.addSubTask(new SubTask("7 ...ть", Status.NEW, "hfgfjddk", "2025.10.29 01:03", 85L), 4);
        manager.addTask(new Task("8 ...ть", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L));
        manager.addSubTask(new SubTask("9 34 ...ть", Status.NEW, "hfgfhjkjddk", "2025.10.21 01:03", 15L), 3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics?id=4&st=st");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json;charset=utf-8")
                .build();
        try {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonString = response.body();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new EpicsHandler.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new EpicsHandler.DurationAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Type listType = new TypeToken<ArrayList<SubTask>>() {}.getType();//Type-класс для создания типа... круто
        List<SubTask> subTasks = gson.fromJson(jsonString, listType);

        assertEquals(200, response.statusCode());
        assertEquals(2,subTasks.size(),"размер списка отличается от ожидаемого");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
