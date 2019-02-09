package forum.student.thebrooker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ViewBookBuy extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference bookref;

    private TextView viewBookTitle;
    private ImageView viewBookCover;
    private TextView viewBookDescription;
    private TextView viewBookAuthor;
    private TextView viewBookRelease;
    private TextView viewBookGenre;
    private TextView viewBookpricebuyer;
    private EditText reversebidd;
    private TextView bidby;
    private TextView note;
    private TextView phone;

    private String title, description, author, release, price, genre, uid, image, bywho, bidphone;
    private String userFirstname, userLastname, currentuser;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference userref;

    private Button viewRemovePostBtn;
    private Button addToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_buy);

        setupUiViews();

        bookref = FirebaseDatabase.getInstance().getReference().child("Books");
        firebaseAuth = FirebaseAuth.getInstance();
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());


        mPost_key = getIntent().getExtras().getString("Book ID");

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userFirstname = (String)dataSnapshot.child("FirstName").getValue();
                userLastname = (String)dataSnapshot.child("LastName").getValue();
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
                genre =(String) dataSnapshot.child("bookGenre").getValue();
                uid = (String) dataSnapshot.child("uid").getValue();
                image = (String) dataSnapshot.child("image").getValue();
                price = (String) dataSnapshot.child("price").getValue();
                bywho = (String) dataSnapshot.child("by").getValue();
                bidphone = (String) dataSnapshot.child("Phone").getValue();

                phone.setText("Bidder Phone Number : " + bidphone);
                phone.setVisibility(View.GONE);
                viewBookTitle.setText(title);
                viewBookAuthor.setText("Author : " + author);
                viewBookRelease.setText("Release Date : " + release);
                viewBookGenre.setText("Genre : " + genre);
                viewBookpricebuyer.setText("Current Price is = $" + price);
                viewBookDescription.setText(description);
                if(bywho != null){
                    bidby.setVisibility(View.VISIBLE);
                    bidby.setText("by : " + bywho);
                }else if(bywho == null){
                    bidby.setVisibility(View.GONE);
                }

                Picasso.get().load(image).into(viewBookCover);

                if(firebaseAuth.getCurrentUser().getUid().equals(uid)){
                    viewRemovePostBtn.setVisibility(View.VISIBLE);
                    addToCart.setVisibility(View.GONE);
                    note.setVisibility(View.GONE);
                    reversebidd.setVisibility(View.GONE);
                    phone.setVisibility(View.VISIBLE);

                }
                else {
                    viewRemovePostBtn.setVisibility(View.GONE);
                    addToCart.setVisibility(View.VISIBLE);
                    addToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!reversebidd.getText().toString().isEmpty()){
                                int finalValue  = Integer.parseInt(reversebidd.getText().toString());
                                int currentPrice = Integer.parseInt(price.toString());
                                if (finalValue >= 1){
                                    HashMap hashMap = new HashMap();
                                    int newprice = 0;

                                    newprice = currentPrice - finalValue;

                                    if(newprice >= 0) {

                                        hashMap.put("price", Integer.toString(newprice));
                                        hashMap.put("by", userFirstname + " " + userLastname);
                                        hashMap.put("bidder UID", FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = firebaseDatabase.getReference().child("Books").child(mPost_key);
                                        ref.updateChildren(hashMap);
                                        Toast.makeText(ViewBookBuy.this, "Successfully Bid", Toast.LENGTH_SHORT).show();

                                        finish();
                                        startActivity(getIntent());
                                    } else {
                                        Toast.makeText(ViewBookBuy.this, "Price cannot be less than $0", Toast.LENGTH_SHORT);
                                    }
                                }else{

                                    Toast.makeText(ViewBookBuy.this, "Bid minimum value is $1", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(ViewBookBuy.this, "Enter your bid amount!!!", Toast.LENGTH_SHORT).show();
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
                Intent homeIntent = new Intent(ViewBookBuy.this, HomeActivity.class);
                Toast.makeText(ViewBookBuy.this, "Book Deleted", Toast.LENGTH_SHORT).show();
                startActivity(homeIntent);
            }
        });

    }

    public void setupUiViews(){
        note = (TextView)findViewById(R.id.tv_note);
        viewBookTitle = (TextView)findViewById(R.id.tv_titlebookposted1);
        viewBookAuthor = (TextView)findViewById(R.id.authorbookposted1);
        viewBookDescription = (TextView) findViewById(R.id.tv_descriptionbookposted1);
        viewBookGenre = (TextView)findViewById(R.id.genrebookposted1);
        viewBookRelease = (TextView)findViewById(R.id.releasebookposted1);
        viewBookCover = (ImageView)findViewById(R.id.coverbookposted1);
        viewRemovePostBtn = (Button)findViewById(R.id.btn_delete1);
        addToCart = (Button)findViewById(R.id.btn_reverseBidding);
        viewBookpricebuyer = (TextView)findViewById(R.id.bookpricebuyer);
        reversebidd = (EditText)findViewById(R.id.et_reversebid);
        bidby = (TextView)findViewById(R.id.tv_requestby);
        phone = (TextView) findViewById(R.id.tv_phonebuy);
    }
}
