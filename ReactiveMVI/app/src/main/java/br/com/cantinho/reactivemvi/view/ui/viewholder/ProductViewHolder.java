package br.com.cantinho.reactivemvi.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.cantinho.reactivemvi.R;
import br.com.cantinho.reactivemvi.businesslogic.model.Product;
import br.com.cantinho.reactivemvi.dependencyinjection.DependencyInjection;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by samirtf on 21/01/18.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {

  public interface ProductClickedListener {
    void onProductClicked(final Product product);
  }

  public static ProductViewHolder create(final LayoutInflater inflater, final
      ProductClickedListener listener) {
    return new ProductViewHolder(inflater.inflate(R.layout.item_product, null, false), listener);
  }

  @BindView(R.id.productImage)
  ImageView image;

  @BindView(R.id.productName)
  TextView name;

  private Product product;

  private ProductViewHolder(View itemView, ProductClickedListener clickedListener) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(v -> {
      clickedListener.onProductClicked(product);
    });
  }

  public void bind(final Product product) {
    this.product = product;
    Glide.with(itemView.getContext())
        .load(DependencyInjection.BASE_IMAGE_URL + product.getImage())
        .apply(RequestOptions.centerCropTransform())
        .into(image);
    name.setText(product.getName());
  }



}
