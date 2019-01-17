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

    String title, release, author, genre, type, postdate;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);
        setupUiViews();

        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendBookDataRequest();
            }
        });
    }

    public void sendBookDataRequest(){

        if(Validate()) {

            title = Booktitle.getText().toString();
            author = BookAuthor.getText().toString();
            genre = BookGenre.getText().toString();
            release = date.getText().toString();
            type = "Want to buy";

            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calFordDate.getTime());


            postdate = saveCurrentDate.toString();

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference().child("Books").child(firebaseAuth.getCurrentUser().getUid() + saveCurrentDate + saveCurrentTime);
            saveBook ss = new saveBook(title, author, release, genre, type, postdate);
            myRef.setValue(ss).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(RequestBook.this, "Successfully Requested", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RequestBook.this, BuyerActivity.class));
                }
            });
        }
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
