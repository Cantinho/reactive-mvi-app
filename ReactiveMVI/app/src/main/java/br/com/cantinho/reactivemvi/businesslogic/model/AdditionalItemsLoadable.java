package br.com.cantinho.reactivemvi.businesslogic.model;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Created by samirtf on 25/01/18.
 */

public class AdditionalItemsLoadable implements FeedItem {
  private final int moreItemsAvailableCount;
  private final String groupName;
  private final boolean loading;
  private final Throwable loadingError;

  public AdditionalItemsLoadable(final int moreItemsAvailableCount, @NonNull final String groupName,
      final boolean loading,
      @Nullable final Throwable loadingError) {
    this.moreItemsAvailableCount = moreItemsAvailableCount;
    this.groupName = groupName;
    this.loading = loading;
    this.loadingError = loadingError;
  }

  public int getMoreItemsAvailableCount() {
    return moreItemsAvailableCount;
  }

  @NonNull
  public String getGroupName() {
    return groupName;
  }

  public boolean isLoading() {
    return loading;
  }

  public Throwable getLoadingError() {
    return loadingError;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AdditionalItemsLoadable that = (AdditionalItemsLoadable) o;

    if (moreItemsAvailableCount != that.moreItemsAvailableCount) {
      return false;
    }
    if (loading != that.loading) {
      return false;
    }
    if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) {
      return false;
    }
    return loadingError != null ? loadingError.equals(that.loadingError)
        : that.loadingError == null;
  }

  @Override
  public int hashCode() {
    int result = moreItemsAvailableCount;
    result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
    result = 31 * result + (loading ? 1 : 0);
    result = 31 * result + (loadingError != null ? loadingError.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AdditionalItemsLoadable{" +
        "moreItemsAvailableCount=" + moreItemsAvailableCount +
        ", groupName='" + groupName + '\'' +
        ", loading=" + loading +
        ", loadingError=" + loadingError +
        '}';
  }
}
