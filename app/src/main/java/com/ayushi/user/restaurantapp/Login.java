package com.ayushi.user.restaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextView login,signup;
    EditText epass,email1;
     String emai,pasword;

    FirebaseAuth fauth;
    ProgressDialog pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email1=(EditText)findViewById(R.id.mail);
        epass=(EditText)findViewById(R.id.pass);
        login=(TextView)findViewById(R.id.login);
        signup=(TextView)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startloginpage();
            }
        });
        pg=new ProgressDialog(this);
        fauth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile();
            }
        });
    }

    private void profile()
    {
        emai = email1.getText().toString();
         pasword = epass.getText().toString();

        if (TextUtils.isEmpty(emai)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pasword)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("emai", "onClick: " + emai);
        Log.d("paas", "onClick: " + pasword);
        pg.setMessage("Logging you in...");
        pg.show();
        fauth.signInWithEmailAndPassword(emai,pasword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pg.dismiss();
                if (task.isSuccessful())
                {
                            finish();
                            startActivity(new Intent(Login.this,DashboardActivity.class));
                }
                else {

                    Toast.makeText(Login.this, "wrong user id password", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    private void startloginpage() {
        Intent i=new Intent(Login.this,Registration.class);
        startActivity(i);
        finish();
    }
}
