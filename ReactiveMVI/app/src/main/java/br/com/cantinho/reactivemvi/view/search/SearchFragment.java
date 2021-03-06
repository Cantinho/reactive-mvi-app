package br.com.cantinho.reactivemvi.view.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.cantinho.reactivemvi.R;
import br.com.cantinho.reactivemvi.SampleApplication;
import br.com.cantinho.reactivemvi.businesslogic.interactor.search.SearchViewState;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import br.com.cantinho.reactivemvi.view.ui.GridSpacingItemDecoration;
import br.com.cantinho.reactivemvi.view.ui.viewholder.ProductViewHolder.ProductClickedListener;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hannesdorfmann.mosby3.mvi.MviFragment;
import com.jakewharton.rxbinding2.widget.RxSearchView;
import io.reactivex.Observable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

/**
 * Created by samirtf on 21/01/18.
 */

public class SearchFragment extends MviFragment<SearchView, SearchPresenter> implements
    SearchView, ProductClickedListener {

  @BindView(R.id.searchView)
  android.widget.SearchView searchView;
  @BindView(R.id.container)
  ViewGroup container;
  @BindView(R.id.loadingView)
  View loadingView;
  @BindView(R.id.errorView)
  TextView errorView;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.emptyView)
  View emptyView;
  @BindInt(R.integer.grid_span_size)
  int spanCount;

  private SearchAdapter adapter;
  private Unbinder unbinder;

  @Override
  public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    final View view = inflater.inflate(R.layout.fragment_search, container, false);
    unbinder = ButterKnife.bind(this, view);

    adapter = new SearchAdapter(inflater, this);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources()
        .getDimensionPixelSize(R.dimen.grid_spacing), true));

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onProductClicked(Product product) {
    //TODO implement this.
    //ProductDetailsActivity.start(getActivity(), product);
  }

  @NonNull
  @Override
  public SearchPresenter createPresenter() {
    Timber.d("createPresenter");
    return SampleApplication.getDependencyInjection(getActivity()).newSearchPresenter();
  }

  @Override
  public Observable<String> searchIntent() {
    return RxSearchView.queryTextChanges(searchView)
        .skip(2) // Because after screen orientation changes query Text will be resubmitted again.
        .filter(queryString -> queryString.length() > 3 || queryString.length() == 0)
        .debounce(500, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .map(CharSequence::toString);
  }

  @Override
  public void render(SearchViewState viewState) {
    Timber.d("render %s", viewState);
    if (viewState instanceof SearchViewState.SearchNotStartedYet) {
      renderSearchNotStarted();
    } else if (viewState instanceof SearchViewState.Loading) {
      renderLoading();
    } else if (viewState instanceof SearchViewState.SearchResult) {
      renderResult(((SearchViewState.SearchResult) viewState).getResult());
    } else if (viewState instanceof SearchViewState.EmptyResult) {
      renderEmptyResult();
    } else if (viewState instanceof SearchViewState.ErrorResult) {
      Timber.e(((SearchViewState.ErrorResult) viewState).getError());
      renderError();
    } else {
      throw new IllegalArgumentException("Don't know how to render viewState " + viewState);
    }
  }

  private void renderResult(final List<Product> result) {
    TransitionManager.beginDelayedTransition(container);
    recyclerView.setVisibility(View.VISIBLE);
    loadingView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    adapter.setProducts(result);
    adapter.notifyDataSetChanged();
  }

  private void renderSearchNotStarted() {
    TransitionManager.beginDelayedTransition(container);
    recyclerView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    emptyView.setVisibility(View.GONE);
  }

  private void renderLoading() {
    TransitionManager.beginDelayedTransition(container);
    recyclerView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
    emptyView.setVisibility(View.GONE);
  }

  private void renderError() {
    TransitionManager.beginDelayedTransition(container);
    recyclerView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.VISIBLE);
    emptyView.setVisibility(View.GONE);
  }

  private void renderEmptyResult() {
    TransitionManager.beginDelayedTransition(container);
    recyclerView.setVisibility(View.GONE);
    loadingView.setVisibility(View.GONE);
    errorView.setVisibility(View.GONE);
    emptyView.setVisibility(View.VISIBLE);
  }

}