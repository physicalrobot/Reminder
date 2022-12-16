package com.example.reminder.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderListDao {


    //categories
    @Query("Select * from Category")
    List<Category> getAllCategoriesList();

    @Insert
    void insertCategory(Category...categories);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);


    //items
    @Query("Select * from Items where categoryId = :catId")
    List<Items> getAllItemsList(int catId);

    @Insert
    void insertItems(Items items);


    @Update
    void updateItems(Items items);

    @Delete
    void deleteItem(Items items);


}
