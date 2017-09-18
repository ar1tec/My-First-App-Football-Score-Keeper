package com.example.mansi.footballscorekeeper;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays score of two football teams where default half time is 45 minutes but it can be changed(made less).
 */

public class MainActivity extends AppCompatActivity {

    int teamA = 0;
    int teamB = 0;
    CountDownTimer firstHalf;
    CountDownTimer secondHalf;
    TextView firstHalfTimer;
    TextView endHalfTimer;
    TextView secondHalfTimer;
    TextView endGameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Make to run your application only in portrait mode

        setContentView(R.layout.activity_main);

        firstHalfTimer = (TextView) findViewById(R.id.first_half_timer);
        endHalfTimer = (TextView) findViewById(R.id.end_half_timer);
        secondHalfTimer = (TextView) findViewById(R.id.second_half_timer);
        endGameTimer = (TextView) findViewById(R.id.end_game_timer);
    }

    /**
     * This method is called when the + button on Home Team is clicked.
     */
    public void addHome(View view) {
        if (isErrorPresent()) {
            return;
        }
        Button minusHome = (Button) findViewById(R.id.minusHome);
        minusHome.setEnabled(true);
        teamA++;
        displayA(teamA);

    }

    /**
     * This method is called when the - button on Home Team is clicked.
     */
    public void subHome(View view) {
        if (isErrorPresent()) {
            return;
        }

        teamA--;
        displayA(teamA);
        if (teamA == 0) {
            Button minusHome = (Button) findViewById(R.id.minusHome);
            minusHome.setEnabled(false);
        }

    }

    /**
     * This method is called when the + button on Away Team is clicked.
     */
    public void addAway(View view) {
        if (isErrorPresent()) {
            return;
        }
        Button minusAway = (Button) findViewById(R.id.minusAway);
        minusAway.setEnabled(true);
        teamB = teamB + 1;
        displayB(teamB);


    }

    /**
     * This method is called when the - button on Away Team is clicked.
     */
    public void subAway(View view) {
        if (isErrorPresent()) {
            return;
        }
        teamB = teamB - 1;
        displayB(teamB);
        if (teamB == 0) {
            Button minusAway = (Button) findViewById(R.id.minusAway);
            minusAway.setEnabled(false);
        }
    }

    public void startFirstHalf(View view) {

        if (isStartCalledBeforeEndGame(firstHalfTimer, endHalfTimer, secondHalfTimer, endGameTimer)) {
            return;
        }
        endHalfTimer.setText("00:00");
        secondHalfTimer.setText("00:00");
        endGameTimer.setText("00:00");

        //passing 0 as starting time to make timer start counting from 1 and limit is 45 minutes that is 2700 seconds
        firstHalf = runTimer(firstHalfTimer, 0, 2700);
        firstHalf.start();

        //enabling End first half button as soon as Start first half is clicked
        Button end1Button = (Button) findViewById(R.id.end_first_half);
        end1Button.setEnabled(true);
    }

    public void endFirstHalf(View view) {
        //To cancel or stop the countdown
        firstHalf.cancel();

        String temporary = (String) firstHalfTimer.getText();
        if (!(temporary.equals("00:00")))
        //If start first half timer is set to any other value than default
        {
            endHalfTimer.setText(temporary);
            firstHalfTimer.setText("00:00");

            //Enable start second half button
            Button start2Button = (Button) findViewById(R.id.start_second_half);
            start2Button.setEnabled(true);
        }
    }

    public void startSecondHalf(View view) {
        String temp = (String) endHalfTimer.getText();
        //Extracting minutes and seconds from End half as integers
        int min = Integer.parseInt(temp.split(":")[0]);
        int sec = Integer.parseInt(temp.split(":")[1]);

        //implicit type casting from int to long
        long limit = (min * 60 + sec);
        if (isStart2CalledBeforeEndGame(secondHalfTimer, endGameTimer)) {
            return;
        }
        secondHalf = runTimer(secondHalfTimer, limit, limit);
        secondHalf.start();

        //Enabling End Game button as soon as Start 2nd Half is clicked
        Button end2Button = (Button) findViewById(R.id.end_game);
        end2Button.setEnabled(true);
    }

    public void endGame(View view) {
        //Cancelling or stopping secondHalf countdown timer
        secondHalf.cancel();

        String temporary = (String) secondHalfTimer.getText();
        endGameTimer.setText(temporary);
        secondHalfTimer.setText(endHalfTimer.getText());
    }

    /**
     * This method is called when the Clear button is clicked.
     */
    public void reset(View view) {
        teamB = 0;
        teamA = 0;
        displayB(teamB);
        displayA(teamA);

        //Setting the default status of all the buttons(Disabled) when Clear button is clicked
        Button minusHome = (Button) findViewById(R.id.minusHome);
        minusHome.setEnabled(false);
        Button minusAway = (Button) findViewById(R.id.minusAway);
        minusAway.setEnabled(false);
        Button end1Button = (Button) findViewById(R.id.end_first_half);
        end1Button.setEnabled(false);
        Button start2Button = (Button) findViewById(R.id.start_second_half);
        start2Button.setEnabled(false);
        Button end2Button = (Button) findViewById(R.id.end_game);
        end2Button.setEnabled(false);

        /**If start 1st half and end half both contains 00:00(default value) then that means firstHalf
         * countdown timer is not initialised and we need not call the cancel method on it*/
        if (!(firstHalfTimer.getText().equals("00:00")) && (endHalfTimer.getText().equals("00:00"))) {
            firstHalf.cancel();
        }

        //But we will change the text on both views irrespective of countdown timer
        firstHalfTimer.setText("00:00");
        endHalfTimer.setText("00:00");

        /**If start 2nd half and end game both contains 00:00(default value) then that means secondHalf
         *  countdown timer is not initialised and we need not call the cancel method on it*/
        if (!(secondHalfTimer.getText().equals("00:00")) && (endGameTimer.getText().equals("00:00"))) {
            secondHalf.cancel();
        }

        //But we will change the text on both views irrespective of countdown timer
        secondHalfTimer.setText("00:00");
        endGameTimer.setText("00:00");
    }

    /**
     * HELPER METHODS
     *
     * @runTimer - Count up timer to keep track of Half Time and Game progress
     * @displayA - To display score of Home Team
     * @displayB - To display score of Away Team
     * @getTime - return time in required format to display on respective Textviews
     * @isErrorPresent - To raise a toast message if user tries to update goals or scores without starting the Timer
     * @isStartCalledBeforeEndGame - method to prevent multiple instances of Countdown timer
     * if start First Half is pressed again before the game finishes
     * @isStart2CalledBeforeEndGame - method to prevent multiple instances of Countdown timer
     * if start First Half is pressed again before the game finishes
     */

    public CountDownTimer runTimer(final TextView timerTextView, final long seconds, final long millisInFuture) {
        long interval = 1000;
        CountDownTimer timer = new CountDownTimer(millisInFuture * 1000, interval - 500) {
            @Override
            public void onTick(long l) {
                timerTextView.setText(getTime(((millisInFuture * 1000 - l) / 1000 + seconds + 1)));
            }

            @Override
            public void onFinish() {
            }
        };
        return timer;
    }

    /**
     * This method is called to display score of Home Team
     */
    private void displayA(int number) {
        TextView score_text_view = (TextView) findViewById(R.id.teamA);
        score_text_view.setText(String.valueOf(number));
    }

    /**
     * This method is called to display score of Away Team
     */
    private void displayB(int number) {
        TextView score_text_view = (TextView) findViewById(R.id.teamB);
        score_text_view.setText(String.valueOf(number));
    }

    /**
     * To return time in proper format min:sec(eg 14:54)
     */
    public String getTime(long sec) {
        long min = sec / 60;
        sec = sec % 60;
        String finalSeconds;
        String finalMinutes;
        if (sec < 10) {
            finalSeconds = "0" + sec;
        } else
            finalSeconds = "" + sec;
        if (min < 10) {
            finalMinutes = "0" + min;
        } else {
            finalMinutes = "" + min;
        }
        return (finalMinutes + ":" + finalSeconds);
    }

    //method to raise a toast if user tries to update scores before starting timer
    public boolean isErrorPresent() {
        Button start1Button = (Button) findViewById(R.id.start_first_half);
        Button end1Button = (Button) findViewById(R.id.end_first_half);
        Button start2Button = (Button) findViewById(R.id.start_second_half);
        Button end2Button = (Button) findViewById(R.id.end_game);
        if (!(end1Button.isEnabled())) {
            Toast.makeText(this, "Please start first half timer", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (start2Button.isEnabled() && (!end2Button.isEnabled())) {
            Toast.makeText(this, "Please start second half timer", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (start1Button.isEnabled() && end1Button.isEnabled() && start2Button.isEnabled() && end2Button.isEnabled()) {
            Toast.makeText(this, "Please reset Game", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * method to prevent multiple instances of Countdown timer
     * if start First Half is pressed again before the game finishes
     */

    public boolean isStartCalledBeforeEndGame(TextView t1, TextView t2, TextView t3, TextView t4) {
        if ((!t1.getText().equals("00:00")) || (!t2.getText().equals("00:00")) || (!t3.getText().equals("00:00")) || (!t4.getText().equals("00:00"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * method to prevent multiple instances of Countdown timer
     * if start Second Half is pressed again before the game finishes
     */

    public boolean isStart2CalledBeforeEndGame(TextView t1, TextView t2) {
        if ((!t1.getText().equals("00:00")) || (!t2.getText().equals("00:00"))) {
            return true;
        } else {
            return false;
        }
    }
}
