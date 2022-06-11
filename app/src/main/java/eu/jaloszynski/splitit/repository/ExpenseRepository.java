package eu.jaloszynski.splitit.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.ExpenseDao;
import eu.jaloszynski.splitit.persistence.ExpenseRoomDatabase;


public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;

    public ExpenseRepository(Application application) {
        ExpenseRoomDatabase db = ExpenseRoomDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllWords();
    }

    public LiveData<List<Expense>> getAllWords() {
        return allExpenses;
    }

    public void insert(Expense expense) {
        new InsertExpanseAsyncTask(expenseDao).execute(expense);
    }

    public void delete(Expense expense) {
        new DeleteExpanseAsyncTask(expenseDao).execute(expense);
    }

    private static class InsertExpanseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao taskExpanseDao;

        public InsertExpanseAsyncTask(ExpenseDao expenseDao) {
            taskExpanseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(final Expense... expenses) {
            taskExpanseDao.insert(expenses[0]);
            return null;
        }
    }

    private static class DeleteExpanseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao taskExpanseDao;

        public DeleteExpanseAsyncTask(ExpenseDao expenseDao) {
            taskExpanseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(final Expense... expenses) {
            taskExpanseDao.delete(expenses[0]);
            return null;
        }
    }



}
