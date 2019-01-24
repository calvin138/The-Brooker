package forum.student.thebrooker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import android.widget.AdapterView.OnItemSelectedListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class BookPosted extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference bookref;

    private TextView viewBookTitle;
    private ImageView viewBookCover;
    private TextView viewBookDescription;
    private TextView viewBookAuthor;
    private TextView viewBookRelease;
    private TextView viewBookPrice;
    private TextView viewBookGenre;
    private String currentprice;
    private int Amount;
    private EditText bidAmount;
    private TextView viewbidder;

    String title, description, author, release, price, genre, uid, image, userFirstName, userLastName, bywho;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference userref;

    private Button viewRemovePostBtn;
    private Button addToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_posted);

        setupUiViews();


        bookref = FirebaseDatabase.getInstance().getReference().child("Books");
        firebaseAuth = FirebaseAuth.getInstance();
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

        mPost_key = getIntent().getExtras().getString("Book ID");

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userFirstName = (String)dataSnapshot.child("FirstName").getValue();
                userLastName = (String)dataSnapshot.child("LastName").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bookref.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title = (String) dataSnapshot.child("bookTitle").getValue();
                description =(String) dataSnapshot.child("descriptions").getValue();
                author = (String) dataSnapshot.child("bookAuthor").getValue();
                release = (String) dataSnapshot.child("bookRelease").getValue();
                price =(String) dataSnapshot.child("price").getValue();
                genre =(String) dataSnapshot.child("bookGenre").getValue();
                uid = (String) dataSnapshot.child("uid").getValue();
                image = (String) dataSnapshot.child("image").getValue();
                bywho = (String) dataSnapshot.child("by").getValue();

                viewBookTitle.setText(title);
                viewBookAuthor.setText("Author : " + author);
                viewBookRelease.setText("Release Date : " + release);
                viewBookPrice.setText("Current price = $" + price);
                viewBookGenre.setText("Genre : " + genre);
                viewBookDescription.setText(description);
                if(bywho == null){
                    viewbidder.setVisibility(View.GONE);
                }else {
                    viewbidder.setText("By : " + bywho);
                }

                Picasso.get().load(image).into(viewBookCover);

                if(firebaseAuth.getCurrentUser().getUid().equals(uid)){
                    viewRemovePostBtn.setVisibility(View.VISIBLE);
                    addToCart.setVisibility(View.GONE);
                }
                else {
                    viewRemovePostBtn.setVisibility(View.GONE);
                    addToCart.setVisibility(View.VISIBLE);

                    addToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!bidAmount.getText().toString().isEmpty()) {
                                String Value = bidAmount.getText().toString();
                                int finalValue = Integer.parseInt(Value);
                                String currentprice = price.toString();
                                int finalcurrentprice = Integer.parseInt(currentprice);

                                if (finalValue > 4) {
                                    HashMap hashMap = new HashMap();
                                    int newprice = 0;

                                    newprice = finalcurrentprice + finalValue;

                                    hashMap.put("price", Integer.toString(newprice));
                                    hashMap.put("by", userFirstName +" "+ userLastName);

                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference bidref = firebaseDatabase.getReference().child("Books").child(mPost_key);
                                    bidref.updateChildren(hashMap);
                                    Toast.makeText(BookPosted.this, "Successfully Bid", Toast.LENGTH_SHORT).show();

                                    finish();
                                    startActivity(getIntent());
                                } else {
                                    Toast.makeText(BookPosted.this, "Bid must be at least $5!!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(BookPosted.this, "Enter your bid amount!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                Intent homeIntent = new Intent(BookPosted.this, BuyerActivity.class);
                Toast.makeText(BookPosted.this, "Book Deleted", Toast.LENGTH_SHORT).show();
                startActivity(homeIntent);
            }
        });

    }

    public void setupUiViews(){
        viewBookTitle = (TextView)findViewById(R.id.tv_titlebookposted);
        viewBookAuthor = (TextView)findViewById(R.id.authorbookposted);
        viewBookDescription = (TextView) findViewById(R.id.tv_descriptionbookposted);
        viewBookGenre = (TextView)findViewById(R.id.genrebookposted);
        viewBookRelease = (TextView)findViewById(R.id.releasebookposted);
        viewBookPrice = (TextView)findViewById(R.id.tv_bookpriceposted);
        viewBookCover = (ImageView)findViewById(R.id.coverbookposted);
        viewRemovePostBtn = (Button)findViewById(R.id.btn_delete);
        addToCart = (Button)findViewById(R.id.btn_forwardBidding);
        bidAmount = (EditText)findViewById(R.id.et_bidamount);
        viewbidder = (TextView)findViewById(R.id.tv_bidder);
    }
}
