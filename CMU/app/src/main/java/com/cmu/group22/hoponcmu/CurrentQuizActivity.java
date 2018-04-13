package com.cmu.group22.hoponcmu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import classes.QuizItem;

import java.util.ArrayList;
import java.util.List;

public class CurrentQuizActivity extends AppCompatActivity {
    private String currentLocation = null;
    ListView listView;
    List<QuizItem> quizItems = new ArrayList<QuizItem>();
    Button nextBtn;
    Button backBtn;
    int currentPos = 0;
    ArrayList<Integer> ans = new ArrayList<>();
    RadioGroup radioAnsGroup;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentquiz);

        getCurrentLocation();
        TextView title = (TextView) findViewById (R.id.QuizTitle);
        title.setText(currentLocation);
        setQuizItemList();

        radioAnsGroup = (RadioGroup) findViewById(R.id.radioAns);
        nextBtn = (Button) findViewById(R.id.Btn_sumbit);
        backBtn = (Button) findViewById(R.id.Btn_back);

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
                        //TODO: to submit ans
                        Log.d("ATLAS",ans.toString());
                        return;
                    }
                    break;
                default:
                    break;
            }

            resetCurrentquiz();

        }
    };




    protected void getCurrentLocation(){
        this.currentLocation = "Cascais";
    }

    protected void setQuizItemList(){
        quizItems.add(new QuizItem("How long it has been1?","1y1","2y","3y","4y"));
        quizItems.add(new QuizItem("How long it has been2?","1y2","2y","3y","4y"));
        quizItems.add(new QuizItem("How long it has been3?","1y3","2y","3y","4y"));
        quizItems.add(new QuizItem("How long it has been4?","1y4","2y","3y","4y"));
        quizItems.add(new QuizItem("How long it has been5?","1y5","2y","3y","4y"));
        quizItems.add(new QuizItem("How long it has been6?","1y6","2y","3y","4y"));
        quizItems.add(new QuizItem("How long it has been7?","1y7","2y","3y","4y"));
        quizItems.add(new QuizItem("How long it has been8?","1y8","2y","3y","4y"));

        //initialize the ans
        for(QuizItem q: quizItems){
            ans.add(0);
        };
    }

    protected void resetCurrentquiz(){
        QuizItem q = quizItems.get(currentPos);

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
        textView.setText((currentPos+1)+"."+q.question);
        Button btn1 = (Button) findViewById(R.id.Btn_option1);
        btn1.setText(q.option1);
        Button btn2 = (Button) findViewById(R.id.Btn_option2);
        btn2.setText(q.option2);
        Button btn3 = (Button) findViewById(R.id.Btn_option3);
        btn3.setText(q.option3);
        Button btn4 = (Button) findViewById(R.id.Btn_option4);
        btn4.setText(q.option4);
        //recover ans
        Log.d("ATLAS",currentPos+" and "+ans);
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


}
