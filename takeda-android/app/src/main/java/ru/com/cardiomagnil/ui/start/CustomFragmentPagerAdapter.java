package ru.com.cardiomagnil.ui.start;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.lang.reflect.Constructor;

import ru.com.cardiomagnil.ui.login_or_restore.LoginOrRestoreFragment;
import ru.com.cardiomagnil.ui.registration.RegistrationFragment;

class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
    private final SparseArray<Fragment> mStartFragments = new SparseArray<>();
    private final Class[] StartFragmentsClasses = new Class[]{
            WelcomeFragment.class,
            LoginOrRestoreFragment.class,
            RegistrationFragment.class};

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment = mStartFragments.get(pos);
        if (fragment != null) {
            return fragment;
        }

        try {
            Constructor<?> ctor = StartFragmentsClasses[pos].getConstructor();
            fragment = (Fragment) ctor.newInstance(new Object[]{});
            mStartFragments.put(pos, fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return StartFragmentsClasses.length;
    }
}
