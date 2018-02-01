package br.com.cantinho.reactivemvi.view.home;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import br.com.cantinho.reactivemvi.businesslogic.model.AdditionalItemsLoadable;
import br.com.cantinho.reactivemvi.businesslogic.model.FeedItem;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import br.com.cantinho.reactivemvi.businesslogic.model.SectionHeader;
import br.com.cantinho.reactivemvi.view.ui.viewholder.LoadingViewHolder;
import br.com.cantinho.reactivemvi.view.ui.viewholder.MoreItemsViewHolder;
import br.com.cantinho.reactivemvi.view.ui.viewholder.ProductViewHolder;
import br.com.cantinho.reactivemvi.view.ui.viewholder.SectionHeaderViewHolder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.List;

/**
 * Created by samirtf on 30/01/18.
 */

public class HomeAdapter extends RecyclerView.Adapter implements
    MoreItemsViewHolder.LoadItemsClickListener {

  static final int VIEW_TYPE_PRODUCT = 0;
  static final int VIEW_TYPE_LOADING_MORE_NEXT_PAGE = 1;
  static final int VIEW_TYPE_SECTION_HEADER = 2;
  static final int VIEW_TYPE_MORE_ITEMS_AVAILABLE = 3;

  private boolean isLoadingNextPage = false;
  private List<FeedItem> items;
  private final LayoutInflater layoutInflater;
  private final ProductViewHolder.ProductClickedListener productClickedListener;

  private PublishSubject<String> loadMoreItemsOfCategoryObservable = PublishSubject.create();

  public HomeAdapter(final LayoutInflater layoutInflater,
      final ProductViewHolder.ProductClickedListener productClickedListener) {
    this.layoutInflater = layoutInflater;
    this.productClickedListener = productClickedListener;
  }

  public List<FeedItem> getItems() {
    return items;
  }

  /**
   * Sets loading next page and notify if it has changes.
   *
   * @param loadingNextPage
   * @return true if value has changed since last invocation.
   */
  public boolean setLoadingNextPage(final boolean loadingNextPage) {
    final boolean hasLoadingMoreChange = loadingNextPage != isLoadingNextPage;
    final boolean notifyInserted = loadingNextPage && hasLoadingMoreChange;
    final boolean notifyRemoved = !loadingNextPage && hasLoadingMoreChange;
    isLoadingNextPage = loadingNextPage;

    if(notifyInserted) {
      notifyItemInserted(items.size());
    } else if (notifyRemoved) {
      notifyItemRemoved(items.size());
    }

    return hasLoadingMoreChange;
  }

  public boolean isLoadingNextPage() {
    return isLoadingNextPage;
  }

  public void setItems(final List<FeedItem> newItems) {
    final List<FeedItem> oldItems = this.items;
    this.items = newItems;

    if(oldItems == null) {
      notifyDataSetChanged();
    } else {
      // Use Diff utils
      DiffUtil.calculateDiff(new DiffUtil.Callback() {

        @Override
        public int getOldListSize() {
          return oldItems.size();
        }

        @Override
        public int getNewListSize() {
          return newItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
          Object oldItem = oldItems.get(oldItemPosition);
          Object newItem = newItems.get(newItemPosition);

          if(oldItem instanceof Product && newItem instanceof Product
              && ((Product) oldItem).getId() == ((Product) newItem).getId()) {
            return true;
          }

          if(oldItem instanceof SectionHeader && newItem instanceof SectionHeader
              && ((SectionHeader)oldItem).getName().equals(((SectionHeader) newItem).getName())) {
            return true;
          }

          if(oldItem instanceof AdditionalItemsLoadable
              && newItem instanceof AdditionalItemsLoadable
              && ((AdditionalItemsLoadable) oldItem).getGroupName().equals((
                  (AdditionalItemsLoadable) newItem).getGroupName())) {
            return true;
          }

          return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
          Object oldItem = oldItems.get(oldItemPosition);
          Object newItem = newItems.get(newItemPosition);
          return oldItem.equals(newItem);

        }
      }, true).dispatchUpdatesTo(this);
    }
  }

  @Override
  public int getItemViewType(final int position) {
    if (isLoadingNextPage && position == items.size()) {
      return VIEW_TYPE_LOADING_MORE_NEXT_PAGE;
    }

    FeedItem item = items.get(position);
    if(item instanceof Product) {
      return VIEW_TYPE_PRODUCT;
    } else if (item instanceof SectionHeader) {
      return VIEW_TYPE_SECTION_HEADER;
    } else if (item instanceof AdditionalItemsLoadable) {
      return VIEW_TYPE_MORE_ITEMS_AVAILABLE;
    }

    throw new IllegalArgumentException("Not able to determine the view type for item at position "
        + position + ". Item is: " + item);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_PRODUCT:
        return ProductViewHolder.create(layoutInflater, productClickedListener);
      case VIEW_TYPE_LOADING_MORE_NEXT_PAGE:
        return LoadingViewHolder.create(layoutInflater);
      case VIEW_TYPE_MORE_ITEMS_AVAILABLE:
        return MoreItemsViewHolder.create(layoutInflater, this);
      case VIEW_TYPE_SECTION_HEADER:
        return SectionHeaderViewHolder.create(layoutInflater);
    }
    throw new IllegalArgumentException("Couldn't create a ViewHolder for viewType = " + viewType);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (holder instanceof LoadingViewHolder) {
      return;
    }

    FeedItem item = items.get(position);
    if(holder instanceof ProductViewHolder) {
      ((ProductViewHolder) holder).bind((Product) item);
    } else if (holder instanceof SectionHeaderViewHolder) {
      ((SectionHeaderViewHolder) holder).onBind((SectionHeader) item);
    } else if (holder instanceof MoreItemsViewHolder) {
      ((MoreItemsViewHolder) holder).bind((AdditionalItemsLoadable) item);
    } else {
      throw new IllegalArgumentException("couldn't accept ViewHolder " + holder);
    }
  }

  /**
   * If it's loading next page, consider it in count.
   *
   * @return
   */
  @Override
  public int getItemCount() {
    return items == null ? 0 : (items.size() + (isLoadingNextPage ? 1 : 0));
  }

  @Override
  public void loadItemsForCategory(String category) {
    loadMoreItemsOfCategoryObservable.onNext(category);
  }

  public Observable<String> loadMoreItemsOfCategoryObservable() {
    return loadMoreItemsOfCategoryObservable;
  }
}
