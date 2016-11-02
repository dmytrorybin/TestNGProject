package businessObject;

/**
 * Created by Ievgen on 05.05.2016.
 */
public class User {

    private String name;
    private String surName;
    private int age;

    public User(String name, String surName) {
        this.name = name;
        this.surName = surName;
    }

    public int getAge() {
        return age < 0 ? 0 : age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
