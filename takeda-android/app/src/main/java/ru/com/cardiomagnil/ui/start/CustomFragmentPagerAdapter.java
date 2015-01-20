package ru.com.cardiomagnil.ui.start;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;

import ru.com.cardiomagnil.ui.start.RegistrationFragment;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
    private final SparseArray<WeakReference<Fragment>> mFragmentReferences = new SparseArray<>();
    private final Class[] StartFragmentsClasses = new Class[]{
            WelcomeFragment.class,
            LoginOrRestoreFragment.class,
            RegistrationFragment.class};

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment = getFragmen(pos);
        if (fragment != null) {
            return fragment;
        }

        try {
            Constructor<?> ctor = StartFragmentsClasses[pos].getConstructor();
            fragment = (Fragment) ctor.newInstance(new Object[]{});
            putFragment(pos, fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return StartFragmentsClasses.length;
    }

    private void putFragment(int pos, Fragment fragment) {
        mFragmentReferences.put(pos, new WeakReference<Fragment>(fragment));
    }

    private Fragment getFragmen(int pos) {
        WeakReference<Fragment> weakReference = mFragmentReferences.get(pos);
        return weakReference == null ? null : weakReference.get();
    }
}
