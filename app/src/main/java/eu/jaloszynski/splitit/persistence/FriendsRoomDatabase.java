package eu.jaloszynski.splitit.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Friends.class}, version = 1)
public abstract class FriendsRoomDatabase extends RoomDatabase {
    private static volatile FriendsRoomDatabase INSTANCE;

    public static FriendsRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FriendsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FriendsRoomDatabase.class, "friends_database")
                            .addCallback(sCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract FriendsDao friendsDao();

    private static Callback sCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDBAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        FriendsDao friendsDao;

        public PopulateDBAsyncTask(FriendsRoomDatabase dbInstance) {
            friendsDao = dbInstance.friendsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            friendsDao.deleteAll();
            Friends friends = new Friends("Marek","Szybki");
            friendsDao.insert(friends);
            Friends friends2 = new Friends("Adam","Wolny");
            friendsDao.insert(friends2);
            return null;
        }
    }
}
