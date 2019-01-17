package forum.student.thebrooker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView signup, signin;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = (TextView)findViewById(R.id.tv_Signup);
        signin = (TextView)findViewById(R.id.tv_Signin);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

    }

    public void onTextClick(View view){
        switch (view.getId()){
            case R.id.tv_Signup:
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                break;
            case R.id.tv_Signin:
                startActivity(new Intent(MainActivity.this, SigninActivity.class));
                break;
            case R.id.tv_guest:
                startActivity(new Intent(MainActivity.this, BuyerActivity.class));
                break;
        }
    }
}
