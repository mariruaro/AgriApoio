package br.com.marianarv.agriapoio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;

public class PostListActivity extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_register);

        sharedPreferences = getSharedPreferences("SortSettings",MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort","novo");

        if(mSorting.equals("novo")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }else if(mSorting.equals("antigo")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference("Data");
    }

    //Search Data
    private void firebaseSearch(String searchTxt){
        Query firebaseSearchQuery = mReference.orderByChild("title").startAt(searchTxt).endAt(searchTxt+"\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.post_row,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),
                                model.getImage(),model.getDate(),
                                model.getHour(),model.getLocation(),model.getDescription());
                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View mView, int position) {
                                String mTitle = getItem(position).getTitle();
                                String mDate = getItem(position).getDate();
                                String mHour = getItem(position).getHour();
                                String mLocation = getItem(position).getLocation();
                                String mDescription = getItem(position).getDescription();
                                String mImage = getItem(position).getImage();
                                String mKey = getItem(position).getKey();

                                Intent intent = new Intent(mView.getContext(),PostDetailActivity.class);
                                intent.putExtra("image",mImage);
                                intent.putExtra("title",mTitle);
                                intent.putExtra("date",mDate);
                                intent.putExtra("hour",mHour);
                                intent.putExtra("location",mLocation);
                                intent.putExtra("description",mDescription);
                                intent.putExtra("key",mKey);
                                startActivity(intent);

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });

                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //Load Data
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.post_row,
                        ViewHolder.class,
                        mReference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),
                                model.getImage(),model.getDate(),
                                model.getHour(),model.getLocation(),model.getDescription());
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View mView, int position) {
                                String mTitle = getItem(position).getTitle();
                                String mDate = getItem(position).getDate();
                                String mHour = getItem(position).getHour();
                                String mLocation = getItem(position).getLocation();
                                String mDescription = getItem(position).getDescription();
                                String mImage = getItem(position).getImage();
                                String mKey = getItem(position).getKey();

                                Intent intent = new Intent(mView.getContext(),PostDetailActivity.class);
                                intent.putExtra("image",mImage);
                                intent.putExtra("title",mTitle);
                                intent.putExtra("date",mDate);
                                intent.putExtra("hour",mHour);
                                intent.putExtra("location",mLocation);
                                intent.putExtra("description",mDescription);
                                intent.putExtra("key",mKey);
                                startActivity(intent);

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });

                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter as you type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.acction_settings){
            showSortDialogue();
            return true;
        }

        if(id==R.id.acction_add){
            startActivity(new Intent(PostListActivity.this,AddPostActivity.class));
            return true;
        }

        if(id==R.id.acction_temp){
            startActivity(new Intent(PostListActivity.this,WeatherController.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //Ordenar Registros
    private void showSortDialogue() {
        String [] sortOptions={"Mais Novo", "Mais Antigo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ordenar por")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort","novo");
                            editor.apply();
                            recreate();
                        }else{
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort","antigo");
                            editor.apply();
                            recreate();
                        }
                    }
                });
        builder.show();
    }
}
