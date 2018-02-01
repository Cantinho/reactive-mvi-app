package br.com.cantinho.reactivemvi.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import br.com.cantinho.reactivemvi.R;

/**
 * Simply shows a progress bar. This is typically used for pagination to indicate that more items
 * are loading.
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {

  public static LoadingViewHolder create(final LayoutInflater inflater) {
    return new LoadingViewHolder(inflater.inflate(R.layout.item_loading, null, false));
  }

  public LoadingViewHolder(View itemView) {
    super(itemView);
  }
}
