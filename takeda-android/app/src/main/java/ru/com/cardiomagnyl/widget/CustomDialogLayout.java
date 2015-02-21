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
import ru.com.cardiomagnyl.util.Callback;

public class CustomDialogLayout extends FrameLayout {
    public enum DialogStandardAction {none, dismiss}

    private final Callback mOnDismissListener;
    private Dialog mDialog;

    public CustomDialogLayout(Context context) {
        super(context);
        mOnDismissListener = null;
        customDialogLayoutHelper(context, this, null, null);
    }

    public CustomDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOnDismissListener = null;
        customDialogLayoutHelper(context, this, null, null);
    }

    public CustomDialogLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mOnDismissListener = null;
        customDialogLayoutHelper(context, this, null, null);
    }

    private CustomDialogLayout(Context context,
                               View bodyView,
                               ArrayList<Pair<Object, Object>> buttonsProperties,
                               Callback onDismissListener) {
        super(context);
        mOnDismissListener = onDismissListener;
        customDialogLayoutHelper(context, createDialogView(context), bodyView, buttonsProperties);
    }

    public void setParentDialog(Dialog dialog) {
        mDialog = dialog;
    }

    public Dialog getParentDialog() {
        return mDialog;
    }

    private View createDialogView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_custom_dialog, null);
        this.removeAllViews();
        this.addView(view);
        return view;
    }

    private void customDialogLayoutHelper(Context context, View view,
                                          View bodyView,
                                          ArrayList<Pair<Object, Object>> buttonsProperties) {
        FrameLayout frameLayoutBodyHolder = (FrameLayout) view.findViewById(R.id.frameLayoutBodyHolder);
        LinearLayout linearLayoutButtonsHolder = (LinearLayout) view.findViewById(R.id.linearLayoutButtonsHolder);

        if (bodyView != null) {
            frameLayoutBodyHolder.addView(bodyView);
        }

        for (Pair<Object, Object> pair : buttonsProperties) {
            Button button;

            if (pair.first instanceof Button) {
                button = (Button) pair.first;
            } else {
                button = new Button(context);
                button.setText((CharSequence) pair.first);
            }

            if (pair.second instanceof DialogStandardAction) {
                this.setOnClickListener(button, (DialogStandardAction) pair.second);
            } else {
                this.setOnClickListener(button, (View.OnClickListener) pair.second);
            }

            button.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
            linearLayoutButtonsHolder.addView(button);
        }

        linearLayoutButtonsHolder.setWeightSum(buttonsProperties.size());
    }

    private void setOnClickListener(Button button, final View.OnClickListener onClickListener) {
        if (button != null) {
            View.OnClickListener onClickListenerInner = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) onClickListener.onClick(v);
                    if (mDialog != null) onDismiss();
                }
            };
            button.setOnClickListener(onClickListenerInner);
        }
    }

    private void setOnClickListener(Button button, DialogStandardAction dialogStandardAction) {
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
                        if (mDialog != null) onDismiss();
                        break;
                }
            }
        };

        return onClickListener;
    }

    private void onDismiss() {
        if (mOnDismissListener != null) mOnDismissListener.execute();
        mDialog.dismiss();
    }

    public static class Builder {
        private final Context mContext;
        private View mBodyView;
        // Pair: <CharSequence|Button, dialogStandardAction|View.OnClickListener>
        private ArrayList<Pair<Object, Object>> mButtonsProperties = new ArrayList<>();
        private Callback mOnDismissListener;

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
            return setButtonHelper(mContext.getText(textId), dialogStandardAction);
        }

        public Builder addButton(CharSequence text, DialogStandardAction dialogStandardAction) {
            return setButtonHelper(text, dialogStandardAction);
        }

        public Builder addButton(Button button, DialogStandardAction dialogStandardAction) {
            return setButtonHelper(button, dialogStandardAction);
        }

        public Builder addButton(int textId, View.OnClickListener onClickListener) {
            return setButtonHelper(mContext.getText(textId), onClickListener);
        }

        public Builder addButton(CharSequence text, View.OnClickListener onClickListener) {
            return setButtonHelper(text, onClickListener);
        }

        public Builder addButton(Button button, View.OnClickListener onClickListener) {
            return setButtonHelper(button, onClickListener);
        }

        public Builder setOnDismissListener(Callback onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setButtonHelper(Object buttonObject, Object onClickListener) {
            mButtonsProperties.add(new Pair(buttonObject, onClickListener));
            return this;
        }

        public CustomDialogLayout create() {
            CustomDialogLayout customDialogLayout = new CustomDialogLayout(
                    mContext,
                    mBodyView,
                    mButtonsProperties,
                    mOnDismissListener);

            return customDialogLayout;
        }
    }
}