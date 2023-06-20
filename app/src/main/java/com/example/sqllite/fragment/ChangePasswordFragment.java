package com.example.sqllite.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sqllite.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    private View view;
    private EditText edt_pass;
    private Button btn_change_pass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_pasword, container, false);
        initUi();
        initListener();
        return view;
    }

    private void initUi() {
        edt_pass = view.findViewById(R.id.edt_newpass);
        btn_change_pass = view.findViewById(R.id.btn_changepass);
    }

    private void initListener() {
        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangePassword();
            }
        });
    }

    private void onClickChangePassword() {
        String password = edt_pass.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "User password updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "User password failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void reAuthenticate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    onClickChangePassword();
                } else {
                    Toast.makeText(getActivity(),"Your email or password is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
