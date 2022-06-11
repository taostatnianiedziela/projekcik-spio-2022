package eu.jaloszynski.splitit.helpers;

import eu.jaloszynski.splitit.persistence.Expense;

public interface OnItemClickListener {
    void onItemClick(Expense item);
}