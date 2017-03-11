package com.vingeapp.android;

import android.content.Context;
import android.provider.ContactsContract;

import android.widget.TextView;

/**
 * Created by deepankursadana on 05/03/17.
 */
public class ContactData {
    TextView textView = null;
    String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

    public ContactData(Context context) {
//        context.getSupportLoaderManager().initLoader(1, null, this);




    }




}
