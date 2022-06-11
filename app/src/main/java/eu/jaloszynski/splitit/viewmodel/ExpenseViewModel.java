package eu.jaloszynski.splitit.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.repository.ExpenseRepository;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;
    private final LiveData<List<Expense>> expensesByFid;
    private final LiveData<List<Expense>> unikeExpensesId;
    private int idFriend;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllWords();
        expensesByFid= repository.getExpensesByFid(idFriend);
        unikeExpensesId = repository.getAllExpensesGroupEKF();


    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getAllExpensesGroupEKF() {
        return unikeExpensesId;
    }

    public LiveData<List<Expense>> getExpensesByFid(int idFriend) {
        return expensesByFid;
    }


    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }


}
