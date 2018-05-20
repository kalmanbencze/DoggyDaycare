package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public abstract class ItemAdapter<ITEM_T, VIEW_MODEL_T extends ItemViewModel<ITEM_T>>
    extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder<ITEM_T, VIEW_MODEL_T>> {

    protected final ArrayList<ITEM_T> items = new ArrayList<>();

    @Override
    public final void onBindViewHolder(ItemViewHolder<ITEM_T, VIEW_MODEL_T> holder, int position) {
        holder.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder<T, VT extends ItemViewModel<T>>
        extends RecyclerView.ViewHolder {

        protected final VT viewModel;

        public ItemViewHolder(View itemView, VT viewModel) {
            super(itemView);
            this.viewModel = viewModel;
        }

        protected void setItem(T item) {
            viewModel.setItem(item);
        }
    }
}