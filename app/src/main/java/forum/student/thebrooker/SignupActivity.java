package forum.student.thebrooker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private Button signup;
    private EditText FirstName, LastName;
    private EditText Email;
    private EditText Password;
    private EditText PhoneNum;

    String firstName, lastName, email, password, phoneNumber;

    private DatabaseReference userref;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupUiViews();

        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String user_email = Email.getText().toString().trim();
                    String user_password = Password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserData();
                                Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();
                                firebaseAuth.signOut();
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void setupUiViews(){
        signup = (Button) findViewById(R.id.btn_signup);
        FirstName = (EditText) findViewById(R.id.et_FirstName);
        LastName = (EditText) findViewById(R.id.et_LastName);
        Email = (EditText) findViewById(R.id.et_Email);
        Password = (EditText) findViewById(R.id.et_Password);
        PhoneNum = (EditText) findViewById(R.id.et_num);
    }

    public Boolean validate(){

        Boolean result = false;

        firstName = FirstName.getText().toString();
        email = Email.getText().toString();
        password = Password.getText().toString();
        lastName = LastName.getText().toString();
        phoneNumber = PhoneNum.getText().toString();

        if(firstName.isEmpty() || email.isEmpty() || password.isEmpty() || lastName.isEmpty()){
            Toast.makeText(this, "Please fill in all the detail", Toast.LENGTH_SHORT).show();
        }
        else{
            return true;
        }
        return result;
    }

    private void sendUserData(){
            HashMap hashMap = new HashMap();

            hashMap.put("Email", email);
            hashMap.put("FirstName", firstName);
            hashMap.put("LastName", lastName);
            hashMap.put("UserId", firebaseAuth.getCurrentUser().getUid());
            hashMap.put("Phone", phoneNumber);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myref = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
            myref.setValue(hashMap);
        }
}
