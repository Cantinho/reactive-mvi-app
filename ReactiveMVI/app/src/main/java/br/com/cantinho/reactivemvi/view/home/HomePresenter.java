package br.com.cantinho.reactivemvi.view.home;

import android.support.v4.util.Pair;
import br.com.cantinho.reactivemvi.businesslogic.feed.HomeFeedLoader;
import br.com.cantinho.reactivemvi.businesslogic.model.AdditionalItemsLoadable;
import br.com.cantinho.reactivemvi.businesslogic.model.FeedItem;
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

    //TODO implement this.
    //TODO remove null return.
    return null;

  }


}
