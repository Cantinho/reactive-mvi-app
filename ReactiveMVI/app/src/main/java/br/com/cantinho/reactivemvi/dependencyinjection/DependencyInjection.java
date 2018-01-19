package br.com.cantinho.reactivemvi.dependencyinjection;


import br.com.cantinho.reactivemvi.businesslogic.http.ProductBackendApi;
import br.com.cantinho.reactivemvi.businesslogic.http.ProductBackendApiDecorator;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchInteractor;
import br.com.cantinho.reactivemvi.businesslogic.searchengine.SearchEngine;
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


}
