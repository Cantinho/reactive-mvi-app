package br.com.cantinho.reactivemvi.businesslogic.interactor.search;

import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState.EmptyResult;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState.ErrorResult;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState.Loading;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState.SearchResult;
import br.com.cantinho.reactivemvi.businesslogic.searchengine.SearchEngine;
import io.reactivex.Observable;

/**
 * I's responsible to execute the search.
 */
public class SearchInteractor {

  /**
   * Engine makes http calls.
   */
  final SearchEngine searchEngine;

  public SearchInteractor(final SearchEngine searchEngine) {
    this.searchEngine = searchEngine;
  }

  /**
   * Searches for items.
   *
   * @param searchString
   * @return
   */
  public Observable<SearchViewState> search(final String searchString) {
    // Empty String, so no search
    if(searchString.isEmpty()) {
      return Observable.just(new SearchViewState.SearchNotStartedYet());
    }

    // search for products
    return searchEngine.searchFor(searchString)
        .map(products -> {
          if(products.isEmpty()) {
            return new EmptyResult(searchString);
          } else {
            return new SearchResult(searchString, products);
          }
        })
        .startWith(new Loading())
        .onErrorReturn(error -> new ErrorResult(searchString, error));
  }
}
