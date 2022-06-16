package eu.jaloszynski.splitit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import eu.jaloszynski.splitit.R;
import eu.jaloszynski.splitit.helpers.BitmapManager;
import eu.jaloszynski.splitit.helpers.OnItemClickListener;
import eu.jaloszynski.splitit.persistence.Expense;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Expense> expenses;

    private final OnItemClickListener listener;

    public ExpenseListAdapter(Context context, OnItemClickListener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
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
        TextView tvValue = expenseViewHolder.valueViewHolder;
        ImageView imageView = expenseViewHolder.image;

        expenseViewHolder.bind(expenses.get(i), listener);

        if (expenses != null) {
            tvName.setText(expenses.get(i).getName());
            tvValue.setText(expenses.get(i).getValue());
            if(expenses.get(i).getImage()!=null) imageView.setImageBitmap(BitmapManager.byteToBitmap(expenses.get(i).getImage()));
            //TODO: More then one expanse


        } else {
            tvName.setText("N/A");
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
        TextView expenseViewHolder;
        TextView valueViewHolder;
        ImageView image;


        public ExpenseViewHolder(View itemView) {
            super(itemView);
            expenseViewHolder = itemView.findViewById(R.id.tvName);
            valueViewHolder = itemView.findViewById(R.id.tvValueAll);
            image = itemView.findViewById(R.id.profile_image);
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

