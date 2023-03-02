package com.angelo_silvestre.lotteryplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String Tag = "MainActivity";


    // Temp storage to store six numbers input
    ArrayList<String> storeSelectedSixNumbers = new ArrayList<String>();
    ArrayList<String> machineDraw = new ArrayList<String>();
    // Array of Objects || Objects
    ArrayList<BettingCard> card = new ArrayList<>();
    ArrayList<Integer> winPriceList = new ArrayList<>();
    User user;



    // Id of buttons
    private static final int[] btnIDList ={
            R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_10,
            R.id.btn_11, R.id.btn_12, R.id.btn_13, R.id.btn_14, R.id.btn_15, R.id.btn_16, R.id.btn_17, R.id.btn_18, R.id.btn_19, R.id.btn_20,
            R.id.btn_21, R.id.btn_22, R.id.btn_23, R.id.btn_24, R.id.btn_25, R.id.btn_26, R.id.btn_27, R.id.btn_28, R.id.btn_29, R.id.btn_30,
            R.id.btn_31, R.id.btn_32, R.id.btn_33, R.id.btn_34, R.id.btn_35, R.id.btn_36, R.id.btn_37, R.id.btn_38, R.id.btn_39, R.id.btn_40,
            R.id.btn_41, R.id.btn_42, R.id.btn_43, R.id.btn_44, R.id.btn_45,
    };
    private int length = btnIDList.length;

    // Initial variable for UI
    private TextView[] btnBox = new TextView[length];
    private Boolean[] isBtnBoxClicked = new Boolean[length];
    private TextView username, balValue, totalBet;
    private TextView drawValOne, drawValTwo,drawValThree,drawValFour,drawValFive,drawValSix, notifWin;
    private Button multiplier,betAgainBtn,playButton,homeBtn;
    private EditText betValue;
    private CardView lottoNumCont,betCont,playGameCont;
    private RecyclerView contactRecView;


    // Global variable
    private String[] multiplierValue = {"1x","2x","4x","8x","10x"};

    private int countMulti = 0;
    private int totalBetInt = 0;
    private boolean validateButton = false;
    private int randomNumber;
    private int multiplierInt = 0 ;






    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();

        initViews();


        multiplier.setOnClickListener(view -> multiplierUpdate());

        betValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("LOGER",charSequence.length()+"" );
                Log.i("Key:", charSequence.toString());
                realTimeUpdateBet();
                if(validateButton()){
                    enabledButton();
                } else {
                    disabledButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        betAgainBtn.setOnClickListener(view ->  {
            if(validateButton) {

                passTheValuesToBettingCard();
                resetInput();


                if(balValue.getText().toString().equals("0")) {
                    enabledButton();
                    totalBet.setTextColor(Color.parseColor("#443BC9"));
                    totalBet.setText("GO! ROLL THE BALL!");
                }
            }

        });

        playButton.setOnClickListener(view -> {
            if(validateButton) {

                passTheValuesToBettingCard();
                resetInput();

                generateMachineDraw();
                switchViewToGame();
                sceneOneOff();
                sceneTwoOn();

            }

        });

        homeBtn.setOnClickListener(view -> {
            sceneTwoOff();
            sceneOneOn();
            card.clear();
        });

//        ArrayList<String> storeSelectedSixNumbers = new ArrayList<String>(Arrays.asList("20", "15", "30", "2", "1", "6"));
//        ArrayList<String> storeSelectedSixNumbers2 = new ArrayList<String>(Arrays.asList("15", "6", "30", "26", "20", "1"));
//
//        card.add(new BettingCard(storeSelectedSixNumbers));
//        card.add(new BettingCard(storeSelectedSixNumbers2));







    }

    private void switchViewToGame() {
        // DISPLAY THE SELECTED NUMBERS BY BET CARDS
        NumberSelectedRecViewAdapter adapter = new NumberSelectedRecViewAdapter(this);
        adapter.setContacts(card);

        contactRecView.setAdapter(adapter);
        contactRecView.setLayoutManager(new LinearLayoutManager(this));

        drawValOne.setText(machineDraw.get(0));
        drawValTwo.setText(machineDraw.get(1));
        drawValThree.setText(machineDraw.get(2));
        drawValFour.setText(machineDraw.get(3));
        drawValFive.setText(machineDraw.get(4));
        drawValSix.setText(machineDraw.get(5));



        checkCorrectSelectedNumbersToDrawNumbers();

        winPriceList.clear();
        Log.d(Tag,"After clear :  " + Arrays.toString(winPriceList.toArray()));

    }



    private void checkCorrectSelectedNumbersToDrawNumbers(){

        Log.d(Tag,"checkCorrectSelectedNumbersToDrawNumbers get started");
       // String selectedNumbers = Arrays.toString(c.getCard().toArray()).substring(1).replaceFirst("]", "").replace(", ", ", ");
        for (int i = 0; i < card.size(); i++) {


            ArrayList<String> tempNumber = new ArrayList<String>(machineDraw);
            // TESTING
            Log.d(Tag,"Selected : Draw  = " +
                    Arrays.toString(card.get(i).getCard().toArray())
                    + " : " +  Arrays.toString(tempNumber.toArray())
            );

            tempNumber.retainAll(card.get(i).getCard());


                if(tempNumber.size() == 3) {
                    winPriceList.add(100 * card.get(i).getMultiplier());
                } else if  (tempNumber.size() == 4) {
                    winPriceList.add(1500 * card.get(i).getMultiplier());
                } else if (tempNumber.size() == 5) {
                    winPriceList.add(50000  * card.get(i).getMultiplier());
                } else if (tempNumber.size() == 6) {
                    winPriceList.add(9000000);
                }


            // TESTING

            Log.d(Tag,"Common elements in both list: " + Arrays.toString(tempNumber.toArray()));
            Log.d(Tag,"Size of : " + tempNumber.size());

        }



        Log.d(Tag,"winPriceList :  " + Arrays.toString(winPriceList.toArray()));
        if(winPriceList.isEmpty()) {
            notifWin.setText("You lose! Please Try again!");
            notifWin.setTextColor(Color.parseColor("#FF4545"));
        } else {
            // add all the price list
            notifWin.setText("You win total of ₱ " + Integer.toString(addAllPrices())+ "!");
            notifWin.setTextColor(Color.parseColor("#2DD872"));

            //add the price to wallet
            user.setWalletBalance(user.getWalletBalance() + addAllPrices());
            balValue.setText(Integer.toString(user.getWalletBalance()));
        }





    }

    private int addAllPrices(){
        int sum = 0;
        for(Integer d : winPriceList)
            sum += d;
        return sum;
    }
    @SuppressLint("SetTextI18n")
    private void passTheValuesToBettingCard() {
        Log.d(Tag,"passTheValuesToBettingCard get started");
        // store to the array of object

        ArrayList<String> storeSelectedSixNumbersTemp = new ArrayList<>(storeSelectedSixNumbers);
        card.add(new BettingCard(totalBetInt,storeSelectedSixNumbersTemp,multiplierInt));



        //deduct the user wallet by total bet
        user.setWalletBalance(user.getWalletBalance() - totalBetInt);
        balValue.setText(Integer.toString(user.getWalletBalance()));



        //Testing console
        Log.d(Tag,"Lotto Card: ");
        for (int i = 0; i < card.size(); i++) {
            Log.d(Tag,"Price: "+ card.get(i).getBetPrice() + " " );
            Log.d(Tag,"Numbers: "+ card.get(i).getCard() + " " );
            Log.d(Tag,"Multiplier: "+ card.get(i).getMultiplier() + " " );

        }
    }

    @SuppressLint("SetTextI18n")
    private void realTimeUpdateBet(){

         String input = betValue.getText().toString();
         String multiplierText = multiplier.getText().toString().replaceAll("[^\\d]", "");

         multiplierInt =  Integer.parseInt("0" + multiplierText);

         totalBetInt = Integer.parseInt("0" + input) * multiplierInt ;

         if(user.getWalletBalance() < totalBetInt) {
//             Toast.makeText(this, "balanceVal < totalBetInt", Toast.LENGTH_SHORT).show();
             totalBet.setTextColor(Color.parseColor("#FF4545"));
             totalBet.setText("Your balance is not enough!");
         } else {
             Log.i("balanceVal: ",String.valueOf(user.getWalletBalance()));
             Log.i("totalBetDouble: ",String.valueOf(totalBetInt));

             totalBet.setTextColor(Color.parseColor("#525685"));
             totalBet.setText("= " + String.valueOf(totalBetInt) );
         }

    }
    private void multiplierUpdate(){
        if(countMulti==5) {
            countMulti =0;
        }

        multiplier.setText(multiplierValue[countMulti]);
        realTimeUpdateBet();
        countMulti++;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void initViews(){
        Log.d(Tag, "Initial Views: Started");
        // TextView
        username = findViewById(R.id.username);
        balValue = findViewById(R.id.balance_value);
        totalBet = findViewById(R.id.total_bet);
        //Buttons
        multiplier = findViewById(R.id.multiplier);
        betAgainBtn = findViewById(R.id.bet_again_button);
        playButton = findViewById(R.id.play_button);
        homeBtn = findViewById(R.id.home_button);
        //EditText
        betValue = findViewById(R.id.bet_value);
        //TextView Buttons Draws
        drawValOne = findViewById(R.id.draw_number_1);
        drawValTwo = findViewById(R.id.draw_number_2);
        drawValThree = findViewById(R.id.draw_number_3);
        drawValFour = findViewById(R.id.draw_number_4);
        drawValFive = findViewById(R.id.draw_number_5);
        drawValSix  = findViewById(R.id.draw_number_6);

        notifWin = findViewById(R.id.notifPriceWin);

        // Container
        contactRecView = findViewById(R.id.contactRecView);
        playGameCont = findViewById(R.id.lottery_machine_draw_container);
        lottoNumCont = findViewById(R.id.lottery_number_container);
        betCont = findViewById(R.id.bet_container);


        balValue.setText(Integer.toString(user.getWalletBalance()));



        resetInput();





        for(int i=0; i < length; i++) {
            //init variables
            btnBox[i] = findViewById(btnIDList[i]);
            btnBox[i].setText(Integer.toString(i+1));

            int cloneI = i;

            btnBox[i].setOnClickListener(view -> {


                if(isBtnBoxClicked[cloneI]) {
                    //set the boolean index to false
                    isBtnBoxClicked[cloneI] = false;

                    //change the background and the text color to default
                   btnBox[cloneI].setBackgroundResource(R.drawable.rounded_corner);
                   btnBox[cloneI].setTextColor(Color.parseColor("#525685"));

                    // Delete the value clicked to Storage input
                    storeSelectedSixNumbers.remove(btnBox[cloneI].getText().toString());
                    if(storeSelectedSixNumbers.size() <= 5) {
                        betCont.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white_gray));
                    }

                    if(validateButton()){
                        enabledButton();
                    } else {
                        disabledButton();
                    }


                    // Testing only
                    Log.d(Tag, "Arraylist rand: "+  Arrays.toString(storeSelectedSixNumbers.toArray()));
                } else {

                    // Only 6 can be added to  store number
                    if(storeSelectedSixNumbers.size() >= 6) {
                        Toast.makeText(this, "You selected 6 number", Toast.LENGTH_SHORT).show();
                    }else {
                        //Take all the values!
                        //set the boolean index to true
                        isBtnBoxClicked[cloneI] = true;

                        //change the background and the text color
                        btnBox[cloneI].setBackgroundResource(R.drawable.rounded_corner_2);
                        btnBox[cloneI].setTextColor(Color.parseColor("#FFFFFF"));

                        // Store the value clicked to 5 digit input
                        storeSelectedSixNumbers.add(btnBox[cloneI].getText().toString());
                        if(storeSelectedSixNumbers.size() == 6) {

                        betCont.setCardBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));

                            if(validateButton()){
                                enabledButton();
                            } else {
                                disabledButton();
                            }

                        }
                    }




                    // Testing only
                    Log.d(Tag, "Arraylist rand: "+  Arrays.toString(storeSelectedSixNumbers.toArray()));
                }
            });

        }

    }

    private void generateMachineDraw(){
        Log.d(Tag, "generateMachineDraw Views: Started");
        Set<String> s = new HashSet<String>();
        int duplicateCount = 0;
        machineDraw.clear();
        for(int i = 0; i< 6; i++) {
            generateRandomNumber();
            machineDraw.add(Integer.toString(this.getRandomNumber()));
        }

//        ArrayList<String> storeSelectedSixNumbers = new ArrayList<String>(Arrays.asList("20", "15", "30", "2", "1", "6"));
//        machineDraw = new ArrayList<>(storeSelectedSixNumbers);


        for (String name : machineDraw) {
            if (s.add(name) == false) {
                duplicateCount++;
            }
        }
        Log.d(Tag, "MachineDraw: " +  Arrays.toString(machineDraw.toArray()));
        Log.d(Tag, "Total duplicate count: " + duplicateCount);

        if(duplicateCount > 0) {
            Set<String> set = new HashSet<>(machineDraw);
            machineDraw.clear();
            machineDraw.addAll(set);
            if(machineDraw.size() != 6) {
                generateMachineDraw();
            }
        }

        Log.d(Tag, "MachineDraw: " +  Arrays.toString(machineDraw.toArray()));
    }
    private void generateRandomNumber(){
        Log.d(Tag, "generateRandomNumber Views: Started");
        Random rand = new Random();
        //generate random values from 0-100
        int upperbound = 45;
        int randomNum = rand.nextInt(upperbound) + 1;
        setRandomNumber(randomNum);
//        Log.d(Tag, "Random Number is : " + this.getRandomNumber());
    }

    private Boolean validateButton(){
        return storeSelectedSixNumbers.size() >= 6 && !betValue.getText().toString().equals("") && user.getWalletBalance() >= totalBetInt;

    }
    private void storeTheLottoNumbers() {
        int count = 0;
        for(int i=0 ; i<length; i++) {
            if(isBtnBoxClicked[i]) {
//                storeSelectedSixNumbers.add(count+1);
            } else {
//                storeSelectedSixNumbers.remove();
            }
        }
    }


    private void enabledButton(){
        validateButton = true;
        betAgainBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        betAgainBtn.setTextColor(Color.parseColor("#3E34B5"));
        playButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
        playButton.setTextColor(Color.parseColor("#3E34B5"));
//        Toast.makeText(this, "enabledButton()", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ResourceAsColor")
    private void disabledButton(){
//        Toast.makeText(this, "disabledButton()", Toast.LENGTH_SHORT).show();
        validateButton = false;
        betAgainBtn.setBackgroundColor(Color.parseColor("#A5A5A8"));
        betAgainBtn.setTextColor(Color.parseColor("#BCBDC3"));
        playButton.setBackgroundColor(Color.parseColor("#A5A5A8"));
        playButton.setTextColor(Color.parseColor("#BCBDC3"));

    }

    private void sceneOneOn(){
        multiplier.setVisibility(View.VISIBLE);
        lottoNumCont.setVisibility(View.VISIBLE);
        betAgainBtn.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
    }

    private void sceneOneOff(){
        multiplier.setVisibility(View.GONE);
        lottoNumCont.setVisibility(View.GONE);
        betAgainBtn.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
    }

    private void sceneTwoOn(){
        playGameCont.setVisibility(View.VISIBLE);
        notifWin.setVisibility(View.VISIBLE);
        homeBtn.setVisibility(View.VISIBLE);

    }
    private void sceneTwoOff(){
        playGameCont.setVisibility(View.GONE);
        notifWin.setVisibility(View.GONE);
        homeBtn.setVisibility(View.GONE);
    }

    private void resetInput() {
        Log.d(Tag, "Reset input Started");
        // reset toggle
        for(int i=0 ; i<length; i++) {
            isBtnBoxClicked[i]= false;
        }

        // disabled the buttons
        disabledButton();

        // back to x1 the multiplier
        multiplier.setText(multiplierValue[0]);

        // Clear the array list
        storeSelectedSixNumbers.clear();

        // clear total and the edit textview
        betValue.setText("");
        betValue.clearFocus();
        totalBet.setText("");


        // change background to default
        betCont.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white_gray));
        for(int i=0; i < length; i++) {
            //change the background and the text color to default
            btnBox[i] = findViewById(btnIDList[i]);
            btnBox[i].setText(Integer.toString(i + 1));
            btnBox[i].setBackgroundResource(R.drawable.rounded_corner);
            btnBox[i].setTextColor(Color.parseColor("#525685"));
            int cloneI = i;
        }
        // console
        Log.d(Tag, "isBtnBoxClicked[i]  after reset : " +  Arrays.toString(isBtnBoxClicked));
        Log.d(Tag, "storeSelectedSixNumbers after reset : " +  Arrays.toString(storeSelectedSixNumbers.toArray()));
        Log.d(Tag, "Total bet  after reset : " +totalBetInt);
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

}