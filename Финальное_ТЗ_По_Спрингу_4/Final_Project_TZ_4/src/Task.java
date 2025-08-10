import java.util.Objects;

public class Task {
  String  name;
  String description; //"Описание"
  Status st=Status.NEW;
  int id;

  public Task(String name, String description, Status st, int id) {
    this.name = name;
    this.description = description;
    this.st = st;
    this.id = id;
  }
int getId () {
    return this.id;
}

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return id == task.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Task{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", st=" + st +
            ", id=" + id +
            '}';
  }
}
