package com.simicart.core.common;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationSet;

/**
 * Created by frank on 9/17/16.
 */
public class AnimationUtil {

    public static void animate(RecyclerView.ViewHolder holder, boolean isGoDown){
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",isGoDown==true?200:-200,0);
        animatorTranslateY.setDuration(1000);

        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();

    }

}
