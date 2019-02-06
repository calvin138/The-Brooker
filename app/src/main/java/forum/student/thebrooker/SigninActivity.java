package forum.student.thebrooker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {

    private EditText email, password;
    private Button signin;
    private String[] accountType;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userref;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setupUiViews();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userref = firebaseDatabase.getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.account_Type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        progressDialog = new ProgressDialog(this);

        if(user != null){
            finish();
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(email.getText().toString(),password.getText().toString());
            }
        });
    }

    public void setupUiViews(){
        email = (EditText)findViewById(R.id.et_Email1);
        password = (EditText)findViewById(R.id.et_Password1);
        signin = (Button)findViewById(R.id.btn_signin);
    }

    public void validate(String email, String password){

        progressDialog.setMessage("Logging in");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                    Toast.makeText(SigninActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SigninActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*public void ValidateBuyerOrSeller(){
        userref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String AccountType = dataSnapshot.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("AccountType").getValue().toString();
                    if(AccountType.toString().equals("Buyer")){
                        startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                    }
                    else if(AccountType.toString().equals("Seller")) {
                        startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

}
