import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        TaskManager t1 = new TaskManager();
        Task task1 = new Task("Переезд", "В теплые края", Status.NEW, t1.idTask);
        t1.addTask(t1.idTask, task1);
        System.out.println(t1.tasks.get(0).toString());


        Task task2 = new Task("Покупка Авто", "Желательно Порше", Status.NEW, t1.idTask);
        t1.addTask(t1.idTask, task2);

        System.out.println(t1.tasks.get(1).toString());
        Epic e1 = new Epic("Епотека", "Особняк на берегу моря", Status.NEW, t1.idEpic);
        t1.addEpics(t1.idEpic, e1);
        SubTask subTask1 = new SubTask("Работа", "Найти высокооплачиваемую работу",
                Status.NEW, t1.idSubTask);
        t1.addSubTask(t1.idSubTask, subTask1, 0);
        SubTask subTask2 = new SubTask("Здоровье", "Следить за здор",
                Status.DONE, t1.idSubTask);
        t1.addSubTask(t1.idSubTask, subTask2, 0);

        System.out.println(e1.toString());
        System.out.println(subTask1.toString());
        System.out.println(subTask2.toString());
        String res1 = t1.checkStatusEpic(e1);
        System.out.println(res1);
        System.out.println(t1.printAllTasks());
        System.out.println(t1.printAllEpics());
        System.out.println(t1.printAllSubTasks());
        t1.deleteSubTask(1);
        System.out.println(t1.printAllSubTasks());
        System.out.println(t1.printAllEpics());
        SubTask subTask4 = new SubTask("Полет на воздушном шаре", "С друзьями", Status.DONE, 0);
        t1.updateSubTask(subTask4, 0);
        System.out.println(t1.printAllEpics());
        System.out.println(t1.printAllSubTasks());
    }
}