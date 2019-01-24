package forum.student.thebrooker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class activity_sell_book extends AppCompatActivity {

    private EditText Booktitle;
    private EditText date;
    private EditText BookAuthor;
    private EditText BookGenre;
    private Button RequestButton;
    private String current_user_id;
    private String saveCurrentDate, saveCurrentTime;
    private EditText price1;
    private MultiAutoCompleteTextView des;
    private ImageButton addcover;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;

    String title, release, author, genre, type, postdate, price, descriptions, uid, image, downloadurl;

    private ProgressDialog loadingbar;
    private StorageReference bookref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);
        setupUiViews();

        bookref = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePostInfo();
            }
        });
        addcover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    public void openGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);

        if(requestcode==Gallery_Pick && resultcode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            addcover.setImageURI(ImageUri);
        }
    }

    public void validatePostInfo(){

        if(ImageUri!=null){
            loadingbar.setTitle("Add new Post");
            loadingbar.setMessage("Please wait for a moment");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            sendBookDataRequestWimage();
        }
        else
        {
            loadingbar.setTitle("Add new Post");
            loadingbar.setMessage("Please wait for a moment");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            sendBookDataRequestWOimage();
        }
    }

    public void sendBookDataRequestWimage(){
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        String postrandomname = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath = bookref.child("Book Covers").child(ImageUri.getLastPathSegment() + postrandomname + ".jpg");

        filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    downloadurl = task.getResult().getDownloadUrl().toString();
                    Toast.makeText(activity_sell_book.this, "Image uploaded successfully",Toast.LENGTH_SHORT).show();

                    if(Validate()){
                        Calendar calFordDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        saveCurrentDate = currentDate.format(calFordDate.getTime());

                        Calendar calFordTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        saveCurrentTime = currentTime.format(calFordTime.getTime());

                        title = Booktitle.getText().toString();
                        author = BookAuthor.getText().toString();
                        genre = BookGenre.getText().toString();
                        release = date.getText().toString();
                        price = price1.getText().toString();
                        descriptions = des.getText().toString();
                        postdate = saveCurrentDate.toString();
                        image = downloadurl;
                        type = "Want to Sell";
                        uid = current_user_id;

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = firebaseDatabase.getReference().child("Books").child(firebaseAuth.getCurrentUser().getUid() + saveCurrentDate + saveCurrentTime);
                        saveBookSeller ss = new saveBookSeller(title, author, release, genre, type, postdate, price, descriptions, uid, image);
                        myRef.setValue(ss).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(activity_sell_book.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(activity_sell_book.this, BuyerActivity.class));
                            }
                        });
                    }

                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(activity_sell_book.this,"Error" + message, Toast.LENGTH_SHORT).show();
                }

                loadingbar.dismiss();
            }
        });
    }

    public void sendBookDataRequestWOimage(){

        if(Validate()) {

            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calFordTime.getTime());

            title = Booktitle.getText().toString();
            author = BookAuthor.getText().toString();
            genre = BookGenre.getText().toString();
            release = date.getText().toString();
            price = price1.getText().toString();
            descriptions = des.getText().toString();
            postdate = saveCurrentDate.toString();
            image = downloadurl;

            type = "Want to Sell";
            uid = current_user_id;

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference().child("Books").child(firebaseAuth.getCurrentUser().getUid() + saveCurrentDate + saveCurrentTime);
            saveBookSeller ss = new saveBookSeller(title, author, release, genre, type, postdate, price, descriptions, uid, image);
            myRef.setValue(ss).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(activity_sell_book.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity_sell_book.this, BuyerActivity.class));
                }
            });
        }

        loadingbar.dismiss();
    }

    public Boolean Validate(){
        Boolean result = false;
        if(!Booktitle.getText().toString().isEmpty() && !BookAuthor.getText().toString().isEmpty() && !BookGenre.getText().toString().isEmpty() && !date.getText().toString().isEmpty() ){
            return true;
        }
        else{
            Toast.makeText(activity_sell_book.this,"Please enter all the book detail to continue", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void setupUiViews(){
        Booktitle = (EditText)findViewById(R.id.et_BookTitleRequest);
        BookAuthor = (EditText)findViewById(R.id.et_BookAuthorRequest);
        BookGenre = (EditText)findViewById(R.id.et_BookGenreRequest);
        date = (EditText) findViewById(R.id.et_DateOfReleaseRequest);
        RequestButton = (Button)findViewById(R.id.btn_requestBook);
        price1 = (EditText)findViewById(R.id.Price);
        des = (MultiAutoCompleteTextView)findViewById(R.id.mtv_descriptionss);
        addcover = (ImageButton)findViewById(R.id.addcovers);
        loadingbar = new ProgressDialog(this);
    }
}
