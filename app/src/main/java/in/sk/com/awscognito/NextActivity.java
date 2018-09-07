package in.sk.com.awscognito;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.sk.com.awscognito.databinding.ActivityNextBinding;

public class NextActivity extends AppCompatActivity {
    ActivityNextBinding nextBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextBinding= DataBindingUtil.setContentView(this,R.layout.activity_next);

    }
}
