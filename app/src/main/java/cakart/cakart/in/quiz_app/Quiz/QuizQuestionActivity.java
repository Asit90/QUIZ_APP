package cakart.cakart.in.quiz_app.Quiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cakart.cakart.in.quiz_app.R;
import cakart.cakart.in.quiz_app.db.HttpHandler;
import cakart.cakart.in.quiz_app.db.MyDatabaseHelper;
import cakart.cakart.in.quiz_app.models.Answer;
import cakart.cakart.in.quiz_app.models.Question;

public class QuizQuestionActivity extends AppCompatActivity {

    int quiz_id;
    ProgressDialog pDialog;
    private String TAG = DeckListActivity.class.getSimpleName();
    ArrayList<Integer> question_ids = new ArrayList<Integer>();
    int qn_index = 0;
    public int elapsedTime = 0;
    TextView chronometer;
    Timer exm_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        quiz_id = getIntent().getExtras().getInt("quiz_id");


        chronometer = (TextView) findViewById(R.id.cmTimer);

        exm_timer = new Timer();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                "app_prefs", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("is_questions_downloaded_" + quiz_id, false)) {
           // Log.d("Akhil", "Downloaded");
            MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
            question_ids = db.get_question_ids(quiz_id);

            if (question_ids.size() > 0) {
                elapsedTime = db.get_taken_time(quiz_id);
                show_qn(question_ids.get(0));
                startTimer();
            } else {
                Toast.makeText(getApplicationContext(), "No questions here try again!", Toast.LENGTH_LONG).show();
            }
        } else {
            new getquizitem().execute();

        }


        final Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qn_index == question_ids.size() - 1) {
                    Toast.makeText(getApplicationContext(), "This is the Last question!", Toast.LENGTH_LONG).show();
                    return;
                }
                qn_index = qn_index + 1;
                show_qn(question_ids.get(qn_index));
                //chronometer.start();
            }
        });


        Button prev = (Button) findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qn_index == 0) {
                    Toast.makeText(getApplicationContext(), "This is the first question!", Toast.LENGTH_LONG).show();
                    return;
                }
                qn_index = qn_index - 1;
                show_qn(question_ids.get(qn_index));
                //chronometer.start();
            }
        });


        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(QuizQuestionActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_submit, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                //  TextView textViewattempt=(TextView) findViewById(R.id.attemptquestion);


                final Button submit = dialog.findViewById(R.id.btnDialogSubmit);
                submit.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(final View v) {

                        dialog.dismiss();
                        exm_timer.cancel();
                        MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
                        Log.d("score", "calling");
                        int sc = db.get_score(quiz_id);
                        Log.d("Calling", "attempt...........................");
                        int attemptedQuestion = db.get_attemptedQuestion(quiz_id);
                        db.set_takenTime(quiz_id, elapsedTime);
                        db.set_score(quiz_id, sc);

                        final AlertDialog alertDialog = new AlertDialog.Builder(
                                QuizQuestionActivity.this).create();

                        alertDialog.setTitle("Your Score is:" + sc);

                        int secondsLeft = elapsedTime % 3600 % 60;
                        int minutes = (int) Math.floor(elapsedTime % 3600 / 60);
                        int hours = (int) Math.floor(elapsedTime / 3600);

                        String padded = String.format("%02d:%02d:%02d" ,hours,minutes,secondsLeft);

                        alertDialog.setMessage("Time :" + padded);

                        String alert1 = "Time Taken :" + " " + padded;
                        String alert2 = "Attempted Question: " + attemptedQuestion;
                        alertDialog.setMessage(alert1 + "\n" + alert2);


                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(QuizQuestionActivity.this, "Thank You for participating", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();


                    }

                });

                Button close = dialog.findViewById(R.id.btnDialogClose);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startTimer();
                    }
                });
            }

        });


    }


    public void startTimer() {
        Log.d("Akhil", "start timer");
        exm_timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime += 1;
                QuizQuestionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int secondsLeft = elapsedTime % 3600 % 60;
                        int minutes = (int) Math.floor(elapsedTime % 3600 / 60);
                        int hours = (int) Math.floor(elapsedTime / 3600);
                        Log.d("Akhil", elapsedTime + "");
                        String padded = String.format("%02d:%02d:%02d" ,hours,minutes,secondsLeft);
                        chronometer.setText(padded);
                    }
                });
            }
        }, 0, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exm_timer != null) {
            exm_timer.cancel();
        }
    }

    public class getquizitem extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(QuizQuestionActivity.this);
            pDialog.setMessage("Downloading Questions.. Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Akhil", "show progress");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
            Log.d("Akhil", "Quiz id = " + quiz_id);
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("https://www.cakart.in/get_practice_questions.json?quiz_id=" + quiz_id);
            if (jsonStr != null) {
                try {
                    JSONArray results = new JSONArray(jsonStr);
                    for (int j = 0; j < results.length(); j++) {
                        JSONObject json = (JSONObject) results.get(j);
                        Question q = new Question();
                        q.setQn_type(json.getString("qn_type"));
                        q.setQuestion(json.getString("question"));
                        q.setQuestion_id(json.getInt("id"));
                        q.setQuiz_id(quiz_id);
                        q.setUser_ans(0);
                        db.add_question(q);

                        JSONArray optns = json.getJSONArray("ioptions");
                        for (int k = 0; k < optns.length(); k++) {
                            JSONObject optn = (JSONObject) optns.get(k);
                            Answer a = new Answer();
                            a.setAnswer(optn.getString("option"));
                            a.setQues_id(q.getQuestion_id());
                            a.setIs_correct(optn.getInt("is_correct"));
                            db.add_answer(a);
                        }
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());


                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
            question_ids = db.get_question_ids(quiz_id);
            if (question_ids.size() > 0) {
                SharedPreferences sharedPref = getSharedPreferences(
                        "app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("is_questions_downloaded_" + quiz_id, true);
                editor.putString("last_questions_downloaded_" + quiz_id, new Date().toString());
                editor.commit();
                startTimer();
                show_qn(question_ids.get(0));
            } else {
                Toast.makeText(getApplicationContext(), "No Questions in this quiz! Try Later", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void show_qn(int question_id) {


        TextView in = (TextView) findViewById(R.id.qn_index);
        in.setText("Question - " + (qn_index + 1) + " / " + question_ids.size());
        QuestionFragment f = new QuestionFragment();
        MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
        Question qn = db.get_question(question_id);
        ArrayList<Answer> ans = db.get_answers(question_id);
        Log.d("Akhil", qn.getQuestion());
        Bundle b = new Bundle();
        b.putString("qn", qn.getQuestion());
        b.putString("optn_a", ans.get(0).getAnswer());
        b.putString("optn_b", ans.get(1).getAnswer());
        b.putString("optn_c", ans.get(2).getAnswer());
        b.putString("optn_d", ans.get(3).getAnswer());
        b.putInt("qn_id", qn.getQuestion_id());
        b.putInt("user_ans", qn.getUser_ans());
        f.setArguments(b);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.question_container, f);
        ft.commit();

    }


    @Override
    public void onBackPressed() {
        Log.d("backbutton", "working..............");
        final AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestionActivity.this);
        builder.setMessage("Are you want to close this ?");
        builder.setCancelable(true);
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}

