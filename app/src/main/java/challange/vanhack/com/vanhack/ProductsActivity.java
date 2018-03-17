package challange.vanhack.com.vanhack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import challange.vanhack.com.vanhack.fragments.OrdersFragment;
import challange.vanhack.com.vanhack.fragments.ProductsFragment;

public class ProductsActivity extends AppCompatActivity {

    ProductsFragment productsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        productsFragment = ProductsFragment.newInstance();

        if(getSupportActionBar() != null)
            getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frmLayout, productsFragment)
                .commit();

        if(getIntent() != null && getIntent().getExtras() != null){
            if(getIntent().getExtras().getString("sid") != null){
                productsFragment.request(getIntent().getExtras().getString("sid"));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
