package br.com.cantinho.reactivemvi.view.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import br.com.cantinho.reactivemvi.R;
import br.com.cantinho.reactivemvi.businesslogic.model.SectionHeader;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samirtf on 31/01/18.
 */

public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {

  public static SectionHeaderViewHolder create(final LayoutInflater layoutInflater) {
    return new SectionHeaderViewHolder(
        layoutInflater.inflate(R.layout.item_section_header, null, false)
    );
  }

  @BindView(R.id.sectionName)
  TextView sectionName;

  public SectionHeaderViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void onBind(final SectionHeader item) {
    sectionName.setText(item.getName());
  }
}
