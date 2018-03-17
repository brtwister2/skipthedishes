package challange.vanhack.com.vanhack.service;


import java.util.List;

import challange.vanhack.com.vanhack.service.model.Cousine;
import challange.vanhack.com.vanhack.service.model.Product;
import challange.vanhack.com.vanhack.service.model.Store;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface VanhackService {
    String SERVICE_ENDPOINT = "http://api-vanhack-event-sp.azurewebsites.net/api/v1";

    @POST("/Costumer/auth")
    Observable<String> auth(@Query("email") String email,
                          @Query("password") String password);


    @GET("/Cousine")
    Observable<List<Cousine>> getCousines();

    @GET("/Cousine/{cid}/stores")
    Observable<List<Store>> getStoresByCid(@Path("cid") String cid);

    @GET("/Store/{storeId}/products")
    Observable<List<Product>> getProductsBySid(@Path("storeId") String sid);

}