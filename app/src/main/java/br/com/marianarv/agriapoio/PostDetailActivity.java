package br.com.marianarv.agriapoio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PostDetailActivity extends AppCompatActivity {
    TextView mTittle, mDate, mHour, mTemp, mLocation, mDescription;
    ImageView mImage;
    Button delete;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detail);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Data");

        mTittle = (TextView) findViewById(R.id.textTitleViewDetail);
        mDate = (TextView) findViewById(R.id.txtDataViewDetail);
        mHour = (TextView) findViewById(R.id.txtHourViewDetail);
        mLocation = (TextView) findViewById(R.id.txtLocationViewDetail);
        mDescription = (TextView) findViewById(R.id.txtDescriptionViewDetail);
        mImage = (ImageView) findViewById(R.id.imageViewDetail);
        delete = (Button) findViewById(R.id.btn_delete);

        final String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String hour = getIntent().getStringExtra("hour");
        String location = getIntent().getStringExtra("location");
        String description = getIntent().getStringExtra("description");
        final String key = getIntent().getStringExtra("key");
        actionBar.setTitle(R.string.detail);

        mTittle.setText(title);
        mDate.setText(date);
        mHour.setText(hour);
        mLocation.setText(location);
        mDescription.setText(description);
        Picasso.get().load(image).into(mImage);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference imageRef = mStorage.getReferenceFromUrl(image);
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDatabaseRef.child(key).removeValue();
                        Toast.makeText(PostDetailActivity.this,"Deletado com sucesso",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostDetailActivity.this, PostListActivity.class);
                        startActivity(intent);
                        PostDetailActivity.this.finish();
                    }
                });
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
