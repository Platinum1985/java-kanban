  public class SubTask extends Task {

      public SubTask(String name, String description, Status st, int id) {
          super(name, description, st, id);
      }


      @Override
      public String toString() {
          return "SubTask{" +
                  "name='" + name + '\'' +
                  ", description='" + description + '\'' +
                  ", st=" + st +
                  ", id=" + id +
                  '}';
      }
  }
