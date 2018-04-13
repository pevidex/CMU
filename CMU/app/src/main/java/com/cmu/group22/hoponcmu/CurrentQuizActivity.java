package com.cmu.group22.hoponcmu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmu.group22.hoponcmu.Task.GetQuestionsTask;
import com.cmu.group22.hoponcmu.Task.SendAnswersTask;

import classes.Answers;
import classes.Question;


import java.util.ArrayList;
import java.util.List;

public class CurrentQuizActivity extends AppCompatActivity {
    private String currentLocation = null;
    ListView listView;
    List<Question> quizItems = new ArrayList<Question>();
    Button nextBtn;
    Button backBtn;
    int currentPos = 0;
    Answers ans;

    RadioGroup radioAnsGroup;
    ArrayList<Question> questions;
    ArrayList<Boolean> answersResults = null;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentquiz);

        GlobalContext globalContext = (GlobalContext) getApplicationContext();
        ans = globalContext.getAnswers();


        setCurrentLocation(getIntent().getStringExtra("location"));
        new GetQuestionsTask(CurrentQuizActivity.this).execute(currentLocation);
        //error here because questions are not setup in time on task. need to wait for the task to complete

        TextView title = (TextView) findViewById (R.id.QuizTitle);
        title.setText(currentLocation);
        setQuizItemList();

        radioAnsGroup = (RadioGroup) findViewById(R.id.radioAns);
        nextBtn = (Button) findViewById(R.id.Btn_sumbit);
        backBtn = (Button) findViewById(R.id.Btn_back);


        //ans = (Answers) getIntent().getSerializableExtra("stock_ans");
        ans.list();
        ans.init(quizItems);
        ans.set(1,2);
        ans.list();
        resetCurrentquiz();


        nextBtn.setOnClickListener(onClickListener);
        backBtn.setOnClickListener(onClickListener);


    }

    private OnClickListener onClickListener = new OnClickListener(){
        public void onClick(View v){

            int selectedId = radioAnsGroup.getCheckedRadioButtonId();
            switch (selectedId){
                case R.id.Btn_option1:
                    ans.set(currentPos,1);
                    break;
                case R.id.Btn_option2:
                    ans.set(currentPos,2);
                    break;
                case R.id.Btn_option3:
                    ans.set(currentPos,3);
                    break;
                case R.id.Btn_option4:
                    ans.set(currentPos,4);
                    break;
                default:
                    ans.set(currentPos,0);
                    break;
            }
            switch (v.getId()){
                case R.id.Btn_back:
                    if(currentPos == 0){
                        return;
                    }
                    currentPos--;
                    break;
                case R.id.Btn_sumbit:
                    currentPos++;
                    if(currentPos == quizItems.size()){
                        nextBtn.setEnabled(false);
                        backBtn.setEnabled(false);
                        new SendAnswersTask(CurrentQuizActivity.this).execute();
                        ans.clear();
                        return;
                    }
                    break;
                default:
                    break;
            }

            resetCurrentquiz();

        }
    };




    protected void setCurrentLocation(String location){
        this.currentLocation = location;
    }

    public void updateQuestions(ArrayList<Question> questions){
        this.questions = questions;
    }

    public void updateAnswers(ArrayList<Boolean> results){
        this.answersResults = results;
        int correctAnswers = 0;

        for(Boolean b : results){
            if(b) correctAnswers++;
        }

        Toast.makeText(CurrentQuizActivity.this, correctAnswers,
                Toast.LENGTH_LONG).show();
    }



    protected void setQuizItemList(){
        quizItems.add(new Question("How long it has been1?","1y1","2y","3y","4y"));
        quizItems.add(new Question("How long it has been2?","1y2","2y","3y","4y"));
        quizItems.add(new Question("How long it has been3?","1y3","2y","3y","4y"));
        quizItems.add(new Question("How long it has been4?","1y4","2y","3y","4y"));
        quizItems.add(new Question("How long it has been5?","1y5","2y","3y","4y"));
        quizItems.add(new Question("How long it has been6?","1y6","2y","3y","4y"));
        quizItems.add(new Question("How long it has been7?","1y7","2y","3y","4y"));
        quizItems.add(new Question("How long it has been8?","1y8","2y","3y","4y"));

    }

    protected void resetCurrentquiz(){
        Question q = quizItems.get(currentPos);

        //disable button
        if(currentPos == quizItems.size()-1){
            nextBtn.setText("SUBMIT");
        }else if(currentPos == 0){
            nextBtn.setEnabled(true);
            backBtn.setEnabled(false);
        }else{
            nextBtn.setEnabled(true);
            backBtn.setEnabled(true);
        }

        //save ans
        TextView textView = (TextView) findViewById(R.id.question);
        textView.setText((currentPos+1)+"."+q.getQuestion());
        Button btn1 = (Button) findViewById(R.id.Btn_option1);
        btn1.setText(q.getAnswer1());
        Button btn2 = (Button) findViewById(R.id.Btn_option2);
        btn2.setText(q.getAnswer2());
        Button btn3 = (Button) findViewById(R.id.Btn_option3);
        btn3.setText(q.getAnswer3());
        Button btn4 = (Button) findViewById(R.id.Btn_option4);
        btn4.setText(q.getAnswer4());

        //recover ans

        switch (ans.get(currentPos)){
            case 1:
                radioAnsGroup.check(R.id.Btn_option1);
                break;
            case 2:
                radioAnsGroup.check(R.id.Btn_option2);
                break;
            case 3:
                radioAnsGroup.check(R.id.Btn_option3);
                break;
            case 4:
                radioAnsGroup.check(R.id.Btn_option4);
                break;
            default:
                radioAnsGroup.clearCheck();
                break;
        }


    }


    public void updateInterface(String reply) {
        Toast.makeText(CurrentQuizActivity.this, reply,
                Toast.LENGTH_LONG).show();
    }

}
