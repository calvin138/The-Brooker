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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ViewBookSell extends AppCompatActivity {

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
    private Button addtoWatch;
    private TextView note;
    private TextView Phone;

    String title, description, author, release, price, genre, uid, image, userFirstName, userLastName, bywho, bidphone, userPhone;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference userref;

    private Button viewRemovePostBtn;
    private Button addToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_sell);

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
                userPhone = (String)dataSnapshot.child("Phone").getValue();
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
                bidphone = (String)dataSnapshot.child("Phone").getValue();

                Phone.setText("Bidder Phone Number : " + bidphone);
                Phone.setVisibility(View.GONE);
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
                    bidAmount.setVisibility(View.GONE);
                    note.setVisibility(View.GONE);
                    Phone.setVisibility(View.VISIBLE);
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
                                    hashMap.put("bidder UID", firebaseAuth.getCurrentUser().getUid());
                                    hashMap.put("Phone", userPhone);

                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference bidref = firebaseDatabase.getReference().child("Books").child(mPost_key);
                                    bidref.updateChildren(hashMap);
                                    Toast.makeText(ViewBookSell.this, "Successfully Bid", Toast.LENGTH_SHORT).show();

                                    finish();
                                    startActivity(getIntent());
                                } else {
                                    Toast.makeText(ViewBookSell.this, "Bid must be at least $5!!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(ViewBookSell.this, "Enter your bid amount!!!", Toast.LENGTH_SHORT).show();
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
                userref.child("Books").child(mPost_key).removeValue();
                bookref.child(mPost_key).removeValue();
                Intent homeIntent = new Intent(ViewBookSell.this, HomeActivity.class);
                Toast.makeText(ViewBookSell.this, "Book Deleted", Toast.LENGTH_SHORT).show();
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
        note = (TextView)findViewById(R.id.tv_notesell);
        Phone = (TextView)findViewById(R.id.tv_phonesell);
    }
}



//                    addtoWatch.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            HashMap hashMap = new HashMap();
//                            hashMap.put("bookid", mPost_key);
//
//                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                            DatabaseReference ref = firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("WatchList");
//                            ref.setValue(hashMap);
//                        }
//                    });
