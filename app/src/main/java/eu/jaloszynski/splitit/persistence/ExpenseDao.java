package eu.jaloszynski.splitit.persistence;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Expense expense);

    @Query("DELETE from expense_table")
    void deleteAll();

    @Query("SELECT * from expense_table ORDER BY expanse asc")
    LiveData<List<Expense>> getAllWords();
}
