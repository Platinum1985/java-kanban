package manager;

public class Managers {
    public static TaskManager getDefault() {
        // Здесь можно выбрать нужную реализацию TaskManager
        // Например, создать новый объект конкретной реализации
        return new InMemoryTaskManager(); // Пример конкретной реализации TaskManager
    }

    public static HistoryManager getDefaultHistory() {
        // Здесь создаём и возвращаем новый объект InMemoryHistoryManager
        return new InMemoryHistoryManager();
    }
}