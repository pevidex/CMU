package com.cmu.group22.hoponcmu;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classes.Question;

public class InitAnswers extends BaseAdapter{
    private Context context;
    private final List<Question> questions;

    public InitAnswers(Context context, List<Question> questions) {
        this.context = context;
        this.questions = new ArrayList<>(questions);
        Log.d("ATLAS",this.questions.size()+"");

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View listView;
        if(convertView == null){
            listView = new View(context);
            listView = inflater.inflate(R.layout.questions,null);
        }else
            listView = (View) convertView;

        TextView textView = (TextView) listView.findViewById(R.id.AnswersResult_question);
        textView.setText(questions.get(position).getQuestion());

        Button btn1 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option1);
        btn1.setText(questions.get(position).getAnswer1());
        btn1.setSelected(true);
        btn1.setTextColor(Color.RED);
        btn1.setEnabled(false);

        Button btn2 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option2);
        btn2.setText(questions.get(position).getAnswer2());
        btn2.setTextColor(Color.GREEN);
        btn2.setEnabled(false);


        Button btn3 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option3);
        btn3.setText(questions.get(position).getAnswer3());

        Button btn4 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option4);
        btn4.setText(questions.get(position).getAnswer4());

        return listView;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
