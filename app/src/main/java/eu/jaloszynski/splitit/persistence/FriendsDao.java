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

    @Query("SELECT * from friends_table ORDER BY name asc")
    List<Friends> getAllFriends2();

    @Query("SELECT * from friends_table WHERE id like :idFriend")
    LiveData<List<Friends>> getFriendByFid(int idFriend);


}
