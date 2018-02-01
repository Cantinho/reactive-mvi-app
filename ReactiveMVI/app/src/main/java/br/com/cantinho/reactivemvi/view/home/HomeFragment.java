package br.com.cantinho.reactivemvi.view.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.cantinho.reactivemvi.R;
import br.com.cantinho.reactivemvi.SampleApplication;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import br.com.cantinho.reactivemvi.view.ui.viewholder.ProductViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hannesdorfmann.mosby3.mvi.MviFragment;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import io.reactivex.Observable;
import timber.log.Timber;


/**
 * Created by samirtf on 30/01/18.
 */

public class HomeFragment extends MviFragment<HomeView, HomePresenter> implements HomeView,
    ProductViewHolder.ProductClickedListener {

  private HomeAdapter adapter;
  private GridLayoutManager layoutManager;
  private Unbinder unbinder;

  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.loadingView)
  View loadingView;

  @BindView(R.id.errorView)
  TextView errorView;

  @BindView(R.integer.grid_span_size)
  int spanCount;

  @NonNull @Override public HomePresenter createPresenter() {
    Timber.d("createPresenter");
    return SampleApplication.getDependencyInjection(getActivity()).newHomePresenter();
  }

  @Override
  public void onProductClicked(final Product product) {
    //TODO implement this.
    //ProductDetailsActivity.start(getActivity(), product);
  }

  @Nullable @Override
  public View onCreateView(final LayoutInflater inflater, final @Nullable ViewGroup container,
      final @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this, view);

    adapter = new HomeAdapter(inflater, this);
    layoutManager = new GridLayoutManager(getActivity(), spanCount);
    layoutManager.setSpanSizeLookup(new SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        int viewType = adapter.getItemViewType(position);
        if(viewType == HomeAdapter.VIEW_TYPE_LOADING_MORE_NEXT_PAGE
          || viewType == HomeAdapter.VIEW_TYPE_SECTION_HEADER) {
          return spanCount;
        }
        return 1;
      }
    });

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(layoutManager);

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public Observable<Boolean> loadFirstPageIntent() {
    return Observable.just(true).doOnComplete(() -> Timber.d("firstPage completed"));
  }

  @Override
  public Observable<Boolean> loadNextPageIntent() {
    return RxRecyclerView.scrollStateChanges(recyclerView)
        .filter(event -> !adapter.isLoadingNextPage())
        .filter(event -> event == RecyclerView.SCROLL_STATE_IDLE)
        .filter(event -> layoutManager.findLastCompletelyVisibleItemPosition()
          == adapter.getItems().size() - 1)
        .map(integer -> true);
  }

  @Override
  public Observable<Boolean> pullToRefreshIntent() {
    return null;
  }

  @Override
  public Observable<String> loadAllProductsFromCategoryIntent() {
    return null;
  }

  @Override
  public void render(HomeViewState viewState) {

  }

}
