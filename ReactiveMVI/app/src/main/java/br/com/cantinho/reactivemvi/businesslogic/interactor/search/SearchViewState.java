package br.com.cantinho.reactivemvi.businesslogic.interactor.search;

import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import java.util.List;

/**
 * Created by samirtf on 17/01/18.
 */

public interface SearchViewState {

  /**
   * The search has not been started yet.
   */
  final class SearchNotStartedYet implements SearchViewState {
  }

  /**
   * Loading: Currently waiting for search result.
   */
  final class Loading implements SearchViewState {
  }

  /**
   * Indicates that the search has delivered an empty result set.
   */
  final class EmptyResult implements SearchViewState {

    /**
     * Search query text.
     */
    private final String searchQueryText;

    /**
     * Builds an empty result.
     *
     * @param searchQueryText a search query text.
     */
    public EmptyResult(final String searchQueryText) {
      this.searchQueryText = searchQueryText;
    }

    /**
     * Retrieves a search query text.
     *
     * @return a string representing a search query text.
     */
    public String getSearchQueryText() {
      return searchQueryText;
    }
  }

  /**
   * A valid search result. Contains a list of items that have matched the searching criteria.
   */
  final class SearchResult implements SearchViewState {

    /**
     * A search query text.
     */
    private final String searchQueryText;

    /**
     * A list of products as result.
     */
    private final List<Product> result;

    /**
     * Builds a custom search result.
     *
     * @param searchQueryText a string as search query text.
     * @param result a list of products as result.
     */
    public SearchResult(final String searchQueryText, final List<Product> result) {
      this.searchQueryText = searchQueryText;
      this.result = result;
    }

    /**
     * Retrieves a search query text.
     *
     * @return a string as search query text.
     */
    public String getSearchQueryText() {
      return searchQueryText;
    }

    /**
     * Retrieves a list of products as result.
     *
     * @return a list os products.
     */
    public List<Product> getResult() {
      return result;
    }
  }

  /**
   * Indicates that an error has occurred while searching.
   */
  final class ErrorResult implements SearchViewState {

    /**
     * A search query text.
     */
    private final String searchQueryText;

    /**
     * An error.
     */
    private final Throwable error;

    /**
     * Builds a custom error.
     *
     * @param searchQueryText a string as search query text.
     * @param error an error.
     */
    public ErrorResult(final String searchQueryText, final Throwable error) {
      this.searchQueryText = searchQueryText;
      this.error = error;
    }

    /**
     * Retrieves a search query text.
     *
     * @return a string as a search query text.
     */
    public String getSearchQueryText() {
      return searchQueryText;
    }

    /**
     * Retrieves an error.
     *
     * @return an error.
     */
    public Throwable getError() {
      return error;
    }
  }


}
