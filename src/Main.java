import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        TaskManager t1 = new InMemoryTaskManager();
        Task task1 = new Task("Переезд", "В теплые края", Status.NEW);
        Task task2 = new Task("Покупка Авто", "Желательно Порше", Status.NEW);
        t1.addTask(task1);
        t1.addTask(task2);

        t1.addTask(task1);
        t1.addTask(task2);


        System.out.println(t1.printAllTasks());
        Epic e1 = new Epic("Епотека", "Особняк на берегу моря", Status.NEW);
        t1.addEpics(e1);
        Epic e2= new Epic("вернуть", "hfjdk",Status.NEW);

        SubTask subTask1 = new SubTask("Работа", "Найти высокооплачиваемую работу",
                Status.NEW);
        SubTask subTask2 = new SubTask("Здоровье", "Следить за здор",
                Status.DONE);
        t1.addSubTask(subTask1, 2);
        t1.addSubTask(subTask2, 2);


       /* System.out.println(e1.toString());
        System.out.println(subTask1.toString());
        System.out.println(subTask2.toString());
        System.out.println(t1.printAllTasks());
        System.out.println(t1.printAllEpics());
        System.out.println(t1.printAllSubTasks());  */
       // t1.deleteSubTask(1);
        t1.getTaskById(0);
        t1.getTaskById(1);
        t1.getEpicById(2);
       // t1.deleteSubTask(3);
        t1.getSubTaskById(4);
        t1.getSubTaskById(4);
        t1.deleteSubTask(4);

        t1.getTaskById(0);
        System.out.println(t1.getHistory());

    }
}