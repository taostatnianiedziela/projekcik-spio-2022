package eu.jaloszynski.splitit.persistence;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Expense expense);

    @Delete
    void delete(Expense expense);


    @Query("DELETE from expense_table")
    void deleteAll();


   @Query("SELECT DISTINCT * from expense_table ORDER BY id asc")
   LiveData<List<Expense>> getAllWords();

    @Query("SELECT * from expense_table WHERE extern_key_Friends like :idFriend")
    LiveData<List<Expense>> getExpensesByFid(int idFriend);

    @Query("SELECT * from expense_table GROUP BY extern_key_Friends")
    LiveData<List<Expense>> getAllExpensesGroupEKF();

}
