package br.com.marianarv.agriapoio;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangeCityController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_city_controller);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.change_city);

        final EditText editTextField = (EditText) findViewById(R.id.queryET);


        editTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity = editTextField.getText().toString();
                Intent newCityIntent = new Intent(ChangeCityController.this,WeatherController.class);
                newCityIntent.putExtra("City",newCity);
                startActivity(newCityIntent);
                return false;
            }
        });
    }
}
