package ru.com.cardiomagnyl.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.com.cardiomagnyl.app.R;

public class CustomDialogLayout extends FrameLayout {
    public enum DialogStandardAction {none, dismiss}

    private Dialog mDialog;

    public CustomDialogLayout(Context context) {
        super(context);
        customDialogLayoutHelper(context, this, null, null);
    }

    public CustomDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        customDialogLayoutHelper(context, this, null, null);
    }

    public CustomDialogLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        customDialogLayoutHelper(context, this, null, null);
    }

    private CustomDialogLayout(Context context,
                               View bodyView,
                               ArrayList<Pair<CharSequence, Object>> buttonsProperties) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_custom_dialog, null);
        this.removeAllViews();
        this.addView(view);

        customDialogLayoutHelper(context, view, bodyView, buttonsProperties);
    }

    public void setParentDialog(Dialog dialog) {
        mDialog = dialog;
    }

    public Dialog getParentDialog() {
        return mDialog;
    }

    private void customDialogLayoutHelper(Context context, View view,
                                          View bodyView,
                                          ArrayList<Pair<CharSequence, Object>> buttonsProperties) {
        FrameLayout frameLayoutBodyHolder = (FrameLayout) view.findViewById(R.id.frameLayoutBodyHolder);
        LinearLayout linearLayoutButtonsHolder = (LinearLayout) view.findViewById(R.id.linearLayoutButtonsHolder);

        if (bodyView != null) {
            frameLayoutBodyHolder.addView(bodyView);
        }

        for (Pair<CharSequence, Object> pair : buttonsProperties) {
            Button button = new Button(context);

            button.setText(pair.first);
            button.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));

            if (pair.second instanceof DialogStandardAction) {
                this.setOnClickListenerFirst(button, (DialogStandardAction) pair.second);
            } else {
                this.setOnClickListenerFirst(button, (View.OnClickListener) pair.second);
            }

            linearLayoutButtonsHolder.addView(button);
        }

        linearLayoutButtonsHolder.setWeightSum(buttonsProperties.size());
    }

    private void setOnClickListenerFirst(Button button, final View.OnClickListener onClickListener) {
        if (button != null) {
            View.OnClickListener onClickListenerInner = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    if (onClickListener != null) {
                        onClickListener.onClick(v);
                    }
                }
            };
            button.setOnClickListener(onClickListenerInner);
        }
    }

    private void setOnClickListenerFirst(Button button, DialogStandardAction dialogStandardAction) {
        if (button != null) {
            button.setOnClickListener(getOnStandartActionClickListener(dialogStandardAction));
        }
    }

    private View.OnClickListener getOnStandartActionClickListener(final DialogStandardAction dialogStandardAction) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewParent view = v.getParent();
                switch (dialogStandardAction) {
                    case none:
                        break;
                    case dismiss:
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                        break;
                }
            }
        };

        return onClickListener;
    }

    public static class Builder {
        private final Context mContext;
        private View mBodyView;
        private ArrayList<Pair<CharSequence, Object>> mButtonsProperties = new ArrayList<Pair<CharSequence, Object>>();

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setBody(CharSequence text) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mBodyView = inflater.inflate(R.layout.layout_custom_dialog_textview, null);
            ((TextView) mBodyView).setText(text);
            return this;
        }

        public Builder setBody(View view) {
            mBodyView = view;
            return this;
        }

        public Builder setBody(int layoutId) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mBodyView = inflater.inflate(layoutId, null);
            return this;
        }

        public Builder addButton(int textId, DialogStandardAction dialogStandardAction) {
            return setButtonFirstHelper(mContext.getText(textId), dialogStandardAction);
        }

        public Builder addButton(CharSequence text, DialogStandardAction dialogStandardAction) {
            return setButtonFirstHelper(text, dialogStandardAction);
        }

        public Builder addButton(int textId, View.OnClickListener onClickListener) {
            return setButtonFirstHelper(mContext.getText(textId), onClickListener);
        }

        public Builder addButton(CharSequence text, View.OnClickListener onClickListener) {
            return setButtonFirstHelper(text, onClickListener);
        }

        public Builder setButtonFirstHelper(CharSequence text, Object onClickListener) {
            mButtonsProperties.add(new Pair(text, onClickListener));
            return this;
        }

        public CustomDialogLayout create() {
            CustomDialogLayout customDialogLayout = new CustomDialogLayout(
                    mContext,
                    mBodyView,
                    mButtonsProperties);

            return customDialogLayout;
        }
    }
}