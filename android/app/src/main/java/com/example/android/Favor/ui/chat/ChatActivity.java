package com.example.android.Favor.ui.chat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.Favor.R;
import com.example.android.Favor.data.models.Message;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {

    private ChatPresenter chatPresenter;

    private ArrayList<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private LinearLayoutManager llm;

    private Button sendButton;
    private EditText chatTextBox;

    private Button backButton;
    private ImageView imageView;
    private TextView nameTextView;
    private ImageView shareLocButton;
    private ImageView paymentButton;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private EditText priceEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userID = getIntent().getStringExtra("userID");

        chatPresenter = new ChatPresenter(this, this);
        chatPresenter.load(userID);

    }

    @Override
    protected void onStart() {
        super.onStart();
        chatPresenter.viewStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatPresenter.viewStopped();
    }

    @Override
    public void loadView() {
        setContentView(R.layout.activity_chat);

        getUIReferences();
        setUIFunctionality();

    }

    public void getUIReferences(){
        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messageListAdapter = new MessageListAdapter(this, messages);
        llm = new LinearLayoutManager(this);
        sendButton = (Button) findViewById(R.id.button_chatbox_send);
        chatTextBox = (EditText) findViewById(R.id.edittext_chatbox);
        imageView = (ImageView) findViewById(R.id.chat_image);
        nameTextView = (TextView) findViewById(R.id.chat_name);
        backButton = (Button) findViewById(R.id.chat_back_button);
        shareLocButton = (ImageView) findViewById(R.id.share_location_button);
        paymentButton = (ImageView) findViewById(R.id.payment_button);
        bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        priceEditText = (EditText) findViewById(R.id.payment_price);

    }

    public void setUIFunctionality() {
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(messageListAdapter);


        chatTextBox.setActivated(true);
        chatTextBox.setPressed(true);
        chatTextBox.setCursorVisible(true);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatTextBox.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Message box is empty", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    chatPresenter.sendMessage(message);
                    chatTextBox.getText().clear();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatPresenter.chatClosed();
                finish();
            }
        });

        shareLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

    }

    public void showAlertDialog() {
        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle("Share location?")
                .setMessage("Send a map with a marker on your current location to the recipient.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        chatPresenter.shareLocationClicked();
                    }
                })
                .setNegativeButton("NO", null)
                .show();

    }

    @Override
    public void displayUserInfo(final String name, final String imageURL) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameTextView.setText(name);
                Glide.with(getApplicationContext()).load(imageURL).into(imageView);
            }
        });

    }

    @Override
    public void loadMessages(final ArrayList messageHistory) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.clear();
                messages.addAll(messageHistory);
                messageListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void loadNewMessage(final Message newMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(newMsg);
                messageListAdapter.notifyDataSetChanged();

                // TODO: when other user sends new message, show button instead of forced scroll
                recyclerView.scrollToPosition(messages.size() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
            chatPresenter.chatClosed();
            finish();
        }
    }
}
