package Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import server.EpicsHandler;
import server.HttpTaskServer;
import server.TasksHandler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    InMemoryTaskManager manager = new FileBackedTaskManager("C:\\Users\\1\\Desktop\\AllTasks.csv");
    // передаём его в качестве аргумента в конструктор server.HttpTaskServer
    server.HttpTaskServer taskServer = new server.HttpTaskServer(manager);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TasksHandler.LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new TasksHandler.DurationAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public HttpTaskManagerTasksTest() throws IOException {
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
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("1 Переезд", Status.NEW, "В теплые края", "2025.11.27 00:01", 600L);
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);
        System.out.println("после json: " + taskJson + "это объект?");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json;charset=utf-8")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        System.out.println("statuscode" + response);
        assertEquals(201, response.statusCode());
        Map<Integer, Task> tasksFromManager = manager.getTasks();
        System.out.println(tasksFromManager.get(1));
        assertNotNull("Задачи не возвращаются", tasksFromManager);
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        System.out.println(tasksFromManager.get(1).getName() + "=имя");
        assertEquals("1 Переезд", tasksFromManager.get(1).getName(), "Некорректное имя задачи");


    }

    @Test
    public void testUpdateTask() {
        // Создаём задачи
        Task task = new Task("1 Переезд", Status.NEW, "В тёплые края", "2025.11.27 00:01", 600L);
        Task task2 = new Task("2 Переезд", Status.NEW, "В тёплые края", "2025.12.27 00:03", 6L);
        Task task3 = new Task("8 ...ть", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L);

        // Конвертируем задачи в JSON
        String taskJson = gson.toJson(task);
        String taskJson2 = gson.toJson(task2);
        String taskJson3 = gson.toJson(task3);
        System.out.println("taskgson" + taskJson);
        HttpClient client = HttpClient.newHttpClient();
        try {
            // Отправляем первый запрос
            URI url = URI.create("http://localhost:8080/tasks?id=1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            // Отправка второго запроса
            url = URI.create("http://localhost:8080/tasks?id=2");
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Отправка третьего запроса
            url = URI.create("http://localhost:8080/tasks?id=2");
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson3))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(manager.getTasks().size()+"=размер");
            assertEquals(2, manager.getTasks().size());
            assertEquals("8 ...ть", manager.getTasks().get(2).getName(), "имена задач не совпадают");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void testDelete () throws IOException, InterruptedException {
        // Создаём задачи
        Task task = new Task("1 Переезд", Status.NEW, "В тёплые края", "2025.11.27 00:01", 600L);
        Task task2 = new Task("2 Переезд", Status.NEW, "В тёплые края", "2025.12.27 00:03", 6L);
        Task task3 = new Task("8 ...ть", Status.NEW, "hfgfjd123dk", "2026.01.29 01:03", 856L);

        // Конвертируем задачи в JSON
        String taskJson = gson.toJson(task);
        String taskJson2 = gson.toJson(task2);
        String taskJson3 = gson.toJson(task3);
        System.out.println("taskgson" + taskJson);
        HttpClient client = HttpClient.newHttpClient();
            // Отправляем первый запрос
            URI url = URI.create("http://localhost:8080/tasks?id=1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            // Отправка второго запроса
            url = URI.create("http://localhost:8080/tasks?id=2");
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Отправка третьего запроса
            url = URI.create("http://localhost:8080/tasks?id=3");
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson3))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            //Запрос на удаление
            url = URI.create("http://localhost:8080/tasks?id=2");
            request = HttpRequest.newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println("код ответа = "+resp);
            assertEquals(200, resp.statusCode());
            System.out.println(manager.getTasks().size()+"=размер");
            assertEquals(2, manager.getTasks().size());


    }

    }

