package com.example.raghav.chatapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class ChatScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    RelativeLayout chat_screen;
    private static int SIGN_IN_RC= 100;
    FloatingActionButton fab;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId()== R.id.menu_sign_out)
      {
          FirebaseAuth.getInstance().signOut();
      }


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SIGN_IN_RC)
        {
            if(resultCode== RESULT_OK)
            {
                Snackbar.make(chat_screen, "welcome!!",Snackbar.LENGTH_LONG).show();
                displayChatMessage();
            }

            else {
                Toast.makeText(this, "sorry", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        chat_screen=findViewById(R.id.chat_relative_screen);
        mAuth=FirebaseAuth.getInstance();
        fab= findViewById(R.id.fab);
        Snackbar.make(chat_screen,"welcome "+ mAuth.getCurrentUser().getProviderId(),Snackbar.LENGTH_LONG).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText in = findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(in.getText().toString().trim(),
                        mAuth.getCurrentUser().getEmail()));
                in.setText("");
            }
        });
        displayChatMessage();
    }

    private void displayChatMessage() {
        ListView messageList= findViewById(R.id.list_of_messages);

        adapter= new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference()) {

                @Override
                protected void populateView(View v, ChatMessage model, int position) {
                    TextView messageText,messageUser,messageTime;
                    messageText= findViewById(R.id.message_text);
                    messageUser= findViewById(R.id.message_user);
                    messageTime= findViewById(R.id.message_time);

                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };


        messageList.setAdapter(adapter);
        }
    }


