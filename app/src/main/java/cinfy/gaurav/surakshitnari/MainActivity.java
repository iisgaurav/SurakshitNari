package cinfy.gaurav.surakshitnari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import cinfy.gaurav.surakshitnari.R;


public class MainActivity extends AppCompatActivity {
    //variables
    Animation bottomAnim;
    TextView name , slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //animations
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        //hooks
        name = findViewById(R.id.textView);

        name.setAnimation(bottomAnim);

        new Handler( ).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.this==null){
                    return;
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}