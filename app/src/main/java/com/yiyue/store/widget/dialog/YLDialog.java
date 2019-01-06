package com.yiyue.store.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.core.util.KeyboardUtil;
import com.yiyue.store.R;
import com.yiyue.store.util.ScreenUtils;


/**
 * the universal dialog from 1.6.1
 * <p>
 * Created by zm on 2018/10/26.
 */

public class YLDialog extends Dialog {

    /**
     * the type for normal message
     */
    public static final int DIALOG_TYPE_NORMAL = 1;
    /**
     * the type for message list
     */
    public static final int DIALOG_TYPE_MESSAGE_LIST = 2;
    /**
     * the type for can editable
     */
    public static final int DIALOG_TYPE_EDITABLE = 3;

    TextView tv_title;

    public TextView getTitle() {
        return tv_title;
    }

    TextView tv_message;

    public TextView getMessage() {
        return tv_message;
    }

    EditText et_input;

    public EditText getInputText() {
        return et_input;
    }

    public YLDialog(Context context) {
        super(context);
    }

    public YLDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public YLDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private CharSequence mTitle;
        private CharSequence mMessage;
        private CharSequence mSubMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private CharSequence mNeutralButtonText;
        private View.OnClickListener mPositiveButtonListener;
        private View.OnClickListener mNegativeButtonListener;
        private View.OnClickListener mNeutralButtonListener;

        private DialogInterface.OnClickListener mPositiveDialogInterfaceButtonListener;
        private DialogInterface.OnClickListener mNegativeDialogInterfaceButtonListener;
        private DialogInterface.OnClickListener mNeutralDialogInterfaceButtonListener;

        private ListAdapter mMessageListAdapter;

        //FIXME: support remote url
        private int mPromptResId;
        private int mTitleIconResId;
        private int mThemeResId;
        private View mCustomView;
        private int mType = YLDialog.DIALOG_TYPE_NORMAL;
        private DialogInterface.OnCancelListener mOnCancelListener;
        private DialogInterface.OnDismissListener mOnDismissListener;
        //default is true
        private boolean mCancelable = true;
        private final Context mContext;


        public Builder(Context context) {
            this(context, YLDialog.DIALOG_TYPE_NORMAL);
        }

        /**
         * @param context
         * @param type    Set Dialog type {@link YLDialog}
         */
        public Builder(Context context, int type) {
            this.mContext = context;
            this.mType = type;
        }

        /**
         * @return
         */
        public Context getContext() {
            return mContext;
        }

        /**
         * get current dialog type
         *
         * @return
         */
        public int getType() {
            return this.mType;
        }

        /**
         * set Dialog Type {@link YLDialog#DIALOG_TYPE_EDITABLE#DIALOG_TYPE_MESSAGE_LIST#DIALOG_TYPE_NORMAL}
         *
         * @param type
         * @return
         */
        public Builder setType(int type) {
            this.mType = type;
            return this;
        }

        /**
         * Set a Custom View,The Custom View parent View is a RelativeLayout
         *
         * @param view the custom view
         * @return this Builder object to allow for chaining of calls to set methods
         */
        public Builder setCustomView(View view) {
            mCustomView = view;
            return this;
        }

        /**
         * Set Message List for Multi-Message
         * <p>
         * The Message List will be added ListView
         *
         * @param adapter the Message List Adapter
         * @return this Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessageListAdapter(ListAdapter adapter) {
            mMessageListAdapter = adapter;
            return this;
        }

        /**
         * Set the title Icon
         *
         * @param titleIconResId the title icon resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitleIcon(int titleIconResId) {
            mTitleIconResId = titleIconResId;
            return this;
        }

        /**
         * Set the prompt outside pop view resource id
         *
         * @param promptResId the prompt outside pop view resource id
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPromptViewResId(int promptResId) {
            mPromptResId = promptResId;
            return this;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(int titleId) {
            mTitle = mContext.getText(titleId);
            return this;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(int messageId) {
            mMessage = mContext.getText(messageId);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        /**
         * Set a sub message for the content view
         *
         * @param subMessage the sub message
         * @return this Builder object to allow for chaining of calls to set methods
         */
        public Builder setSubMessage(CharSequence subMessage) {
            this.mSubMessage = subMessage;
            return this;
        }

        /**
         * Set the dialog theme
         *
         * @param themeResId the theme resource id
         * @return this Builder object to allow for chaining of calls to set methods
         */
        public Builder setThemeResId(int themeResId) {
            this.mThemeResId = themeResId;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
            mPositiveButtonText = mContext.getText(textId);
            mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(int textId, final DialogInterface.OnClickListener listener) {
            mPositiveButtonText = mContext.getText(textId);
            mPositiveDialogInterfaceButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveDialogInterfaceButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(int textId, final View.OnClickListener listener) {
            mNegativeButtonText = mContext.getText(textId);
            mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(int textId, final DialogInterface.OnClickListener listener) {
            mNegativeButtonText = mContext.getText(textId);
            mNegativeDialogInterfaceButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeDialogInterfaceButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the neutral button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNeutralButton(int textId, final View.OnClickListener listener) {
            mNeutralButtonText = mContext.getText(textId);
            mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the neutral button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNeutralButton(int textId, final DialogInterface.OnClickListener listener) {
            mNeutralButtonText = mContext.getText(textId);
            mNeutralDialogInterfaceButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text     The text to display in the neutral button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNeutralButton(CharSequence text, final View.OnClickListener listener) {
            mNeutralButtonText = text;
            mNeutralButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text     The text to display in the neutral button
         * @param listener The {@link DialogInterface.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNeutralButton(CharSequence text, final DialogInterface.OnClickListener listener) {
            mNeutralButtonText = text;
            mNeutralDialogInterfaceButtonListener = listener;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         * <p>
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         * setOnDismissListener}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Creates a {@link YLDialog} with the arguments supplied to this builder. It does not
         * {@link Dialog#show()} the dialog. This allows the user to do any extra processing
         * before displaying the dialog. Use {@link #show()} if you don't have any other processing
         * to do and want this to be created and displayed.
         *
         * @return
         */
        public YLDialog create() {
            final int themeResId = mThemeResId > 0 ? mThemeResId : R.style.popup_dialog;
            final YLDialog dialog = new YLDialog(mContext, themeResId);
            initDialogView(dialog, null);
            dialog.setCancelable(mCancelable);
            if (mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(mOnCancelListener);
            dialog.setOnDismissListener(mOnDismissListener);
            mDialog = dialog;
            return dialog;
        }

        /**
         * Clear dialog data
         */
        public void resetData() {
            mTitle = null;
            mMessage = null;
            mSubMessage = null;
            mPositiveButtonText = null;
            mNegativeButtonText = null;
            mNeutralButtonText = null;
        }

        /**
         * refresh dialog view ,you can do below:
         * <p>
         * 1、resetData()
         * 2、set dialog data
         * 3、refreshView() update dialog
         */
        public void refreshView() {
            initDialogView(mDialog, mRootView);
        }

        private YLDialog mDialog;
        private View mRootView;

        private void findViewById(YLDialog dialog, DialogView dialogView, View view) {
            //prompt
            dialogView.iv_prompt = (ImageView) view.findViewById(R.id.iv_prompt);
            //title
            dialog.tv_title = (TextView) view.findViewById(R.id.tv_title);
            dialog.et_input = (EditText) view.findViewById(R.id.et_input);
            dialogView.fl_title = (FrameLayout) view.findViewById(R.id.fl_title);
            dialogView.iv_title_icon = (ImageView) view.findViewById(R.id.iv_title_icon);
            //message
            dialog.tv_message = (TextView) view.findViewById(R.id.tv_message);
            dialogView.tv_sub_message = (TextView) view.findViewById(R.id.tv_sub_message);
            dialogView.rl_editable = (RelativeLayout) view.findViewById(R.id.rl_editable);
            dialogView.tv_input = (TextView) view.findViewById(R.id.tv_input);
            dialogView.fl_message = (FrameLayout) view.findViewById(R.id.fl_message);
            dialogView.ll_message = (LinearLayout) view.findViewById(R.id.ll_message);
            dialogView.listView = (ListView) view.findViewById(R.id.listView);
            //button
            dialogView.negativeButton = (Button) view.findViewById(R.id.negativeButton);
            dialogView.divider = view.findViewById(R.id.divider);
            dialogView.view_space = view.findViewById(R.id.view_space);
            dialogView.positiveButton = (Button) view.findViewById(R.id.positiveButton);
            dialogView.neutralButton = (Button) view.findViewById(R.id.neutralButton);
        }

        /**
         * Init Dialog View and set View data
         *
         * @param dialog
         */
        private void initDialogView(YLDialog dialog, View rootView) {
            View view = null;
            if (null == rootView) {
                view = LayoutInflater.from(mContext).inflate(R.layout.dialog_yl, null);
            } else {
                view = rootView;
            }
            if (mCustomView != null) {
                RelativeLayout rl_container = (RelativeLayout) view.findViewById(R.id.rl_container);
                rl_container.removeAllViews();
                rl_container.addView(mCustomView);
                dialog.setContentView(view);
                return;
            }

            if (null == rootView) {
                mRootView = view;
                dialog.setContentView(view);
            }
            DialogView dialogView = new DialogView();
            findViewById(dialog, dialogView, view);

            dialogView.iv_title_icon.setVisibility(View.GONE);
            dialog.tv_title.setVisibility(View.GONE);

            //first set title
            if (mTitleIconResId > 0) {
                dialogView.iv_title_icon.setImageResource(mTitleIconResId);
                dialogView.iv_title_icon.setVisibility(View.VISIBLE);
            } else {
                if (!TextUtils.isEmpty(mTitle)) {
                    dialog.tv_title.setVisibility(View.VISIBLE);
                    dialog.tv_title.setText(mTitle);
                }
            }
            //second set message
            if (mType == YLDialog.DIALOG_TYPE_EDITABLE) {
                //show soft input
                dialog.et_input.setHint(mMessage);
                dialogView.tv_input.setText(mSubMessage);
                dialogView.rl_editable.setVisibility(View.VISIBLE);
                dialogView.rl_editable.setOnClickListener(v -> {
                    if (!KeyboardUtil.isSoftInputOpen((Activity) mContext)) {
                        dialog.et_input.requestFocus();
                        KeyboardUtil.showSoftInput((Activity) mContext);
                    }
                });
                dialogView.ll_message.setVisibility(View.GONE);
                dialogView.listView.setVisibility(View.GONE);
                dialog.et_input.requestFocus();
                KeyboardUtil. showSoftInput((Activity) mContext);
            } else if (mType == YLDialog.DIALOG_TYPE_MESSAGE_LIST) {
                dialogView.rl_editable.setVisibility(View.GONE);
                dialogView.ll_message.setVisibility(View.GONE);
                dialogView.listView.setVisibility(View.VISIBLE);
                dialogView.listView.setAdapter(mMessageListAdapter);
            } else {
                dialogView.rl_editable.setVisibility(View.GONE);
                dialogView.ll_message.setVisibility(View.VISIBLE);
                dialogView.listView.setVisibility(View.GONE);
                //no title ,only message
                if (mTitleIconResId <= 0 && TextUtils.isEmpty(mTitle)) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dialogView.fl_message.getLayoutParams();
                    params.setMargins(0, ScreenUtils.dip2px(mContext, 24), 0, 0);
                    dialogView.fl_message.setLayoutParams(params);
                }
                if (!TextUtils.isEmpty(mMessage)) {
                    dialog.tv_message.setText(mMessage);
                    dialog.tv_message.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(mSubMessage)) {
                    dialogView.tv_sub_message.setText(mSubMessage);
                    dialogView.tv_sub_message.setVisibility(View.VISIBLE);
                }
            }
            dialogView.neutralButton.setVisibility(View.GONE);
            dialogView.positiveButton.setVisibility(View.GONE);
            dialogView.negativeButton.setVisibility(View.GONE);
            dialogView.divider.setVisibility(View.GONE);
            dialogView.view_space.setVisibility(View.GONE);
            //third set button
            if (!TextUtils.isEmpty(mNeutralButtonText)) {
                dialogView.neutralButton.setVisibility(View.VISIBLE);
                dialogView.neutralButton.setText(mNeutralButtonText);
                dialogView.neutralButton.setOnClickListener(v -> {

                    if (mType == YLDialog.DIALOG_TYPE_EDITABLE) {
                        if (KeyboardUtil.isSoftInputOpen((Activity) mContext)) {
                            KeyboardUtil.showSoftInput((Activity) mContext);
                        }
                    }
                    if (null != mNeutralButtonListener) {
                        mNeutralButtonListener.onClick(v);
                    }
                    if (null != mNeutralDialogInterfaceButtonListener) {
                        mNeutralDialogInterfaceButtonListener.onClick(dialog, 0);
                    }
                });
            } else {
                if (!TextUtils.isEmpty(mNegativeButtonText)) {
                    dialogView.view_space.setVisibility(View.VISIBLE);
                    dialogView.negativeButton.setVisibility(View.VISIBLE);
                    dialogView.negativeButton.setOnClickListener(v -> {
                        if (null != mNegativeButtonListener) {
                            mNegativeButtonListener.onClick(v);
                            return;
                        }
                        if (null != mNegativeDialogInterfaceButtonListener) {
                            mNegativeDialogInterfaceButtonListener.onClick(dialog, 0);
                        }
                    });

                    dialogView.negativeButton.setText(mNegativeButtonText);
                }

                if (!TextUtils.isEmpty(mPositiveButtonText)) {
                    dialogView.divider.setVisibility(View.VISIBLE);
                    dialogView.view_space.setVisibility(View.VISIBLE);
                    dialogView.positiveButton.setVisibility(View.VISIBLE);
                    dialogView.positiveButton.setOnClickListener(v -> {
                        if (null != mPositiveButtonListener) {
                            mPositiveButtonListener.onClick(v);
                            return;
                        }
                        if (null != mPositiveDialogInterfaceButtonListener) {
                            mPositiveDialogInterfaceButtonListener.onClick(dialog, 0);
                        }
                    });
                    dialogView.positiveButton.setText(mPositiveButtonText);
                }
            }
            // four set prompt image
            dialogView.iv_prompt.setVisibility(View.GONE);
            if (mPromptResId > 0) {
                dialogView.iv_prompt.setImageResource(mPromptResId);
                dialogView.iv_prompt.setVisibility(View.VISIBLE);
                final int marginTop = ScreenUtils.dip2px(mContext, 60);
                //if have title or message ,should be change title layout margin top
                if (!TextUtils.isEmpty(mTitle)) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dialogView.fl_title.getLayoutParams();
                    params.setMargins(0, marginTop, 0, 0);
                    dialogView.fl_title.setLayoutParams(params);
                    return;
                }
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dialogView.fl_message.getLayoutParams();
                params.setMargins(0, marginTop, 0, 0);
                dialogView.fl_message.setLayoutParams(params);
            }
        }

        /**
         * Creates a {@link YLDialog} with the arguments supplied to this builder and
         * {@link Dialog#show()}'s the dialog.
         */
        public YLDialog show() {
            YLDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    /**
     * Dialog layout view
     */
    private final static class DialogView {
        //prompt
        ImageView iv_prompt;
        //title
        FrameLayout fl_title;
        ImageView iv_title_icon;
        //message
        TextView tv_sub_message;
        FrameLayout fl_message;
        RelativeLayout rl_editable;
        TextView tv_input;
        LinearLayout ll_message;
        ListView listView;
        //button
        Button negativeButton;
        View divider;
        View view_space;
        Button positiveButton;
        Button neutralButton;
    }
}
