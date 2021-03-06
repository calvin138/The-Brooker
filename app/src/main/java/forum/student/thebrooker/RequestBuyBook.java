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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestBuyBook extends AppCompatActivity {

    private EditText Booktitle;
    private EditText date;
    private EditText BookAuthor;
    private EditText BookGenre;
    private EditText BookPrice;
    private Button RequestButton;
    private String current_user_id;
    private MultiAutoCompleteTextView des;
    private String saveCurrentDate, saveCurrentTime;
    private ImageButton addCover;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;

    String title, release, author, genre, type, postdate, descriptions, uid, downloadUrl, image, price;


    private ProgressDialog loadingbar;

    private FirebaseAuth firebaseAuth;
    private StorageReference bookref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_buy_book);
        setupUiViews();

        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();
        bookref = FirebaseStorage.getInstance().getReference();

        RequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    validatePostInfo();
            }
        });
        addCover.setOnClickListener(new View.OnClickListener() {
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
            addCover.setImageURI(ImageUri);
        }
    }

    public void validatePostInfo(){

        if(ImageUri!=null){
            loadingbar.setTitle("Add new Post");
            loadingbar.setMessage("Please wait for a moment");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            sendBookDataRequestWimage();
            loadingbar.dismiss();
        }
        else
        {
            loadingbar.setTitle("Add new Post");
            loadingbar.setMessage("Please wait for a moment");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);
            sendBookDataRequestWOimage();
            loadingbar.dismiss();
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
                if(task.isSuccessful())
                {
                    downloadUrl = task.getResult().getDownloadUrl().toString();
                    Toast.makeText(RequestBuyBook.this, "Image uploaded successfully",Toast.LENGTH_SHORT).show();
                    if(Validate()) {

                        title = Booktitle.getText().toString();
                        author = BookAuthor.getText().toString();
                        genre = BookGenre.getText().toString();
                        release = date.getText().toString();
                        type = "Want to buy";
                        descriptions = des.getText().toString();
                        price = BookPrice.getText().toString();
                        image = downloadUrl;

                        Calendar calFordDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        saveCurrentDate = currentDate.format(calFordDate.getTime());
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        saveCurrentTime = currentTime.format(calFordDate.getTime());


                        postdate = saveCurrentDate.toString();
                        uid = current_user_id;

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = firebaseDatabase.getReference().child("Books").child(firebaseAuth.getCurrentUser().getUid() + saveCurrentDate + saveCurrentTime);
                        DatabaseReference mybook = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Books").child(firebaseAuth.getCurrentUser().getUid() + saveCurrentDate + saveCurrentTime);
                        saveBookBuyer ss = new saveBookBuyer(title, author, release, genre, type, postdate, descriptions, uid, image, price);
                        mybook.setValue(ss);
                        myRef.setValue(ss).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RequestBuyBook.this, "Successfully Requested", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RequestBuyBook.this, HomeActivity.class));
                            }
                        });
                    }


                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(RequestBuyBook.this,"Error" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void sendBookDataRequestWOimage(){

        if(Validate()) {

            title = Booktitle.getText().toString();
            author = BookAuthor.getText().toString();
            genre = BookGenre.getText().toString();
            release = date.getText().toString();
            type = "Want to buy";
            descriptions = des.getText().toString();
            image = downloadUrl;
            price = BookPrice.getText().toString();

            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calFordDate.getTime());


            postdate = saveCurrentDate.toString();
            uid = current_user_id;

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference().child("Books").child(firebaseAuth.getCurrentUser().getUid() + saveCurrentDate + saveCurrentTime);
            DatabaseReference mybook = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Books").child(firebaseAuth.getCurrentUser().getUid() + saveCurrentDate + saveCurrentTime);
            saveBookBuyer ss = new saveBookBuyer(title, author, release, genre, type, postdate, descriptions, uid, image, price);
            mybook.setValue(ss);
            myRef.setValue(ss).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(RequestBuyBook.this, "Successfully Requested", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RequestBuyBook.this, HomeActivity.class));
                }
            });
        }
    }

    public Boolean Validate(){
        Boolean result = false;
        if(!Booktitle.getText().toString().isEmpty() && !BookAuthor.getText().toString().isEmpty() && !BookGenre.getText().toString().isEmpty() && !date.getText().toString().isEmpty() && !BookPrice.getText().toString().isEmpty()){
            return true;
        }
        else{
            Toast.makeText(RequestBuyBook.this,"Please enter all the book detail to continue", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void setupUiViews(){
        Booktitle = (EditText)findViewById(R.id.et_BookTitleRequest);
        BookAuthor = (EditText)findViewById(R.id.et_BookAuthorRequest);
        BookGenre = (EditText)findViewById(R.id.et_BookGenreRequest);
        date = (EditText) findViewById(R.id.et_DateOfReleaseRequest);
        RequestButton = (Button)findViewById(R.id.btn_requestBook);
        des = (MultiAutoCompleteTextView)findViewById(R.id.mtv_descriptions);
        addCover = (ImageButton)findViewById(R.id.addcover);
        BookPrice = (EditText) findViewById(R.id.et_requestprice);
        loadingbar = new ProgressDialog(this);
    }
}
