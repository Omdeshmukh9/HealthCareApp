package com.example.myapplication.VideoChat;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.event.CallEndListener;
import com.zegocloud.uikit.prebuilt.call.event.ErrorEventsListener;
import com.zegocloud.uikit.prebuilt.call.event.SignalPluginConnectListener;
import com.zegocloud.uikit.prebuilt.call.event.ZegoCallEndReason;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallInvitationData;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import com.zegocloud.uikit.service.express.IExpressEngineEventHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason;
import im.zego.zim.enums.ZIMConnectionEvent;
import im.zego.zim.enums.ZIMConnectionState;
import timber.log.Timber;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        TextView yourUserID = findViewById(R.id.your_user_id);
        TextView yourUserName = findViewById(R.id.your_user_name);

        // Retrieve the current user's data from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Extract user email and name
            String userEmail = currentUser.getEmail();
            String userName = currentUser.getEmail();

            // Use the user's email as user ID
            String userID = userEmail;

            // Display user ID and name
            yourUserID.setText("Your User ID: " + userID);
            yourUserName.setText("Your User Name: " + userEmail);

            // Initialize call invitation service
            long appID = 639358251;
            String appSign = "0a119936150265fdb6e75f1d4f572f5019e25b4b07895957eac7543502b24ccb";
            initCallInviteService(appID, appSign, userID, userName);
        }

        // Initialize buttons and request permissions
        initVoiceButton();
        initVideoButton();
        requestPermissions();
    }

    private void initVideoButton() {
        ZegoSendCallInvitationButton newVideoCall = findViewById(R.id.new_video_call);
        newVideoCall.setIsVideoCall(true);
        newVideoCall.setResourceID("zego_data");
        newVideoCall.setOnClickListener(v -> {
            TextInputLayout inputLayout = findViewById(R.id.target_user_id);
            String targetUserID = inputLayout.getEditText().getText().toString();
            String[] split = targetUserID.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
            for (String userID : split) {
                String userName = userID + "_name";
                users.add(new ZegoUIKitUser(userID, userName));
            }
            newVideoCall.setInvitees(users);
        });
    }

    private void initVoiceButton() {
        ZegoSendCallInvitationButton newVoiceCall = findViewById(R.id.new_voice_call);
        newVoiceCall.setIsVideoCall(false);
        newVoiceCall.setResourceID("zego_data");
        newVoiceCall.setOnClickListener(v -> {
            TextInputLayout inputLayout = findViewById(R.id.target_user_id);
            String targetUserID = inputLayout.getEditText().getText().toString();
            String[] split = targetUserID.split(",");
            List<ZegoUIKitUser> users = new ArrayList<>();
            for (String userID : split) {
                String userName = userID + "_name";
                users.add(new ZegoUIKitUser(userID, userName));
            }
            newVoiceCall.setInvitees(users);
        });
    }

    public void initCallInviteService(long appID, String appSign, String userID, String userName) {
        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        callInvitationConfig.provider = invitationData -> getConfig(invitationData);

        ZegoUIKitPrebuiltCallService.events.setErrorEventsListener(new ErrorEventsListener() {
            @Override
            public void onError(int errorCode, String message) {
                Timber.d("onError() called with: errorCode = [" + errorCode + "], message = [" + message + "]");
            }
        });

        ZegoUIKitPrebuiltCallService.events.invitationEvents.setPluginConnectListener(
                new SignalPluginConnectListener() {
                    @Override
                    public void onSignalPluginConnectionStateChanged(ZIMConnectionState state, ZIMConnectionEvent event,
                                                                     JSONObject extendedData) {
                        Timber.d(
                                "onSignalPluginConnectionStateChanged() called with: state = [" + state + "], event = [" + event
                                        + "], extendedData = [" + extendedData + "]");
                    }
                });

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);

        ZegoUIKitPrebuiltCallService.events.callEvents.setCallEndListener(new CallEndListener() {
            @Override
            public void onCallEnd(ZegoCallEndReason callEndReason, String jsonObject) {
                Timber.d("onCallEnd() called with: callEndReason = [" + callEndReason + "], jsonObject = [" + jsonObject
                        + "]");
            }
        });

        ZegoUIKitPrebuiltCallService.events.callEvents.setExpressEngineEventHandler(
                new IExpressEngineEventHandler() {
                    @Override
                    public void onRoomStateChanged(String roomID, ZegoRoomStateChangedReason reason, int errorCode,
                                                   JSONObject extendedData) {
                        Timber.d("onRoomStateChanged() called with: roomID = [" + roomID + "], reason = [" + reason
                                + "], errorCode = [" + errorCode + "], extendedData = [" + extendedData + "]");
                    }
                });
    }

    public ZegoUIKitPrebuiltCallConfig getConfig(ZegoCallInvitationData invitationData) {
        boolean isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.getValue();
        boolean isGroupCall = invitationData.invitees.size() > 1;
        if (isVideoCall && isGroupCall) {
            return ZegoUIKitPrebuiltCallConfig.groupVideoCall();
        } else if (!isVideoCall && isGroupCall) {
            return ZegoUIKitPrebuiltCallConfig.groupVoiceCall();
        } else if (!isVideoCall) {
            return ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall();
        } else {
            return ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall();
        }
    }

    private void requestPermissions() {
        PermissionX.init(this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
                        String message = "We need your consent for the following permissions in order to use the offline call function properly";
                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny");
                    }
                }).request((allGranted, grantedList, deniedList) -> {
                    // Handle permissions request result
                });
    }
}
