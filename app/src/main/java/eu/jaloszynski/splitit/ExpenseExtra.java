package eu.jaloszynski.splitit;

import java.io.Serializable;

public class ExpenseExtra implements Serializable {

    public ExpenseExtra(String name, String value, String expense) {
        Name = name;
        Value = value;
        Expense = expense;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getExpense() {
        return Expense;
    }

    public void setExpense(String expense) {
        Expense = expense;
    }

    String Name;
    String Value;
    String Expense;



}
