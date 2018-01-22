package br.com.cantinho.reactivemvi.view.search;

import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchInteractor;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState
    .SearchNotStartedYet;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by samirtf on 21/01/18.
 */

public class SearchPresenter extends MviBasePresenter<SearchView, SearchViewState> {

  private final SearchInteractor searchInteractor;

  public SearchPresenter(final SearchInteractor interactor) {
    super(new SearchNotStartedYet());
    this.searchInteractor = interactor;
  }

  @Override
  protected void bindIntents() {
    final Observable<SearchViewState> search =
        intent(SearchView::searchIntent)
        .doOnNext(string -> Timber.d("intent: Search '%s'", string))
        .switchMap(searchInteractor::search)
        .observeOn(AndroidSchedulers.mainThread());
    subscribeViewState(search, SearchView::render);
  }
}
