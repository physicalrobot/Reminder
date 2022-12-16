package com.example.reminder.viewmodel;

import android.app.Application;
import android.content.ClipData;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reminder.db.AppDatabase;
import com.example.reminder.db.Category;
import com.example.reminder.db.Items;

import java.util.List;

public class ShowItemListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemListActivityViewModel(Application application) {
        super(application);
        listOfItems = new MutableLiveData<>();

        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Items>>  getItemsListObserver() {
        return listOfItems;
    }

    public void getAllItemsList(int categoryID) {
        List<Items> itemsList=  appDatabase.reminderListDao().getAllItemsList(categoryID);
        if(itemsList.size() > 0)
        {
            listOfItems.postValue(itemsList);
        }else {
            listOfItems.postValue(null);
        }
    }

    public void insertItems(Items item) {

        appDatabase.reminderListDao().insertItems(item);
        getAllItemsList(item.categoryId);
    }

    public void updateItems(Items item) {
        appDatabase.reminderListDao().updateItems(item);
        getAllItemsList(item.categoryId);
    }

    public void deleteItems(Items item) {
        appDatabase.reminderListDao().deleteItem(item);
        getAllItemsList(item.categoryId);
    }

}