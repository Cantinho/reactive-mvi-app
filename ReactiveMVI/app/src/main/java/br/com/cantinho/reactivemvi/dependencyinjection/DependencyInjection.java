package br.com.cantinho.reactivemvi.dependencyinjection;


import br.com.cantinho.reactivemvi.businesslogic.feed.GroupedPagedFeedLoader;
import br.com.cantinho.reactivemvi.businesslogic.feed.HomeFeedLoader;
import br.com.cantinho.reactivemvi.businesslogic.feed.PagingFeedLoader;
import br.com.cantinho.reactivemvi.businesslogic.http.ProductBackendApi;
import br.com.cantinho.reactivemvi.businesslogic.http.ProductBackendApiDecorator;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchInteractor;
import br.com.cantinho.reactivemvi.businesslogic.searchengine.SearchEngine;
import br.com.cantinho.reactivemvi.view.home.HomePresenter;
import br.com.cantinho.reactivemvi.view.search.SearchPresenter;
import com.squareup.moshi.Moshi;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * This is just a very simple example that creates dependency injection.
 * In a real project you might would like to use dagger.
 *
 * @author Hannes Dorfmann
 */
public class DependencyInjection {

  public static String BASE_URL = "https://raw.githubusercontent.com";
  public static final String BASE_URL_BRANCH = "master";
  public static final String BASE_IMAGE_URL = BASE_URL
      + "/sockeqwe/mosby/"
      + DependencyInjection.BASE_URL_BRANCH
      + "/sample-mvi/server/images/";

  // Don't do this in your real app
  private final PublishSubject<Boolean> clearSelectionRelay = PublishSubject.create();
  private final PublishSubject<Boolean> deleteSelectionRelay = PublishSubject.create();

  // Some singletons
  private final Retrofit retrofit = new Retrofit.Builder().baseUrl(ProductBackendApi.BASE_URL)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build();

  private final ProductBackendApi backendApi = retrofit.create(ProductBackendApi.class);
  private final ProductBackendApiDecorator backendApiDecorator =
      new ProductBackendApiDecorator(backendApi);

  private SearchEngine newSearchEngine() {
    return new SearchEngine(backendApiDecorator);
  }

  private SearchInteractor newSearchInteractor() {
    return new SearchInteractor(newSearchEngine());
  }

  PagingFeedLoader newPagingFeedLoader() {
    return new PagingFeedLoader(backendApiDecorator);
  }

  GroupedPagedFeedLoader newGroupedPagedFeedLoader() {
    return new GroupedPagedFeedLoader(newPagingFeedLoader());
  }

  HomeFeedLoader newHomeFeedLoader() {
    return new HomeFeedLoader(newGroupedPagedFeedLoader(), backendApiDecorator);
  }

  public SearchPresenter newSearchPresenter() {
    return new SearchPresenter(newSearchInteractor());
  }

  public HomePresenter newHomePresenter() {
    return new HomePresenter(newHomeFeedLoader());
  }

}
