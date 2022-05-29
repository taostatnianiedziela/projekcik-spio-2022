package eu.jaloszynski.splitit.persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FriendsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Friends friends);

    @Query("DELETE from friends_table")
    void deleteAll();

    @Query("SELECT * from friends_table ORDER BY name asc")
    LiveData<List<Friends>> getAllFriends();
}
