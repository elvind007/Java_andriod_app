package com.example.e_vaccinationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import java.util.ArrayList;

class Faq_Adapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> faq_ids;
    ArrayList<String> faq_questions;
    ArrayList<String> faq_answers;

    Faq_Adapter (Context c, ArrayList<String> faq_ids, ArrayList<String> faq_questions, ArrayList<String> faq_answers) {
        super(c, R.layout.faq_list_layout, R.id.faq_question_list, faq_questions);
        this.faq_ids = faq_ids;
        this.context = c;
        this.faq_questions = faq_questions;
        this.faq_answers = faq_answers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.faq_list_layout, parent, false);

        TextView faq_ques = row.findViewById(R.id.faq_question_list);
        TextView faq_ans = row.findViewById(R.id.faq_answer_list);

        row.setTag(faq_ids.get(position));

        // now set our resources on views
        faq_ques.setText(faq_questions.get(position));
        faq_ans.setText(faq_answers.get(position));

        return row;
    }

}
