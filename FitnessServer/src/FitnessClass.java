import java.sql.Date;
import java.sql.Time;

public class FitnessClass {

    private int class_id;
    private String name;
    private Date class_date;
    private Time class_time;
    private String location;
    private int capacity;
    private int trainer_id;

    public FitnessClass() {
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getClass_date() {
        return class_date;
    }

    public void setClass_date(Date class_date) {
        this.class_date = class_date;
    }

    public Time getClass_time() {
        return class_time;
    }

    public void setClass_time(Time class_time) {
        this.class_time = class_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(int trainer_id) {
        this.trainer_id = trainer_id;
    }
}