package com.zxly.o2o.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.shyz.downloadutil.ViewUtil;
import com.zxly.o2o.activity.ShopHotArticleAct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by hejun on 2016/10/9.
 */
public class ArticleCommentDialog extends BaseDialog implements View.OnClickListener{

    private TextView txt_cancle;
    private TextView comment_sendbtn_new;
    private EditText comment_content_new;
    private CallBack callBack;
    private String content="";
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    private InputMethodManager imm;

    @Override
    protected void initView() {
        txt_cancle = (TextView) findViewById(R.id.txt_cancle);
        comment_sendbtn_new = (TextView) findViewById(R.id.comment_sendbtn_new);
        comment_content_new = (EditText) findViewById(R.id.comment_content_new);
        setHeightAndWidth();
        txt_cancle.setOnClickListener(this);
        comment_sendbtn_new.setOnClickListener(this);
        comment_content_new.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        comment_content_new.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp=s;
                if (!resetText) {
                    cursorPos = comment_content_new.getSelectionEnd();
                    // 这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText= s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    if (before != 0) {

                        return;

                    }

                    if (count >= 2&&count<200) {//表情符号的字符长度最小为2
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (containsEmoji(input.toString())) {
                            resetText = true;
                            ViewUtils.showToast("暂不支持输入表情哦");
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            comment_content_new.setText(inputAfterText);
                            CharSequence text = comment_content_new.getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, text.length());
                            }
                        }
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    comment_sendbtn_new.setTextColor(Color.parseColor("#ff5f19"));
                    if (temp.length() > 200) {
                        s.delete(200, s.length());
                        comment_content_new.setText(s);
                        comment_content_new.setSelection(s.length());
                    }
                } else {
                    comment_sendbtn_new.setTextColor(Color.parseColor("#ffb798"));
                }
            }
        });
    }
    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
    @Override
    public int getLayoutId() {
        return R.layout.win_mycircle_editinput_new;
    }

    public void show(CallBack callBack) {
        comment_content_new.setText("");
        this.callBack=callBack;
        super.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_cancle:
                dismiss();
                break;
            case R.id.comment_sendbtn_new:
                String trim = comment_content_new.getText().toString().trim();
                if(TextUtils.isEmpty(trim)){
                }else{
                    content=trim;
                    callBack.onCall();
                    dismiss();
                }
                break;
        }
    }

    public String getContent() {
        return content;
    }

    protected void setHeightAndWidth() {
        WindowManager.LayoutParams lp;
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }
}
