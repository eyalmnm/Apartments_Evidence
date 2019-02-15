package md.starlab.apartmentsevidenceapp.view.controllers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.view.fragments.BaseFragment;

public class NavigationController {

    private FragmentManager fragmentManager;

    public NavigationController(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * Display the given fragment without adding it to back stack
     *
     * @param fragment the new BaseFragment to be displayed
     */
    public void showFragment(BaseFragment fragment) {
        showNextFragment(fragment, false);
    }

    /**
     * Display the given fragment and add it to back stack
     *
     * @param fragment the new BaseFragment to be displayed
     */
    public void showFragmentAddToStack(BaseFragment fragment) {
        showNextFragment(fragment, true);
    }

    private void showNextFragment(BaseFragment fragment, boolean addToBackStack) {
        int exitAnim = 0;
        int enterAnim = 0;
        BaseFragment currentFragment = (BaseFragment) fragmentManager.findFragmentById(R.id.mainContentFrame);
        if (null != currentFragment) {
            exitAnim = currentFragment.getExitAnimation();
        }
        enterAnim = fragment.getEnterAnimation();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (0 != enterAnim && 0 != enterAnim) {
            transaction.setCustomAnimations(enterAnim, exitAnim);
        }
        transaction.replace(R.id.mainContentFrame, fragment);
        if (true == addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
