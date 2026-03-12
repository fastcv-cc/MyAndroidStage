# DateSwitchView 组件文档

## 一、组件概述

DateSwitchView 是一个功能强大的 Android 自定义日期选择组件，支持按日（Day）和按月（Month）两种模式切换查看日期。该组件提供了流畅的动画效果、灵活的自定义配置以及直观的用户交互体验。

### 主要特性

- 支持日视图和月视图两种模式切换
- 提供平滑的切换动画效果
- 支持自定义日期范围
- 可配置周起始日（周一或周日）
- 高度可定制的 UI 样式
- 支持日期选择回调
- 自动识别今天日期并高亮显示
- 支持无效日期的灰化处理

---

## 二、核心架构设计

### 2.1 架构模式

DateSwitchView 采用了 MVC（Model-View-Controller）架构模式，通过多个控制器类分离职责：

```
DateSwitchView (主视图)
    ├── TabSwitchViewController (Tab切换控制器)
    ├── DateSelectController (日期选择控制器)
    ├── DateShowController (日期显示控制器)
    └── AnimationController (动画控制器)
```

### 2.2 核心类说明

#### 主要类

| 类名 | 职责 |
|------|------|
| `DateSwitchView` | 主视图类，继承自 FrameLayout，负责整体协调 |
| `DateSwitchViewParams` | 参数配置类，管理所有可配置属性 |
| `DateInfo` | 数据模型类，包含日期信息 |
| `DateUtil` | 日期工具类，提供日期计算功能 |

#### 控制器类

| 类名 | 职责 |
|------|------|
| `TabSwitchViewController` | 管理 Day/Month 切换按钮的布局和状态 |
| `DateSelectController` | 管理日期选择的 RecyclerView 和周标题 |
| `DateShowController` | 管理顶部日期文本的显示 |
| `AnimationController` | 管理切换动画的执行 |

#### 适配器和辅助类

| 类名 | 职责 |
|------|------|
| `DateSwitchAdapter` | RecyclerView 适配器，管理日期列表 |
| `DateSwitchVH` | ViewHolder，管理单个日期项的视图 |
| `DateBuildFactory` | 日期数据构建工厂，生成日/月视图数据 |
| `DateSwitchExtRecyclerView` | 自定义 RecyclerView，禁用 fling 效果 |
| `CardLinearSnapHelper` | 自定义 SnapHelper，实现卡片对齐效果 |

#### 接口

| 接口名 | 用途 |
|--------|------|
| `DateSelectCallback` | 日期选择回调接口 |
| `OnDateItemClickListener` | 日期项点击监听接口 |

---

## 三、实现原理

### 3.1 视图结构

组件的视图层次结构如下：

```
DateSwitchView (FrameLayout)
└── LinearLayout (垂直布局)
    ├── FrameLayout (Tab切换区域)
    │   ├── View (背景)
    │   ├── View (选中背景，可移动)
    │   ├── TextView (Day按钮)
    │   └── TextView (Month按钮)
    ├── TextView (日期显示文本)
    ├── LinearLayout (周标题，7个TextView)
    └── DateSwitchExtRecyclerView (日期列表)
```

### 3.2 数据模型

#### DateInfo.DayInfoDetail

```kotlin
class DayInfoDetail {
    var isSelected: Boolean    // 是否被选中
    var isInvalid: Boolean     // 是否无效（超出范围）
    var year: Int              // 年份
    var month: Int             // 月份
    var day: Int               // 日期（-1表示月视图）
    var isToday: Boolean       // 是否是今天
}
```

#### DateInfo

```kotlin
class DateInfo {
    var weekContent = Array(7) { DayInfoDetail() }  // 一周7天的数据
}
```

### 3.3 日期数据生成

#### 日视图数据生成流程

1. 根据起始日期和结束日期计算日期范围
2. 根据周起始设置（周一/周日）调整起始日期到该周的第一天
3. 计算总共需要显示的周数
4. 按周为单位生成数据，每周包含7天
5. 标记无效日期（超出范围）、今天、选中状态

```kotlin
// 关键代码片段
fun buildDayList(weekStart: Int): MutableList<DateInfo> {
    val startDateStr = getListStartDate(bindDateStr, weekStart)
    val endDateStr = getListEndDate(endDate, weekStart)
    val difDayNumber = dateUtil.getDateDifferenceDay(startDateStr, endDateStr) + 1
    val listTotal: Int = difDayNumber / 7
    
    // 生成每周数据...
}
```

#### 月视图数据生成流程

1. 计算起始日期和结束日期之间的月份差
2. 补齐数据使其能被7整除（一行显示7个月）
3. 按7个月为一组生成数据
4. 标记无效月份和当前月份

### 3.4 切换动画实现

切换动画通过 `AnimationController` 实现，包含两个同步动画：

1. **Tab 背景移动动画**：选中背景从一个按钮平滑移动到另一个按钮
2. **周标题淡入淡出动画**：日视图显示周标题，月视图隐藏周标题

```kotlin
private fun buildToggleAnimation() {
    val animator = if (targetIndex == 0) {
        ValueAnimator.ofFloat(0f, -len)  // 向左移动
    } else {
        ValueAnimator.ofFloat(0f, len)   // 向右移动
    }
    
    animator.addUpdateListener {
        // 更新背景位置
        tabSwitchViewController.updateViewSwitchBgMarginStart(...)
        // 更新周标题透明度和高度
        dateSelectController.updateWeekLayoutHeightAndAlpha(faction)
    }
}
```

### 3.5 日期选择机制

1. 用户点击日期项
2. `DateSwitchVH` 触发点击事件
3. `DateSwitchAdapter` 处理点击，更新选中状态
4. 通过 `OnDateItemClickListener` 回调到 `DateSelectController`
5. `DateSelectController` 调用 `DateSwitchView.selectDate()`
6. 更新日期显示文本
7. 触发外部 `DateSelectCallback` 回调

### 3.6 RecyclerView 滚动控制

- 使用 `CardLinearSnapHelper` 实现卡片对齐效果
- 使用 `DateSwitchExtRecyclerView` 禁用 fling 手势，防止快速滚动
- 默认滚动到最后一项（最新日期）

```kotlin
class DateSwitchExtRecyclerView : RecyclerView {
    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        return false  // 禁用快速滑动
    }
}
```

---

## 四、使用指南

### 4.1 基本使用

#### 步骤 1：在布局文件中添加组件

```xml
<cc.fastcv.date_switch_view.DateSwitchView
    android:id="@+id/dateSwitchView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:tabWidth="158dp"
    app:tabHeight="36dp"
    app:tabMargin="4dp"
    app:tabTextColor="#61003324"
    app:tabSelectedTextColor="#F9F9F9"
    app:tabTextSize="12sp"
    app:dayTabText="日"
    app:monthTabText="月"
    app:weekStrArrays="@array/week_strings"
    app:monthStrArrays="@array/month_strings"
    app:weekStartWith="monday"
    app:weekTextMargin="5dp" />
```

#### 步骤 2：定义字符串数组资源

在 `res/values/arrays.xml` 中定义：

```xml
<resources>
    <string-array name="week_strings">
        <item>周一</item>
        <item>周二</item>
        <item>周三</item>
        <item>周四</item>
        <item>周五</item>
        <item>周六</item>
        <item>周日</item>
    </string-array>
    
    <string-array name="month_strings">
        <item>一月</item>
        <item>二月</item>
        <item>三月</item>
        <item>四月</item>
        <item>五月</item>
        <item>六月</item>
        <item>七月</item>
        <item>八月</item>
        <item>九月</item>
        <item>十月</item>
        <item>十一月</item>
        <item>十二月</item>
    </string-array>
</resources>
```

#### 步骤 3：在代码中初始化

```kotlin
val dateSwitchView = findViewById<DateSwitchView>(R.id.dateSwitchView)

// 设置日期范围（必须）
dateSwitchView.setDateRange("2023-01-01", "2024-12-31")

// 设置日期选择回调
dateSwitchView.setDateSelectCallback(object : DateSelectCallback {
    override fun onDateSelect(info: DateInfo.DayInfoDetail) {
        Log.d("DateSwitch", "选中日期: ${info.getDateYmd()}")
        // 处理日期选择事件
    }
})
```

### 4.2 XML 属性配置

| 属性名 | 类型 | 说明 | 默认值 |
|--------|------|------|--------|
| `tabWidth` | dimension | Tab 按钮宽度 | 158dp |
| `tabHeight` | dimension | Tab 按钮高度 | 36dp |
| `tabMargin` | dimension | Tab 按钮外边距 | 4dp |
| `tabTextColor` | color | Tab 未选中文字颜色 | #61003324 |
| `tabSelectedTextColor` | color | Tab 选中文字颜色 | #F9F9F9 |
| `tabTextSize` | dimension | Tab 文字大小 | 12sp |
| `dayTabText` | string | 日视图 Tab 文本 | - |
| `monthTabText` | string | 月视图 Tab 文本 | - |
| `weekStrArrays` | reference | 周标题字符串数组（必须7个） | - |
| `monthStrArrays` | reference | 月份字符串数组（必须12个） | - |
| `weekStartWith` | enum | 周起始日（monday/sunday） | monday |
| `weekTextMargin` | dimension | 周标题右边距 | 5dp |

### 4.3 代码配置方法

```kotlin
// 设置日期范围（格式：yyyy-MM-dd）
dateSwitchView.setDateRange("2023-01-01", "2024-12-31")

// 设置 Tab 文本
dateSwitchView.setTabDayText("日")
dateSwitchView.setTabMonthText("月")

// 设置 Tab 文字大小
dateSwitchView.setTabTextSize(14)

// 设置 Tab 颜色
dateSwitchView.setTabTextColor(Color.parseColor("#61003324"))
dateSwitchView.setTabTextSelectedColor(Color.parseColor("#F9F9F9"))

// 设置 Tab 尺寸和边距
dateSwitchView.setCustomizeTabWidth(160)
dateSwitchView.setCustomizeTabHeight(40)
dateSwitchView.setCustomizeTabMargin(5)

// 设置周标题边距
dateSwitchView.setWeekTextMargin(10)

// 设置周起始日（true=周日开始，false=周一开始）
dateSwitchView.setWeekStartWithMonday(false)

// 设置动画时长（毫秒）
dateSwitchView.setAnimationDuration(300)
```

### 4.4 完整示例

```kotlin
class MainActivity : AppCompatActivity() {
    
    private lateinit var dateSwitchView: DateSwitchView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        dateSwitchView = findViewById(R.id.dateSwitchView)
        
        // 设置日期范围：从2023年1月1日到今天
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
        dateSwitchView.setDateRange("2023-01-01", today)
        
        // 设置日期选择回调
        dateSwitchView.setDateSelectCallback(object : DateSelectCallback {
            override fun onDateSelect(info: DateInfo.DayInfoDetail) {
                val dateStr = info.getDateYmd()
                Toast.makeText(
                    this@MainActivity,
                    "选中日期: $dateStr",
                    Toast.LENGTH_SHORT
                ).show()
                
                // 根据选中的日期加载数据
                loadDataForDate(dateStr)
            }
        })
        
        // 自定义样式
        dateSwitchView.setTabTextSize(14)
        dateSwitchView.setAnimationDuration(250)
    }
    
    private fun loadDataForDate(date: String) {
        // 加载指定日期的数据
    }
}
```

---

## 五、自定义样式

### 5.1 自定义背景样式

组件使用了以下 drawable 资源，可以根据需要自定义：

#### ui_shape_date_tab_normal_bg.xml
Tab 容器的背景样式

#### ui_shape_date_tab_selected_bg.xml
Tab 选中状态的背景样式

#### ui_shape_date_switch_selected_bg.xml
日期选中状态的背景样式

#### ui_shape_date_switch_today_bg.xml
今天日期的背景样式

### 5.2 自定义颜色

可以通过代码或 XML 属性自定义以下颜色：

- Tab 文字颜色（选中/未选中）
- 日期文字颜色（普通/选中/今天/无效）
- 背景颜色

### 5.3 自定义尺寸

可以调整以下尺寸参数：

- Tab 按钮的宽度和高度
- Tab 按钮的边距
- 周标题的边距
- 日期项的大小（通过修改布局文件）

---

## 六、注意事项

### 6.1 必须配置项

1. **weekStrArrays**：必须提供包含7个元素的字符串数组
2. **monthStrArrays**：必须提供包含12个元素的字符串数组
3. **setDateRange()**：必须在使用前调用设置日期范围

### 6.2 日期格式

所有日期字符串必须使用 `yyyy-MM-dd` 格式，例如：`2023-01-01`

### 6.3 性能优化

- 组件内部使用 RecyclerView 实现列表，支持大量日期数据
- 禁用了 fling 手势，避免快速滚动导致的性能问题
- 使用 SnapHelper 实现精确对齐

### 6.4 线程安全

- 所有方法都应在主线程调用
- 日期选择回调在主线程执行

---

## 七、技术亮点

### 7.1 控制器分离

通过将不同职责分离到不同的控制器类，实现了清晰的代码结构和良好的可维护性。

### 7.2 动画同步

切换动画同时控制多个视图元素（背景位置、周标题透明度和高度），实现流畅的视觉效果。

### 7.3 日期计算

`DateUtil` 和 `DateBuildFactory` 提供了完善的日期计算功能，支持：
- 日期偏移计算
- 周起始日调整
- 日期范围验证
- 今天日期识别

### 7.4 状态管理

通过 `DateInfo.DayInfoDetail` 管理每个日期的多种状态：
- 选中状态
- 今天标记
- 无效状态
- 日期信息

### 7.5 自定义 RecyclerView

通过继承 RecyclerView 并重写 `fling()` 方法，精确控制滚动行为。

---

## 八、扩展建议

### 8.1 可能的扩展方向

1. **多选模式**：支持选择日期范围
2. **自定义日期标记**：支持在特定日期显示标记点
3. **手势支持**：支持左右滑动切换周/月
4. **国际化**：支持多语言和不同日历系统
5. **主题支持**：支持深色模式
6. **动画自定义**：提供更多动画效果选项

### 8.2 集成建议

- 可以与 ViewModel 和 LiveData 结合使用
- 可以集成到 Fragment 中实现模块化
- 可以与数据绑定库配合使用

---

## 九、依赖说明

### 9.1 核心依赖

```gradle
dependencies {
    implementation 'androidx.core:core-ktx'
    implementation 'androidx.appcompat:appcompat'
    implementation 'com.google.android.material:material'
}
```

### 9.2 最低版本要求

- minSdk: 24 (Android 7.0)
- compileSdk: 36
- Kotlin: 支持
- Java: 11

---

## 十、总结

DateSwitchView 是一个设计精良、功能完善的日期选择组件。它通过清晰的架构设计、流畅的动画效果和灵活的配置选项，为 Android 应用提供了优秀的日期选择体验。组件的代码结构清晰，易于理解和扩展，适合在各种需要日期选择功能的场景中使用。
