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
import server.BaseHttpHandler;
import server.HistoryHandler;
import server.SubTasksHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpHistoryTest extends BaseHttpTest {
    // создаём экземпляр InMemoryTaskManager
    InMemoryTaskManager manager = new FileBackedTaskManager("C:\\Users\\1\\Desktop\\AllTasks.csv");
    // передаём его в качестве аргумента в конструктор server.HttpTaskServer
    server.HttpTaskServer taskServer = new server.HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.gson;

    public HttpHistoryTest() throws IOException {
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
    public void testAddSubTask() throws IOException, InterruptedException, TimeOverlapException {
        // создаём задачу
        manager.addTask(new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L));
        manager.addTask(new Task("2 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 6L));
        manager.addEpics(new Epic("3 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L));
        manager.addEpics(new Epic("4 Епотека", Status.NEW, "Особняк на берегу моря", "2025.10.27 00:06", 0L));
        manager.addEpics(new Epic("5 вернуть", Status.NEW, "hfjdk", "2025.12.27 01:03", 0L));
        manager.addSubTask(new SubTask("6 вернуть", Status.NEW, "hfdk", "2025.01.27 01:03", 75L), 4);
        manager.addSubTask(new SubTask("7 ...ть", Status.NEW, "hfgfjddk", "2025.10.29 01:03", 85L), 4);
        manager.addTask(new Task("8 ...ть", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L));
        manager.addSubTask(new SubTask("9 34 ...ть", Status.NEW, "hfgfhjkjddk", "2025.10.21 01:03", 15L), 3);
        // конвертируем её в JSON
        manager.getTaskById(1);
        manager.getEpicById(4);
        manager.getSubTaskById(6);
        manager.getTaskById(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = createUri("http://localhost:8080/history");
        HttpRequest request = buildGetRequest(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonString = response.body();
        Collection<Task> hist = gson.fromJson(jsonString, new TypeToken<Collection<Task>>() {
        }.getType());
        // проверяем код ответа

        assertEquals(200, response.statusCode());
        assertNotNull("Задачи не возвращаются", hist);
        assertEquals(manager.getHistory().size(), hist.size(), "Некорректное количество задач");

    }
}


