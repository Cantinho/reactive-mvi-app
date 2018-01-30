package br.com.cantinho.reactivemvi.view.home;

import android.support.v4.util.Pair;
import br.com.cantinho.reactivemvi.businesslogic.feed.HomeFeedLoader;
import br.com.cantinho.reactivemvi.businesslogic.model.AdditionalItemsLoadable;
import br.com.cantinho.reactivemvi.businesslogic.model.FeedItem;
import br.com.cantinho.reactivemvi.businesslogic.model.SectionHeader;
import br.com.cantinho.reactivemvi.view.home.PartialStateChanges.PullToRefreshLoading;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

/**
 * Created by samirtf on 29/01/18.
 */

public class HomePresenter extends MviBasePresenter<HomeView, HomeViewState> {

  private final HomeFeedLoader feedLoader;

  public HomePresenter(final HomeFeedLoader feedLoader) {
    this.feedLoader = feedLoader;
  }

  @Override
  protected void bindIntents() {

    // In a real app this code would rather be moved to an Interactor
    Observable<PartialStateChanges> loadFirstPage = intent(HomeView::loadFirstPageIntent)
        .doOnNext(ignored -> {
          Timber.d("intent: load first page");
        })
        .flatMap(ignored -> feedLoader.loadFirstPage())
        .map(items -> (PartialStateChanges) new PartialStateChanges.FirstPageLoaded(items))
        .startWith(new PartialStateChanges.FirstPageLoading())
        .onErrorReturn(PartialStateChanges.FirstPageError::new)
        .subscribeOn(Schedulers.io());

    Observable<PartialStateChanges> nextPage =
        intent(HomeView::loadFirstPageIntent)
        .doOnNext(ignored -> Timber.d("intent: load next page"))
        .flatMap(ignored -> feedLoader.loadNextPage())
        .map(items -> (PartialStateChanges) new PartialStateChanges.NextPageLoaded(items))
        .startWith(new PartialStateChanges.NextPageLoading())
        .onErrorReturn(PartialStateChanges.NextPageLoadingError::new)
        .subscribeOn(Schedulers.io());

    Observable<PartialStateChanges> pullToRefresh =
        intent(HomeView::pullToRefreshIntent)
        .doOnNext(ignored -> Timber.d("intent: pull to refresh"))
        .flatMap(ignored -> feedLoader.loadNewestPage())
        .subscribeOn(Schedulers.io())
        .map(items -> (PartialStateChanges) new PartialStateChanges.PullToRefreshLoaded(items))
        .startWith(new PullToRefreshLoading())
        .onErrorReturn(PartialStateChanges.PullToRefreshLoadingError::new);

    Observable<PartialStateChanges> loadMoreFromGroup =
        intent(HomeView::loadAllProductsFromCategoryIntent)
        .doOnNext(categoryName -> Timber.d("Intent: load more from category %s", categoryName))
        .flatMap(categoryName -> feedLoader.loadProductsOfCategory(categoryName)
            .subscribeOn(Schedulers.io())
            .map(products -> (PartialStateChanges) new PartialStateChanges
                .ProductsOfCategoryLoaded(products, categoryName))
            .startWith(new PartialStateChanges.ProductsOfCategoryLoading(categoryName))
            .onErrorReturn(error -> new PartialStateChanges.ProductsOfCategoryLoadingError
                (categoryName, error)));

    Observable<PartialStateChanges> allIntentObservable =
        Observable.merge(loadFirstPage, nextPage, pullToRefresh, loadMoreFromGroup)
        .observeOn(AndroidSchedulers.mainThread());

    HomeViewState initialState = new HomeViewState.Builder().firstPageLoading(true).build();

    subscribeViewState(
        allIntentObservable.scan(initialState, this::viewStateReducer). distinctUntilChanged(),
        HomeView::render);
  }

  private HomeViewState viewStateReducer(final HomeViewState previousState,
      final PartialStateChanges partialChanges) {

    if(partialChanges instanceof PartialStateChanges.FirstPageLoading) {
      return previousState.builder().firstPageLoading(true).firstPageError(null).build();
    }

    if(partialChanges instanceof PartialStateChanges.FirstPageError) {
      return previousState.builder().firstPageLoading(false)
          .firstPageError(((PartialStateChanges.FirstPageError) partialChanges).getError())
          .build();
    }

    if(partialChanges instanceof PartialStateChanges.FirstPageLoaded) {
      return previousState.builder().firstPageLoading(false)
          .firstPageError(null)
          .data(((PartialStateChanges.FirstPageLoaded) partialChanges).getData())
          .build();
    }

    if(partialChanges instanceof PartialStateChanges.NextPageLoading) {
      return previousState.builder().nextPageLoading(true)
          .nextPageError(null)
          .build();
    }

    if(partialChanges instanceof PartialStateChanges.NextPageLoadingError) {
      return previousState.builder().nextPageLoading(false)
          .nextPageError(((PartialStateChanges.NextPageLoadingError) partialChanges).getError())
          .build();
    }

    if(partialChanges instanceof PartialStateChanges.NextPageLoaded) {
      List<FeedItem> data = new ArrayList<>(previousState.getData().size()
      + ((PartialStateChanges.PullToRefreshLoaded) partialChanges).getData().size());

      data.addAll(((PartialStateChanges.PullToRefreshLoaded) partialChanges).getData());
      data.addAll(previousState.getData());
      return previousState.builder()
          .pullToRefreshLoading(false)
          .pullToRefreshError(null)
          .data(data)
          .build();
    }

    if(partialChanges instanceof PartialStateChanges.PullToRefreshLoading) {
      return previousState.builder().pullToRefreshLoading(true).pullToRefreshError(null).build();
    }

    if(partialChanges instanceof PartialStateChanges.PullToRefreshLoadingError) {
      return previousState.builder()
          .pullToRefreshLoading(false)
          .pullToRefreshError(((PartialStateChanges.PullToRefreshLoadingError) partialChanges).getError())
          .build();
    }

    if(partialChanges instanceof PartialStateChanges.ProductsOfCategoryLoading) {
      Pair<Integer, AdditionalItemsLoadable> found = findAdditionalItems(((PartialStateChanges
          .ProductsOfCategoryLoading) partialChanges).getCategoryName(), previousState.getData());

      AdditionalItemsLoadable foundItem = found.second;
      AdditionalItemsLoadable toInsert =
          new AdditionalItemsLoadable(foundItem.getMoreItemsAvailableCount(),
              foundItem.getGroupName(), true, null);

      List<FeedItem> data = new ArrayList<>(previousState.getData().size());
      data.addAll(previousState.getData());
      data.set(found.first, toInsert);

      return previousState.builder().data(data).build();
    }

    if(partialChanges instanceof PartialStateChanges.ProductsOfCategoryLoadingError) {
      Pair<Integer, AdditionalItemsLoadable> found = findAdditionalItems(
          ((PartialStateChanges.ProductsOfCategoryLoadingError) partialChanges).getCategoryName(),
          previousState.getData());

      AdditionalItemsLoadable foundItem = found.second;
      AdditionalItemsLoadable toInsert =
          new AdditionalItemsLoadable(foundItem.getMoreItemsAvailableCount(),
              foundItem.getGroupName(), false,
              ((PartialStateChanges.ProductsOfCategoryLoadingError) partialChanges).getError());

      List<FeedItem> data = new ArrayList<>(previousState.getData().size());
      data.addAll(previousState.getData());
      data.set(found.first, toInsert);

      return previousState.builder().data(data).build();
    }

    if(partialChanges instanceof PartialStateChanges.ProductsOfCategoryLoaded) {
      Pair<Integer, AdditionalItemsLoadable> found = findAdditionalItems(
          ((PartialStateChanges.ProductsOfCategoryLoaded) partialChanges).getCategoryName(),
          previousState.getData());

      List<FeedItem> data = new ArrayList<>(previousState.getData().size()
        + ((PartialStateChanges.ProductsOfCategoryLoaded) partialChanges).getData().size());
      data.addAll(previousState.getData());

      // Search for the section header
      int sectionHeaderIndex = -1;
      for (int i = found.first; i >= 0; i--) {
        FeedItem item = previousState.getData().get(i);
        if(item instanceof SectionHeader && ((SectionHeader) item).getName()
            .equals(
                ((PartialStateChanges.ProductsOfCategoryLoaded) partialChanges).getCategoryName()
            )) {
          sectionHeaderIndex = i;
          break;
        }
        // Remove all items of that category. The new list of products will be added afterwards
        data.remove(i);
      }

      if(sectionHeaderIndex < 0) {
        throw new RuntimeException("Couldn't find the section header for category "
          + ((PartialStateChanges.ProductsOfCategoryLoaded) partialChanges).getCategoryName());
      }
      data.addAll(sectionHeaderIndex + 1, ((PartialStateChanges.ProductsOfCategoryLoaded)
          partialChanges).getData());

      return previousState.builder().data(data).build();
    }

    throw new IllegalStateException("Don't know how to reduce the partial state " + partialChanges);
  }

  /**
   * Finds the @{@link AdditionalItemsLoadable} for the given category name.
   *
   * @param categoryName The name of the category.
   * @param items The list of feeditems.
   * @return
   */
  private Pair<Integer, AdditionalItemsLoadable> findAdditionalItems(final String categoryName,
      final List<FeedItem> items) {
    int size = items.size();
    for(int i = 0; i < size; i++) {
      FeedItem item = items.get(i);
      if(item instanceof AdditionalItemsLoadable
          && ((AdditionalItemsLoadable) item).getGroupName().equals(categoryName)) {
        return Pair.create(i, (AdditionalItemsLoadable) item);
      }
    }

    throw new RuntimeException("No "
    + AdditionalItemsLoadable.class.getSimpleName()
    + " has been found for category = "
    + categoryName);
  }


}
