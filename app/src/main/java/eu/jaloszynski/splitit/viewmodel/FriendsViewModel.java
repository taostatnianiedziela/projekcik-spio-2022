package eu.jaloszynski.splitit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import eu.jaloszynski.splitit.persistence.Expense;
import eu.jaloszynski.splitit.persistence.Friends;
import eu.jaloszynski.splitit.repository.FriendsRepository;

public class FriendsViewModel extends AndroidViewModel {

    private final FriendsRepository repository;
    private final LiveData<List<Friends>> allFriendses;

    public FriendsViewModel(@NonNull Application application) {
        super(application);
        repository = new FriendsRepository(application);
        allFriendses = repository.getAllFriendses();

    }

    public LiveData<List<Friends>> getAllFriendses() {
        return allFriendses;
    }

    public LiveData<List<Friends>> getFriendsByFid(int id) {
        return repository.getFriendsByFid(id);
    }


    public void insert(Friends friends) {
        repository.insert(friends);
    }
}
