package br.com.cantinho.reactivemvi.businesslogic.http;


import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by samirtf on 18/01/18.
 */

public class ProductBackendApiDecorator {

  private final ProductBackendApi api;

  public ProductBackendApiDecorator(final ProductBackendApi api) {
    this.api = api;
  }

  /**
   * Gets a list products of specific page.
   *
   * @param pagination
   * @return
   */
  public Observable<List<Product>> getProducts(int pagination) {
    return api.getProducts(pagination);
  }

  /**
   * Gets a list with all products from backend.
   *
   * @return an observable product list.
   */
  public Observable<List<Product>> getAllProducts() {
    return Observable.zip(getProducts(0), getProducts(1), getProducts(2), getProducts(3),
        (products0, products1, products2, products3) -> {
          final List<Product> productList = new ArrayList<Product>();
          productList.addAll(products0);
          productList.addAll(products1);
          productList.addAll(products2);
          productList.addAll(products3);
          return productList;
        });
  }

  /**
   * Gets all products of a certain category.
   *
   * @param categoryName The name of the category.
   * @return an observable of a product list.
   */
  public Observable<List<Product>> getAllProductsOfCategory(final String categoryName) {
    return getAllProducts().flatMap(Observable::fromIterable)
        .filter(product ->  product.getCategory().equals(categoryName))
        .toList()
        .toObservable();
  }

  /**
   * Gets a list with all categories.
   *
   * @return an observable of product list.
   */
  public Observable<List<String>> getAllCategories() {
    return getAllProducts().map(products -> {
      final Set<String> categories = new HashSet<String>();
      for(Product product : products) {
        categories.add(product.getCategory());
      }
      final List<String> result = new ArrayList<>(categories.size());
      result.addAll(categories);
      return result;
    });
  }

  /**
   * Gets the product with the given id.
   *
   * @param productId The product id.
   * @return an observable product.
   */
  public Observable<Product> getProduct(int productId) {
    return getAllProducts().flatMap(products -> Observable.fromIterable(products))
        .filter(product -> product.getId() == productId)
        .take(1);
  }

}
