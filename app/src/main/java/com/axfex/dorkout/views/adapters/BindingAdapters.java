package com.axfex.dorkout.views.adapters;

import android.animation.Animator;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.axfex.dorkout.R;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Rest;
import com.axfex.dorkout.data.Status;
import com.axfex.dorkout.util.FormatUtils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import static com.axfex.dorkout.data.Status.DONE;
import static com.axfex.dorkout.data.Status.PAUSED;
import static com.axfex.dorkout.data.Status.SKIPPED;
import static com.axfex.dorkout.data.Status.UNDONE;
import static com.axfex.dorkout.data.Status.RUNNING;

public class BindingAdapters {
    public static final String TAG = "BINDING_ADAPTERS";

    @BindingAdapter({"lock_view", "active_exercise"})
    public static void setActiveExercise(RecyclerView recyclerView, View lockView, Exercise exercise) {
        if (exercise != null) {
            Transition transition = new ChangeBounds();
            transition.setInterpolator(new LinearInterpolator());
            transition.setDuration(500L);
            transition.addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(@NonNull Transition transition) {
                    if (lockView != null) lockView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    if (lockView != null) lockView.setVisibility(View.GONE);
                    recyclerView.smoothScrollToPosition(exercise.getOrderNumber() - 1);
                }
            });
            TransitionManager.beginDelayedTransition(recyclerView, transition);
        }

    }


    @BindingAdapter({"active"})
    public static void setActive(View view, Boolean isActive) {
        Transition transition = new Fade();
        transition.setDuration(750L);
        TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
        if (isActive == null) {
            view.setVisibility(View.GONE);
            view.setEnabled(true);
        } else {
            view.setVisibility(isActive ? View.VISIBLE : View.GONE);
            view.setEnabled(!isActive);
        }


    }


//    @BindingAdapter({"exercise1", "binding1", "binding2"})
//    public static void setExercise1(ViewSwitcher view, Exercise exercise, ActionWorkoutPanelBinding binding1, ActionWorkoutPanelBinding binding2) {
//        if (view.getNextView().equals(binding1.getRoot())) {
//            binding1.setExercise(exercise);
//        } else if (view.getNextView().equals(binding2.getRoot())) {
//            binding2.setExercise(exercise);
////            view.getHeight()=((ViewGroup) view).get
//        }
//        view.showNext();
//    }

//    @BindingAdapter("exercise")
//    public static void setExercise(CardView view, Exercise exercise) {
//
//        view.animate()
//                .setDuration(650)
//                .alpha(0.0f)
//                .setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                        if (exercise!=null)
//                        ((TextView) view.findViewById(R.id.reps)).setText(FormatUtils.getTimeString(exercise.getTimePlan()));
//                        view.animate().setDuration(650).alpha(1.0f);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//
//        Log.i(TAG, "setExercise: ");
//
//    }

    @BindingAdapter({"status", "rest", "canStart"})
    public static void setStatusIcon(ImageView view, Status status, Rest rest, boolean canStart) {
        final int res;
        if (status == UNDONE && rest != null) {
            res = R.drawable.action_rest_await_icon;
        } else if (status == UNDONE && canStart || status == PAUSED) {
            res = R.drawable.action_start_icon;
        } else if (status == UNDONE || status == RUNNING) {
            res = R.drawable.action_finish_icon;
        } else if (status == DONE) {
            res = R.drawable.action_done_icon;
        } else if (status == SKIPPED) {
            res = R.drawable.action_skipped_icon;
        } else {
            res = R.drawable.action_empty_icon;
        }
        view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), res));
    }

    @BindingAdapter({"status", "rest", "canStart"})
    public static void setStatusColor(CardView view, Status status, Rest rest, boolean canStart) {
        final int res;
        if (status == UNDONE && rest != null) {
            res = R.color.state_rest_await_back;
        } else if (status == UNDONE && canStart || status == PAUSED) {
            res = R.color.state_start_back;
        } else if (status == UNDONE || status == RUNNING) {
            res = R.color.state_finish_back;
        } else if (status == DONE) {
            res = R.color.state_done_back;
        } else if (status == SKIPPED) {
            res = R.color.state_skipped_back;
        } else {
            res = R.color.state_empty_back;
        }
        view.setCardBackgroundColor(ContextCompat.getColor(view.getContext(), res));
    }

    @BindingAdapter({"time", "timePlan"})
    public static void setTime(TextView view, Long time, Long timePlan) {
        final int color;
        final String text;
        if (time == null) {
            color = R.color.result_digit_plan;
            text = FormatUtils.getTimeString(timePlan);
        } else {
            color = R.color.result_digit;
            text = FormatUtils.getTimeString(time);
        }
        view.setText(text);
        view.setTextColor(ContextCompat.getColor(view.getContext(), color));
        Log.i(TAG, "setTime: " + text);
//
//                view.animate().alpha(1f).setDuration(50);
//        view.clearAnimation();
//        view.setAlpha(1f);
//        view.animate().alpha(0.2f).setDuration(50).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setText(text);
//                view.setTextColor(ContextCompat.getColor(view.getContext(), color));
//
//                view.animate().alpha(1f).setDuration(50);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
    }


    @BindingAdapter("statusText")
    public static void setEnumStatusText(TextView view, Status status) {
        final int res;
        if (status == null) {
            res = R.string.bt_empty;
            view.setText(res);
        } else {
            view.setText(status.toString());
        }

//            switch (status) {
//                case UNDONE:
//                    res = R.string.value_one;
//                    break;
//                case NEXT:
//                    res = R.string.value_two;
//                    break;
//                case RUNNING:
//                    res = R.string.value_three;
//                    break;
//                case DONE:
//                    res = R.string.value_two;
//                    break;
//                case SKIPPED:
//                    res = R.string.value_three;
//                    break;
//                default:
//                    res = R.string.bt_empty;
//                    break;
//            }
//        }

    }


}
