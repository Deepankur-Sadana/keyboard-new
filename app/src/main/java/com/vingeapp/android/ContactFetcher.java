package com.vingeapp.android;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.vingeapp.android.models.ContactsModel;

import java.util.ArrayList;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class ContactFetcher {

    private static final String TAG = ContactFetcher.class.getSimpleName();


    public ArrayList<ContactsModel> getContacts(Context context) {
        ArrayList<ContactsModel> contactsModels = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = replaceAllWhiteCharacters(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        String disname = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        ContactsModel model = new ContactsModel(disname, phoneNo);
                        contactsModels.add(model);
                        Log.d(TAG, "aVoid: " + disname + " \t " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        ArrayList<ContactsModel> filteredList = filterOutDuplicateEntries(contactsModels);
        return filteredList;
    }

    private String replaceAllWhiteCharacters(String originalNumber) {
        if (originalNumber == null) return "";
        return originalNumber.replaceAll("\\s", "");
    }

    /**
     * This method will filter out all the duplicate  ebtries ie. All the
     * entries having same name and number
     *
     * @param initialList the complete list obtained from the O.S.
     * @return the filtered list
     */
    private ArrayList<ContactsModel> filterOutDuplicateEntries(ArrayList<ContactsModel> initialList) {

        ArrayList<ContactsModel> duplicateEntries = new ArrayList<>();
        for (int i = 0; i < initialList.size(); i++) {
            ContactsModel originalContact = initialList.get(i);
            for (int j = i + 1; j < initialList.size(); j++) {
                ContactsModel loopingContact = initialList.get(j);
                if (originalContact.name != null && originalContact.number != null && loopingContact.name != null && loopingContact.number != null &&
                        originalContact.name.equals(loopingContact.name) && originalContact.number.equals(loopingContact.number)) {
                    duplicateEntries.add(loopingContact);
                }
            }

        }
        initialList.removeAll(duplicateEntries);
        return initialList;
    }
}


