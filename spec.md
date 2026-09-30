# 收纳物品管理 Android 应用 - 规格说明

## 1. 项目概述

一款物品管理 Android 原生应用，通过底部导航栏切换四个模块：

1. **冰箱收纳** — 追踪冰箱内物品的存放、保质期和库存，减少食物浪费
2. **数码产品** — 记录买入价格与每日成本，支持卖出
3. **宠物用品** — 按种类管理宠物用品，支持重量/个数两种计量
4. **物品统计** — 汇总所有物品的类别与均摊每日价格，提供房间地图标注存放位置

- **应用名称**：收纳助手
- **技术栈**：Kotlin + Jetpack Compose + Room + MVVM
- **最低 SDK**：Android 8.0 (API 26)
- **目标 SDK**：Android 14 (API 34)
- **UI 框架**：Material Design 3

## 2. 技术架构

```
┌─────────────────────────────────────────────────────┐
│                  UI Layer (Compose)                   │
│  FridgeScreen / DigitalScreen / PetScreen / StatsScreen│
├─────────────────────────────────────────────────────┤
│                ViewModel Layer (MVVM)                  │
│  FridgeVM / DigitalVM / PetVM / StatsVM              │
├─────────────────────────────────────────────────────┤
│                  Repository Layer                      │
│  FridgeRepo / DigitalRepo / PetRepo / StatsRepo      │
├─────────────────────────────────────────────────────┤
│                  Data Layer (Room)                     │
│  ItemDao / DigitalItemDao / PetItemDao / MapDao      │
│  AppDatabase (v4)                                     │
└─────────────────────────────────────────────────────┘
```

### 2.1 核心依赖

- `androidx.compose` — UI 框架（含 material-icons-extended）
- `androidx.room` — 本地数据库
- `androidx.lifecycle:viewmodel-compose` — ViewModel 集成
- `kotlinx.coroutines` — 异步处理

## 3. 数据模型

### 3.1 冰箱物品分类（预设）

| 分类标识 | 分类名称 | 图标 |
|---------|---------|------|
| VEGETABLE_FRUIT | 蔬菜水果 | 🥬 |
| MEAT_SEAFOOD | 肉类海鲜 | 🥩 |
| DAIRY | 乳制品 | 🥛 |
| BEVERAGE | 饮料 | 🥤 |
| COOKED_FOOD | 熟食剩菜 | 🍱 |
| OTHER | 其他 | 📦 |

### 3.2 冰箱物品实体 (ItemEntity)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long (PK, 自增) | 物品唯一标识 |
| name | String | 物品名称 |
| category | String | 物品大类（枚举值） |
| quantity | Int | 数量 |
| storageDate | Long (时间戳) | 存放时间 |
| expirationDate | Long (时间戳) | 过期时间 |
| isCleared | Boolean (默认 false) | 是否已清空（隐藏不显示） |
| createdAt | Long | 创建时间 |
| updatedAt | Long | 更新时间 |

计算字段（非持久化）：

| 字段 | 计算方式 | 说明 |
|------|---------|------|
| remainingDays | `(expirationDate - 当前时间) / 一天毫秒数`，向下取整 | 保质期剩余天数 |
| isExpired | `remainingDays <= 0` | 是否过期（用于红色告警） |

> **说明**：用户原始描述为"保质期剩余时间 = 物品过期时间减去存放时间"。实际业务中，"剩余"天数应为过期日期减去当前日期，这样才能反映实时的剩余情况。spec 采用此实用解释。

### 3.3 数码产品实体 (DigitalItemEntity)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long (PK, 自增) | 唯一标识 |
| name | String | 物品名称 |
| purchasePrice | Double | 买入价格（元） |
| purchaseDate | Long | 买入时间 |
| sellPrice | Double? (可空) | 卖出价格 |
| sellDate | Long? (可空) | 卖出时间 |
| createdAt / updatedAt | Long | 创建/更新时间 |

计算字段（非持久化）：

| 字段 | 计算方式 | 说明 |
|------|---------|------|
| isSold | `sellPrice != null && sellDate != null` | 是否已卖出 |
| daysHeld | 未卖出：`(当日 - purchaseDate)`；已卖出：`(sellDate - purchaseDate)`，均向下取整且最少按 1 天 | 持有天数 |
| dailyCost | `purchasePrice / daysHeld` | 每日成本价 |
| profit | `sellPrice - purchasePrice`（仅已卖出） | 损益 |

> **每日成本价规则**：未卖出时 = 买入价格 /（当日时间 - 买入时间，按天计算，最少 1 天），随时间增长；卖出后按卖出日冻结，不再计算。

### 3.4 宠物用品分类实体 (PetCategoryEntity)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long (PK, 自增) | 唯一标识 |
| name | String | 种类名称（预置：猫粮、猫砂、猫玩具） |
| isDefault | Boolean | 是否为预置种类 |
| createdAt | Long | 创建时间 |

### 3.5 宠物用品实体 (PetItemEntity)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long (PK, 自增) | 唯一标识 |
| name | String | 物品名称 |
| categoryId | Long | 关联 PetCategoryEntity.id |
| purchasePrice | Double | 买入价格（元） |
| purchaseDate | Long | 买入时间 |
| quantityType | String | `WEIGHT`（重量）/ `COUNT`（个数） |
| weightUnit | String? | `G` / `KG`，仅重量类型有效，默认 G |
| quantity | Double | 数量值 |
| tags | String | 标签，英文逗号分隔（如品牌） |
| createdAt / updatedAt | Long | 创建/更新时间 |

### 3.6 地图标记实体 (MapMarkerEntity)

用于第四页房间地图，记录物品在地图上的存放位置。

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long (PK, 自增) | 唯一标识 |
| itemType | String | 物品来源模块：`FRIDGE` / `DIGITAL` / `PET` |
| itemId | Long | 对应模块物品的 id |
| itemDisplayName | String | 物品显示名称（冗余存储，便于展示） |
| posX | Float | 地图上的 X 坐标（0.0~1.0 归一化） |
| posY | Float | 地图上的 Y 坐标（0.0~1.0 归一化） |
| roomLabel | String? | 房间/区域标签（如"客厅"、"冰箱"） |
| createdAt | Long | 创建时间 |

> 坐标使用归一化值（0.0~1.0），适配不同屏幕尺寸的地图缩放。

## 4. 页面设计

### 4.1 冰箱收纳页面 (FridgeScreen)

页面从上到下分为三个区域：

#### 区域一：统计栏（顶部）

两行数据展示：

**第一行**：冰箱物品总量
- 显示当前冰箱中所有未清空物品的数量总和
- 格式：`📦 冰箱物品总量：XX 件`

**第二行**：各大类物品数量
- 横向滚动展示每个大类的物品数量
- 格式：`🥬 蔬菜水果 5  |  🥩 肉类海鲜 3  |  🥛 乳制品 2  | ...`
- 只显示数量 > 0 的分类

#### 区域二：物品列表（主体）

- 以列表形式展示所有未清空（`isCleared = false`）的物品
- 每个物品为一个可滑动的卡片（ItemCard）
- 列表按保质期剩余天数升序排列（即将过期的排前面）

#### 区域三：浮动添加按钮 (FAB)

- 右下角圆形按钮，点击弹出添加物品对话框

### 4.2 物品卡片 (ItemCard)

#### 默认状态展示

```
┌──────────────────────────────────────────┐
│  [图标] 物品名称              数量: 5    │
│        分类标签                           │
│        存放时间：2026-09-01               │
│        过期时间：2026-09-10               │
│        保质期剩余：9天                    │
└──────────────────────────────────────────┘
```

- 当 `remainingDays <= 0` 时，整个卡片背景强制显示为**红色告警**，剩余天数文字显示"已过期"
- 当 `remainingDays <= 3` 时，剩余天数文字显示为**橙色警告**

#### 左滑展开状态

用户向左滑动物品卡片，右侧露出操作区域：

```
┌──────────────────────────────┬─────────┐
│  物品信息（同上）             │ 消耗/加仓│
│                              │   [- 5 +]│
└──────────────────────────────┴─────────┘
```

- 显示当前数量，两侧各有 `-` 和 `+` 按钮
- 点击 `-`：数量减 1（消耗物品），最小为 0
- 点击 `+`：数量加 1（增加库存）
- 数量实时更新，滑动收回后保持

#### 数量为 0 状态

当物品 `quantity == 0` 时：

```
┌──────────────────────────────────────────┐
│  [图标] 物品名称              [已清空]   │
│        ...（其他信息灰色显示）            │
└──────────────────────────────────────────┘
```

- 卡片右侧出现「已清空」按钮
- 点击「已清空」按钮 → 将物品 `isCleared` 设为 `true` → 物品从列表中消失
- 数量为 0 时不再显示左滑操作区域

### 4.3 添加物品对话框 (AddItemDialog)

| 输入项 | 类型 | 是否必填 | 说明 |
|--------|------|---------|------|
| 物品名称 | 文本 | 是 | 限制 20 字符 |
| 物品分类 | 下拉选择 | 是 | 6 个预设分类 |
| 数量 | 数字 | 是 | 默认 1，最小 1 |
| 存放时间 | 日期选择器 | 是 | 默认今天 |
| 过期时间 | 日期选择器 | 是 | 不可早于存放时间 |

### 4.4 数码产品页面 (DigitalScreen)

- 物品列表展示所有数码产品，未卖出在前，同状态按买入时间倒序
- **未卖出卡片**：名称、买入价格、买入时间、已持有天数、每日成本价（橙色），右侧「卖出」按钮
- **已卖出卡片**：买入/卖出时间、卖出价、损益（盈利绿/亏损红）、最终成本价（按卖出日冻结）
- 右下角 FAB 弹出添加对话框

### 4.5 宠物用品页面 (PetScreen)

#### 分类筛选条

页面顶部横向滚动的 FilterChip 列表：「全部」+ 所有种类（猫粮、猫砂、猫玩具、用户新增的...），点击筛选对应种类。

#### 物品列表

按买入时间倒序展示，每个卡片包含：
- 物品名称 + 种类徽标
- 买入时间
- 数量（重量类型显示 `500g` / `1.5kg`，个数类型显示 `3个`）
- 买入价格
- 标签（品牌等，橙色小标签，FlowRow 排列）

#### 添加对话框 (AddPetItemDialog)

| 输入项 | 类型 | 是否必填 | 说明 |
|--------|------|---------|------|
| 物品名称 | 文本 | 是 | 限制 20 字符 |
| 种类 | 下拉选择 + 新种类按钮 | 是 | 预置猫粮/猫砂/猫玩具，可新增 |
| 买入价格 | 数字 | 是 | >0，最多 2 位小数 |
| 买入时间 | 日期选择器 | 是 | 默认今天 |
| 数量类型 | FilterChip | 是 | 重量 / 个数，默认重量 |
| 重量单位 | FilterChip | 仅重量类型 | g / kg，默认 g |
| 数量值 | 数字 | 是 | >0，最多 2 位小数 |
| 标签 | 文本 | 否 | 逗号分隔，如"皇家,猫粮" |

「新种类」按钮弹出小对话框，输入种类名称（去重），确认后自动选中。

### 4.6 物品统计页面 (StatsScreen)

第四页分为上下两个区域。

#### 区域一：统计汇总卡片

汇总三大模块（冰箱、数码、宠物）的物品数据，按模块分组展示：

```
┌──────────────────────────────────────────┐
│  📦 冰箱物品                              │
│  物品总数：12 件    总花费：¥186.50       │
│  均摊每日价格：¥6.22/天                   │
├──────────────────────────────────────────┤
│  📱 数码产品                              │
│  持有中：3 件       总投入：¥12,800.00    │
│  均摊每日价格：¥45.20/天                  │
├──────────────────────────────────────────┤
│  🐾 宠物用品                              │
│  物品总数：5 件     总花费：¥580.00       │
│  均摊每日价格：¥3.87/天                   │
├──────────────────────────────────────────┤
│  📊 总计                                  │
│  均摊每日价格：¥55.29/天                  │
└──────────────────────────────────────────┘
```

**均摊每日价格计算规则**：

| 模块 | 计算方式 |
|------|---------|
| 冰箱物品 | `SUM(物品买入估算价格) / 平均持有天数`；若无价格字段则显示「—」 |
| 数码产品 | `SUM(未卖出物品的 dailyCost)`，即每个未卖出物品的 `买入价格 / 持有天数` 之和 |
| 宠物用品 | `SUM(买入价格) / 平均持有天数`，平均持有天数 = `SUM(各物品持有天数) / 物品数` |
| 总计 | 三个模块均摊每日价格之和 |

> 冰箱物品当前无买入价格字段，统计卡片中价格相关项显示「—」。

#### 区域二：房间地图

一个可交互的房间平面图区域，用户可以在地图上标注物品存放位置。

**地图交互**：

- 地图以一个矩形 Canvas 呈现，背景为浅色房间示意图
- 地图上已标注的物品显示为小圆点（带图标），标注位置由 `posX`/`posY`（归一化坐标）决定
- **点击圆点** → 弹出 BottomSheet/Dialog 显示物品简略信息：
  - 物品名称
  - 来源模块（冰箱/数码/宠物）
  - 剩余数量或当前状态
  - 「移除标注」按钮
- **长按地图空白处** → 弹出物品选择器，从三大模块的物品列表中选择一个放置在此位置
- 坐标使用归一化值（0.0~1.0），适配不同屏幕尺寸

**物品选择器**：

弹出一个 Dialog，分 Tab 展示三个模块的物品列表（冰箱/数码/宠物），选中后在该坐标创建 MapMarker。

**简略信息弹窗内容**：

| 模块 | 显示内容 |
|------|---------|
| 冰箱 | 物品名称、数量、保质期剩余天数 |
| 数码 | 物品名称、持有天数、每日成本价、是否已卖出 |
| 宠物 | 物品名称、种类、数量（含单位）、买入价格 |

## 5. 交互逻辑

### 5.1 左滑手势（冰箱页面）

- 使用 `SwipeToDismiss` / `anchoredDraggable` 实现左滑展开
- 滑动阈值：滑动超过卡片宽度 25% 时自动展开操作区
- 点击卡片空白区域收回滑动

### 5.2 数量增减（冰箱页面）

```
点击 "-" → quantity = max(0, quantity - 1)
点击 "+" → quantity = quantity + 1
```

- 每次操作更新 `updatedAt` 时间戳
- 数量变为 0 时，卡片状态切换为"已清空"模式

### 5.3 已清空操作（冰箱页面）

```
点击"已清空" → isCleared = true → 列表自动刷新，该物品消失
```

- 已清空的物品数据保留在数据库中，仅通过 `isCleared` 字段过滤

### 5.4 列表排序

**冰箱页面**：
1. 过期物品（remainingDays <= 0）排最前
2. 按剩余天数升序排列
3. 同一剩余天数的按存放时间升序排列

**数码页面**：未卖出在前，同状态按买入时间倒序

**宠物页面**：按买入时间倒序

### 5.5 数码产品交互

**添加**：点击 FAB → 对话框填写名称、买入价格、买入时间 → 确认后入库。

**卖出**：点击未卖出卡片上的「卖出」→ 对话框填写卖出价格与卖出时间（不可早于买入时间）→ 确认后冻结每日成本价。

**每日成本价刷新**：UiState 每小时自动重算一次；已卖出物品不受影响。

### 5.6 宠物用品交互

**添加种类**：添加对话框中点击「新种类」→ 输入名称（去重）→ 确认后自动选中。

**添加物品**：点击 FAB → 填写名称/种类/价格/日期/数量类型/数量/标签 → 确认入库。

**筛选**：顶部 FilterChip 切换「全部」或特定种类。

### 5.7 房间地图交互

**添加标注**：长按地图空白处 → 弹出物品选择器 → 选择模块 Tab → 选择物品 → 在长按位置创建标注。

**查看详情**：点击标注圆点 → 弹出简略信息弹窗。

**移除标注**：在简略信息弹窗中点击「移除标注」→ 删除 MapMarker 记录。

## 6. 数据库设计

数据库版本 4，迁移链：v1→v2（数码表）→v3（宠物表）→v4（地图标记表）。

### 6.1 冰箱物品 DAO

```kotlin
@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE isCleared = 0 ORDER BY expirationDate ASC")
    fun observeActiveItems(): Flow<List<ItemEntity>>

    @Query("SELECT category, SUM(quantity) as count FROM items WHERE isCleared = 0 GROUP BY category")
    fun observeCategoryCounts(): Flow<List<CategoryCount>>

    @Query("SELECT SUM(quantity) FROM items WHERE isCleared = 0")
    fun observeTotalCount(): Flow<Int?>

    @Insert
    suspend fun insert(item: ItemEntity): Long

    @Query("UPDATE items SET quantity = :quantity, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int, timestamp: Long)

    @Query("UPDATE items SET isCleared = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun markCleared(id: Long, timestamp: Long)
}
```

### 6.2 数码产品 DAO

```kotlin
@Dao
interface DigitalItemDao {
    @Query("SELECT * FROM digital_items ORDER BY CASE WHEN sellDate IS NULL THEN 0 ELSE 1 END ASC, purchaseDate DESC")
    fun observeAllItems(): Flow<List<DigitalItemEntity>>

    @Insert
    suspend fun insert(item: DigitalItemEntity): Long

    @Query("UPDATE digital_items SET sellPrice = :sellPrice, sellDate = :sellDate, updatedAt = :timestamp WHERE id = :id")
    suspend fun markSold(id: Long, sellPrice: Double, sellDate: Long, timestamp: Long)
}
```

### 6.3 宠物用品 DAO

```kotlin
@Dao
interface PetCategoryDao {
    @Query("SELECT * FROM pet_categories ORDER BY CASE WHEN isDefault = 1 THEN 0 ELSE 1 END ASC, id ASC")
    fun observeAll(): Flow<List<PetCategoryEntity>>

    @Query("SELECT COUNT(*) FROM pet_categories WHERE name = :name")
    suspend fun countByName(name: String): Int

    @Insert
    suspend fun insert(category: PetCategoryEntity): Long
}

@Dao
interface PetItemDao {
    @Query("SELECT * FROM pet_items ORDER BY purchaseDate DESC")
    fun observeAll(): Flow<List<PetItemEntity>>

    @Insert
    suspend fun insert(item: PetItemEntity): Long
}
```

### 6.4 地图标记 DAO

```kotlin
@Dao
interface MapMarkerDao {
    @Query("SELECT * FROM map_markers ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<MapMarkerEntity>>

    @Insert
    suspend fun insert(marker: MapMarkerEntity): Long

    @Query("DELETE FROM map_markers WHERE id = :id")
    suspend fun delete(id: Long)
}
```

### 6.5 数据库迁移

```kotlin
// v2 -> v3：宠物用品表 + 预置猫粮/猫砂/猫玩具
// v3 -> v4：地图标记表
private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `map_markers` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`itemType` TEXT NOT NULL, " +
                "`itemId` INTEGER NOT NULL, " +
                "`itemDisplayName` TEXT NOT NULL, " +
                "`posX` REAL NOT NULL, " +
                "`posY` REAL NOT NULL, " +
                "`roomLabel` TEXT, " +
                "`createdAt` INTEGER NOT NULL)"
        )
    }
}
```

全新安装时通过 `Callback.onCreate` 预置默认宠物种类。

## 7. 底部导航

四个 Tab，使用 Material 3 NavigationBar：

| Tab | 图标 | 标签 | 页面 |
|-----|------|------|------|
| 0 | Icons.Filled.Kitchen | 冰箱收纳 | FridgeScreen |
| 1 | Icons.Filled.Devices | 数码产品 | DigitalScreen |
| 2 | Icons.Filled.Pets | 宠物用品 | PetScreen |
| 3 | Icons.Filled.Analytics | 物品统计 | StatsScreen |

## 8. 项目结构

```
com.fridgekeeper/
├── FridgeApp.kt                      # Application 类
├── MainActivity.kt                   # 入口 Activity（底部导航四页切换）
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt            # Room 数据库（v4）
│   │   ├── ItemDao.kt                # 冰箱物品 DAO
│   │   ├── ItemEntity.kt             # 冰箱物品实体
│   │   ├── CategoryCount.kt          # 分类统计结果
│   │   ├── DigitalItemDao.kt         # 数码产品 DAO
│   │   ├── DigitalItemEntity.kt      # 数码产品实体
│   │   ├── PetCategoryDao.kt         # 宠物种类 DAO
│   │   ├── PetCategoryEntity.kt      # 宠物种类实体
│   │   ├── PetItemDao.kt             # 宠物物品 DAO
│   │   ├── PetItemEntity.kt          # 宠物物品实体
│   │   ├── MapMarkerDao.kt           # 地图标记 DAO
│   │   └── MapMarkerEntity.kt        # 地图标记实体
│   └── repository/
│       ├── FridgeRepository.kt       # 冰箱仓库层
│       ├── DigitalRepository.kt      # 数码产品仓库层
│       ├── PetRepository.kt          # 宠物用品仓库层
│       └── StatsRepository.kt        # 统计+地图仓库层
├── model/
│   ├── ItemCategory.kt               # 冰箱分类枚举
│   ├── ItemUiState.kt                # 冰箱 UI 状态模型
│   ├── DigitalItemUiState.kt         # 数码产品 UI 状态模型
│   ├── QuantityType.kt               # 数量类型/重量单位枚举
│   ├── PetItemUiState.kt             # 宠物用品 UI 状态模型
│   ├── StatsUiState.kt               # 统计页面 UI 状态模型
│   ├── MapMarkerUiState.kt           # 地图标记 UI 状态模型
│   └── DateUtils.kt                  # 日期工具类
├── ui/
│   ├── theme/
│   │   ├── Color.kt                  # 颜色定义
│   │   ├── Theme.kt                  # 主题配置
│   │   └── Type.kt                   # 字体配置
│   ├── components/
│   │   ├── StatsBar.kt               # 冰箱统计栏组件
│   │   ├── ItemCard.kt               # 冰箱物品卡片组件
│   │   ├── AddItemDialog.kt          # 冰箱添加物品对话框
│   │   ├── DigitalItemCard.kt        # 数码产品卡片组件
│   │   ├── AddDigitalItemDialog.kt   # 添加数码产品对话框
│   │   ├── SellDigitalItemDialog.kt  # 卖出对话框
│   │   ├── PetItemCard.kt            # 宠物用品卡片组件
│   │   ├── AddPetItemDialog.kt       # 添加宠物用品对话框
│   │   ├── StatsCard.kt              # 统计汇总卡片组件
│   │   ├── RoomMap.kt                # 房间地图 Canvas 组件
│   │   ├── ItemPickerDialog.kt       # 物品选择器（地图标注用）
│   │   └── MarkerInfoSheet.kt        # 标注简略信息弹窗
│   └── screen/
│       ├── FridgeScreen.kt           # 冰箱收纳页面
│       ├── DigitalScreen.kt          # 数码产品页面
│       ├── PetScreen.kt              # 宠物用品页面
│       └── StatsScreen.kt            # 物品统计页面
└── viewmodel/
    ├── FridgeViewModel.kt            # 冰箱页面 ViewModel
    ├── DigitalViewModel.kt           # 数码产品页面 ViewModel
    ├── PetViewModel.kt               # 宠物用品页面 ViewModel
    ├── StatsViewModel.kt             # 统计页面 ViewModel
    └── ViewModelFactory.kt           # ViewModel 工厂
```

## 9. 非功能需求

| 项目 | 要求 |
|------|------|
| 性能 | 列表滑动流畅 60fps，数据库操作异步不阻塞 UI |
| 数据持久化 | 使用 Room 本地数据库，应用重启数据不丢失 |
| 日期处理 | 统一使用时间戳存储，UI 层格式化为 `yyyy-MM-dd` |
| 适配 | 支持竖屏，适配主流 Android 手机屏幕尺寸 |
| 最低版本 | Android 8.0 (API 26) |
| 地图坐标 | 归一化（0.0~1.0），适配不同屏幕分辨率 |
