package com.example.ahmed.task_6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView counterTextView ;
    int decisecond, seconds , minutes ;
    MyThread thread ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thread = new MyThread() ;
        counterTextView = (TextView) findViewById(R.id.counterTextView) ;
        decisecond = seconds  = minutes = 0 ;
    }

    //To prevent Our Thread from being active even after the current activity goes to background
    //We have to pause the thread every time the activity pauses
    @Override
    protected void onPause() {
        super.onPause();
        thread.setCurrentState(MyThread.STATE_PAUSED);
    }

    public void click(View v){
        if(v.getId()==R.id.startButton){
            if(thread.getCurrentState()==MyThread.STATE_RUNNING) return;
            thread = new MyThread() ;
            thread.start();
        }
        else if (v.getId()==R.id.pauseButton){
            thread.setCurrentState(MyThread.STATE_PAUSED);
        }
        else if (v.getId()==R.id.resetButton){
            if(thread.getCurrentState()==MyThread.STATE_RUNNING) thread.setCurrentState(MyThread.STATE_RESET);
            else{
                minutes = seconds = decisecond = 0 ;
                String text = String.format("%02d:%02d:%02d",minutes,seconds,decisecond);
                counterTextView.setText(text);
            }
        }
    }
    public void goToImageSelectActivity(View view){
        Intent intent = new Intent(MainActivity.this,ImageActivity.class) ;
        startActivity(intent);

    }
    // Threads by Java(Threads)
    public class MyThread extends Thread {
        static final int STATE_RUNNING=0 , STATE_PAUSED=1, STATE_RESET=2 ;
        int state = -1 ;
        @Override
        public void run() {
            state = STATE_RUNNING ;
            while (true){
                if(decisecond==9) {
                    decisecond= -1 ;
                    if(seconds==59){
                        seconds=-1 ;
                        minutes++ ;
                    }
                    seconds++ ;
                }
                decisecond++ ;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("%02d:%02d:%02d",minutes,seconds,decisecond);
                        counterTextView.setText(text);
                    }
                });
                if(state!=STATE_RUNNING) break;
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if(state==STATE_RESET){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        decisecond = seconds = minutes = 0 ;
                        String text = String.format("%02d:%02d:%02d",minutes,seconds,decisecond);
                        counterTextView.setText(text);
                    }
                });
            }
        }
        public void setCurrentState(int state){
            this.state = state ;
        }

        public int getCurrentState() {
            return state;
        }
    }
}
