package com.project.b_mart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.project.b_mart.R;
import com.project.b_mart.models.User;
import com.project.b_mart.utils.Constants;
import com.project.b_mart.utils.Helper;
import com.project.b_mart.utils.SharedPreferencesUtils;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);

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
        Helper.showProgressDialog(this, "Loading...");
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferencesUtils.saveString(SignUpActivity.this, SharedPreferencesUtils.EMAIL, email);
                            SharedPreferencesUtils.saveString(SignUpActivity.this, SharedPreferencesUtils.PASSWORD, password);

                            saveUserData();
                        } else {
                            Helper.dismissProgressDialog();
                            Toast.makeText(SignUpActivity.this,
                                    "Cannot create account. Try again!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Helper.dismissProgressDialog();
            Toast.makeText(this, "Fail to save user data!", Toast.LENGTH_SHORT).show();
            return;
        }

        User u = new User();
        u.setUid(user.getUid());
        u.setEmail(user.getEmail());

        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(user.getUid())
                .setValue(u)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Helper.dismissProgressDialog();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }
}
