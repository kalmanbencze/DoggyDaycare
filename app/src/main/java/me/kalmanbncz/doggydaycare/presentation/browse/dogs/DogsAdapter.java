package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.presentation.ItemAdapter;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigator;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class DogsAdapter extends ItemAdapter<Dog, DogItemViewModel> {

    private final BrowseNavigator browseNavigator;

    DogsAdapter(BrowseNavigator browseNavigator) {
        this.browseNavigator = browseNavigator;
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);
        DogItemViewModel viewModel = new DogItemViewModel(browseNavigator);
        return new DogViewHolder(parent.getContext(), itemView, viewModel);
    }

    public void setItems(List<Dog> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DogsDiffCallback(newItems, items), true);
        items.clear();
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    public void addItems(List<Dog> items) {
        ArrayList<Dog> concatenated = new ArrayList<>();
        concatenated.addAll(this.items);
        concatenated.addAll(items);
        setItems(concatenated);
    }

    static class DogViewHolder
        extends ItemViewHolder<Dog, DogItemViewModel> {

        private final Context context;

        @BindView(R.id.text_view_dog_title)
        TextView title;

        @BindView(R.id.text_view_dog_description)
        TextView description;

        DogViewHolder(Context context, View itemView, DogItemViewModel viewModel) {
            super(itemView, viewModel);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void setItem(Dog item) {
            super.setItem(item);
            title.setText(item.getName());
            description.setText(DogDescriptionFormatter.format(item));
        }

        @OnClick(R.id.item_dog_layout)
        void onClickVersionItem() {
            viewModel.onClick();
        }
    }

    private static class DogDescriptionFormatter {

        private static final String TAG = DogDescriptionFormatter.class.getSimpleName();

        public static String format(Dog item) {
            StringBuilder builder = new StringBuilder();
            builder.append(StringUtils.capitalize(item.getBreed().toLowerCase().trim()))
                .append(", ")
                .append(formatYears(item.getYearOfBirth()))
                .append("\n")
                .append(StringUtils.capitalize(item.getGender().toLowerCase().trim()))
                .append(", ")
                .append((item.getGender().toLowerCase().trim().equals("male") ? "Neutered: " : "Spayed: ") +
                            (item.isNeutered() ? "Yes" : "No"));
            return builder.toString();
        }

        private static String formatYears(String yearOfBirth) {
            int diff = 0;
            try {
                int year = Integer.parseInt(yearOfBirth);
                diff = Calendar.getInstance().get(Calendar.YEAR) - year;
            } catch (NumberFormatException e) {
                Log.d(TAG, "formatYears: ");
            }
            return diff == 0 ? "few months old" : diff == 1 ? diff + " year old" : diff + " years old";
        }
    }
}