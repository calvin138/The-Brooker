package forum.student.thebrooker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyerActivity extends AppCompatActivity {

    private Button signout;
    private Button requestBook;
    private Button viewBook;
    private FirebaseAuth firebaseAuth;
    private Button requestsell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        setupUiViews();

        firebaseAuth = FirebaseAuth.getInstance();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(BuyerActivity.this, MainActivity.class));
            }
        });

        requestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyerActivity.this, RequestBook.class));
            }
        });

        requestsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyerActivity.this, activity_sell_book.class));
            }
        });
        viewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyerActivity.this, ViewBook.class));
            }
        });


    }

    public void setupUiViews(){

        signout = (Button)findViewById(R.id.btn_logout);
        requestBook = (Button)findViewById(R.id.btn_requestBook);
        requestsell = (Button)findViewById(R.id.btn_requestsellbook);
        viewBook = (Button)findViewById(R.id.btn_viewpost);
    }
}
