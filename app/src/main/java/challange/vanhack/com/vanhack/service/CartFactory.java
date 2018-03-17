package challange.vanhack.com.vanhack.service;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import challange.vanhack.com.vanhack.service.model.CartItem;
import challange.vanhack.com.vanhack.service.model.Product;

public class CartFactory {

    static public List<CartItem> getList(Context c) {
        List<CartItem> list = new ArrayList<>();
        String data = c.
                getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getString("cart", null);

        if (data == null) return list;

        try {

            JSONArray ja = new JSONArray(data);
            for (int i = 0; i <= ja.length() - 1; i++) {

                JSONObject o = (JSONObject) ja.get(i);
                JSONObject p = o.getJSONObject("product");
                int quantity = o.getInt("quantity");

                Product product = new Product();
                product.setId(p.getString("id"));
                product.setName(p.getString("name"));
                product.setDescription(p.getString("description"));
                product.setPrice(p.getString("price"));
                product.setStoreId(p.getString("storeId"));

                CartItem cartItem = new CartItem(product, quantity);
                list.add(cartItem);

            }

        } catch (JSONException e) { }


        return list;

    }

    static public void updateList(Context c, List<CartItem> list) {
        c.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        .edit()
        .putString("cart", (new Gson()).toJson(list))
        .commit();
    }

    static public void addItem(Context c, CartItem cartItem) {
        List<CartItem> list = getList(c);
        list.add(cartItem);
        updateList(c, list);
    }

    static public void removeItem(Context c, CartItem cartItem) {
        List<CartItem> list = getList(c);
        for (int i = 0; i <= list.size()-1; i++){
            if(list.get(i).getId().equalsIgnoreCase(cartItem.getId())){
                list.remove(i);
            }
        }
        updateList(c, list);
    }


}
