package forum.student.thebrooker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BookPostedBuyer extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference bookref;

    private TextView viewBookTitle;
    private ImageView viewBookCover;
    private TextView viewBookDescription;
    private TextView viewBookAuthor;
    private TextView viewBookRelease;
    private TextView viewBookGenre;

    private String title, description, author, release, price, genre, uid, image;


    private FirebaseAuth firebaseAuth;

    private Button viewRemovePostBtn;
    private Button addToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_posted_buyer);

        setupUiViews();

        bookref = FirebaseDatabase.getInstance().getReference().child("Books");
        firebaseAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("Book ID");

        bookref.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title = (String) dataSnapshot.child("bookTitle").getValue();
                description =(String) dataSnapshot.child("descriptions").getValue();
                author = (String) dataSnapshot.child("bookAuthor").getValue();
                release = (String) dataSnapshot.child("bookRelease").getValue();
                genre =(String) dataSnapshot.child("bookGenre").getValue();
                uid = (String) dataSnapshot.child("uid").getValue();
                image = (String) dataSnapshot.child("image").getValue();

                viewBookTitle.setText(title);
                viewBookAuthor.setText("Author : " + author);
                viewBookRelease.setText("Release Date : " + release);
                viewBookGenre.setText("Genre : " + genre);
                viewBookDescription.setText(description);

                Picasso.get().load(image).into(viewBookCover);

                if(firebaseAuth.getCurrentUser().getUid().equals(uid)){
                    viewRemovePostBtn.setVisibility(View.VISIBLE);
                    addToCart.setVisibility(View.GONE);
                }
                else {
                    viewRemovePostBtn.setVisibility(View.GONE);
                    addToCart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        viewRemovePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookref.child(mPost_key).removeValue();
                Intent homeIntent = new Intent(BookPostedBuyer.this, BuyerActivity.class);
                Toast.makeText(BookPostedBuyer.this, "Book Deleted", Toast.LENGTH_SHORT).show();
                startActivity(homeIntent);
            }
        });

    }

    public void setupUiViews(){
        viewBookTitle = (TextView)findViewById(R.id.tv_titlebookposted1);
        viewBookAuthor = (TextView)findViewById(R.id.authorbookposted1);
        viewBookDescription = (TextView) findViewById(R.id.tv_descriptionbookposted1);
        viewBookGenre = (TextView)findViewById(R.id.genrebookposted1);
        viewBookRelease = (TextView)findViewById(R.id.releasebookposted1);
        viewBookCover = (ImageView)findViewById(R.id.coverbookposted1);
        viewRemovePostBtn = (Button)findViewById(R.id.btn_delete1);
        addToCart = (Button)findViewById(R.id.btn_reverseBidding);
    }
}
