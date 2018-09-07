package in.sk.com.awscognito;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

import in.sk.com.awscognito.Utill.AppHelper;
import in.sk.com.awscognito.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails1=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        AWSMobileClient.getInstance().initialize(this).execute();


/*
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();*/


     //   SignInActivity.startSignInActivity(this, UILApplication.sAuthUIConfiguration);

     /*   AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(
                        MainActivity.this,
                        SignInUI.class);
                signin.login(
                        MainActivity.this,
                        NextActivity.class).execute();
            }
        }).execute();*/
        binding.btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqConfCode();
            }
        });

        binding.btSignpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.init(MainActivity.this);
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get("Email"), "sksakilhosan1@gmail.com");

                AppHelper.getPool().signUpInBackground("sksagor", "!Loveumom2", userAttributes, null, signUpHandler);

            }
        });
    }
    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Check signUpConfirmationState to see if the user is already confirmed
           // closeWaitDialog();
            Boolean regState = signUpConfirmationState;
            if (signUpConfirmationState) {
                // User is already confirmed
                Log.e("Sign up successful!",  " has been Confirmed");
            }
            else {
                // User is not confirmed
                confirmSignUp(cognitoUserCodeDeliveryDetails);
                cognitoUserCodeDeliveryDetails1=cognitoUserCodeDeliveryDetails;
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Log.e("Sign up failed",AppHelper.formatException(exception));
        }
    };

    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
    /*    Intent intent = new Intent(this, SignUpConfirm.class);
        intent.putExtra("source","signup");
        intent.putExtra("name", usernameInput);
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivityForResult(intent, 10);*/
    }


    private void reqConfCode() {
       EditText code_et=(EditText)findViewById(R.id.code_et);
       String code=code_et.getText().toString();
       AppHelper.getPool().getUser("sksagor").confirmSignUpInBackground(code,true,confHandler);

    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.e("Success!"," has been confirmed!");
        }

        @Override
        public void onFailure(Exception exception) {

        Log.e("TAG", AppHelper.formatException(exception));
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if(resultCode == RESULT_OK){
                String name = null;
                if(data.hasExtra("name")) {
                    name = data.getStringExtra("name");
                }
              //  exit(name, userPasswd);
            }
        }
    }



}
