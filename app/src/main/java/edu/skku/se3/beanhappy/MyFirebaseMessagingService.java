package edu.skku.se3.beanhappy;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseInstanceIdService {
    public void onMessagingReceived(RemoteMessage remoteMessage){
        Map<String, String> bundle = remoteMessage.getData();
    }
}