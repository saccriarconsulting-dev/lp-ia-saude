package com.axys.redeflexmobile.ui.component.customdistancebutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * @author Rogério Massa on 01/02/19.
 */

public class CustomDistanceButton extends RelativeLayout implements View.OnClickListener {

    private static final int TRANSLATION_DURATION = 500;

    @BindView(R.id.custom_distance_rl_container) RelativeLayout rlContainer;
    @BindView(R.id.custom_distance_selected_view) LinearLayout selectedView;
    @BindView(R.id.custom_distance_selected_tv_text) TextView tvSelectedText;
    @BindView(R.id.custom_distance_unselected_ll_container) View unselectedView;
    @BindView(R.id.custom_distance_unselected_tv_text) TextView tvUnselectedText;

    private int heightSize;
    private boolean isOpen;
    private String[] fullInfo;

    public CustomDistanceButton(Context context) {
        super(context);
        initialize();
    }

    public CustomDistanceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void setInfoText(String[] fullInfo) {
        this.fullInfo = fullInfo;
        if (StringUtils.isEmpty(fullInfo[0]) || StringUtils.isEmpty(fullInfo[1])) return;
        setUnselectedText();
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View view = inflater.inflate(R.layout.custom_distance_button, this);
        ButterKnife.bind(this, view);

        this.heightSize = (int) getContext().getResources().getDimension(R.dimen.cpt_distance_button_height);

        rlContainer.setVisibility(GONE);
        prepareInitialState();
    }

    private void setSelectedText() {
        if (StringUtils.isEmpty(fullInfo[0]) || StringUtils.isEmpty(fullInfo[1])) return;
        tvSelectedText.setText(String.format("%s - %s", fullInfo[0], fullInfo[1]));
        selectedView.setOnClickListener(this);
        unselectedView.setOnClickListener(this);
    }

    private void setUnselectedText() {
        if (StringUtils.isEmpty(fullInfo[0]) || StringUtils.isEmpty(fullInfo[1])) return;
        tvUnselectedText.setText(prepareLabels(String.format("%s - %s", fullInfo[0], fullInfo[1])));
        rlContainer.setVisibility(VISIBLE);
    }

    private void prepareInitialState() {
        unselectedView.setTranslationY(0F);
        selectedView.setTranslationY(heightSize);
    }

    private void openDisplayInfo() {
        initialUnselectedAnimation();
    }

    private void closeDisplayInfo() {
        finalSelectedAnimation();
    }

    private void initialUnselectedAnimation() {
        unselectedView.animate()
                .translationX(-((selectedView.getWidth() - unselectedView.getWidth()) / 2F))
                .setDuration(TRANSLATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        initialSelectedAnimation();
                    }
                })
                .start();
    }

    private void initialSelectedAnimation() {
        selectedView.animate()
                .translationY(0F)
                .setDuration(TRANSLATION_DURATION)
                .start();

        unselectedView.animate()
                .translationY(heightSize)
                .setDuration(TRANSLATION_DURATION)
                .start();
    }

    private void finalSelectedAnimation() {
        unselectedView.animate()
                .translationY(0F)
                .setDuration(TRANSLATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        finalUnselectedAnimation();
                    }
                })
                .start();

        selectedView.animate()
                .translationY(heightSize)
                .setDuration(TRANSLATION_DURATION)
                .start();
    }

    private void finalUnselectedAnimation() {
        unselectedView.animate()
                .translationX(0F)
                .setDuration(TRANSLATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        prepareInitialState();
                    }
                })
                .start();
    }

    private String prepareLabels(String label) {
        return label.replace("horas", "h")
                .replace("minutos", "m")
                .replace("segundos", "s");
    }

    @Override
    public void onClick(View view) {
        if (isOpen) closeDisplayInfo();
        else openDisplayInfo();
        isOpen = !isOpen;
    }
}
