package com.cmu.group22.hoponcmu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.cmu.group22.hoponcmu.WifiDirect.WifiDirectService;

import org.w3c.dom.Text;

import classes.Answers;
import classes.Question;


import java.util.ArrayList;
import java.util.List;

public class CurrentQuizActivity extends AppCompatActivity {
    private String currentLocation = null;
    ListView listView;
    Button nextBtn;
    Button backBtn;
    int currentPos = 0;
    Answers ans;

    int correctAnswers = 0;
    long time;

    RadioGroup radioAnsGroup;
    ArrayList<Question> questions;
    ArrayList<Boolean> answersResults = null;

    GlobalContext globalContext;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentquiz);

        globalContext = (GlobalContext) getApplicationContext();
        ans = globalContext.getAnswers();

        radioAnsGroup = (RadioGroup) findViewById(R.id.radioAns);
        nextBtn = (Button) findViewById(R.id.Btn_sumbit);
        backBtn = (Button) findViewById(R.id.Btn_back);
        TextView tvQuestion = (TextView) findViewById(R.id.question);

        radioAnsGroup.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        backBtn.setVisibility(View.INVISIBLE);
        tvQuestion.setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById (R.id.QuizTitle);

        setCurrentLocation(getIntent().getStringExtra("location"));
        Log.d("CurrentQuizz", "Current Location = " + currentLocation);
        if(currentLocation.equals("") && globalContext.getQuizz().isEmpty()) {
            //SET EVERYTHING TO INVISIBLE
            radioAnsGroup.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);

            title.setText("TOO LATE TO DOWNLOAD QUIZZ");
            Log.d("CurrentQuizz", "Too late to download");

        }
        else {
            if(globalContext.getQuizz().isEmpty()) {

                Log.d("CurrentQuizz", "Downloading from server");
                GetQuestionsTask g = (GetQuestionsTask) new GetQuestionsTask(CurrentQuizActivity.this, globalContext).execute(currentLocation);
                try {
                    String temp = g.get();
                } catch (Exception e) {
                    Log.d("DummyClient", "ERROR on get questions task");
                }
            }
            else
                questions = globalContext.getQuizz();

            AlertDialog.Builder builder = new AlertDialog.Builder(CurrentQuizActivity.this);
            builder.setMessage("Questions Downloaded! Do you want to answer now?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            title.setText(currentLocation);

            radioAnsGroup.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.VISIBLE);
            tvQuestion.setVisibility(View.VISIBLE);

            ans.init(questions);
            resetCurrentquiz();


            nextBtn.setOnClickListener(onClickListener);
            backBtn.setOnClickListener(onClickListener);


        }
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
                    if(currentPos == questions.size()){
                        nextBtn.setEnabled(false);
                        backBtn.setEnabled(false);

                        //send the time elapsed to server(start time should be the moment that the user start quizz? or the quizz is published?
                        long endTime = SystemClock.elapsedRealtime();

                        ArrayList<Integer> answers = new ArrayList<>(ans.getAnswers());

                        long quizztime = SystemClock.elapsedRealtime() - globalContext.getStartTime(currentLocation);
                        Log.d("CurrentQuizActivity", "TIME="+ quizztime/1000);
                        WifiDirectService wservice;
                        String myName;
                        if(WifiDirectService.isRunning()) {
                            wservice = WifiDirectService.getInstance();
                            myName = globalContext.getUserName();
                            String userNumber = myName.substring(4);
                            if((Integer.parseInt(userNumber) % 2) == 0) {//FOREIGN USER
                                String result = wservice.sendAnswersToNative(CurrentQuizActivity.this, answers, currentLocation, globalContext.getUserName(), quizztime);
                                Log.d("CurrentQuizActivity",result);
                            } else {  //NATIVE USER
                                SendAnswersTask s = (SendAnswersTask)  new SendAnswersTask(CurrentQuizActivity.this, answers, currentLocation, globalContext.getUserName(),globalContext, quizztime).execute();
                                try{
                                    String temp = s.get();}
                                catch(Exception e){Log.d("CurrentQuizActivity","task error");}
                            }
                        }


                         ans.clear();

                        //redirect to the answer result list
                        globalContext.setQuizz(new ArrayList<Question>());  //CLEAR QUESTIONS AFTER SUBMIT
                        Intent intent = new Intent(v.getContext(),QuizSubmitActivity.class);
                        intent.putExtra("name_of_quiz",currentLocation);
                        intent.putExtra("quizzTime", quizztime);
                        intent.putExtra("correctAnswers", correctAnswers);
                        startActivity(intent);
                        finish();
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
        globalContext.setQuizz(questions);
    }

    public void updateAnswers(ArrayList<Boolean> results){
        Log.d("CurrentQuizActivity: ", "Receiving update answers");
        this.answersResults = results;

        correctAnswers = 0;

        for(Boolean b : answersResults){
            Log.d("CurrentQuizActivity", "" +b);
            if(b) correctAnswers++;
        }

        Log.d("CurrentQuizActivity", "Number of correct answers: " + correctAnswers);
    }

    protected void resetCurrentquiz(){
        Question q = questions.get(currentPos);

        //disable button
        if(currentPos == questions.size()-1){
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

    public void updateAnswersViews(){
        //TODO Edit necessary views to display correct answers
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    long time = SystemClock.elapsedRealtime();
                    globalContext.setStartTime(currentLocation, time);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    finish();
                    break;
            }
        }
    };

}
