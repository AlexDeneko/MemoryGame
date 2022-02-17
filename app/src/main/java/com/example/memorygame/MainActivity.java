package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //declaration of class members
    private static final int SIZE_OF_TEXT = 18;
    private static final int FIRST_IMAGE = 1;
    private static final int VISIBLE = 1;
    private static final int INVISIBLE = 0;
    private static final int SECOND = 1000;
    private static final int TIME_TO_HARD_GAME = 81;
    private static final int TIME_TO_NORMAL_GAME = 101;
    private static final int TIME_TO_EASY_GAME = 121;
    private static final int END_TIME = 0;
    private static final int ALL_IMAGES_FOUND = 6;
    private boolean isWin = false;
    private ImageView firstImageClick;
    private int firstImageFromBoardGame;
    private int countOnClick = 1;
    private int countWins = 0;
    private Handler uiHandler;
    private BoardGame boardGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.uiHandler = new Handler();
    }

    public void setDifficult(View view) {
        TableLayout tableGame = findViewById(R.id.tableGame);//connect to table with all images
        TextView timeLeft = findViewById(R.id.timeLeft);//connect to some text views
        TextView showTimeLeft = findViewById(R.id.showTimeLeft);
        TextView messageToUser = findViewById(R.id.messageToUser);
        Button easyGame = findViewById(R.id.easy);//connect to buttons to choose difficult of the game
        Button mediumGame = findViewById(R.id.normal);
        Button hardGame = findViewById(R.id.hard);
        switch (view.getId()) {//set time to game after gamer chose complexity
            case R.id.easy:
                showTimeLeft.setText(String.valueOf(TIME_TO_EASY_GAME));
                break;
            case R.id.normal:
                showTimeLeft.setText(String.valueOf(TIME_TO_NORMAL_GAME));
                break;
            case R.id.hard:
                showTimeLeft.setText(String.valueOf(TIME_TO_HARD_GAME));
                break;
        }
        boardGame = new BoardGame();//create new board game
        easyGame.setClickable(false);//set not clickable and invisibility to buttons
        easyGame.setVisibility(View.INVISIBLE);
        mediumGame.setClickable(false);
        mediumGame.setVisibility(View.INVISIBLE);
        hardGame.setClickable(false);
        hardGame.setVisibility(View.INVISIBLE);
        messageToUser.setAlpha(INVISIBLE);//set invisibility to message to user
        timeLeft.setVisibility(View.VISIBLE);//set visibility to text view with message 'Time left' and count for time
        showTimeLeft.setVisibility(View.VISIBLE);
        tableGame.setVisibility(View.VISIBLE);//show the table with images
        setClickableForImages(true);//allow clickable to images
        startGame();//start the game
    }

    public void startGame() {
        final TextView showTimeLeft = findViewById(R.id.showTimeLeft);//connect to text view with count time to left
        new Thread(() -> {//open new thread
            int secondLeft = Integer.parseInt(showTimeLeft.getText().toString());//get the time from text view
            try {
                while (secondLeft != END_TIME && !isWin) {//while time is not over and the user not win
                    secondLeft--;//sub one second in second
                    int finalSecondLeft = secondLeft;
                    uiHandler.post(() -> {
                        showTimeLeft.setText(String.valueOf(finalSecondLeft));//update time to left
                    });
                    Thread.sleep(SECOND);//thread sleep one second
                    if (secondLeft == END_TIME)//if time is over
                        uiHandler.post(() -> {
                            endGame(END_TIME);//end game
                        });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void clickCard(View view) {//create two toast message, one to find two identical cards and one not find
        Toast messageFoundCards = Toast.makeText(getApplicationContext(), "You found two identical cards", Toast.LENGTH_SHORT);
        Toast messageNotFoundCards = Toast.makeText(getApplicationContext(), "You didn't find two identical cards", Toast.LENGTH_SHORT);
        if (countOnClick == FIRST_IMAGE) {//if the user clicked to first image
            firstImageClick = findViewById(view.getId());//connect to this image
            firstImageClick.setClickable(false);//set to this image no clickable
            countOnClick++;//update count to second image
            firstImageFromBoardGame = showAndGetCard(firstImageClick);//show this image and get numerical id from board game
        } else {//if clicked second images
            ImageView secondImageClick = findViewById(view.getId());//connect to this image
            countOnClick = FIRST_IMAGE;//update count to first image
            int secondImageFromBoardGame = showAndGetCard(secondImageClick);//show this image and get numerical id from board game
            if (firstImageFromBoardGame != secondImageFromBoardGame) {//if numerical id of both images not equals
                returnImageToOriginalState(firstImageClick);//return original state to first image
                returnImageToOriginalState(secondImageClick);//return original state to second image
                messageNotFoundCards.show();//show message to user he didn't find two identical cards
            } else {//if both of images equals
                countWins++;//update count for finding two identical images
                secondImageClick.setClickable(false);//set no clickable to second images
                messageFoundCards.show();//show message to user, he found two identical images
            }
        }
        if (countWins == ALL_IMAGES_FOUND)//if count of finding identical card equal to 6 = 'all identical images found'
            endGame(ALL_IMAGES_FOUND);//end the game
    }

    public void returnImageToOriginalState(ImageView image) {
        image.setClickable(true);//set to this image allow to clickable
        new Thread(() -> {//open new thread
            try {
                Thread.sleep(2 * SECOND);//wait two seconds
                uiHandler.post(() -> {
                    image.animate().rotationYBy(180);//change the rotation to this image
                });
                Thread.sleep(SECOND / 2);//wait half second
                uiHandler.post(() -> {
                    image.setImageResource(R.drawable.question);//and change the image to image 'question'
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public int showAndGetCard(ImageView image) {
        int row = boardGame.getRow(image.getTransitionName());//find the row about this image in board game
        int column = boardGame.getColumn(image.getTransitionName());//find the column about this image in board game
        int imageFromBoardGame = boardGame.getCard(row, column);//get image from board game
        new Thread(() -> {//open new thread
            try {
                uiHandler.post(() -> {
                    image.animate().rotationYBy(90).setDuration(SECOND);//change the rotation to image
                });
                Thread.sleep(SECOND);//wait a second
                uiHandler.post(() -> {
                    image.setImageResource(imageFromBoardGame);//change to image from board game
                    image.animate().rotationYBy(90).setDuration(SECOND);//change the rotation to image to show to user
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return imageFromBoardGame;//return numerical id from board game
    }

    public void endGame(int reason) {
        TextView messageToUser = findViewById(R.id.messageToUser);//connect to text view that show to user some messages
        if (reason == ALL_IMAGES_FOUND) {//if the user won the game
            isWin = true;//update this member to stop count time
            messageToUser.setText("Congratulations! You won the game!!!");//sset this message to user
            TextView timeLeft = findViewById(R.id.timeLeft);//connect to some text views
            TextView showTimeLeft = findViewById(R.id.showTimeLeft);
            timeLeft.setAlpha(INVISIBLE);//and set both of them invisible to user
            showTimeLeft.setAlpha(INVISIBLE);
        } else {//if the user lost the game
            setClickableForImages(false);//set no clickable to all images
            messageToUser.setText("Sorry, time is over, you lose the game");//and set this message
        }
        messageToUser.setTextSize(SIZE_OF_TEXT);//set size of text
        messageToUser.setAlpha(VISIBLE);//and show the message
    }

    public void setClickableForImages(boolean status) {
        //connect to all 12 images view and set to all the status of clickable that function received
        ImageView img1 = findViewById(R.id.question00);
        img1.setClickable(status);
        ImageView img2 = findViewById(R.id.question01);
        img2.setClickable(status);
        ImageView img3 = findViewById(R.id.question02);
        img3.setClickable(status);
        ImageView img4 = findViewById(R.id.question03);
        img4.setClickable(status);
        ImageView img5 = findViewById(R.id.question10);
        img5.setClickable(status);
        ImageView img6 = findViewById(R.id.question11);
        img6.setClickable(status);
        ImageView img7 = findViewById(R.id.question12);
        img7.setClickable(status);
        ImageView img8 = findViewById(R.id.question13);
        img8.setClickable(status);
        ImageView img9 = findViewById(R.id.question20);
        img9.setClickable(status);
        ImageView img10 = findViewById(R.id.question21);
        img10.setClickable(status);
        ImageView img11 = findViewById(R.id.question22);
        img11.setClickable(status);
        ImageView img12 = findViewById(R.id.question23);
        img12.setClickable(status);
    }
}
