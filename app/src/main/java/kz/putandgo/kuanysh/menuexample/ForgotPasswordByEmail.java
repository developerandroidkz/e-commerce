package kz.putandgo.kuanysh.menuexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kuanysh on 16.02.2017.
 */

public class ForgotPasswordByEmail extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forgot_by_email, container, false);

    }

}
