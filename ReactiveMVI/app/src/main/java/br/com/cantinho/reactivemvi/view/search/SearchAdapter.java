package br.com.cantinho.reactivemvi.view.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.ViewGroup;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import br.com.cantinho.reactivemvi.view.ui.viewholder.ProductViewHolder;
import java.util.List;

/**
 * Adapter display search results
 *
 * @author Hannes Dorfmann
 */
public class SearchAdapter extends RecyclerView.Adapter<ProductViewHolder> {

  private final LayoutInflater inflater;
  private final ProductViewHolder.ProductClickedListener productClickedListener;
  private List<Product> products;

  public SearchAdapter(final LayoutInflater inflater, final ProductViewHolder
      .ProductClickedListener productClickedListener) {
    this.inflater = inflater;
    this.productClickedListener = productClickedListener;
  }

  public void setProducts(final List<Product> products) {
    this.products = products;
  }

  @Override
  public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return ProductViewHolder.create(inflater, productClickedListener);
  }

  @Override
  public void onBindViewHolder(ProductViewHolder holder, int position) {
    holder.bind(products.get(position));
  }

  @Override
  public int getItemCount() {
    return products == null ? 0 : products.size();
  }
}
