package cakart.cakart.in.quiz_app.Quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cakart.cakart.in.quiz_app.db.HttpHandler;
import cakart.cakart.in.quiz_app.models.QuizDeck;
import cakart.cakart.in.quiz_app.R;
import cakart.cakart.in.quiz_app.db.MyDatabaseHelper;

public class CaFoundationQuiz extends Fragment {
    private ProgressDialog pDialog;
    private String TAG=CaFoundationQuiz.class.getSimpleName();
    ArrayList<QuizDeck> fds;
    ListView listView;
    public boolean is_showing=false;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cafoundationquiz,container,false);
        listView=view.findViewById(R.id.quiz_list);
        //Log.d("Akhil","CA foundation quiz");
        SharedPreferences pref=getActivity().getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        if(pref.getBoolean("is_quiz_Downloaded",false)){
            showList();
        }else{
            new getquizitem().execute();
        }
        return view;

    }

    public void showList() {
        MyDatabaseHelper db=new MyDatabaseHelper(getActivity());
        fds=new ArrayList<QuizDeck>(db.getAllQuizDecks());
        if (fds.size() > 0) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(
                    "app_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("is_quiz_Downloaded", true);
            editor.putString("last_quiz_downloaded", new Date().toString());

            editor.commit();
        }

        QuizDeckAdapter itemArrayAdapter = new QuizDeckAdapter(getActivity(), R.layout.quiz_item, fds);
        listView.setAdapter(itemArrayAdapter);
    }

    public class getquizitem extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Cards.. Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... args) {
            MyDatabaseHelper db= new MyDatabaseHelper(getActivity());
            HttpHandler sh=new HttpHandler();
            String jsonStr =sh.makeServiceCall("https://www.cakart.in/get_practice_tests.json");
            if(jsonStr!=null){
                try{
                    JSONArray results= new JSONArray(jsonStr);
                    long deck_id=0;
                    for(int j=0;j<results.length();j++){
                        JSONObject json=(JSONObject)results.get(j);
                        QuizDeck fc=new QuizDeck();
                        fc.setName(json.getString("name"));
                        fc.setTestid(json.getInt("id"));
                        db.insert_quiz(fc);
                    }
                }catch(final JSONException e){
                    Log.d(TAG,"JSON parsing error:"+e.getMessage());

                }
            }else {
                Log.d(TAG,"Could not get json from server");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            showList();
        }
    }
}



