package com.cmu.group22.hoponcmu;

import android.content.Context;
import android.content.res.ColorStateList;
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

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import classes.Question;

public class InitAnswers extends BaseAdapter{
    private Context context;
    private final List<Question> questions;
    private final ArrayList<Boolean> userResult;
    private final ArrayList<Integer> userAnswers;

    public InitAnswers(Context context, List<Question> questions, List<Boolean> userResult, List<Integer> userAnswers) {
        this.context = context;
        this.questions = new ArrayList<>(questions);
        this.userResult = new ArrayList<>(userResult);
        this.userAnswers = new ArrayList<>(userAnswers);
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
        List<Button> buttons = new ArrayList<>();
        Button btn1 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option1);
        Button btn2 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option2);
        Button btn3 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option3);
        Button btn4 = (Button) listView.findViewById(R.id.AnswersResult_Btn_option4);

        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);

        int option_i = 0;
        for(Button btn : buttons){
            btn.setEnabled(false);
            btn.setTextColor(Color.WHITE);
            btn.setTag("result_"+option_i+"_"+position);
            btn.setText(questions.get(position).getAnsweri(option_i));
            if(userAnswers.get(position)-1==option_i)
                if(userResult.get(position))
                    btn.setTextColor(Color.GREEN);
                else
                    btn.setTextColor(Color.RED);
            if(questions.get(position).getCorrectAnswer()==option_i)
                btn.setTextColor(Color.GREEN);
            option_i++;
        }

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
