package cakart.cakart.in.quiz_app.models;

import java.util.Date;

public class QuizDeck {
    int testid;
    String name;
    Date downloaded;

    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Date downloaded) {
        this.downloaded = downloaded;
    }

}
