package br.com.cantinho.reactivemvi.view.home;

import br.com.cantinho.reactivemvi.businesslogic.model.FeedItem;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import java.util.List;

/**
 * Created by samirtf on 28/01/18.
 */

public interface PartialStateChanges {

  /**
   * Indicates that the first page is loading.
   */
  final class FirstPageLoading implements PartialStateChanges {

    @Override
    public String toString() {
      return "FirstPageLoadingState{}";
    }
  }

  /**
   * Indicates that an error has occurred while loading the first page.
   */
  final class FirstPageError implements PartialStateChanges {
    private final Throwable error;

    public FirstPageError(final Throwable error) {
      this.error = error;
    }

    public Throwable getError() {
      return error;
    }

    @Override
    public String toString() {
      return "FirstPageError{" +
          "error=" + error +
          '}';
    }
  }

  /**
   * Next page has been loaded successfully.
   */
  final class FirstPageLoaded implements PartialStateChanges {
    private final List<FeedItem> data;

    public FirstPageLoaded(final List<FeedItem> data) {
      this.data = data;
    }

    public List<FeedItem> getData() {
      return data;
    }

    @Override
    public String toString() {
      return "FirstPageLoaded{" +
          "data=" + data +
          '}';
    }
  }

  /**
   * Next page has been loaded successfully.
   */
  final class NextPageLoaded implements PartialStateChanges {
    private final List<FeedItem> data;

    public NextPageLoaded(final List<FeedItem> data) {
      this.data = data;
    }

    public List<FeedItem> getData() {
      return this.data;
    }

    @Override
    public String toString() {
      return "NextPageLoaded{" +
          "data=" + data +
          '}';
    }
  }

  /**
   * Indicates that loading the next page has started.
   */
  final class NextPageLoadingError implements PartialStateChanges {
    private final Throwable error;

    public NextPageLoadingError(final Throwable error) {
      this.error = error;
    }

    public Throwable getError() {
      return this.error;
    }

    @Override
    public String toString() {
      return "NextPageLoadingError{" +
          "error=" + error +
          '}';
    }
  }

  /**
   * Indicates that loading the next page has started.
   */
  final class NextPageLoading implements PartialStateChanges {
  }

  /**
   * Indicates that an error while loading the newest items via pull to refresh has occurred.
   */
  final class PullToRefreshLoading implements PartialStateChanges {
  }

  /**
   * Indicates that an error while loading the newest items via pull to refresh has occurred.
   */
  final class PullToRefreshLoadingError implements PartialStateChanges {
    private final Throwable error;

    public PullToRefreshLoadingError(final Throwable error) {
      this.error = error;
    }

    public Throwable getError() {
      return this.error;
    }
  }

  /**
   * Indicates that data has been loaded successfully over pull-to-refresh.
   */
  final class PullToRefreshLoaded implements  PartialStateChanges {
    private final List<FeedItem> data;

    public PullToRefreshLoaded(final List<FeedItem> data) {
      this.data = data;
    }

    public List<FeedItem> getData() {
      return this.data;
    }
  }

  /**
   * Loading all products of a given category has been started.
   */
  final class ProductsOfCategoryLoading implements PartialStateChanges {
    private final String categoryName;

    public ProductsOfCategoryLoading(final String categoryName) {
      this.categoryName = categoryName;
    }

    public String getCategoryName() {
      return categoryName;
    }
  }

  /**
   * An error while loading all products has been occurred.
   */
  final class ProductsOfCategoryLoadingError implements PartialStateChanges {
    private final String categoryName;
    private final Throwable error;

    public ProductsOfCategoryLoadingError(final String categoryName, final Throwable error) {
      this.categoryName = categoryName;
      this.error = error;
    }

    public String getCategoryName() {
      return categoryName;
    }

    public Throwable getError() {
      return error;
    }
  }

  /**
   * Products of a given category has been loaded.
   */
  final class ProductsOfCategoryLoaded implements PartialStateChanges {
    private final List<Product> data;
    private final String categoryName;

    public ProductsOfCategoryLoaded(final List<Product> data, final String categoryName) {
      this.data = data;
      this.categoryName = categoryName;
    }

    public List<Product> getData() {
      return data;
    }

    public String getCategoryName() {
      return categoryName;
    }

    @Override
    public String toString() {
      return "ProductsOfCategoryLoaded{" +
          "data=" + data +
          ", categoryName='" + categoryName + '\'' +
          '}';
    }
  }

}
