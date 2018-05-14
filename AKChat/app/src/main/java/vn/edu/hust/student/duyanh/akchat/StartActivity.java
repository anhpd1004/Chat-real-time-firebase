package vn.edu.hust.student.duyanh.akchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StartActivity extends AppCompatActivity {

    private Button btDangnhap, btDangky;
    private Toolbar mToolbar;
    private ProgressBar startProgress;
    private TextClock date_time_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btDangnhap = (Button) findViewById(R.id.btDangnhap);
        btDangky = (Button) findViewById(R.id.btDangky);
        mToolbar = (Toolbar) findViewById(R.id.main_app_sidebar);
        startProgress = (ProgressBar) findViewById(R.id.start_progress);
        startProgress.setVisibility(View.INVISIBLE);

        date_time_start = (TextClock) findViewById(R.id.date_time_start);
        date_time_start.setFormat24Hour("dd-MM-yyyy HH:mm:ss");
        date_time_start.setFormat12Hour(null);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("AKChat");

        btDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(StartActivity.this, SignInActivity.class);
                startProgress.setVisibility(View.VISIBLE);
                startActivity(signInIntent);
            }
        });
        btDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startProgress.setVisibility(View.VISIBLE);
                startActivity(registerIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
