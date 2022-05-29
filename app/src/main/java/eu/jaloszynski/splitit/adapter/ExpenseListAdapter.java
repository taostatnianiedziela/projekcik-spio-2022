package eu.jaloszynski.splitit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import eu.jaloszynski.R;
import eu.jaloszynski.splitit.persistence.Expense;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Expense> expenses;

    public ExpenseListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ExpenseListAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View recyclerViewItem = layoutInflater.inflate(R.layout.recyclerview_item, viewGroup, false);

        return new ExpenseViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListAdapter.ExpenseViewHolder expenseViewHolder, int i) {
        TextView tvName = expenseViewHolder.expenseViewHolder;
        TextView tvExpense = expenseViewHolder.expenseViewHolder2;
        TextView tvValue = expenseViewHolder.expenseViewHolder3;
        if (expenses != null) {
            tvName.setText(expenses.get(i).getFriends_id());
            //tvExpense.setText(expenses.get(i).getValue());
            //tvValue.setText(expenses.get(i).getValue());
            //TODO: More then one expanse


        } else {
            tvName.setText("N/A");
            tvExpense.setText("N/A");
            tvValue.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        if (expenses != null) {
            return expenses.size();
        }
        return 0;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseViewHolder, expenseViewHolder2, expenseViewHolder3;


        public ExpenseViewHolder(View itemView) {
            super(itemView);
            //expenseViewHolder = itemView.findViewById(R.id.tvName);
            //expenseViewHolder2 = itemView.findViewById(R.id.tvExpense);
            //expenseViewHolder3 = itemView.findViewById(R.id.tvValue);

        }
    }
}
