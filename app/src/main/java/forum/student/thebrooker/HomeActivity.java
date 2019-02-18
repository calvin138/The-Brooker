package forum.student.thebrooker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button signout;
    private Button requestBook;
    private Button viewBook;
    private FirebaseAuth firebaseAuth;
    private Button requestsell;
    private Button viewyours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupUiViews();

        firebaseAuth = FirebaseAuth.getInstance();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        requestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RequestBuyBook.class));
            }
        });

        requestsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RequestSellBook.class));
            }
        });
        viewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ViewAllBook.class));
            }
        });
        viewyours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, YourBook.class));
            }
        });


    }

    public void setupUiViews(){

        signout = (Button)findViewById(R.id.btn_logout);
        requestBook = (Button)findViewById(R.id.btn_requestBook);
        requestsell = (Button)findViewById(R.id.btn_requestsellbook);
        viewBook = (Button)findViewById(R.id.btn_viewpost);
        viewyours = (Button)findViewById(R.id.viewyours);
    }
}
