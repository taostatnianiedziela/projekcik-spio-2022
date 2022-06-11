package eu.jaloszynski.splitit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.jaloszynski.splitit.R;
import eu.jaloszynski.splitit.helpers.OnItemClickListener;
import eu.jaloszynski.splitit.persistence.Expense;

public class ExpenseListAdapter2 extends RecyclerView.Adapter<ExpenseListAdapter2.ExpenseViewHolder2> {
    private LayoutInflater layoutInflater;
    private List<Expense> expenses;

    private final OnItemClickListener listener;

    public ExpenseListAdapter2(Context context, OnItemClickListener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseListAdapter2.ExpenseViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View recyclerViewItem2 = layoutInflater.inflate(R.layout.recyclerview_item2, viewGroup, false);

        return new ExpenseViewHolder2(recyclerViewItem2);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListAdapter2.ExpenseViewHolder2 expenseViewHolder2, int i) {
        TextView tvExpense_2 = expenseViewHolder2.expenseViewHolder2_2;
        TextView tvValue_2 = expenseViewHolder2.expenseViewHolder3_2;

        expenseViewHolder2.bind(expenses.get(i), listener);

        if (expenses != null) {

            tvExpense_2.setText(expenses.get(i).getExpanse());
            tvValue_2.setText(expenses.get(i).getValue());
            //TODO: More then one expanse


        } else {
            tvExpense_2.setText("N/A");
            tvValue_2.setText("N/A");
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

    public static class ExpenseViewHolder2 extends RecyclerView.ViewHolder {
        TextView expenseViewHolder2_2, expenseViewHolder3_2;


        public ExpenseViewHolder2(View itemView2) {
            super(itemView2);
            expenseViewHolder2_2 = itemView2.findViewById(R.id.tvExpense2);
            expenseViewHolder3_2 = itemView2.findViewById(R.id.tvValue2);
        }

        public void bind(final Expense item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}

