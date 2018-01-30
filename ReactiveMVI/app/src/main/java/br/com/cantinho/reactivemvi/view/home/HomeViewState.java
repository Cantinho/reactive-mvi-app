package br.com.cantinho.reactivemvi.view.home;

import br.com.cantinho.reactivemvi.businesslogic.model.FeedItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by samirtf on 23/01/18.
 */

public final class HomeViewState {

  /**
   * Shows the loading indicator instead of recyclerView.
   */
  private final boolean loadingFirstPage;

  /**
   * Shows an error view if != null.
   */
  private final Throwable firstPageError;

  /**
   * The items displayed in the recyclerview.
   */
  private final List<FeedItem> data;

  /**
   * Show the loading indicator for pagination.
   */
  private final boolean loadingNextPage;

  /**
   * If != null, shows error toast that pagination failed.
   */
  private final Throwable nextPageError;

  /**
   * Shows the pull-to-refresh indicator.
   */
  private final boolean loadingPullToRefresh;

  /**
   * If != null, shows error toast that pull-to-refresh failed.
   */
  private final Throwable pullToRefreshError;

  /**
   * Builds a custom homeViewState.
   *
   * @param data
   * @param loadingFirstPage
   * @param firstPageError
   * @param loadingNextPage
   * @param nextPageError
   * @param loadingPullToRefresh
   * @param pullToRefreshError
   */
  private HomeViewState(final List<FeedItem> data, boolean loadingFirstPage,
      final Throwable firstPageError, boolean loadingNextPage, final Throwable nextPageError,
      boolean loadingPullToRefresh, final Throwable pullToRefreshError) {
    this.data = data;
    this.loadingNextPage = loadingNextPage;
    this.nextPageError = nextPageError;
    this.loadingPullToRefresh = loadingPullToRefresh;
    this.pullToRefreshError = pullToRefreshError;
    this.firstPageError = firstPageError;
    this.loadingFirstPage = loadingFirstPage;
  }

  public boolean isLoadingFirstPage() {
    return loadingFirstPage;
  }

  public Throwable getFirstPageError() {
    return firstPageError;
  }

  public List<FeedItem> getData() {
    return data;
  }

  public boolean isLoadingNextPage() {
    return loadingNextPage;
  }

  public Throwable getNextPageError() {
    return nextPageError;
  }

  public boolean isLoadingPullToRefresh() {
    return loadingPullToRefresh;
  }

  public Throwable getPullToRefreshError() {
    return pullToRefreshError;
  }

  public Builder builder() {
    return new Builder(this);
  }


  @Override
  public String toString() {
    return "HomeViewState{" +
        "loadingFirstPage=" + loadingFirstPage +
        ", firstPageError=" + firstPageError +
        ", data=" + data +
        ", loadingNextPage=" + loadingNextPage +
        ", nextPageError=" + nextPageError +
        ", loadingPullToRefresh=" + loadingPullToRefresh +
        ", pullToRefreshError=" + pullToRefreshError +
        '}';
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    HomeViewState that = (HomeViewState) o;

    if (loadingFirstPage != that.loadingFirstPage) {
      return false;
    }
    if (loadingNextPage != that.loadingNextPage) {
      return false;
    }
    if (loadingPullToRefresh != that.loadingPullToRefresh) {
      return false;
    }
    if (firstPageError != null ? !firstPageError.equals(that.firstPageError)
        : that.firstPageError != null) {
      return false;
    }
    if (data != null ? !data.equals(that.data) : that.data != null) {
      return false;
    }
    if (nextPageError != null ? !nextPageError.equals(that.nextPageError)
        : that.nextPageError != null) {
      return false;
    }
    return pullToRefreshError != null ? pullToRefreshError.equals(that.pullToRefreshError)
        : that.pullToRefreshError == null;
  }

  @Override
  public int hashCode() {
    int result = (loadingFirstPage ? 1 : 0);
    result = 31 * result + (firstPageError != null ? firstPageError.hashCode() : 0);
    result = 31 * result + (data != null ? data.hashCode() : 0);
    result = 31 * result + (loadingNextPage ? 1 : 0);
    result = 31 * result + (nextPageError != null ? nextPageError.hashCode() : 0);
    result = 31 * result + (loadingPullToRefresh ? 1 : 0);
    result = 31 * result + (pullToRefreshError != null ? pullToRefreshError.hashCode() : 0);
    return result;
  }

  public static final class Builder {

    /**
     * Shows the loading indicator instead of recyclerView
     */
    private boolean loadingFirstPage;

    /**
     * Show an error view if != null.
     */
    private Throwable firstPageError;

    /**
     * The items displayed in the recyclerview.
     */
    private List<FeedItem> data;

    /**
     * Shows the loading indicator for pagination.
     */
    private boolean loadingNextPage;

    /**
     * if != null, shows error toast that pagination failed.
     */
    private Throwable nextPageError;

    /**
     * Shows the pull-to-refresh indicator.
     */
    private boolean loadingPullToRefresh;

    /**
     * if != null, shows error toast that pull-to-refresh failed.
     */
    private Throwable pullToRefreshError;

    public Builder() {
      data = Collections.emptyList();
    }

    public Builder(final HomeViewState toCopyFrom) {
      this.data = new ArrayList<>(toCopyFrom.getData().size());
      this.data.addAll(toCopyFrom.getData());
      this.loadingFirstPage = toCopyFrom.isLoadingFirstPage();
      this.loadingNextPage = toCopyFrom.isLoadingNextPage();
      this.nextPageError = toCopyFrom.getNextPageError();
      this.pullToRefreshError = toCopyFrom.getPullToRefreshError();
      this.firstPageError = toCopyFrom.getFirstPageError();
    }

    public Builder firstPageLoading(boolean loadingFirstPage) {
      this.loadingFirstPage = loadingFirstPage;
      return this;
    }

    public Builder firstPageError(final Throwable error) {
      this.firstPageError = error;
      return this;
    }

    public Builder data(final List<FeedItem> data) {
      this.data = data;
      return this;
    }

    public Builder nextPageLoading(boolean loadingNextPage) {
      this.loadingNextPage = loadingNextPage;
      return this;
    }

    public Builder nextPageError(final Throwable error) {
      this.nextPageError = error;
      return this;
    }

    public Builder pullToRefreshLoading(boolean loading) {
      this.loadingPullToRefresh = loading;
      return this;
    }

    public Builder pullToRefreshError(final Throwable error) {
      this.pullToRefreshError = error;
      return this;
    }

    public HomeViewState build() {
      return new HomeViewState(data, loadingFirstPage, firstPageError, loadingNextPage,
          nextPageError, loadingPullToRefresh, pullToRefreshError);
    }



  }
}
