package forum.student.thebrooker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button signup, signin;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = (Button) findViewById(R.id.btnlogin);
        signin = (Button) findViewById(R.id.btnsignup);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

    }

    public void onButtonClick(View view){
        switch (view.getId()){
            case R.id.btnsignup:
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                break;
            case R.id.btnlogin:
                startActivity(new Intent(MainActivity.this, SigninActivity.class));
                break;
        }
    }
}