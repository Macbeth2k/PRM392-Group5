package com.example.sqllite.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private EditText edt_pass, old_pass, cf_pass;
    private Button btn_change_pass;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_pasword, container, false);
        initUi();
        initListener();
        return view;
    }

    private void initUi() {
        old_pass = view.findViewById(R.id.edt_oldpass);
        edt_pass = view.findViewById(R.id.edt_newpass);
        cf_pass = view.findViewById(R.id.edt_cfpass);
        btn_change_pass = view.findViewById(R.id.btn_changepass);
        progressBar = view.findViewById(R.id.processBar_update_password);
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
        progressBar.setVisibility(View.VISIBLE);
        String old_password = old_pass.getText().toString().trim();
        String new_password = edt_pass.getText().toString().trim();
        String cf_password = cf_pass.getText().toString().trim();
        if (!cf_password.equals(new_password)){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(),getString(R.string.cf_pass_error),Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), old_password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), getString(R.string.update_pass_success), Toast.LENGTH_LONG).show();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), getString(R.string.update_pass_fail), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    ///
                });
    }

}
