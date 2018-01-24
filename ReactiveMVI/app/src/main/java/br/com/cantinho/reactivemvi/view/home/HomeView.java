package br.com.cantinho.reactivemvi.view.home;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import io.reactivex.Observable;

/**
 * Created by samirtf on 23/01/18.
 */

public interface HomeView extends MvpView {

  /**
   * The intent to load the first page.
   *
   * @return The value of the emitted item (boolean) can be ignored. True or false has no
   * difference.
   */
  public Observable<Boolean> loadFirstPageIntent();

  /**
   * The intent to load the next page (pagination)
   *
   * @return The valur or the emitted item (boolean) can be ignored. True or false has no
   * difference.
   */
  public Observable<Boolean> loadNextPageIntent();

  /**
   * The intent to react on pull-to-refresh.
   *
   * @return The value of the emitted item (boolean) can be ignored. True or false has no
   * difference.
   */
  public Observable<Boolean> pullToRefreshIntent();

  /**
   * The intent to load more items from a given category.
   *
   * @return Observavle with the name of category.
   */
  public Observable<String> loadAllProductsFromCategoryIntent();

  /**
   * Renders the viewState.
   *
   * @param viewState
   */
  public void render(HomeViewState viewState);

}
