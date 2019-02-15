package md.starlab.apartmentsevidenceapp.view.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private int enterAnimation = 0; // R.anim.enter_from_right;
    private int exitAnimation = 0;  // R.anim.exit_to_left;

    private Context context;


    public void setContext(Context context, int enterAnimation, int exitAnimation) {
        this.context = context;
        this.enterAnimation = enterAnimation;
        this.exitAnimation = exitAnimation;
    }

    public Context getContext() {
        return this.context;
    }

    public int getEnterAnimation() {
        return enterAnimation;
    }

    public int getExitAnimation() {
        return exitAnimation;
    }
}
