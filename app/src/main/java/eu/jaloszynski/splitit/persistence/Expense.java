package eu.jaloszynski.splitit.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;


    @NonNull
    private String name;
    private String expanse;
    private String value;
    private boolean history;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    private Date endData;
    private Date createData;

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    private int extern_key_Friends;

    public Expense(@NonNull String name, String expanse, String value, int extern_key_Friends) {
        //this.id = id;
        this.name = name;
        this.expanse = expanse;
        this.value = value;
        this.extern_key_Friends = extern_key_Friends;
        this.history = false;
        this.createData = new Date();
    }


    public String getExpanse() {
        return expanse;
    }

    public double getValueInDouble()
    {
        return Double.parseDouble(this.value);
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public Date getCreateData() {
        return createData;
    }

    public void setCreateData(Date createData) {
        this.createData = createData;
    }

    public Date getEndData() {
        return endData;
    }

    public void setEndData(Date endData) {
        this.endData = endData;
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
