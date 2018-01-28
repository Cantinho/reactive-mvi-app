package br.com.cantinho.reactivemvi.businesslogic.feed;

import br.com.cantinho.reactivemvi.businesslogic.model.AdditionalItemsLoadable;
import br.com.cantinho.reactivemvi.businesslogic.model.FeedItem;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import br.com.cantinho.reactivemvi.businesslogic.model.SectionHeader;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by samirtf on 25/01/18.
 */

public class GroupedPagedFeedLoader {

  private final PagingFeedLoader feedLoader;
  private final int collapsedGroupProductItemCount = 3;

  public GroupedPagedFeedLoader(final PagingFeedLoader feedLoader) {
    this.feedLoader = feedLoader;
  }

  public Observable<List<FeedItem>> getGroupedFirstPage() {
    return getGroupedNextPage();
  }

  public Observable<List<FeedItem>> getGroupedNextPage() {
    return groupByCategory(feedLoader.nextPage());
  }

  public Observable<List<FeedItem>> getNewestPage() {
    return groupByCategory(feedLoader.newestPage());
  }

  private Observable<List<FeedItem>> groupByCategory(
      final Observable<List<Product>> originalListGroup) {

    return originalListGroup.flatMap(Observable::fromIterable)
        .groupBy(Product::getCategory)
        .flatMap(groupByCategory -> groupByCategory.toList().map(productsInGroup -> {
          final String groupName = groupByCategory.getKey();
          final List<FeedItem> items = new ArrayList<>();
          items.add(new SectionHeader(groupName));
          if (collapsedGroupProductItemCount < productsInGroup.size()) {
            for (int i = 0; i < collapsedGroupProductItemCount; i++) {
              items.add(productsInGroup.get(i));
            }
            items.add(
                new AdditionalItemsLoadable(productsInGroup.size() -
                    collapsedGroupProductItemCount, groupName, false, null));
          } else {
            items.addAll(productsInGroup);
          }
          return items;
        }).toObservable())
        .concatMap(Observable::fromIterable)
        .toList()
        .toObservable();

  }

}
