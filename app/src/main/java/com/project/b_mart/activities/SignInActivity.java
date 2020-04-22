package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.b_mart.R;
import com.project.b_mart.utils.Constants;

public class SignInActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        findViewById(R.id.btn_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputAndDoSignIn();
            }
        });

        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SignInActivity.this, SignUpActivity.class), Constants.SIGN_UP_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Constants.SIGN_UP_REQUEST_CODE) {
            doSignInSuccess();
        }
    }

    private void checkInputAndDoSignIn() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtEmail.requestFocus();
            edtEmail.setError("Enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.requestFocus();
            edtPassword.setError("Enter password");
            return;
        }

        doSignIn(email, password);
    }

    private void doSignIn(String email, String password) {
        progressDialog.show();
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            doSignInSuccess();
                        } else {
                            Toast.makeText(SignInActivity.this, "Cannot sign in. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void doSignInSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
