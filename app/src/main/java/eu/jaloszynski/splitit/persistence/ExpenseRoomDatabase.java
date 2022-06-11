package eu.jaloszynski.splitit.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Expense.class}, version = 1)
public abstract class ExpenseRoomDatabase extends RoomDatabase {
    private static volatile ExpenseRoomDatabase INSTANCE;

    public static ExpenseRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ExpenseRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseRoomDatabase.class, "expense_database")
                            .addCallback(sCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ExpenseDao expenseDao();

    private static RoomDatabase.Callback sCallback = new Callback() {
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
        ExpenseDao expenseDao;

        public PopulateDBAsyncTask(ExpenseRoomDatabase dbInstance) {
            expenseDao = dbInstance.expenseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            expenseDao.deleteAll();
//            Expense expense = new Expense( "Marek Szybki","Pizza","134,15",1);
//            expenseDao.insert(expense);
//            expense = new Expense( "Adam Wolny","Piwo","11",2);
//            expenseDao.insert(expense);
//
//            expense = new Expense( "Adam Wolny","Dwa piwa","11",2);
//            expenseDao.insert(expense);
//
//            expense = new Expense( "Marek Szybki","Piwo","11",1);
//            expenseDao.insert(expense);
            return null;
        }
    }
}
