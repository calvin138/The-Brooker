package forum.student.thebrooker;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class ViewAllBook extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference bookref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        bookref = FirebaseDatabase.getInstance().getReference().child("Books");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_book);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        GetData();


    }

    private void GetData() {
        FirebaseRecyclerAdapter<saveBookSeller, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<saveBookSeller, PostViewHolder>
                (
                        saveBookSeller.class,
                        R.layout.activity_view_all_book_adapter,
                        PostViewHolder.class,
                        bookref
                )
        {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final saveBookSeller model, int position)
            {
                final String post_key = getRef(position).getKey();

                viewHolder.setBookTitle(model.getBookTitle());
                viewHolder.setBookAuthor(model.getBookAuthor());
                viewHolder.setBookGenre(model.getBookGenre());
                viewHolder.setType(model.getType());
                viewHolder.setBookRelease(model.getBookRelease());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(model.getImage());
                viewHolder.setPostDate(model.getPostDate());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.getType().equals("Want to Sell")) {
                            Intent viewPageIntent = new Intent(ViewAllBook.this, ViewBookSell.class);
                            viewPageIntent.putExtra("Book ID", post_key);
                            startActivity(viewPageIntent);
                        }
                        else if(model.getType().equals("Want to buy")) {
                            Intent viewPageIntent = new Intent(ViewAllBook.this, ViewBookBuy.class);
                            viewPageIntent.putExtra("Book ID", post_key);
                            startActivity(viewPageIntent);
                        }
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setCountdown(String countdown){
            final TextView countdownss = (TextView)mView.findViewById(R.id.tv_countdown);
            new CountDownTimer(3600000,1000){
                public void onTick(long millisUntilFinished) {
                    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                    countdownss.setText("Time Remaining : " + time.format(millisUntilFinished));
                }

                public void onFinish() {
                    countdownss.setText("End!!!");
                }
            }.start();
        }
        public void setPostDate(String postDate){
            TextView postdate = (TextView)mView.findViewById(R.id.tv_countdown);
            postdate.setText("Posted on " + postDate);
        }

        public void setPrice(String price){
            TextView pricess = (TextView)mView.findViewById(R.id.tv_price);
            pricess.setText("Current price = $" + price);
            if (price == null){
                pricess.setVisibility(View.GONE);
            }
        }
        public void setImage(String Image){
            ImageView image = (ImageView)mView.findViewById(R.id.CoverAdapter);
            Picasso.get().load(Image).into(image);
        }

        public void setBookTitle(String bookTitle) {
            TextView bookTitless = (TextView)mView.findViewById(R.id.TitlePosted);
            bookTitless.setText(bookTitle);
        }
        public void setBookAuthor(String bookAuthor) {
            TextView bookAuthorss = (TextView)mView.findViewById(R.id.AuthorAdapter);
            bookAuthorss.setText(bookAuthor);
        }
        public void setBookRelease(String bookRelease) {
            TextView bookReleasss = (TextView)mView.findViewById(R.id.ReleaseDateAdapter);
            bookReleasss.setText(bookRelease);
        }
        public void setBookGenre(String BookGenre) {
            TextView BookGenress = (TextView)mView.findViewById(R.id.GenreAdapter);
            BookGenress.setText(BookGenre);
        }
        public void setType(String type) {
            TextView typess = (TextView)mView.findViewById(R.id.TypeAdapter);
            typess.setText(type);
            /*if(type.equals("Want to buy")){
                typess.setVisibility(View.INVISIBLE);
            }*/
        }
    }

}
