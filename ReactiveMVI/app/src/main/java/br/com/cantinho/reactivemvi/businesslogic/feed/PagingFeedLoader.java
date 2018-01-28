package br.com.cantinho.reactivemvi.businesslogic.feed;

import br.com.cantinho.reactivemvi.businesslogic.http.ProductBackendApiDecorator;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.List;

/**
 * Created by samirtf on 25/01/18.
 */

public class PagingFeedLoader {

  private final ProductBackendApiDecorator backend;
  private int currentPage = 1;
  private boolean endReached = false;
  private boolean newestPageLoaded = false;

  public PagingFeedLoader(final ProductBackendApiDecorator backend) {
    this.backend = backend;
  }

  public Observable<List<Product>> newestPage() {
    if(newestPageLoaded) {
      return Observable.fromCallable(() -> {
        Thread.sleep(2000);
        return Collections.emptyList();
      });
    }
    return backend.getProducts(0).doOnNext(products -> newestPageLoaded = true);
  }

  public Observable<List<Product>> nextPage() {
    // I know, it's a pure function nor elegant code
    // but that is not the purpose of this demo.
    // This code should be understandable by everyone.
    if(endReached) {
      return Observable.just(Collections.emptyList());
    }

    return backend.getProducts(currentPage).doOnNext(result -> {
      currentPage++;
      if(result.isEmpty()) {
        endReached = true;
      }
    });
  }

}
