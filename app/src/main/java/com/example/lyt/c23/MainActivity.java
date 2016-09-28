package com.example.lyt.c23;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


        TextView userInfoTextView;
        //  MenuItem addItem;
        //MenuItem exitItem;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            userInfoTextView = (TextView) findViewById(R.id.textview_userInfo);
            //  addItem = (MenuItem) findViewById(R.id.item_add);
            //    exitItem = (MenuItem) findViewById(R.id.item_exit);
            Button button2 = (Button) findViewById(R.id.button2);
            Button button = (Button) findViewById(R.id.button);
            //调用getContactInfo()方法获取联系人的信息
            String result = getContactInfo();
            userInfoTextView.setTextColor(Color.BLUE);
            userInfoTextView.setText("记录\t名字\t电话\n" + result);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result=getContactInfo();
                    userInfoTextView.setTextColor(Color.BLUE);
                    userInfoTextView.setText("记录\t名字\t电话\n"+result); }});



            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 创建一个空的ContentValues
                    ContentValues values = new ContentValues();
                    // 向RawContacts.CONTENT_URI执行一个空值插入，
                    // 目的是获取系统返回的rawContactId
                    Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                    long rawContactId = ContentUris.parseId(rawContactUri);
                    values.clear();

                    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                    // 设置内容类型
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                    // 设置联系人名字
                    values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "lytt");
                    // 向联系人URI添加联系人名字
                    getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                    values.clear();


                    values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    // 设置联系人的电话号码
                    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "12");
                    // 设置电话类型
                    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                    // 向联系人电话号码URI添加电话号码
                    getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                    values.clear();


                    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                    // 设置联系人的Email地址
                    values.put(ContactsContract.CommonDataKinds.Email.DATA, "");
                    // 设置该电子邮件的类型
                    values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                    // 向联系人Email URI添加Email数据
                    getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                    Toast.makeText(MainActivity.this, "联系人数据添加成功", 8000).show();

                }


            });


        }






        public String getContactInfo() {
            String result = "";
            ContentResolver resolver = getContentResolver();
            //查询联系人
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            // 取得联系人名字 (显示出来的名字)，实际内容在 ContactsContract.Contacts中
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            for (cursor.moveToFirst(); (!cursor.isAfterLast()); cursor.moveToNext()) {
                //获取联系人ID
                String contactId = cursor.getString(idIndex);
                result = result + contactId + "\t\t\t";
                result = result + cursor.getString(nameIndex) + "\t\t\t";
                // 根据联系人ID查询对应的电话号码
                Cursor phoneNumbers = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                // 取得电话号码(可能存在多个号码)
                while (phoneNumbers.moveToNext()) {
                    String strPhoneNumber = phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    result = result + strPhoneNumber + "\t\t\t";
                }
                phoneNumbers.close();

                // 根据联系人ID查询对应的email
                Cursor emails = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
                // 取得email(可能存在多个email)
                while (emails.moveToNext()) {
                    String strEmail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    result = result + strEmail + "\t\t\t";
                }
                emails.close();
                result = result + "\n";
            }
            cursor.close();
            return result;
        }
    }

