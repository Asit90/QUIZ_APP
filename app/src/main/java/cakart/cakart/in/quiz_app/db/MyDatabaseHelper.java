package cakart.cakart.in.quiz_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cakart.cakart.in.quiz_app.models.Answer;
import cakart.cakart.in.quiz_app.models.Question;
import cakart.cakart.in.quiz_app.models.QuizDeck;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME="quiz.db";

    //table name

    private static final String QUIZ_TABLE="quiz";
    private static final String FLASH_CARD="flashcards";
    private static final String QUESTIONS="questions";
    private static final String QUESTION_ANSWER="answers";
    private static final String FLASH_DECS="flashdecs";
    //quiz table column name

    private static final String QUIZ_ID="id";
    private static final String NAME="name";
    private static final String TAKEN_TIME="taken_time";
    private static final String SCORE="score";

    //flashcard table column name

    private static final String FLASHCARD_ID="id";
    private static final String DECK_ID="deck_id";
    private static final String KNOWN="known";
    private static final String TITLE="title";
    private static final String DESCRIPTION="description";

    //question table column name

    private static final String QUIZID="quiz_id";
    private static final String QUESTIONID="question_id";
    private static final String QUESTION="question";
    private static final String QUESTIONTYPE="question_type";
    private static final String USERANS="user_ans";

    //answer table column name

    private static final String ANSWERID="id";
    private static final String QUESTIONSID="question_id";
    private static final String OPTION="option";
    private static final String IS_CORRECT="is_correct";

    //flash_decs table column name

    private static final String DECKID="deck_id";
    private static final String NUMBER_OF_CARDS="number_of_cards";
    private static final String DECK_NAME="name";
    private static final String DOWNLOADED_DATE="downloaded_date";

    //version number

    private static final int VERSION_NUMBER=3;

    //table creation

    private static final String QUIZ="CREATE TABLE " + QUIZ_TABLE + "(" + QUIZ_ID + " integer primary key autoincrement," + NAME + " Text," + TAKEN_TIME + " integer," + SCORE + " integer"+ ")";
    private static final String FLASHCARD="CREATE TABLE " + FLASH_CARD + "(" + FLASHCARD_ID + " integer primary key autoincrement," + DECK_ID + " integer, " + KNOWN + " integer default 0," + TITLE + " text, " + DESCRIPTION + " text"+ ")";
    private static final String QUESTION_TABLE="CREATE TABLE " + QUESTIONS + "(" + QUIZID + " integer," + QUESTIONID + " integer primary key," + QUESTION + " text," + QUESTIONTYPE + " varchar(200)," + USERANS + " integer"+ ")";
    private static final String  ANSWER_TABLE="CREATE TABLE " + QUESTION_ANSWER + "(" + ANSWERID + " integer primary key," + QUESTIONSID + " integer," + OPTION + " text," + IS_CORRECT + " integer"+ ")";
    private static final String  FLASHDECS= "CREATE TABLE " + FLASH_DECS + "(" + DECKID + " integer primary key autoincrement," + NUMBER_OF_CARDS + " integer," + DECK_NAME + " text," + DOWNLOADED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP"+ ")";

    //drop table

    private static final String DROP_QUIZ="DROP TABLE IF EXISTS QUIZ"+QUIZ_TABLE;
    private static final String DROP_FLASHCARD="DROP TABLE IF EXISTS FLASHCARDS"+FLASH_CARD;
    private static final String DROP_QUESTION="DROP TABLE IF EXISTS QUESTIONS"+QUESTIONS;
    private static final String DROP_ANSWER="DROP TABLE IF EXISTS QUESTION_ANSWER"+QUESTION_ANSWER;
    private static final String DROP_FLASHDECS="DROP TABLE IF EXISTS FLASH_DECS"+FLASH_DECS;


    private  Context context;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUIZ);
        db.execSQL(FLASHCARD);
        db.execSQL(QUESTION_TABLE);
        db.execSQL(ANSWER_TABLE);
        db.execSQL(FLASHDECS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_QUIZ);
        db.execSQL(DROP_FLASHCARD);
        db.execSQL(DROP_QUESTION);
        db.execSQL(DROP_ANSWER);
        db.execSQL(DROP_FLASHDECS);
        onCreate(db);
    }

    public List<QuizDeck> getAllQuizDecks() {
        List<QuizDeck> notes1 = new ArrayList<QuizDeck>();

        // Select All Query

        String selectQuery = "SELECT * FROM QUIZ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuizDeck fd = new QuizDeck();
                fd.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                fd.setTestid(cursor.getInt(cursor.getColumnIndex(QUIZ_ID)));
                notes1.add(fd);
                System.out.println("MESSAGE" + fd.getName());
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return notes1;
    }

    public long insert_quiz(QuizDeck fc) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(NAME, fc.getName());
        values.put(QUIZ_ID, fc.getTestid());

        // insert row
        long id = db.insert (QUIZ_TABLE, null, values);

        // close db connection
        db.close();


        // return newly inserted row id
        return id;
    }

    public int getKnown(int deck_id) {
        String selectQuery = "SELECT * FROM "+FLASHCARD+"  where "+DECK_ID +"= "+deck_id+" and known=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public ArrayList<Integer> get_question_ids(int quiz_id) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT "+QUESTIONID+" FROM "+QUESTIONS+" where "+QUIZID+"="+ quiz_id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                a.add(cursor.getInt(cursor.getColumnIndex(QUESTIONID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return a;
    }
    public int get_taken_time(int quiz_id) {
        int time_taken = 0;
        String qns = "select "+TAKEN_TIME+" from "+QUIZ_TABLE+" where "+QUIZ_ID+"=" + quiz_id;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(qns, null);
        if (c.moveToFirst()) {
            do {
                time_taken = c.getInt(c.getColumnIndex(TAKEN_TIME));
            } while (c.moveToNext());
        }
        c.close();
        return time_taken;
    }

    public int get_score(int quiz_id) {
        String qns = "select * from "+QUESTIONS+" where "+QUIZID+"=" + quiz_id;
        SQLiteDatabase db = getWritableDatabase();
        int score = 0;
        Cursor c = db.rawQuery(qns, null);
        if (c.moveToFirst()) {
            do {
                int qn_id = c.getInt(c.getColumnIndex(QUESTIONSID));
                String ans = "select * from  "+ QUESTION_ANSWER + " where "+QUESTIONSID+"=" + qn_id + " order by id asc";
               // Log.d("score", "subhankar");
                int user_ans = c.getInt(c.getColumnIndex("user_ans"));
                if (user_ans == 0) {
                    continue;
                }
                int c_in = 0;
                Cursor c2 = db.rawQuery(ans, null);
                if (c2.moveToFirst()) {
                    do {
                        c_in++;
                      //  Log.d("subhankar", "va-" + c2.getInt(c2.getColumnIndex("is_correct")));
                        if (c2.getInt(c2.getColumnIndex(IS_CORRECT)) == 1) {
                           // Log.d("subhankar", "Ans=" + c2.getString(c2.getColumnIndex("option")));
                            break;
                        }
                    } while (c2.moveToNext());
                }
                c2.close();
               // Log.d("subhankar12345", "u=" + user_ans + " c=" + c_in);
                if (user_ans == c_in) {
                    score++;
                }
            } while (c.moveToNext());
        }
        c.close();
        return score;
    }
    public int get_attemptedQuestion(int quiz_id) {

       // Log.d("calling ", "done..............................................");
        String attenptedQuestion;
        attenptedQuestion = "SELECT *  FROM "+QUESTIONS+" where "+USERANS+"!=" + 0 + " AND "+QUIZID+"=" + quiz_id;
       // Log.d("calling ", attenptedQuestion);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(attenptedQuestion, null);
        return cursor.getCount();
    }
    public void set_takenTime(int quiz_id, int taken_time) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("subh", "timer");
        ContentValues values = new ContentValues();
        values.put(TAKEN_TIME, taken_time);
        db.update(QUIZ_TABLE, values,  QUIZ_ID + "= ?", new String[]{"" + quiz_id});

    }
    public void set_score(int quiz_id, int score) {

        SQLiteDatabase db = this.getWritableDatabase();
        //Log.d("subh", "timer");
        ContentValues values = new ContentValues();
        values.put(SCORE, score);
        db.update(QUIZ_TABLE, values, QUIZ_ID + "= ?", new String[]{"" + quiz_id});

    }
    public long add_question(Question q) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUESTIONTYPE, q.getQn_type());
        values.put(QUESTION, q.getQuestion());
        values.put(QUIZID, q.getQuiz_id());
        values.put(USERANS, q.getUser_ans());
        values.put(QUESTIONID, q.getQuestion_id());
        long id = db.insert(QUESTIONS, null, values);
        db.close();
        return id;

    }
    public long add_answer(Answer a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OPTION, a.getAnswer());
        values.put(IS_CORRECT, a.getIs_correct());
        values.put(QUESTIONSID, a.getQues_id());
        long id = db.insert(QUESTION_ANSWER, null, values);
        db.close();
        return id;
    }
    public void store_ans(int question_id, int ans) {
       // Log.d("Akhil", question_id + "  - " + ans);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERANS, ans);
        db.update(QUESTIONS, values, QUESTIONID +"= ?", new String[]{"" + question_id});
    }
    public Question get_question(int qn_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM "+QUESTIONS+" where " +QUESTIONID+ "=" + qn_id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Question q = new Question();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                q.setQuiz_id(cursor.getInt(cursor.getColumnIndex(QUIZID)));
                q.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION)).replaceAll("<p>", "").replaceAll("</p>", ""));
                q.setQuestion_id(qn_id);
                q.setUser_ans(cursor.getInt(cursor.getColumnIndex(USERANS)));
            } while (cursor.moveToNext());
        }
        return q;
    }
    public ArrayList<Answer> get_answers(int qn_id) {
        ArrayList<Answer> a = new ArrayList<Answer>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM "+QUESTION_ANSWER+" where "+QUESTIONSID+"=" + qn_id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Answer an = new Answer();
                an.setAnswer(cursor.getString(cursor.getColumnIndex(OPTION)).replaceAll("<p>", "").replaceAll("</p>", ""));
                an.setQues_id(qn_id);
                an.setIs_correct(cursor.getInt(cursor.getColumnIndex(IS_CORRECT)));
                a.add(an);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return a;
    }

}
