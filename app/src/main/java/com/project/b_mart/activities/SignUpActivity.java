package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.project.b_mart.utils.SharedPreferencesUtils;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputAndDoSignUp();
            }
        });
    }

    private void checkInputAndDoSignUp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

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

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.setError("Enter confirm password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Confirm password does not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        doSignUp(email, password);
    }

    private void doSignUp(final String email, final String password) {
        progressDialog.show();
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            SharedPreferencesUtils.saveString(SignUpActivity.this, SharedPreferencesUtils.EMAIL, email);
                            SharedPreferencesUtils.saveString(SignUpActivity.this, SharedPreferencesUtils.PASSWORD, password);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Cannot create account. Try again!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
