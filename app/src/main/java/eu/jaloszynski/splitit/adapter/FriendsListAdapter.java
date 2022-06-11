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
import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.Friends;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Friends> friendses;
    private ItemClickListener itemClickListener;


    public FriendsListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FriendsListAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View recyclerViewItem = layoutInflater.inflate(R.layout.recyclerview_item, viewGroup, false);

        return new FriendsViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapter.FriendsViewHolder expenseViewHolder, int i) {
        TextView tvName = expenseViewHolder.friendsViewHolder;
        TextView tvSurname = expenseViewHolder.friendsViewHolder2;
        if (friendses != null) {
            tvName.setText(friendses.get(i).getName());
            tvSurname.setText(friendses.get(i).getSurname());

            //TODO: More then one expanse


        } else {
            tvName.setText("N/A");
            tvSurname.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        if (friendses != null) {
            return friendses.size();
        }
        return 0;
    }

    public void setFriendses(List<Friends> friendses) {
        this.friendses = friendses;
        notifyDataSetChanged();
    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friendsViewHolder, friendsViewHolder2;



        public FriendsViewHolder(View itemView) {
            super(itemView);
            friendsViewHolder = itemView.findViewById(R.id.tvName);
            friendsViewHolder2 = itemView.findViewById(R.id.tvExpense);
        }


        @Override
        public void onClick(View view) {

        }
    }


    public interface ItemClickListener {
        void onClick(int position,String value);
    }

}
