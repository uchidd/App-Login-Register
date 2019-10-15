package id.uchidd.apploginregister.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import id.uchidd.apploginregister.DashboardActivity;
import id.uchidd.apploginregister.helper.HelperClass;
import id.uchidd.apploginregister.R;
import id.uchidd.apploginregister.handler.SessionHandler;
import id.uchidd.apploginregister.helper.Singleton;

public class LoginActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private String login_url = "https://api-uchidd.000webhostapp.com/api/login_register/v1/login.php";

    private EditText etUsername;
    private EditText etPassword;

    private String Username;
    private String Password;

    private ProgressDialog pdDialog;
    private SessionHandler session;

    HelperClass helperClass = new HelperClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if (session.isLoggedIn()){
            loadDashboard();
        }

        setContentView(R.layout.activity_login);
        
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        
        Button btn_register = (Button)findViewById(R.id.btnLoginRegister);
        Button btn_login = (Button)findViewById(R.id.btnLogin) ;

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helperClass.pindahActivity(LoginActivity.this, RegisterActivity.class);
                finish();

            }
        });
        
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Username = etUsername.getText().toString().toLowerCase().trim();
                Password = etPassword.getText().toString().trim();
                
                if (validateInputs()){
                    processLogin();
                }
                
            }
        });

    }

    private void processLogin() {
        displayLoader();

        JSONObject request = new JSONObject();

        try {
            request.put(KEY_USERNAME, Username);
            request.put(KEY_PASSWORD, Password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pdDialog.dismiss();

                try {
                    if (response.getInt(KEY_STATUS) == 0){
                        session.LoginUser(Username, response.getString(KEY_FULL_NAME));
                        loadDashboard();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        Singleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }

    private void displayLoader() {
        pdDialog = new ProgressDialog(LoginActivity.this);
        pdDialog.setMessage("Logging In.. Please wait...");
        pdDialog.setIndeterminate(false);
        pdDialog.setCancelable(false);
        pdDialog.show();
    }

    private boolean validateInputs() {

        if (KEY_EMPTY.equals(Username)){
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(Password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void loadDashboard() {
        Intent intentContext = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intentContext);
        finish();
    }
}
