package md.starlab.apartmentsevidenceapp.login.view;

import android.animation.Animator;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.cities_streets.view.CitiesAndStreetsActivity;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.login.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.login.model.LoginServerResponse;
import md.starlab.apartmentsevidenceapp.login.viewmodel.LoginViewModel;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "LoginActivity";

    private final int ANIMATION_DURATION = 1000;
    private final int ANIMATION_DURATION_FORM = 250;
    // UI Components
    @BindView(R.id.loginRootView)
    public View loginRootView;
    // Login Form Components
    @BindView(R.id.loginTitleTextView)
    public TextView loginTitleTextView;
    @BindView(R.id.loginFormRootLayout)
    public View loginFormRootLayout;
    @BindView(R.id.loginUserNameEditText)
    public EditText loginUserNameEditText;
    @BindView(R.id.loginPasswordEditText)
    public EditText loginPasswordEditText;
    @BindView(R.id.loginConfirmButton)
    public TextView loginConfirmButton;
    // Animation components
    @BindView(R.id.loginStarNetLogoImageView)
    public ImageView loginStarNetLogoImageView;
    @BindView(R.id.loginWhitCircleImageView)
    public ImageView loginWhitCircleImageView;
    @BindView(R.id.errorView)
    public LinearLayout errorView;
    @BindView(R.id.errorText)
    public TextView errorText;
    @BindView(R.id.loginUserNameTextInputLayout)
    public TextInputLayout loginUserNameTextInputLayout;
    @BindView(R.id.loginPasswordTextInputLayout)
    public TextInputLayout loginPasswordTextInputLayout;
    private boolean loadForm = true;
    private float screenW;
    private float screenH;
    private float circleSize;
    private float circleReSize = 2.0f;

    // Login ViewModel
    private LoginViewModel viewModel;

    // Indicate if screen is already shrink
    private boolean focusGained = false;


    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Init view Model
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // Init Login Form components to be invisible
        loginTitleTextView.setVisibility(View.INVISIBLE);
        loginFormRootLayout.setVisibility(View.INVISIBLE);
        loginConfirmButton.setVisibility(View.INVISIBLE);


        screenW = Dynamic.screenWidth;
        screenH = Dynamic.screenHeight;
        circleSize = getResources().getDimensionPixelSize(R.dimen.login_circle_size) * 2;
        // add Focus listener
        addFocusListener();

        initPasswordAction();

        enterBGAnimation();

    }

    private void addFocusListener() {
        loginUserNameEditText.setOnTouchListener(this);
        loginPasswordEditText.setOnTouchListener(this);
    }

    private void initPasswordAction() {
        // Handle ime Options - actionSend
        loginPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    loginConfirmButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void enterBGAnimation() {
        loginWhitCircleImageView.setX(screenW);
        loginWhitCircleImageView.animate()
                .setDuration(ANIMATION_DURATION)
                .translationX(screenW / 1.5f)
                .scaleX(circleReSize)
                .scaleY(circleReSize)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override   public void onAnimationStart(Animator animator) { }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        // Show Login Forum
                        if (true == loadForm) {
                            loadForm = false;
                            showLoginForm();
                        }
                    }
                    @Override   public void onAnimationCancel(Animator animator) { }
                    @Override   public void onAnimationRepeat(Animator animator) { }
                });

        float logoEndScale = 0.5f;
        loginStarNetLogoImageView.animate()
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .scaleX(logoEndScale)
                .scaleY(logoEndScale)
                .translationX(-screenW / 12f);
    }

    private void showLoginForm() {
        // Set title visibility to visible and animate
        loginTitleTextView.setVisibility(View.VISIBLE);
        Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        titleAnim.setDuration(75);
        titleAnim.setInterpolator(new DecelerateInterpolator());
        loginTitleTextView.startAnimation(titleAnim);

        // Set form visibility to be visible and animate
        loginFormRootLayout.setVisibility(View.VISIBLE);
        Animation formAnim = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        formAnim.setDuration(450);
        formAnim.setInterpolator(new DecelerateInterpolator());
        loginFormRootLayout.startAnimation(formAnim);

        // Set Confirm button visibility to be visible and animate
        loginConfirmButton.setVisibility(View.VISIBLE);
        Animation confirmBtnAnim = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        confirmBtnAnim.setDuration(750);
        confirmBtnAnim.setInterpolator(new DecelerateInterpolator());
        loginConfirmButton.startAnimation(confirmBtnAnim);
    }

    @OnClick(R.id.loginConfirmButton)
    protected void onConfirmClick(View view) {
        Log.d(TAG, "onConfirmClick");
        final String usr = loginUserNameEditText.getText().toString();
        final String pwd = loginPasswordEditText.getText().toString();
        setActiveForm(false);
        if (true == StringUtils.isNullOrEmpty(usr) || true == StringUtils.isNullOrEmpty(pwd)) {
            showErrorMessage(Dynamic.badRequest, false);
            setActiveForm(true);
            return;
        } else {
            replaceForum();
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        viewModel.doLogin(usr, pwd).observe(this, new Observer<DataWrapper<LoginServerResponse>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<LoginServerResponse> loginDataWrapper) {
                Log.d(TAG, "onConfirmClick onChanged");

                setActiveForm(true);

                if (null != loginDataWrapper.getThrowable()) {
                    Log.d(TAG, "onConfirmClick onChanged: " + loginDataWrapper.getThrowable().getMessage());
                    showErrorMessage(loginDataWrapper.getThrowable().getMessage(), true);
                }
                if (null != loginDataWrapper.getData()) {
                    LoginServerResponse response = loginDataWrapper.getData();
                    if (false == response.isStaff()) {
                        Toast toast = Toast.makeText(LoginActivity.this, R.string.login_not_staff_message, Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        TextView textView = view.findViewById(android.R.id.message);
                        textView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        textView.setTextColor(getResources().getColor(R.color.colorBlack));
                        toast.show();
                        return;
                    } else {
                        //Toast.makeText(LoginActivity.this, R.string.login_success_message, Toast.LENGTH_SHORT).show();
                        Dynamic.TOKEN = response.getToken();
                        Dynamic.USERNAME = usr;
                        moveToNextScreen();
                    }
                }
            }
        });
    }
    private void showErrorMessage(String message, boolean showErrorView){
        if (true == StringUtils.isNullOrEmpty(message)) {
            errorText.setText(getResources().getString(R.string.errorConnection));
        } else {
            errorText.setText(message.equals(Dynamic.badRequest) ?
                    getResources().getString(R.string.errorLogin) :
                    getResources().getString(R.string.errorConnection)
            );
        }
        if (true == showErrorView) {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void hideErrorMessage(){
        errorView.setVisibility(View.GONE);
    }

    private void setActiveForm(boolean isActive){
        loginConfirmButton.setEnabled(isActive);
        loginConfirmButton.getBackground().setTint(getResources().getColor(
                isActive ? R.color.colorYellowGold : R.color.grey_light));
        loginConfirmButton.setTextColor(getResources().getColor(
                isActive ? R.color.colorBlack : R.color.colorGray));
        loginUserNameEditText.setEnabled(isActive);
        loginPasswordEditText.setEnabled(isActive);
        loginUserNameTextInputLayout.setEnabled(isActive);
        loginPasswordTextInputLayout.setEnabled(isActive);
        if(!isActive){
            loginUserNameEditText.clearFocus();
            loginPasswordEditText.clearFocus();
            loginUserNameTextInputLayout.clearFocus();
            loginPasswordTextInputLayout.clearFocus();
        }
    }

    private void moveToNextScreen() {
        // Hide the Form components
        hideErrorMessage();
        loginTitleTextView.setVisibility(View.GONE);
        loginFormRootLayout.setVisibility(View.GONE);
        loginConfirmButton.setVisibility(View.GONE);

        loginWhitCircleImageView.animate()
//                .setDuration((long) (ANIMATION_DURATION * 0.5F))
                .setDuration((long) (ANIMATION_DURATION))
                .translationX(-circleSize)
                .scaleX(circleReSize)
                .scaleY(circleReSize)
                .setInterpolator(new AnticipateOvershootInterpolator());  // whiteCircleTargetHeight

        // Hide StarNet Logo
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                loginStarNetLogoImageView.setVisibility(View.GONE);
            }
        }, (long) (ANIMATION_DURATION * 0.1F));

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, CitiesAndStreetsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
        }, (long) (ANIMATION_DURATION * 0.5F));
    }

    // View.OnTouchListener implementation
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (false == focusGained) {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(loginUserNameEditText, InputMethodManager.SHOW_IMPLICIT);
            imm.showSoftInput(loginPasswordEditText, InputMethodManager.SHOW_IMPLICIT);
            focusGained = true;
            animateFormTitle();
            animateForm();
            animateConfirmButton();
            return true;
        }
        return false;
    }

    private void animateFormTitle() {
        int posY = -(int)(screenH / 20f);
        loginTitleTextView.animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATION_DURATION_FORM)
                .scaleX(0.75f)
                .scaleY(0.75f)
                .translationX(50)
                .translationY(posY);
    }

    private void animateConfirmButton() {
        int posY = -(int)(screenH / 2.18f);
        loginConfirmButton.animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATION_DURATION_FORM)
                .translationX(0)
                .translationY(posY);
    }

    private void animateForm() {
        int posY = -(int)(screenH / 4.0f);
        loginFormRootLayout.animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATION_DURATION_FORM)
                .translationX(0)
                .translationY(posY);
    }

    private void replaceForum() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                focusGained = false;
            }
        }, ANIMATION_DURATION);
        animateFormTitleBack();
        animateFormBack();
        animateConfirmButtonBack();
    }

    private void animateConfirmButtonBack() {
        loginConfirmButton.animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATION_DURATION_FORM)
                .translationX(0)
                .translationY(0); // back to start position
    }

    private void animateFormBack() {
        loginFormRootLayout.animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATION_DURATION_FORM)
                .translationX(0)
                .translationY(0); // back to start position
    }

    private void animateFormTitleBack() {
        loginTitleTextView.animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(ANIMATION_DURATION_FORM)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0)
                .translationY(0); // back to start position
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        replaceForum();
    }


}
