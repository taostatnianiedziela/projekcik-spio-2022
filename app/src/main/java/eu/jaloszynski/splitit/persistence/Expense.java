package eu.jaloszynski.splitit.persistence;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int friends_id;
    private double value;

    public Expense(int friends_id, double value) {
        this.friends_id = friends_id;
        this.value = value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFriends_id(int friends_id) {
        this.friends_id = friends_id;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getFriends_id() {
        return friends_id;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expanse1 = (Expense) o;
        return Objects.equals(id, expanse1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
