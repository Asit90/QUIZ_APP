package cakart.cakart.in.quiz_app.models;

public class Question {
    int question_id;
    String question;
    String qn_type;
    int quiz_id;

    int user_ans;

    public int getUser_ans() {
        return user_ans;
    }

    public void setUser_ans(int user_ans) {
        this.user_ans = user_ans;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQn_type() {
        return qn_type;
    }

    public void setQn_type(String qn_type) {
        this.qn_type = qn_type;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }
}
