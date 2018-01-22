package br.com.cantinho.reactivemvi.view.search;

import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import io.reactivex.Observable;


/**
 * Created by samirtf on 21/01/18.
 */

public interface SearchView extends MvpView {

  /**
   * The search intent.
   *
   * @return An observable emitting search query text.
   */
  Observable<String> searchIntent();

  /**
   * Renders the viewState.
   *
   * @param viewState The current @{@link SearchViewState} state that should be displayed.
   */
  void render(final SearchViewState viewState);
}
