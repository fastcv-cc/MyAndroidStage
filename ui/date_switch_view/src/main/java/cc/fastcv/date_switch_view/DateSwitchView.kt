package cc.fastcv.date_switch_view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout

class DateSwitchView : FrameLayout {
    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    companion object {
        internal const val WEEK_START_WITH_MONDAY = 0
        internal const val WEEK_START_WITH_SUNDAY = 1
    }

    private var callback: DateSelectCallback? = null

    private lateinit var tabSwitchViewController: TabSwitchViewController
    private lateinit var dateSelectController: DateSelectController
    private lateinit var dateShowController: DateShowController
    private lateinit var animationController: AnimationController
    internal val params = DateSwitchViewParams()

    fun setDateSelectCallback(callback: DateSelectCallback) {
        this.callback = callback
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.ui_date_switch_layout, this)
        if (attrs != null) {
            params.initWithAttributeSet(context, attrs)
        }
        tabSwitchViewController = TabSwitchViewController(this)
        dateSelectController = DateSelectController(this)
        dateShowController = DateShowController(this)
        animationController =
            AnimationController(this, tabSwitchViewController, dateSelectController)
        tabSwitchViewController.updateLayoutParams()
        dateSelectController.syncLayoutParams()
        tabSwitchViewController.syncTextParam()
        dateSelectController.setWeekText()

        switchToDayModel()
    }

    fun selectDate(dayInfo: DateInfo.DayInfoDetail) {
        dateShowController.updateDateText(dayInfo)
        //回调接口
        callback?.onDateSelect(dayInfo)
    }


    internal fun runAnim(targetIndex: Int) {
        animationController.runAnim(targetIndex)
    }

    private fun updateDateViewContent() {
        //更新边距等内容后 需要重置位置
        params.selectedIndex = 0
        tabSwitchViewController.updateLayoutParams()
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        super.setLayoutDirection(layoutDirection)
        updateDateViewContent()
        updateTextColor()
    }

    private fun updateWeekAndRvContent() {
        dateSelectController.syncLayoutParams()
        dateSelectController.setWeekText()
        dateSelectController.rebuildData()
    }

    internal fun updateTextColor() {
        tabSwitchViewController.syncTextParam()
    }

    /**
     * 格式：yyyy-MM-dd
     */
    fun setDateRange(start: String, end: String) {
        dateSelectController.setDateRange(start, end)
    }

    internal fun switchToDayModel() {
        dateSelectController.switchToDayModel()
    }

    internal fun switchToMonthModel() {
        dateSelectController.switchToMonthModel()
    }


    private fun dp2px(context: Context, dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun setTabTextSize(progress: Int) {
        params.tabTextSize = progress.toFloat()
        tabSwitchViewController.syncTextParam()
    }

    fun setCustomizeTabMargin(progress: Int) {
        params.customizeTabMargin = dp2px(context, progress)
        updateDateViewContent()
    }

    fun setCustomizeTabWidth(progress: Int) {
        params.customizeTabWidth = dp2px(context, progress)
        updateDateViewContent()
    }

    fun setCustomizeTabHeight(progress: Int) {
        params.customizeTabHeight = dp2px(context, progress)
        updateDateViewContent()
    }

    fun setWeekTextMargin(progress: Int) {
        params.weekTextMargin = dp2px(context, progress)
        updateWeekAndRvContent()
    }

    fun setTabDayText(text: String) {
        params.dayTabText = text
        tabSwitchViewController.syncTextParam()
    }

    fun setTabMonthText(text: String) {
        params.monthTabText = text
        tabSwitchViewController.syncTextParam()
    }

    fun setWeekStartWithMonday(isSunday: Boolean) {
        params.weekStart = if (isSunday) {
            WEEK_START_WITH_SUNDAY
        } else {
            WEEK_START_WITH_MONDAY
        }
        updateWeekAndRvContent()
    }

    fun setTabTextSelectedColor(color: Int) {
        params.customizeTabSelectedTextColor = color
        updateTextColor()
    }

    fun setTabTextColor(color: Int) {
        params.customizeTabTextColor = color
        updateTextColor()
    }

    fun setAnimationDuration(duration: Long) {
        params.animDuration = duration
    }
}