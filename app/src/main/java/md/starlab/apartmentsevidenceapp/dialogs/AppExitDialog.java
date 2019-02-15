package md.starlab.apartmentsevidenceapp.dialogs;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.login.view.LoginActivity;
import md.starlab.apartmentsevidenceapp.login.viewmodel.LoginViewModel;


public class AppExitDialog extends DialogFragment implements View.OnClickListener {

    private TextView okButton, cancelButton;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_app_exit, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setCancelable(false);

        okButton = view.findViewById(R.id.okButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        viewModel = ViewModelProviders.of((Fragment) this).get(LoginViewModel.class);
    }

    @Override
    public void onClick(View view) {
        if (R.id.okButton == view.getId()) {
            sendLogoutToServer(Dynamic.TOKEN);
//            Toast.makeText(getActivity(), "send Logout To Server", Toast.LENGTH_SHORT).show();
            // Go To login Screen
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        dismiss();
    }

    private void sendLogoutToServer(String token) {
        viewModel.logout(token);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.exit_window_width);
        int height = getResources().getDimensionPixelSize(R.dimen.exit_window_height);
        getDialog().getWindow().setLayout(width, height);
    }
}