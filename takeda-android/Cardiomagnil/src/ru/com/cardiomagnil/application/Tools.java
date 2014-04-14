package ru.com.cardiomagnil.application;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import ru.com.cardiomagnil.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Tools {
    public static void hideKeyboard(Activity currentActivity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // do nothing
        }
    }

    public static JsonElement jsonElementByMemberName(JsonObject jsonObject, String memberName) {
        if (jsonObject == null || !jsonObject.isJsonObject() || memberName == null || memberName.isEmpty()) {
            return null;
        }

        JsonElement jsonElement = null;

        if (jsonObject.has(memberName)) {
            jsonElement = jsonObject.get(memberName);
            jsonElement = jsonElement.isJsonNull() ? null : jsonElement;
        }

        return jsonElement;
    }

    public static void showAlertDialog(final Activity activity, String message, final boolean fnish) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(message)
            /**/.setCancelable(false)
            /**/.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (fnish) {
                        activity.finish();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            // TODO: ExeptionsHandler
            e.printStackTrace();
            // ExeptionsHandler.getInstatce().handleException(TheHillApplication.getAppContext(), e);
        }
    }

    public static String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formatedDate = dateFormat.format(date);
        formatedDate = Character.toUpperCase(formatedDate.charAt(0)) + formatedDate.substring(1);

        return formatedDate;
    }

    public static ViewGroup initActionBar(LayoutInflater layoutInflater, ActionBar actionBar, boolean extended) {
        ViewGroup actionBarLayout = (ViewGroup) layoutInflater.inflate(R.layout.custom_action_bar, null);
        ImageView imageViewMenuDark = (ImageView) actionBarLayout.findViewById(R.id.imageViewMenuDark);
        ImageView imageViewPerson = (ImageView) actionBarLayout.findViewById(R.id.imageViewPerson);
        ImageView imageViewBell = (ImageView) actionBarLayout.findViewById(R.id.imageViewBell);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBar.setBackgroundDrawable(layoutInflater.getContext().getResources().getDrawable(R.drawable.action_bar_background));

        if (extended) {
            imageViewMenuDark.setVisibility(View.VISIBLE);
            imageViewBell.setVisibility(View.VISIBLE);
            imageViewPerson.setVisibility(View.VISIBLE);
        } else {
            imageViewMenuDark.setVisibility(View.INVISIBLE);
            imageViewBell.setVisibility(View.INVISIBLE);
            imageViewPerson.setVisibility(View.INVISIBLE);
        }

        return actionBarLayout;
    }

    public static RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            onCheckedChangedHelper(radioGroup, i);
        }
    };

    public static void onCheckedChangedHelper(final RadioGroup radioGroup, final int i) {
        for (int j = 0; j < radioGroup.getChildCount(); j++) {
            final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
            view.setChecked(view.getId() == i);
        }
    }

    public static String getAppKeyHashB64() {
        String keyHashB64 = "";

        try {
            PackageInfo info = CardiomagnilApplication.getAppContext().getPackageManager().getPackageInfo("ru.com.cardiomagnil", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                byte[] digest = md.digest();
                keyHashB64 = Base64.encodeToString(digest, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyHashB64;
    }

    public static void setFontSegoeWP(ViewGroup viewGroup) {
        Typeface typeface = Typeface.createFromAsset(CardiomagnilApplication.getAppContext().getAssets(), CardiomagnilApplication.getAppContext().getString(R.string.segoe_wp_font));
        setFont(viewGroup, typeface);
    }

    public static void setFontSegoeWPLight(ViewGroup viewGroup) {
        Typeface typeface = Typeface.createFromAsset(CardiomagnilApplication.getAppContext().getAssets(), CardiomagnilApplication.getAppContext().getString(R.string.segoe_wp_font_light));
        setFont(viewGroup, typeface);
    }

    public static void setFont(ViewGroup viewGroup, Typeface typeface) {
        int count = viewGroup.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = viewGroup.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeface);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v, typeface);
            }
        }
    }
}
