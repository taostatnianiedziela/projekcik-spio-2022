package eu.jaloszynski.splitit.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "expense_table")
public class Expense_copy {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    private String expanse;
    private String value;

    public Expense_copy(@NonNull String name, String expanse, String value) {
        //this.id = id;
        this.name = name;
        this.expanse = expanse;
        this.value = value;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense_copy expanse1 = (Expense_copy) o;
        return Objects.equals(name, expanse1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
