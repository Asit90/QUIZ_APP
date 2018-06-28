package cakart.cakart.in.quiz_app.Quiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cakart.cakart.in.quiz_app.R;
import cakart.cakart.in.quiz_app.db.MyDatabaseHelper;
import cakart.cakart.in.quiz_app.models.QuizDeck;


public class QuizDeckAdapter extends ArrayAdapter<QuizDeck> {

    private int listItemLayout;

    public QuizDeckAdapter(Context context, int layoutId, ArrayList<QuizDeck> itemList) {
        super(context, layoutId, itemList);
        listItemLayout = layoutId;
    }

    private static class ViewHolder {
        TextView name;
        Button start;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get
        // the data item for this position
        final QuizDeck item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        QuizDeckAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new QuizDeckAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayout, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.deck_name);
            viewHolder.start = (Button) convertView.findViewById(R.id.btn_taketest);
            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (QuizDeckAdapter.ViewHolder) convertView.getTag();
        }

        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        // int known_count = db.etKnown(item.getDeck_id());
        // Populate the data into the template view using the data object
        viewHolder.name.setText(item.getName());

        // viewHolder.pb.setMax(item.getNumber_of_decks());
        //viewHolder.pb.setProgress(known_count);
        // viewHolder.status.setText(known_count+" out of "+item.getNumber_of_decks()+" words mastered");


        viewHolder.start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), QuizQuestionActivity.class);
                intent.putExtra("quiz_id", item.getTestid());
                getContext().startActivity(intent);


            }
        });


        // Return the completed view to render on screen
        return convertView;
    }

}




