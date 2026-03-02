package cc.fastcv.date_switch_view

import android.animation.Animator
import android.animation.ValueAnimator
import kotlin.math.max
import kotlin.math.min

internal class AnimationController(private val dateSwitchView: DateSwitchView, private val tabSwitchViewController: TabSwitchViewController, private val dateSelectController: DateSelectController) {

    private var toggleAnimation: ValueAnimator? = null

    internal fun runAnim(targetIndex: Int) {
        if ((toggleAnimation != null && toggleAnimation!!.isRunning) || dateSwitchView.params.selectedIndex == targetIndex) {
            return
        }
        dateSwitchView.params.selectedIndex = targetIndex
        buildToggleAnimation()
        toggleAnimation!!.start()
    }

    private fun buildToggleAnimation() {
        val len = tabSwitchViewController.getTabButtonMargin()

        val targetIndex = dateSwitchView.params.selectedIndex
        val initMarginStart = tabSwitchViewController.getViewSwitchBgMarginStart()
        dateSelectController.initWeekLayoutHeightIfNeed()

        val animator = if (targetIndex == 0) {
            ValueAnimator.ofFloat(0f, -len)
        } else {
            ValueAnimator.ofFloat(0f, len)
        }
        val totalDuration = dateSwitchView.params.animDuration
        animator.duration = totalDuration
        animator.addUpdateListener {
            tabSwitchViewController.updateViewSwitchBgMarginStart(initMarginStart + (it.animatedValue as Float).toInt())

            val faction = if (targetIndex == 0) {
                min(it.currentPlayTime, totalDuration)
            } else {
                max(totalDuration - it.currentPlayTime, 0)
            } * 1f / totalDuration

            dateSelectController.updateWeekLayoutHeightAndAlpha(faction)
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                dateSwitchView.updateTextColor()
                if (targetIndex == 0) {
                    dateSwitchView.switchToDayModel()
                } else {
                    dateSwitchView.switchToMonthModel()
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        toggleAnimation = animator
    }

}