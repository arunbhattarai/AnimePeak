package np.com.arunb.animepeak.Fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import np.com.arunb.animepeak.Activity.MainActivity;
import np.com.arunb.animepeak.Adapters.FavAdapter;
import np.com.arunb.animepeak.Functions.Fav_object;
import np.com.arunb.animepeak.R;

public class FavouriteFragment extends Fragment {
    public static RecyclerView fav_recycler;
    public static TextView no_fav;
    TextView FavTitle;
    static String Source;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fav_recycler = requireView().findViewById(R.id.fav_recycler);
        no_fav = requireView().findViewById(R.id.no_fav);
        FavTitle = requireView().findViewById(R.id.fav_title);

        SharedPreferences sharedpreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Source = sharedpreferences.getString("Source_Name", "GogoAnime");


        fav_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topVisiblePosition = ((GridLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager()))
                        .findFirstVisibleItemPosition();
                if (dy > 0) {
                    // The user has scrolled down, so shrink the title text
                    animateTextSizeChange(FavTitle, 20);
                }


                if (topVisiblePosition == 0) {
                    // The user has scrolled to the top, so expand the title text
                    animateTextSizeChange(FavTitle, 34);
                }
            }
        });


        int orientation = getResources().getConfiguration().orientation;


        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait orientation
            fav_recycler.setLayoutManager(new GridLayoutManager(requireView().getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape orientation
            fav_recycler.setLayoutManager(new GridLayoutManager(requireView().getContext(), 4));
        }
        FavAdapter favAdapter = new FavAdapter(getActivity(), MainActivity.fav_list);
        fav_recycler.setAdapter(favAdapter);
        favAdapter.notifyDataSetChanged();

    }

    private void animateTextSizeChange(final TextView textView, final int newSize) {

        ValueAnimator animator = ValueAnimator.ofFloat(textView.getTextSize(), convertDpToPixel(newSize, requireActivity()));
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue);
            }
        });
        animator.start();
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static ArrayList<Fav_object> temp_fav_list(){

        return new ArrayList<>(MainActivity.fav_list);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 4));

        } else {
            fav_recycler.setLayoutManager(new GridLayoutManager(getView().getContext(), 2));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}