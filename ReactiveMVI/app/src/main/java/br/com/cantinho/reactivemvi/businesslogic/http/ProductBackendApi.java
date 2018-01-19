package br.com.cantinho.reactivemvi.businesslogic.http;

import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by samirtf on 18/01/18.
 */

public interface ProductBackendApi {

  public static final String BASE_URL = "https://raw.githubusercontent.com";
  public static final String BASE_URL_BRANCH = "master";
  public static final String BASE_IMAGE_URL =
      BASE_URL + "/sockeqwe/mosby/" + BASE_URL_BRANCH + "/sample-mvi/server/images/";

  @GET("/sockeqwe/mosby/" + BASE_URL_BRANCH + "sample-mvi/server/api/products{pagination}.json")
  public Observable<List<Product>> getProducts(@Path("pagination") int pagination);


}
