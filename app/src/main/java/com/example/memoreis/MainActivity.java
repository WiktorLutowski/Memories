package com.example.memoreis;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running = false;
    private int[] imageIds = new int[12];
    private int[] drawables;
    private TextView timerTV;
    private Button startButton;

    private int selectedIndex = -1;
    private int selectedIndexImage = -1;
    private int toReset1 = -1;
    private int toReset2 = -1;

    private ImageView[] imageViews = new ImageView[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            numbers.add(i);
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        for (int i = 0; i < numbers.size(); i++) {
            imageIds[i] = numbers.get(i);
        }


        startButton = findViewById(R.id.startButton);
        timerTV = findViewById(R.id.timerTV);
        runTimer();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
            }
        });

        for (int i = 0; i < 12; i++) {
            String imageID = "image" + (i + 1);
            int resID = getResources().getIdentifier(imageID, "id", getPackageName());

            imageViews[i] = findViewById(resID);

            imageViews[i].setImageResource(R.drawable.znaczek);
            imageViews[i].setTag(-1);

            int finalI = i;
            imageViews[finalI].setOnClickListener(v -> {

                if(!running || selectedIndexImage == finalI)
                    return;

                if(toReset1 != -1) {
                    imageViews[toReset1].setImageResource(R.drawable.znaczek);
                    imageViews[toReset2].setImageResource(R.drawable.znaczek);
                    imageViews[toReset2].setTag(-1);
                    imageViews[toReset1].setTag(-1);

                    toReset1 = -1;
                    toReset2 = -1;
                }

                switch (imageIds[finalI])
                {
                    case 1:
                        imageViews[finalI].setImageResource(R.drawable.auto1);
                        break;
                    case 2:
                        imageViews[finalI].setImageResource(R.drawable.auto2);
                        break;
                    case 3:
                        imageViews[finalI].setImageResource(R.drawable.auto3);
                        break;
                    case 4:
                        imageViews[finalI].setImageResource(R.drawable.auto4);
                        break;
                    case 5:
                        imageViews[finalI].setImageResource(R.drawable.auto5);
                        break;
                    case 6:
                        imageViews[finalI].setImageResource(R.drawable.auto6);
                        break;
                }


                imageViews[finalI].setTag(1);

                if(selectedIndex == -1) {
                    selectedIndexImage = finalI;
                    selectedIndex = imageIds[finalI];
                }
                else
                {
                    if(selectedIndex != imageIds[finalI])
                    {
                        toReset1 = selectedIndexImage;
                        toReset2 = finalI;


                    }
                    selectedIndex = -1;
                    selectedIndexImage = -1;
                }

                for (int j = 0; j < 12; j++) {
                    if((Integer) imageViews[j].getTag() == -1)
                        return;
                }

                running = false;

            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


        private void runTimer() {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int minutes = (seconds % 3600) / 60;
                    int secs = seconds % 60;

                    String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                    timerTV.setText(time);

                    if (running) {
                        seconds++;
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }


        @Override
        protected void onDestroy() {
            super.onDestroy();
            running = false;
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        seconds = savedInstanceState.getInt("seconds");
        running = savedInstanceState.getBoolean("running");
    }
    }
