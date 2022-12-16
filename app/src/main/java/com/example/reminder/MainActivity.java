package com.example.reminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder.db.Category;
import com.example.reminder.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private TextView noResulttextView;
    private RecyclerView recyclerView;
    private MainActivityViewModel viewModel;
    private CategoryListAdapter categoryListAdapter;
    private Category categoryForEdit;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setTitle("Reminder App");

        noResulttextView  = findViewById(R.id.noResult);
        recyclerView  = findViewById(R.id.recyclerView);
        findViewById(R.id.addNewCategoryImageView);

        ImageView addNew = findViewById(R.id.addNewCategoryImageView);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });

        initViewModel();
        initRecyclerView();
        viewModel.getAllCategoryList();
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryListAdapter = new CategoryListAdapter(this, this);
        recyclerView.setAdapter(categoryListAdapter);
    }
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getCategoryListObserver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if(categories == null) {
                    noResulttextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    //show in the recyclerview
                    categoryListAdapter.setCategoryList(categories);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResulttextView.setVisibility(View.GONE);
                }
            }
        });
    }


    private void showAddCategoryDialog(boolean isForEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate( R.layout.add_category_layout, null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        if(isForEdit){
            createButton.setText("Update");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterCategoryInput.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "Enter category name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isForEdit){
                    categoryForEdit.categoryName = name;
                    viewModel.updateCategory(categoryForEdit);
                } else {
                    //here we need to call view model.
                    viewModel.insertCategory(name);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
//    private void showAddCategoryDialog(boolean isForEdit) {
//
//        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
//        View dialogView = getLayoutInflater().inflate(R.layout.add_category_layout, null);
//        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
//        EditText createButton = dialogView.findViewById(R.id.createButton);
//        EditText cancelButton = dialogView.findViewById(R.id.cancelButton);
//
//        if(isForEdit){
//            createButton.setText("Update");
//            enterCategoryInput.setText(categoryForEdit.categoryName);
//        }
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name =  enterCategoryInput.getText().toString();
//                if(TextUtils.isEmpty(name)){
//                    Toast.makeText(MainActivity.this,"Enter category name", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(isForEdit){
//                    categoryForEdit.categoryName = name;
//                    viewModel.updateCategory(categoryForEdit);
//
//                }else{
//                    viewModel.insertCategory(name);
//
//                }
//
//                // here we call view model.
//
//                viewModel.insertCategory(name);
//                dialogBuilder.dismiss();
//            }
//        });
//
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.show();
//
//
//    }


    @Override
    public void itemClick(Category category) {

    }

    @Override
    public void removeItem(Category category) {

        viewModel.deleteCategory(category);

    }

    @Override
    public void editItem(Category category) {
        this.categoryForEdit = category;

        showAddCategoryDialog(true);

    }
}