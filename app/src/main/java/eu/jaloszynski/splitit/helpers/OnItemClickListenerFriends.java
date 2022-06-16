package eu.jaloszynski.splitit.helpers;

import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.Friends;

public interface OnItemClickListenerFriends {
    void onItemClick(Expense item);
    void afterTextChanged(Expense item, String text);
}