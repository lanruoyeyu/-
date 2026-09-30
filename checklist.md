# 冰箱收纳应用 - 开发检查清单

## 一、项目初始化

- [ ] 创建 Android 项目（Empty Compose Activity 模板）
- [ ] 配置 build.gradle.kts（Kotlin、Compose、Room 依赖）
- [ ] 配置 minSdk = 26, targetSdk = 34
- [ ] 设置应用名称为「冰箱收纳助手」
- [ ] 配置 Material Design 3 主题
- [ ] 定义应用配色方案（主色、告警红色、警告橙色、正常绿色）

## 二、数据层

- [ ] 定义 `ItemCategory` 枚举（6 个预设分类 + 图标 + 显示名称）
- [ ] 创建 `ItemEntity` Room 实体（id、name、category、quantity、storageDate、expirationDate、isCleared、createdAt、updatedAt）
- [ ] 创建 `CategoryCount` 数据类（分类统计结果）
- [ ] 创建 `ItemDao` 接口（observeActiveItems、observeCategoryCounts、observeTotalCount、insert、update、updateQuantity、markCleared）
- [ ] 创建 `AppDatabase` Room 数据库类
- [ ] 创建 `FridgeRepository` 仓库层，封装 Dao 调用
- [ ] 添加数据库迁移策略（fallbackToDestructiveMigration）

## 三、UI 组件

### 3.1 统计栏 (StatsBar)

- [ ] 第一行：显示冰箱物品总量（动态数据）
- [ ] 第二行：横向滚动显示各分类物品数量
- [ ] 数量为 0 的分类不显示
- [ ] 每个分类带图标 + 名称 + 数量

### 3.2 物品卡片 (ItemCard)

- [ ] 默认状态：展示物品名称、分类标签、存放时间、过期时间、剩余天数、数量
- [ ] 红色告警：`remainingDays <= 0` 时卡片背景变红，文字显示"已过期"
- [ ] 橙色警告：`remainingDays <= 3` 时剩余天数文字变橙
- [ ] 正常状态：剩余天数文字为默认色
- [ ] 日期格式化：`yyyy-MM-dd` 格式展示

### 3.3 左滑操作区

- [ ] 实现 `SwipeToDismissBox` / `anchoredDraggable` 左滑手势
- [ ] 展开后显示：`[-]  当前数量  [+]`
- [ ] `-` 按钮：quantity 减 1，最小为 0
- [ ] `+` 按钮：quantity 加 1
- [ ] 点击卡片空白区域收回滑动

### 3.4 已清空状态

- [ ] `quantity == 0` 时卡片右侧显示「已清空」按钮
- [ ] `quantity == 0` 时不显示左滑操作区
- [ ] 点击「已清空」→ `isCleared = true` → 物品从列表消失
- [ ] 物品信息灰色显示

### 3.5 添加物品对话框 (AddItemDialog)

- [ ] 物品名称输入框（必填，限制 20 字符）
- [ ] 物品分类下拉选择（6 个预设分类）
- [ ] 数量输入（默认 1，最小 1）
- [ ] 存放时间日期选择器（默认今天）
- [ ] 过期时间日期选择器（不可早于存放时间）
- [ ] 表单验证（名称非空、过期时间合法）
- [ ] 确认/取消按钮

## 四、ViewModel 层

- [ ] 创建 `FridgeViewModel`
- [ ] 暴露 `uiState: StateFlow<FridgeUiState>`（物品列表 + 总量 + 分类统计）
- [ ] `addItem()` 方法：添加新物品
- [ ] `decreaseQuantity(id)` 方法：消耗物品（减 1，最小 0）
- [ ] `increaseQuantity(id)` 方法：增加库存（加 1）
- [ ] `clearItem(id)` 方法：标记已清空
- [ ] 计算 `remainingDays` 并在 UiState 中体现

## 五、页面组装

- [ ] `FridgeScreen` 组合 StatsBar + LazyColumn(物品列表) + FAB
- [ ] 列表按剩余天数升序排列（过期优先）
- [ ] FAB 点击弹出 AddItemDialog
- [ ] 空状态：列表为空时显示提示文案

## 六、日期计算逻辑

- [ ] `calculateRemainingDays(expirationDate)`：返回剩余天数（过期日期 - 当前日期，向下取整）
- [ ] `isExpired(remainingDays)`：remainingDays <= 0 返回 true
- [ ] `isWarning(remainingDays)`：0 < remainingDays <= 3 返回 true
- [ ] `formatDate(timestamp)`：格式化为 `yyyy-MM-dd` 字符串

## 七、测试验证

- [ ] 添加物品：新增物品后出现在列表中
- [ ] 数量增减：左滑操作 +/- 按钮正常工作
- [ ] 数量为 0：自动切换为已清空状态，显示按钮
- [ ] 已清空：点击后物品从列表消失
- [ ] 红色告警：过期物品卡片显示红色
- [ ] 橙色警告：3 天内过期显示橙色
- [ ] 统计数据：总量和分类数量实时更新
- [ ] 数据持久化：重启应用数据不丢失
- [ ] 列表排序：过期/即将过期排在前面
