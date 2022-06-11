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


    @NonNull
    private String name;
    private String expanse;
    private String value;



    private int extern_key_Friends;

    public Expense(@NonNull String name, String expanse, String value, int extern_key_Friends) {
        //this.id = id;
        this.name = name;
        this.expanse = expanse;
        this.value = value;
        this.extern_key_Friends = extern_key_Friends;
    }

    public String getExpanse() {
        return expanse;
    }

    public String getValue() {
        return value;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setExpanse(String expanse) {
        this.expanse = expanse;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExtern_key_Friends() {
        return extern_key_Friends;
    }

    public void setExtern_key_Friends(int extern_key_Friends) {
        this.extern_key_Friends = extern_key_Friends;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expanse1 = (Expense) o;
        return Objects.equals(name, expanse1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
