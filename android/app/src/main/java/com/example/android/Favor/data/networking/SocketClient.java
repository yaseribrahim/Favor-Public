package com.example.android.Favor.data.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import com.example.android.Favor.ui.chat.ChatContract;
import com.example.android.Favor.ui.feed.FeedContract;
import com.example.android.Favor.ui.inbox.InboxContract;
import com.example.android.Favor.ui.profile.ProfileContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by Ydot on 2018-08-01.
 */

public class SocketClient {

    private static SocketClient instance = null;
    private Socket socket;
    private SharedPreferences pref;
    private FeedContract.Presenter feedPresenter;
    private ChatContract.Presenter chatPresenter;
    private ProfileContract.ProfileBarPresenter profileBarPresenter;
    private ProfileContract.LivePostsPresenter livePostsPresenter;
    private InboxContract.Presenter inboxPresenter;
    private static Context mContext;



    private String URL = "http://192.168.1.73:3000/";



    private SocketClient(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.query = "token=" + pref.getString("token", null);
            socket = IO.socket(URL, opts);

        } catch (URISyntaxException e) {
        }
    }

    public static synchronized SocketClient getSocketClient(Context context) {
        if (instance == null) {
            instance = new SocketClient(context);
            mContext = context;
        }
        return instance;
    }

    public void setFeedPresenter(FeedContract.Presenter feedPresenter) {
        this.feedPresenter = feedPresenter;
        initFeedListeners();

    }

    public void setChatPresenter(ChatContract.Presenter chatPresenter) {
        this.chatPresenter = chatPresenter;
        initChatListeners();
    }

    public void setProfileBarPresenter(ProfileContract.ProfileBarPresenter profileBarPresenter) {
        this.profileBarPresenter = profileBarPresenter;
        initProfileBarListeners();
    }

    public void setLivePostsPresenter(ProfileContract.LivePostsPresenter livePostsPresenter) {
        this.livePostsPresenter = livePostsPresenter;
        initLivePostsListeners();
    }



    public void setInboxPresenter(InboxContract.Presenter inboxPresenter) {
        this.inboxPresenter = inboxPresenter;
        initInboxListeners();
    }



    public void startConnection () {
        socket.connect();
    }


    public void makePost(JSONObject post) {
        socket.emit("postMade", post);
    }

    public void requestFeed() {
        socket.emit("requestFeed");
    }

    public void updateLocation(JSONObject coordinates, String locality) {
        JSONObject locationObj = new JSONObject();

        try {
            locationObj.put("coordinates", coordinates);
            locationObj.put("locality", locality);

        } catch (JSONException e){}

        Log.d("locationUpdate", locationObj.toString());
        socket.emit("locationUpdate", locationObj);

    }

    public void loadChat(String userID) {
        socket.emit("loadChat", userID);
    }

    public void requestPossibleHelpers() {
        socket.emit("requestPossibleHelpers");
    }

    public void sendMessage(String userID, String message) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("receiver", userID);
            obj.put("body", message);

            Log.d("sendMessage", obj.toString());

            socket.emit("sendMessage", obj);

        }catch (JSONException e) {}
    }

    public void markAsRead(String participantID){
        try {
            JSONObject obj = new JSONObject();
            obj.put("participantID", participantID);

            socket.emit("markChatAsRead", obj);

        }catch (JSONException e) {}

    }

    public void requestAllPosts(){
        socket.emit("requestAllPosts");
    }

//    public void requestProfile(String userID) {
//        socket.emit("requestProfile", userID);
//    }

//    public void requestChats() {socket.emit("requestChats");}

    public void shareLocation(String receiverID) {
        socket.emit("shareLocation", receiverID);
    }

    public void closeChatListeners(){
        socket.off("newMessage");
    }

    public void closeConnection() {
        socket.disconnect();
    }


    private void initFeedListeners(){
        socket.on("newPost", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject)args[0];
                Log.d("newPost", obj.toString());
                feedPresenter.onNewPost(obj);
            }
        });

        socket.on("newFeed", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray array = (JSONArray) args[0];
                Log.d("newFeed", array.toString());

                feedPresenter.onNewFeed(array);

            }
        });

        socket.on("locationUpdated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                feedPresenter.onLocationUpdated();
            }
        });

        socket.on("noPostsInLocality", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                feedPresenter.onNoPostsInLocality();
            }
        });

    }

    private void initChatListeners() {
        socket.on("previousMessages", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray array = (JSONArray) args[0];
                Log.d("prevMessages", array.toString());

                chatPresenter.onPrevMessages(array);
            }
        });

        socket.on("newMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject)args[0];
//                try {
//                    Log.d("newMessages", obj.toString(4));
//                } catch (JSONException e) {}
                chatPresenter.onNewMessage(obj);

            }
        });

        socket.on("userInfo", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject)args[0];
                chatPresenter.onUserInfo(obj);
            }
        });


    }

    private void initProfileBarListeners() {
        socket.on("profileInfo", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject)args[0];
                profileBarPresenter.onProfileInfo(obj);
            }
        });
    }

    private void initLivePostsListeners() {

        socket.on("livePostsUpdate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray arr = (JSONArray) args[0];
                Log.d("LIVEPOSTRECEIVER", "true");
                livePostsPresenter.onLivePosts(arr);
            }
        });

        socket.on("possibleHelpers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray arr = (JSONArray)args[0];
                livePostsPresenter.onPossibleHelpers(arr);


            }
        });



    }

    private void initInboxListeners() {
        socket.on("chats", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray arr = (JSONArray)args[0];
                try{
                    Log.d("chatObjects", arr.toString(4));

                } catch (JSONException e){}
                inboxPresenter.onChatsReceived(arr, mContext);
            }
        });

        socket.on("chatUpdate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                try {
                    Log.d("chatUpdate", obj.toString(4));
                } catch (JSONException e) {}
                inboxPresenter.onChatUpdate(obj, mContext);
            }
        });


    }
}