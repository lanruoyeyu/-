# 冰箱收纳应用 - 任务分解

## 阶段一：项目搭建与数据层

### Task 1: 初始化 Android 项目
- 创建 Empty Compose Activity 项目
- 配置 build.gradle.kts：Kotlin、Compose BOM、Room、Coroutines、Navigation 依赖
- 配置 minSdk 26 / targetSdk 34
- 设置应用名称「冰箱收纳助手」
- **产出**：项目可编译运行，显示空白 Compose 页面

### Task 2: 定义数据模型与枚举
- 创建 `ItemCategory` 枚举（VEGETABLE_FRUIT、MEAT_SEAFOOD、DAIRY、BEVERAGE、COOKED_FOOD、OTHER），含 displayName 和 icon
- 创建 `ItemEntity` Room 实体（8 个字段）
- 创建 `CategoryCount` 数据类（category + count）
- 创建 `ItemUiState` UI 状态模型（含 remainingDays、isExpired、isWarning 计算字段）
- **产出**：数据模型类可编译通过

### Task 3: 实现数据库与 DAO
- 创建 `ItemDao` 接口（7 个查询/操作方法）
- 创建 `AppDatabase` Room 数据库类
- 创建 `FridgeRepository` 仓库层，封装 Dao，暴露 Flow 数据
- **产出**：数据库可初始化，DAO 方法可调用

---

## 阶段二：ViewModel 与业务逻辑

### Task 4: 实现 FridgeViewModel
- 创建 `FridgeViewModel`，注入 `FridgeRepository`
- 实现 `uiState: StateFlow<FridgeUiState>`，组合物品列表、总量、分类统计
- 在 UiState 中计算每个物品的 remainingDays、isExpired、isWarning
- 实现 `addItem()`、`decreaseQuantity()`、`increaseQuantity()`、`clearItem()` 方法
- **产出**：ViewModel 可提供完整 UI 状态，操作方法可调用

### Task 5: 实现日期工具类
- 创建 `DateUtils` 工具类
- `calculateRemainingDays(expirationDate: Long): Int` — 剩余天数（向下取整）
- `isExpired(remainingDays: Int): Boolean` — remainingDays <= 0
- `isWarning(remainingDays: Int): Boolean` — 0 < remainingDays <= 3
- `formatDate(timestamp: Long): String` — 格式化为 yyyy-MM-dd
- **产出**：工具类方法逻辑正确，可单元测试

---

## 阶段三：UI 组件开发

### Task 6: 配置主题与配色
- 定义 `Color.kt`：主色、告警红（#FF4444）、警告橙（#FF9800）、正常绿、背景色
- 定义 `Theme.kt`：Material Design 3 主题配置
- 定义 `Type.kt`：字体样式配置
- **产出**：主题配置完成，可应用于所有页面

### Task 7: 实现统计栏 (StatsBar)
- 创建 `StatsBar` Composable
- 第一行：物品总量显示（图标 + 数字）
- 第二行：`LazyRow` 横向滚动展示各分类数量（图标 + 名称 + 数量）
- 数量为 0 的分类不显示
- 接收参数：totalCount: Int, categoryCounts: List<CategoryCount>
- **产出**：StatsBar 可独立预览，数据驱动显示

### Task 8: 实现物品卡片 (ItemCard) — 默认状态
- 创建 `ItemCard` Composable
- 展示：分类图标 + 物品名称 + 分类标签 + 存放时间 + 过期时间 + 剩余天数 + 数量
- 剩余天数 <= 0：卡片背景红色，文字"已过期"
- 剩余天数 1~3：剩余天数文字橙色
- 剩余天数 > 3：默认色
- 日期格式化为 yyyy-MM-dd
- **产出**：ItemCard 可独立预览，三种颜色状态正确

### Task 9: 实现左滑操作区
- 在 ItemCard 中集成 `SwipeToDismissBox` 或 `anchoredDraggable`
- 左滑展开右侧操作区：`[-]  当前数量  [+]`
- `-` 点击：调用 `onDecrease(id)` 回调
- `+` 点击：调用 `onIncrease(id)` 回调
- 点击卡片空白区域收回滑动
- 滑动阈值：超过卡片宽度 25% 自动展开
- **产出**：左滑可展开操作区，+/- 按钮点击有响应

### Task 10: 实现已清空状态
- 当 `quantity == 0` 时，卡片右侧显示「已清空」按钮
- `quantity == 0` 时不显示左滑操作区（禁用滑动）
- 点击「已清空」调用 `onClear(id)` 回调
- 已清空状态下物品信息灰色显示
- **产出**：数量为 0 时正确切换状态，点击已清空按钮有响应

### Task 11: 实现添加物品对话框 (AddItemDialog)
- 创建 `AddItemDialog` Composable
- 物品名称：TextField，必填，限 20 字符
- 物品分类：DropdownMenu，6 个预设选项
- 数量：数字输入，默认 1，最小 1
- 存放时间：DatePicker，默认今天
- 过期时间：DatePicker，不可早于存放时间
- 表单验证 + 确认/取消按钮
- 确认时调用 `onAdd(item)` 回调
- **产出**：对话框可弹出，表单填写验证正常，可成功添加物品

---

## 阶段四：页面组装与集成

### Task 12: 组装 FridgeScreen
- 创建 `FridgeScreen` Composable
- 布局：Column { StatsBar + LazyColumn(物品列表) }
- 列表项使用 ItemCard，传入 ViewModel 回调
- 列表按 remainingDays 升序排列（过期优先）
- 右下角 FAB 按钮，点击弹出 AddItemDialog
- 空状态：列表为空时显示「冰箱空空如也，点击右下角添加物品」提示
- 收集 `FridgeViewModel.uiState` 驱动 UI
- **产出**：完整页面可运行，数据流畅

### Task 13: 集成 MainActivity
- 在 `MainActivity` 中设置 `FridgeScreen` 为内容
- 通过 `viewModel { FridgeViewModel(...) }` 获取 ViewModel 实例
- 配置状态栏颜色匹配主题
- **产出**：应用启动后显示完整的冰箱收纳页面

---

## 阶段五：测试与优化

### Task 14: 功能测试
- 添加物品 → 列表出现新物品
- 左滑 → 展开 +/- 操作区
- 点 "-" → 数量减 1
- 点 "+" → 数量加 1
- 数量为 0 → 显示已清空按钮
- 点已清空 → 物品从列表消失
- 过期物品 → 红色背景
- 3 天内过期 → 橙色文字
- 统计栏数据 → 随操作实时更新
- 杀掉应用重进 → 数据不丢失
- **产出**：所有功能点验证通过

### Task 15: UI 打磨
- 检查不同屏幕尺寸适配
- 确保列表滑动流畅 60fps
- 卡片圆角、阴影、间距等视觉细节调整
- 空状态页面美化
- **产出**：视觉效果达标，运行流畅
