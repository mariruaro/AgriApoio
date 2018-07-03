package br.com.marianarv.agriapoio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PostDetailActivity extends AppCompatActivity {
    TextView mTittle, mDate, mHour, mTemp, mLocation, mDescription;
    ImageView mImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detail);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mTittle = (TextView) findViewById(R.id.textTitleViewDetail);
        mDate = (TextView) findViewById(R.id.txtDataViewDetail);
        mHour = (TextView) findViewById(R.id.txtHourViewDetail);
        mLocation = (TextView) findViewById(R.id.txtLocationViewDetail);
        mDescription = (TextView) findViewById(R.id.txtDescriptionViewDetail);
        mImage = (ImageView) findViewById(R.id.imageViewDetail);

        String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String hour = getIntent().getStringExtra("hour");
        String location = getIntent().getStringExtra("location");
        String description = getIntent().getStringExtra("description");
        actionBar.setTitle(R.string.detail);

        mTittle.setText(title);
        mDate.setText(date);
        mHour.setText(hour);
        mLocation.setText(location);
        mDescription.setText(description);
        Picasso.get().load(image).into(mImage);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
