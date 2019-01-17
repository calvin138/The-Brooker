package forum.student.thebrooker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RequestBook extends AppCompatActivity {

    private EditText Booktitle;
    private EditText date;
    private EditText BookAuthor;
    private EditText BookGenre;
    private Button RequestButton;
    private String current_user_id;
    private String saveCurrentDate, saveCurrentTime;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference bookref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);
        setupUiViews();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        bookref = firebaseDatabase.getReference().child("Books").child("BuyerRequest").child(firebaseAuth.getCurrentUser().getUid());
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendBookDataRequest();
            }
        });
    }

    public void sendBookDataRequest(){

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        String Booktitle1, date1, BookAuthor1, BookGenre1;
        Booktitle1 = Booktitle.getText().toString().trim();
        BookAuthor1 = BookAuthor.getText().toString().trim();
        BookGenre1 = BookGenre.getText().toString().trim();
        date1 = date.getText().toString().trim();


                    HashMap hashMap = new HashMap();
                    hashMap.put("BookTitle", Booktitle1);
                    hashMap.put("ReleaseDateOfBook", date1);
                    hashMap.put("BookAuthor", BookAuthor1);
                    hashMap.put("BookGenre", BookGenre1);
                    hashMap.put("RequestedUserId", current_user_id);
                    hashMap.put("Type", "Want to buy");

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myref = firebaseDatabase.getReference().child("Books").child(current_user_id + saveCurrentDate + saveCurrentTime);
                    myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(RequestBook.this, "Book Requested Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RequestBook.this, BuyerActivity.class));
                        }
                    });
    }

    public Boolean Validate(){
        Boolean result = false;
        if(!Booktitle.getText().toString().isEmpty() && !BookAuthor.getText().toString().isEmpty() && !BookGenre.getText().toString().isEmpty() && !date.getText().toString().isEmpty() ){
            return true;
        }
        else{
            Toast.makeText(RequestBook.this,"Please enter all the book detail to continue", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void setupUiViews(){
        Booktitle = (EditText)findViewById(R.id.et_BookTitleRequest);
        BookAuthor = (EditText)findViewById(R.id.et_BookAuthorRequest);
        BookGenre = (EditText)findViewById(R.id.et_BookGenreRequest);
        date = (EditText) findViewById(R.id.et_DateOfReleaseRequest);
        RequestButton = (Button)findViewById(R.id.btn_requestBook);
    }
}
