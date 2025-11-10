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
import server.EpicsHandler;
import server.PrioritizedHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PrioritizedTest extends BaseHttpTest {
    // создаём экземпляр InMemoryTaskManager
    InMemoryTaskManager manager = new FileBackedTaskManager("C:\\Users\\1\\Desktop\\AllTasks.csv");
    // передаём его в качестве аргумента в конструктор server.HttpTaskServer
    server.HttpTaskServer taskServer = new server.HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.gson;

    public PrioritizedTest() throws IOException {
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
    public void getPrioritized() throws TimeOverlapException, IOException, InterruptedException {
        manager.addTask(new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L));
        manager.addEpics(new Epic("2 Переезд", Status.NEW, "В теплые края", "2025.12.27 00:03", 0L));
        manager.addEpics(new Epic("3 Епотека", Status.NEW, "Особняк на берегу моря", "2025.10.27 00:06", 0L));
        manager.addEpics(new Epic("4 вернуть", Status.NEW, "hfjdk", "2025.12.27 01:03", 0L));
        manager.addTask(new Task("5 ...ть", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L));
        HttpClient client = HttpClient.newHttpClient();
        URI url = createUri("http://localhost:8080/prioritized");
        HttpRequest request = buildGetRequest(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonString = response.body();

        List<Task> tasks = gson.fromJson(jsonString, new TypeToken<List<Task>>() {
        }.getType());
        System.out.println(tasks);
        // Проверяем, что задачи отсортированы по startTime
        for (int i = 0; i < tasks.size() - 1; i++) {
            Task currentTask = tasks.get(i);
            Task nextTask = tasks.get(i + 1);

            if (currentTask.getStartTime().equals(nextTask.getStartTime())) {
                // Если startTime совпадает, проверяем id
                assert currentTask.getId() <= nextTask.getId();
            } else {
                assert currentTask.getStartTime().isBefore(nextTask.getStartTime());
            }
        }
    }
}
