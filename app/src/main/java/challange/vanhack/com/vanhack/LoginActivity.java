package challange.vanhack.com.vanhack;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import challange.vanhack.com.vanhack.service.ServiceFactory;
import challange.vanhack.com.vanhack.service.VanhackService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v) {

        VanhackService service = ServiceFactory.createRetrofitService(VanhackService.class, VanhackService.SERVICE_ENDPOINT);
        service.auth("a@a.com", "bruno")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public final void onCompleted() {}

                    @Override
                    public final void onError(Throwable e) {
                        saveToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy" +
                                "54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYn" +
                                "J1bm8iLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW" +
                                "50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJhQGEuY29tIiwiaHR0cDovL3NjaG" +
                                "VtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy91c2" +
                                "VyZGF0YSI6IjgiLCJuYmYiOjE1MjEzMTA5MjQsImV4cCI6MTUyMzczMDEyNCwiaX" +
                                "NzIjoidmFuaGFjay1zYW9wYXVsby1mYWlyLWFwaSIsImF1ZCI6InZhbmhhY2stc2" +
                                "FvcGF1bG8tZmFpci1hcGkifQ.oOFUnxESL-UfODEubyWXUF9I7ibMn-gPPKs9psW" +
                                "2ubI");
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public final void onNext(String token) {
                        Log.d("Token:", token);
                        saveToken(token);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                });

    }

    private void saveToken(String t) {
        getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("token", t)
                .apply();
    }

}