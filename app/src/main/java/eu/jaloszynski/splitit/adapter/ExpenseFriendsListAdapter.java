package eu.jaloszynski.splitit.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eu.jaloszynski.splitit.R;
import eu.jaloszynski.splitit.helpers.OnItemClickListenerFriends;
import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.Friends;

public class ExpenseFriendsListAdapter extends RecyclerView.Adapter<ExpenseFriendsListAdapter.ViewHolder> {

    private List<Expense> friendsList;
    private final OnItemClickListenerFriends listener;


    public ExpenseFriendsListAdapter(Context context, OnItemClickListenerFriends listener, List<Expense> friendsList) {
        this.friendsList = friendsList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item3, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Expense friend = friendsList.get(position);


        //holder.name.setText(friend.getName() + " " +friend.getSurname());
        holder.name.setText(friend.getName());// + " " +friend.getSurname());
        holder.partOfExpenses.setText(friend.getValue());

        holder.bind(friendsList.get(position), listener);

//        holder.description.setText(city.getDescription());
//        Picasso.get().load(city.getImageURL()).into(holder.image);

    }


    @Override
    public int getItemCount() {

        if (friendsList != null) {
            return friendsList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView name;
        public final TextView partOfExpenses;
        public final ImageView imageDelete;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.tvNameElement);
            partOfExpenses = view.findViewById(R.id.etPartOfExpenses);
            imageDelete = view.findViewById(R.id.btDeleteElement);
        }

        public void bind(final Expense item, final OnItemClickListenerFriends listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                }
            });

            partOfExpenses.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listener.afterTextChanged(item, partOfExpenses.getText().toString());
                }
            });

            imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    switch(v.getId()) {
                        case R.id.btDeleteElement:
                            listener.onItemClick(item);
                            break;
                        default:
                            // code block
                    }
                }
            });
        }

    }
}


