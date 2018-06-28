package cakart.cakart.in.quiz_app.Quiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cakart.cakart.in.quiz_app.R;
import cakart.cakart.in.quiz_app.db.MyDatabaseHelper;


public class QuestionFragment extends Fragment {

    private int qn_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment, container, false);
        TextView tv = (TextView) view.findViewById(R.id.question);
        tv.setText(Html.fromHtml(getArguments().getString("qn")));
        RadioButton rba =  view.findViewById(R.id.optn_a);
        RadioButton rbb =  view.findViewById(R.id.optn_b);
        RadioButton rbc =  view.findViewById(R.id.optn_c);
        RadioButton rbd =  view.findViewById(R.id.optn_d);
        rba.setText(Html.fromHtml(getArguments().getString("optn_a")));
        rbb.setText(Html.fromHtml(getArguments().getString("optn_b")));
        rbc.setText(Html.fromHtml(getArguments().getString("optn_c")));
        rbd.setText(Html.fromHtml(getArguments().getString("optn_d")));
        qn_id = getArguments().getInt("qn_id");
        RadioGroup radioGroup = view.findViewById(R.id.RGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                int user_ans = 0;
                if (checkedId == R.id.optn_a) {
                    user_ans = 1;
                } else if (checkedId == R.id.optn_b) {
                    user_ans = 2;
                } else if (checkedId == R.id.optn_c) {
                    user_ans = 3;
                } else if (checkedId == R.id.optn_d) {
                    user_ans = 4;
                }

                MyDatabaseHelper db = new MyDatabaseHelper(getContext());
                db.store_ans(qn_id, user_ans);
            }
        });
        int user_ans = getArguments().getInt("user_ans");
        Log.d("Akhil", user_ans + "  aa ");
        if (user_ans > 0) {
            Log.d("Akhil", user_ans + "  nn ");
            ((RadioButton) radioGroup.getChildAt(user_ans - 1)).setChecked(true);
        }
        return view;


    }



}
