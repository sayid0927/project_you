/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.ui;

import android.content.Intent;
import android.os.Bundle;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.easemob.easeui.widget.EaseAlertDialog.AlertDialogUser;

public class ForwardMessageActivity extends PickContactNoCheckboxActivity {
	private EaseYAMUser selectUser;
	private String forward_msg_id;

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		forward_msg_id = getIntent().getStringExtra("forward_msg_id");
	}
	
	@Override
	protected void onListItemClick(int position) {
//		if (position != 0) {
			selectUser = contactAdapter.getItem(position);
			new EaseAlertDialog(this, null, getString(R.string.confirm_forward_to, selectUser.getFirendsUserInfo().getNickname()), null, new AlertDialogUser() {
                @Override
                public void onResult(boolean confirmed, Bundle bundle) {
                    if (confirmed) {
                        if (selectUser == null)
                            return;
                        try {
                            ChatActivity.activityInstance.finish();
                        } catch (Exception e) {
                        }
                        Intent intent = new Intent(ForwardMessageActivity.this, ChatActivity.class);
                        // it is single chat

                        if(EaseConstant.shopID>0&&selectUser.getFirendsUserInfo().getIsTop()==1){
                            intent.putExtra("userId", HXApplication.getInstance().parseUserFromID(selectUser
                                    .getFirendsUserInfo().getId(), HXConstant.TAG_SHOP));
                        }else{
                            intent.putExtra("userId", HXApplication.getInstance().parseUserFromID(selectUser
                                    .getFirendsUserInfo().getId(), HXConstant.TAG_USER));
                        }

                        intent.putExtra("forward_msg_id", forward_msg_id);
                        startActivity(intent);
                        finish();
                    }
                }
            }, true).show();
//		}
	}

}
