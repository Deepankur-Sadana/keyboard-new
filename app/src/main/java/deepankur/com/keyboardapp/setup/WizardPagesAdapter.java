package deepankur.com.keyboardapp.setup;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class WizardPagesAdapter extends FragmentPagerAdapter {

    private static final boolean MARSHMALLOW = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    private final Fragment[] mFragments;
    WizardPagesAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new Fragment[MARSHMALLOW ? 3 :3];
        mFragments[0] = new WizardPageEnableKeyboardFragment();//done
        mFragments[1] = new WizardPageSwitchToKeyboardFragment();//done
        if (MARSHMALLOW) {
//            mFragments[2] = new WizardPermissionsFragment();//partially done, skipper ask prefs
            mFragments[2] = new WizardPageDoneAndMoreSettingsFragment();
        } else {
            mFragments[2] = new WizardPageDoneAndMoreSettingsFragment();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    public int getItemPosition(Object object) {
        //so "notifyDataSetChanged()" will cause recreation
        return POSITION_NONE;
    }
}
