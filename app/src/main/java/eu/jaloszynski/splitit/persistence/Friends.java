package eu.jaloszynski.splitit.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "friends_table")
public class Friends implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    private String surname;
    private String phone;

    public Friends(@NonNull String name, String surname) {
        //this.id = id;
        this.name = name;
        this.surname = surname;

    }

//    @Override
//    public String toString() {
//        return this.name + " " + this.surname; // What to display in the Spinner list.
//    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friends friends1 = (Friends) o;
        return Objects.equals(name, friends1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
