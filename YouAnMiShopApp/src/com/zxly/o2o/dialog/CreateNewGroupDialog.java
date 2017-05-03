package com.zxly.o2o.dialog;

import android.app.Activity;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zxly.o2o.activity.AddPeopleToGroupAct;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by hejun on 2016/9/6.
 * 客多多--新建会员组弹出框
 */
public class CreateNewGroupDialog extends BaseDialog implements View.OnClickListener {

    private EditText groupname_input;
    private Button btn_cancel;
    private Button btn_done;
    private String groupTitle;
    private ParameCallBack callBack;
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;

    @Override
    protected void initView() {
        groupname_input = (EditText) findViewById(R.id.groupname_input);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_done = (Button) findViewById(R.id.btn_done);
        btn_cancel.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        groupTitle=groupname_input.getHint().toString();

        groupname_input.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
                if (!resetText) {
                    cursorPos = groupname_input.getSelectionEnd();
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
                    if (count >= 2&&count<7) {//表情符号的字符长度最小为2
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (StringUtil.containsEmoji(input.toString())) {
                            resetText = true;
                            ViewUtils.showToast("暂不支持输入表情哦");
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            groupname_input.setText(inputAfterText);
                            CharSequence text = groupname_input.getText();
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
                if (temp.length() > 7) {
                    s.delete(7, s.length());
                    groupname_input.setText(s);
                    groupname_input.setSelection(s.length());
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_create_newgroup;
    }

    public void show(ParameCallBack callBack) {
        this.callBack=callBack;
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
            break;
            case R.id.btn_done:
                String trim = groupname_input.getText().toString().trim();
                if(!TextUtils.isEmpty(trim)){
                    groupTitle=groupname_input.getText().toString().trim();
                }else if(groupname_input.getText().toString().length()>0){
                        ViewUtils.showToast("新建会员组名称不能为空");
                        return;
                }
                for (int i = 0; i < Config.groupList.size(); i++) {
                    if(groupTitle.equals(Config.groupList.get(i).getName())){
                        ViewUtils.showToast("已存在相同名称会员组");
                        return;
                    }
                }
                AddPeopleToGroupAct.start((Activity) context,groupTitle,callBack);
                dismiss();

                UmengUtil.onEvent(context,new UmengUtil().HOME_NEWGROUP_SAVE_CLICK,null);

            break;
        }
    }

    @Override
    protected void setHeightAndWidth() {
        WindowManager.LayoutParams lp;
        lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected boolean isShowAnimation() {
        return false;
    }
}
