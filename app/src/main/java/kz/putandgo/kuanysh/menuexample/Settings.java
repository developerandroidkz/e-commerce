package kz.putandgo.kuanysh.menuexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Kuanysh on 13.03.2017.
 */

public class Settings extends Fragment {
    private  UserDatas userDatas;
    Button signout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);
        getActivity().setTitle(getResources().getString(R.string.setting));
        signout=(Button)view.findViewById(R.id.signout);
        userDatas=new UserDatas(getContext());
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDatas.deleteUser();
                Intent intent = new Intent(getContext(),Login.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
