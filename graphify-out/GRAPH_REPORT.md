# Graph Report - TaskManager  (2026-09-08)

## Corpus Check
- 27 files · ~5,564 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 315 nodes · 703 edges · 18 communities (17 shown, 1 thin omitted)
- Extraction: 92% EXTRACTED · 8% INFERRED · 0% AMBIGUOUS · INFERRED: 57 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `fd083155`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- TaskViewModel
- TaskRepository
- Category
- MenuAdapter
- Task
- MenuFragment.java
- TaskAdapter
- TaskFilter
- AppDatabase
- gradlew
- Photo
- StatsFragment.java
- DonutChartView
- TaskListFragment.java
- .observeToday

## God Nodes (most connected - your core abstractions)
1. `Task` - 39 edges
2. `TaskViewModel` - 32 edges
3. `TaskRepository` - 20 edges
4. `Category` - 19 edges
5. `Photo` - 16 edges
6. `TaskFilter` - 14 edges
7. `CreateTaskFragment` - 13 edges
8. `MainActivity` - 12 edges
9. `TaskDao` - 12 edges
10. `TaskAdapter` - 11 edges

## Surprising Connections (you probably didn't know these)
- `MainActivity` --references--> `TaskViewModel`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/MainActivity.java → app/src/main/java/co/edu/poli/taskmanager/ui/TaskViewModel.java
- `TaskViewModel` --references--> `Category`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/TaskViewModel.java → app/src/main/java/co/edu/poli/taskmanager/data/Category.java
- `TaskRepository` --references--> `CategoryDao`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/data/TaskRepository.java → app/src/main/java/co/edu/poli/taskmanager/data/CategoryDao.java
- `PhotoAdapter` --references--> `Photo`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/GalleryFragment.java → app/src/main/java/co/edu/poli/taskmanager/data/Photo.java
- `TaskViewModel` --references--> `Photo`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/TaskViewModel.java → app/src/main/java/co/edu/poli/taskmanager/data/Photo.java

## Import Cycles
- None detected.

## Communities (18 total, 1 thin omitted)

### Community 0 - "TaskViewModel"
Cohesion: 0.12
Nodes (14): AndroidViewModel, CreateTaskFragment, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup (+6 more)

### Community 1 - "TaskRepository"
Cohesion: 0.10
Nodes (25): ActivityResultLauncher, Adapter, GalleryFragment, Holder, Bundle, LayoutInflater, NonNull, Nullable (+17 more)

### Community 2 - "Category"
Cohesion: 0.21
Nodes (7): CategoryDao, Dao, Delete, Insert, LiveData, Query, Update

### Community 3 - "MenuAdapter"
Cohesion: 0.16
Nodes (14): BrowserFragment, Holder, Bundle, LayoutInflater, NonNull, Nullable, Override, View (+6 more)

### Community 4 - "Task"
Cohesion: 0.09
Nodes (18): Category, Entity, Ignore, Entity, Ignore, Task, Dao, Delete (+10 more)

### Community 5 - "MenuFragment.java"
Cohesion: 0.19
Nodes (8): ActivityMainBinding, Bundle, Fragment, Override, MainActivity, AppCompatActivity, Menu, MenuItem

### Community 6 - "TaskAdapter"
Cohesion: 0.26
Nodes (5): NonNull, Override, ViewGroup, TaskHolder, ItemTaskBinding

### Community 7 - "TaskFilter"
Cohesion: 0.13
Nodes (16): HomeFragment, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, Kind (+8 more)

### Community 8 - "AppDatabase"
Cohesion: 0.19
Nodes (9): AppDatabase, Context, Override, SeedCallback, Application, Callback, Database, RoomDatabase (+1 more)

### Community 9 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 13 - "Photo"
Cohesion: 0.18
Nodes (9): Entity, Ignore, Photo, Dao, Delete, Insert, LiveData, Query (+1 more)

### Community 14 - "StatsFragment.java"
Cohesion: 0.27
Nodes (9): Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, StatsFragment, FragmentStatsBinding (+1 more)

### Community 15 - "DonutChartView"
Cohesion: 0.24
Nodes (8): DonutChartView, Context, Override, AttributeSet, Canvas, Paint, RectF, View

### Community 16 - "TaskListFragment.java"
Cohesion: 0.28
Nodes (8): Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, TaskListFragment, FragmentTaskListBinding

## Knowledge Gaps
- **5 isolated node(s):** `ALL`, `TODAY`, `IMPORTANT`, `COMPLETED`, `SEARCH`
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Task` connect `Task` to `TaskViewModel`, `.observeToday`, `MenuFragment.java`, `TaskAdapter`?**
  _High betweenness centrality (0.242) - this node is a cross-community bridge._
- **Why does `TaskViewModel` connect `TaskViewModel` to `TaskRepository`, `Task`, `MenuFragment.java`, `TaskFilter`, `Photo`, `TaskListFragment.java`, `.observeToday`?**
  _High betweenness centrality (0.236) - this node is a cross-community bridge._
- **Why does `Category` connect `Task` to `TaskViewModel`, `TaskListFragment.java`, `Category`?**
  _High betweenness centrality (0.114) - this node is a cross-community bridge._
- **What connects `ALL`, `TODAY`, `IMPORTANT` to the rest of the system?**
  _5 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TaskViewModel` be split into smaller, more focused modules?**
  _Cohesion score 0.12096774193548387 - nodes in this community are weakly interconnected._
- **Should `TaskRepository` be split into smaller, more focused modules?**
  _Cohesion score 0.10256410256410256 - nodes in this community are weakly interconnected._
- **Should `Task` be split into smaller, more focused modules?**
  _Cohesion score 0.09268707482993198 - nodes in this community are weakly interconnected._