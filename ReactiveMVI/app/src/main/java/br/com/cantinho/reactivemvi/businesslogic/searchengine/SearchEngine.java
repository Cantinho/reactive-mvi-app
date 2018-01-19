package br.com.cantinho.reactivemvi.businesslogic.searchengine;

import br.com.cantinho.reactivemvi.businesslogic.http.ProductBackendApiDecorator;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import java.util.List;

/**
 * Created by samirtf on 18/01/18.
 */
public class SearchEngine {

  private final ProductBackendApiDecorator backend;

  public SearchEngine(final ProductBackendApiDecorator productApi) {
    this.backend = productApi;
  }

  public Observable<List<Product>> searchFor(final @NonNull String searchQueryText) {
    if(searchQueryText == null) {
      return Observable.error(new NullPointerException("SearchQueryText == null"));
    }

    if(searchQueryText.length() == 0) {
      return Observable.error(new IllegalArgumentException("SearchQueryText is blank"));
    }

    return backend.getAllProducts()
        .flatMap(Observable::fromIterable)
        .filter(product -> isProductMatchingSearchCriteria(product, searchQueryText))
        .toList()
        .toObservable();
  }

  /**
   * Filters those items that contains the search query text in name, description or category.
   *
   * @param product
   * @param searchQueryText
   * @return
   */
  private boolean isProductMatchingSearchCriteria(final Product product,
      final String searchQueryText) {
    final String words[] = searchQueryText.split(" ");
    for(final String word : words) {
      if(product.getName().contains(word)) {
        return true;
      }
      if(product.getDescription().contains(word)) {
        return true;
      }
      if(product.getCategory().contains(word)) {
        return true;
      }
    }
    return false;
  }

}
