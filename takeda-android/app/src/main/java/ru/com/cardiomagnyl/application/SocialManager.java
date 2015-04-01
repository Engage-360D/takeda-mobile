package ru.com.cardiomagnyl.application;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.github.gorbin.asne.core.AccessToken;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestAccessTokenCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.odnoklassniki.OkSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.vk.sdk.VKScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseFragmentActivityWrapper;

public class SocialManager implements
        SocialNetworkManager.OnInitializationCompleteListener,
        OnLoginCompleteListener,
        OnRequestDetailedSocialPersonCompleteListener,
        OnRequestAccessTokenCompleteListener,
        OnPostingCompleteListener {
    private BaseFragmentActivityWrapper mFragmentActivityWrapper;

    private Object mSocialSynchroObject = new Object();
    private OnTokenReceived mOnTokenReceived;
    private String mMessage;
    private SocialNetworkManager mSocialNetworkManager;

    private int mSelectedSocialNetwork;
    private SocialPerson mSocialPerson;
    private AccessToken mAccessToken;
    private Pair<String, String> mHeadAndLink = null;

    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";

    public SocialManager(BaseFragmentActivityWrapper fragmentActivityWrapper) {
        mFragmentActivityWrapper = fragmentActivityWrapper;
    }

    public interface OnTokenReceived {
        public void execute(int networkId, SocialPerson socialPerson, AccessToken accessToken);
    }

    public interface OnMessagePosted {
        public void execute(int networkId, SocialPerson socialPerson, AccessToken accessToken);
    }

    public synchronized void initSocials(final Fragment fragment,
                                         final View socialsHolderView,
                                         final OnTokenReceived onTokenReceived,
                                         final String message) {
        synchronized (mSocialSynchroObject) {
            mOnTokenReceived = onTokenReceived;
            mMessage = message;
            mSocialPerson = null;

            View imageViewGP = socialsHolderView.findViewById(R.id.imageViewGP);
            View imageViewVK = socialsHolderView.findViewById(R.id.imageViewVK);
            View imageViewFB = socialsHolderView.findViewById(R.id.imageViewFB);
            View imageViewOK = socialsHolderView.findViewById(R.id.imageViewOK);

            imageViewGP.setOnClickListener(socialClick);
            imageViewVK.setOnClickListener(socialClick);
            imageViewFB.setOnClickListener(socialClick);
            imageViewOK.setOnClickListener(socialClick);

            //Get Keys for initiate SocialNetworks
            String OK_APP_ID = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.ok_app_id);
            String OK_PUBLIC_KEY = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.ok_public_key);
            String OK_SECRET_KEY = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.ok_secret_key);
            String VK_KEY = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.vk_app_id);

            //Chose permissions
            ArrayList<String> fbScope = new ArrayList<String>(Arrays.asList("public_profile, basic_info, email, user_birthday"));
            String[] okScope = new String[]{"VALUABLE_ACCESS"};
            String[] vkScope = new String[]{VKScope.WALL};

            //Use manager to manage SocialNetworks
            mSocialNetworkManager = (SocialNetworkManager) fragment.getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);

            //Check if manager exist
            if (mSocialNetworkManager == null) {
                mSocialNetworkManager = new SocialNetworkManager();

                //Init and add to manager GooglePlusSocialNetwork
                GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(fragment);
                mSocialNetworkManager.addSocialNetwork(gpNetwork);

                //Init and add to manager FacebookSocialNetwork
                FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(fragment, fbScope);
                mSocialNetworkManager.addSocialNetwork(fbNetwork);

                //Init and add to manager VkSocialNetwork
                VkSocialNetwork vkNetwork = new VkSocialNetwork(fragment, VK_KEY, vkScope);
                mSocialNetworkManager.addSocialNetwork(vkNetwork);

                //Init and add to manager OkSocialNetwork
                OkSocialNetwork okNetwork = new OkSocialNetwork(fragment, OK_APP_ID, OK_PUBLIC_KEY, OK_SECRET_KEY, okScope);
                mSocialNetworkManager.addSocialNetwork(okNetwork);

                //Initiate every network from mSocialNetworkManager
                mFragmentActivityWrapper.getFragmentActivity().getSupportFragmentManager().beginTransaction().add(mSocialNetworkManager, SOCIAL_NETWORK_TAG).commit();
                mSocialNetworkManager.setOnInitializationCompleteListener(this);
            } else {
                //if manager exist - get and setup login only for initialized SocialNetworks
                if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                    List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                    for (SocialNetwork socialNetwork : socialNetworks) {
                        socialNetwork.setOnLoginCompleteListener(this);
                        initSocialNetwork(socialNetwork);
                    }
                }
            }

        }
    }

    private View.OnClickListener socialClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag() instanceof Pair) {
                mHeadAndLink = (Pair<String, String>) view.getTag();
            } else {
                mHeadAndLink = null;
            }

            switch (view.getId()) {
                case R.id.imageViewGP:
                    mSelectedSocialNetwork = GooglePlusSocialNetwork.ID;
                    break;
                case R.id.imageViewVK:
                    mSelectedSocialNetwork = VkSocialNetwork.ID;
                    break;
                case R.id.imageViewFB:
                    mSelectedSocialNetwork = FacebookSocialNetwork.ID;
                    break;
                case R.id.imageViewOK:
                    mSelectedSocialNetwork = OkSocialNetwork.ID;
                    break;
            }

            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(mSelectedSocialNetwork);
            if (!socialNetwork.isConnected()) {
                if (mSelectedSocialNetwork != 0) {
                    socialNetwork.requestLogin(SocialManager.this);
                    mFragmentActivityWrapper.showProgressDialog();
                } else {
                    Toast.makeText(mFragmentActivityWrapper.getFragmentActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                }
            } else {
                getProfileAndTryToLogin(socialNetwork.getID());
            }

//            socialNetwork.requestPostMessage();
        }
    };

    private void initSocialNetwork(SocialNetwork socialNetwork) {
        if (socialNetwork.isConnected()) {
            switch (socialNetwork.getID()) {
                case GooglePlusSocialNetwork.ID: /* does nothing */
                    break;
                case FacebookSocialNetwork.ID: /* does nothing */
                    break;
                case VkSocialNetwork.ID: /* does nothing */
                    break;
                case OkSocialNetwork.ID: /* does nothing */
                    break;
            }
        }
    }

    private void getProfileAndTryToLogin(int networkId) {
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.requestDetailedCurrentPerson(this);
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        mFragmentActivityWrapper.hideProgressDialog();

        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }

    @Override
    public void onLoginSuccess(int networkId) {
        getProfileAndTryToLogin(networkId);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        mFragmentActivityWrapper.hideProgressDialog();

        if (networkId != mSelectedSocialNetwork) return;

        mSocialNetworkManager.getSocialNetwork(networkId).logout();

        if (/*FB*/    TextUtils.isEmpty(errorMessage)
            /*FB*/ || errorMessage.equals("Operation canceled")
            /*OK*/ || errorMessage.equals("ОK Login cancaled!")
            /*GP*/ || errorMessage.equals("canceled")
            /*VK*/ || errorMessage.equals("VKError (Canceled)")) return;

        if (/**/      requestID.equals(SocialNetwork.REQUEST_LOGIN)
            /*GP*/ || errorMessage.equals("error: 4")) {
            Toast.makeText(mFragmentActivityWrapper.getFragmentActivity(), mFragmentActivityWrapper.getFragmentActivity().getString(R.string.error_authorization), Toast.LENGTH_LONG).show();
        } else if (requestID.equals(SocialNetwork.REQUEST_GET_PERSON)
                || requestID.equals(SocialNetwork.REQUEST_GET_CURRENT_PERSON)
                || requestID.equals(SocialNetwork.REQUEST_GET_DETAIL_PERSON)) {
            Toast.makeText(mFragmentActivityWrapper.getFragmentActivity(), mFragmentActivityWrapper.getFragmentActivity().getString(R.string.error_getting_user_data), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mFragmentActivityWrapper.getFragmentActivity(), mFragmentActivityWrapper.getFragmentActivity().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
            if (!TextUtils.isEmpty(errorMessage)) {
                Log.d(CardiomagnylApplication.getInstance().getTag(), errorMessage);
            }
        }
    }

    @Override
    public void onRequestDetailedSocialPersonSuccess(int networkId, SocialPerson socialPerson) {
        if (networkId != mSelectedSocialNetwork) return;

        mSocialPerson = socialPerson;
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.requestAccessToken(this);
    }

    @Override
    public void onRequestAccessTokenComplete(int networkId, AccessToken accessToken) {
        synchronized (mSocialSynchroObject) {
            if (networkId == mSelectedSocialNetwork && mOnTokenReceived != null && mSocialPerson != null) {
                mAccessToken = accessToken;
                if (!TextUtils.isEmpty(mMessage)) {
                    postMessage(networkId);
                } else {
                    mOnTokenReceived.execute(networkId, mSocialPerson, mAccessToken);
                }
            }
        }
    }

    @Override
    public void onPostSuccessfully(int socialNetworkID) {
        mFragmentActivityWrapper.hideProgressDialog();

        mOnTokenReceived.execute(socialNetworkID, mSocialPerson, mAccessToken);
    }

    private void postMessage(final int networkId) {
        if (mHeadAndLink == null) return;

        Bundle postParams = new Bundle();
        postParams.putString(SocialNetwork.BUNDLE_NAME, mHeadAndLink.first);
        postParams.putString(SocialNetwork.BUNDLE_LINK, mHeadAndLink.second);
        if (networkId == GooglePlusSocialNetwork.ID) {
            mSocialNetworkManager.getSocialNetwork(networkId).requestPostDialog(postParams, SocialManager.this);
        } else {
            mSocialNetworkManager.getSocialNetwork(networkId).requestPostLink(postParams, "", SocialManager.this);
        }
    }

    private AlertDialog.Builder alertDialogInit(String title, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(mFragmentActivityWrapper.getFragmentActivity());
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setCancelable(true);
        return ad;
    }

}
