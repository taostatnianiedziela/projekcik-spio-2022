package eu.jaloszynski.splitit.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.ExpenseDao;
import eu.jaloszynski.splitit.persistence.ExpenseRoomDatabase;
import eu.jaloszynski.splitit.persistence.Friends;
import eu.jaloszynski.splitit.persistence.FriendsDao;
import eu.jaloszynski.splitit.persistence.FriendsRoomDatabase;


public class FriendsRepository {
    private FriendsDao friendsDao;
    private LiveData<List<Friends>> allFriendses;

    public FriendsRepository(Application application) {
        FriendsRoomDatabase db = FriendsRoomDatabase.getInstance(application);
        friendsDao = db.friendsDao();
        allFriendses = friendsDao.getAllFriends();
    }

    public LiveData<List<Friends>> getAllFriendses() {
        return allFriendses;
    }

    public void insert(Friends friends) {
        new InsertExpanseAsyncTask(friendsDao).execute(friends);
    }

    private static class InsertExpanseAsyncTask extends AsyncTask<Friends, Void, Void> {
        private FriendsDao taskFriendsDao;

        public InsertExpanseAsyncTask(FriendsDao friendsDao) {
            taskFriendsDao = friendsDao;
        }

        @Override
        protected Void doInBackground(final Friends... friends) {
            taskFriendsDao.insert(friends[0]);
            return null;
        }
    }

}
