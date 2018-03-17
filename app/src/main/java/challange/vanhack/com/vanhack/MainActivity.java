package challange.vanhack.com.vanhack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import challange.vanhack.com.vanhack.fragments.StoresFragment;
import challange.vanhack.com.vanhack.service.ServiceFactory;
import challange.vanhack.com.vanhack.service.VanhackService;
import challange.vanhack.com.vanhack.service.model.Cousine;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout frameLayout;
    private StoresFragment StoresFragment;
    private MainActivity self;
    private ArrayList<Cousine> cousineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        self = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCousines();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        frameLayout = findViewById(R.id.frmLayout);

        request();


        StoresFragment = StoresFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frmLayout, StoresFragment)
                .commit();

        
    }

    public void request(){
        VanhackService service = ServiceFactory.createRetrofitService(VanhackService.class, VanhackService.SERVICE_ENDPOINT);
        service.getCousines()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Cousine>>() {
                    @Override
                    public final void onCompleted() { }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("GithubDemo", e.getMessage());
                    }

                    @Override
                    public final void onNext(List<Cousine> response) {
                        cousineList = new ArrayList<>();
                        for(Cousine cousine: response){
                            cousineList.add(cousine);
                        }
                    }
                });
    }

    private void displayCousines(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Pick Cousine");

        List<String> list = new ArrayList<>();
        for(Cousine cousine: cousineList){
            list.add(cousine.getName());
        }
        CharSequence[] items = list.toArray(new CharSequence[list.size()]);

        dialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                StoresFragment.request(cousineList.get(position).getId());
            }

        });
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_orders) {
            startActivity(new Intent(this, CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frmLayout, StoresFragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
