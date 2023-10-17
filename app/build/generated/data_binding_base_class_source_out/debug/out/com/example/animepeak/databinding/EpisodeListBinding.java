// Generated by view binder compiler. Do not edit!
package com.example.animepeak.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.animepeak.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class EpisodeListBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView episodeName;

  private EpisodeListBinding(@NonNull CardView rootView, @NonNull TextView episodeName) {
    this.rootView = rootView;
    this.episodeName = episodeName;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static EpisodeListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static EpisodeListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.episode_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static EpisodeListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.episode_name;
      TextView episodeName = ViewBindings.findChildViewById(rootView, id);
      if (episodeName == null) {
        break missingId;
      }

      return new EpisodeListBinding((CardView) rootView, episodeName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}