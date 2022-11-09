package com.example.agoraproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddUserDialog extends AppCompatDialogFragment {
    private EditText name,email;
    private AddUserListener addUserListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            addUserListener = (AddUserListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must Implement Listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_add_users,null);
        builder.setView(v).setTitle("Add Friend").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = name.getText().toString();
                String useremail = email.getText().toString();
                addUserListener.applyText(username,useremail);
            }
        });
        name = v.findViewById(R.id.add_friend_name_ET);
        email = v.findViewById(R.id.add_friend_email_ET);
        return builder.create();
    }
    public interface AddUserListener{
        void applyText(String username,String email);
    }
}
