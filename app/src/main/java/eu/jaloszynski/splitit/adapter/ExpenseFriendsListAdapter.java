package eu.jaloszynski.splitit.adapter;

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
import eu.jaloszynski.splitit.persistence.Friends;

public class ExpenseFriendsListAdapter extends RecyclerView.Adapter<ExpenseFriendsListAdapter.ViewHolder> {

    private List<Friends> friendsList;

    public ExpenseFriendsListAdapter(List<Friends> friendsList) {
        this.friendsList = friendsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item3, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Friends city = friendsList.get(position);


        holder.name.setText(city.getName());
        holder.partOfExpenses.setText("tutaj podamy ile");

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

    }
}


