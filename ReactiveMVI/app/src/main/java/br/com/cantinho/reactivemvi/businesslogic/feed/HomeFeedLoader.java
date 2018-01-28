package br.com.cantinho.reactivemvi.businesslogic.feed;

import br.com.cantinho.reactivemvi.businesslogic.http.ProductBackendApiDecorator;
import br.com.cantinho.reactivemvi.businesslogic.model.FeedItem;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import io.reactivex.Observable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by samirtf on 28/01/18.
 */

public class HomeFeedLoader {

  private final GroupedPagedFeedLoader groupedLoader;
  private final ProductBackendApiDecorator backendApi;

  public HomeFeedLoader(final GroupedPagedFeedLoader groupedLoader,
      final ProductBackendApiDecorator backendApi) {
    this.groupedLoader = groupedLoader;
    this.backendApi = backendApi;
  }

  /**
   * Typically triggered with a pull-to-refresh.
   *
   * @return
   */
  public Observable<List<FeedItem>> loadNewestPage() {
    return groupedLoader.getNewestPage().delay(2, TimeUnit.SECONDS);
  }

  /**
   * Loads the first page.
   *
   * @return
   */
  public Observable<List<FeedItem>> loadFirstPage() {
    return groupedLoader.getGroupedFirstPage().delay(2, TimeUnit.SECONDS);
  }

  /**
   * Loads the next page.
   *
   * @return
   */
  public Observable<List<FeedItem>> loadNextPage() {
    return groupedLoader.getGroupedNextPage().delay(2, TimeUnit.SECONDS);
  }

  public Observable<List<Product>> loadProductsOfCategory(final String category) {
    return backendApi.getAllProductsOfCategory(category).delay(3, TimeUnit.SECONDS);
  }

}
