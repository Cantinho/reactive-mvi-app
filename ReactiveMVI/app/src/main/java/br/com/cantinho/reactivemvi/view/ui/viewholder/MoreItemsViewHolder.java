package br.com.cantinho.reactivemvi.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import br.com.cantinho.reactivemvi.R;
import br.com.cantinho.reactivemvi.businesslogic.model.AdditionalItemsLoadable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samirtf on 31/01/18.
 */

public class MoreItemsViewHolder extends RecyclerView.ViewHolder {

  public interface LoadItemsClickListener {
    public void loadItemsForCategory(final String category);
  }

  public static MoreItemsViewHolder create(LayoutInflater layoutInflater,
      LoadItemsClickListener clickListener) {
    return new MoreItemsViewHolder(layoutInflater.inflate(R.layout.item_more_available, null,
        false), clickListener);
  }

  @BindView(R.id.moreItemsCount)
  TextView moreItemsCount;

  @BindView(R.id.loadingView)
  View loadingView;

  @BindView(R.id.loadMoreButton)
  View loadMoreButton;

  @BindView(R.id.errorRetryButton)
  Button errorRetryButton;

  private AdditionalItemsLoadable currentItem;

  private MoreItemsViewHolder(View itemView, LoadItemsClickListener listener) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(v -> listener.loadItemsForCategory(currentItem.getGroupName()));
    errorRetryButton.setOnClickListener( v -> listener.loadItemsForCategory(currentItem.getGroupName()));
    loadMoreButton.setOnClickListener(v -> listener.loadItemsForCategory(currentItem.getGroupName()));
  }

  public void bind(final AdditionalItemsLoadable item) {
    this.currentItem = item;

    if(item.isLoading()) {
      moreItemsCount.setVisibility(View.GONE);
      loadMoreButton.setVisibility(View.GONE);
      loadingView.setVisibility(View.VISIBLE);
      errorRetryButton.setVisibility(View.GONE);
      itemView.setClickable(false);
    } else if (item.getLoadingError() != null) {
      moreItemsCount.setVisibility(View.GONE);
      loadMoreButton.setVisibility(View.GONE);
      loadingView.setVisibility(View.GONE);
      errorRetryButton.setVisibility(View.VISIBLE);
      itemView.setClickable(true);
    } else {
      moreItemsCount.setText("+" + item.getMoreItemsAvailableCount());
      moreItemsCount.setVisibility(View.VISIBLE);
      loadMoreButton.setVisibility(View.VISIBLE);
      loadingView.setVisibility(View.GONE);
      errorRetryButton.setVisibility(View.GONE);
      itemView.setClickable(true);
    }
  }
}
