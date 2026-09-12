# Graph Report - TaskManager  (2026-09-08)

## Corpus Check
- 27 files · ~5,689 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 317 nodes · 708 edges · 19 communities
- Extraction: 92% EXTRACTED · 8% INFERRED · 0% AMBIGUOUS · INFERRED: 58 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `dc7cc4e6`
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
- TaskAdapter
- TaskViewModel

## God Nodes (most connected - your core abstractions)
1. `Task` - 39 edges
2. `TaskViewModel` - 32 edges
3. `TaskRepository` - 20 edges
4. `Category` - 19 edges
5. `Photo` - 16 edges
6. `CreateTaskFragment` - 14 edges
7. `TaskFilter` - 14 edges
8. `MainActivity` - 13 edges
9. `TaskDao` - 12 edges
10. `TaskAdapter` - 11 edges

## Surprising Connections (you probably didn't know these)
- `MainActivity` --references--> `TaskViewModel`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/MainActivity.java → app/src/main/java/co/edu/poli/taskmanager/ui/TaskViewModel.java
- `TaskAdapter` --references--> `Category`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/TaskAdapter.java → app/src/main/java/co/edu/poli/taskmanager/data/Category.java
- `TaskViewModel` --references--> `Category`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/TaskViewModel.java → app/src/main/java/co/edu/poli/taskmanager/data/Category.java
- `TaskRepository` --references--> `CategoryDao`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/data/TaskRepository.java → app/src/main/java/co/edu/poli/taskmanager/data/CategoryDao.java
- `PhotoAdapter` --references--> `Photo`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/GalleryFragment.java → app/src/main/java/co/edu/poli/taskmanager/data/Photo.java

## Import Cycles
- None detected.

## Communities (19 total, 0 thin omitted)

### Community 0 - "TaskViewModel"
Cohesion: 0.22
Nodes (8): CreateTaskFragment, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, FragmentCreateTaskBinding

### Community 1 - "TaskRepository"
Cohesion: 0.15
Nodes (16): ActivityResultLauncher, Adapter, GalleryFragment, Holder, Bundle, LayoutInflater, NonNull, Nullable (+8 more)

### Community 2 - "Category"
Cohesion: 0.17
Nodes (10): Category, Entity, Ignore, CategoryDao, Dao, Delete, Insert, LiveData (+2 more)

### Community 3 - "MenuAdapter"
Cohesion: 0.16
Nodes (14): BrowserFragment, Holder, Bundle, LayoutInflater, NonNull, Nullable, Override, View (+6 more)

### Community 4 - "Task"
Cohesion: 0.14
Nodes (12): Entity, Ignore, Task, Dao, Delete, Insert, LiveData, Query (+4 more)

### Community 5 - "MenuFragment.java"
Cohesion: 0.21
Nodes (8): ActivityMainBinding, Bundle, Fragment, Override, MainActivity, AppCompatActivity, Menu, MenuItem

### Community 6 - "TaskAdapter"
Cohesion: 0.27
Nodes (8): Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, VideoFragment, FragmentVideoBinding

### Community 7 - "TaskFilter"
Cohesion: 0.13
Nodes (17): HomeFragment, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, Kind (+9 more)

### Community 8 - "AppDatabase"
Cohesion: 0.14
Nodes (10): AppDatabase, Context, Override, SeedCallback, Application, DateUtils, Callback, Database (+2 more)

### Community 9 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 13 - "Photo"
Cohesion: 0.19
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

### Community 17 - "TaskAdapter"
Cohesion: 0.19
Nodes (7): NonNull, Override, ViewGroup, Listener, TaskAdapter, TaskHolder, ItemTaskBinding

### Community 18 - "TaskViewModel"
Cohesion: 0.19
Nodes (7): AndroidViewModel, TaskStats, Application, LiveData, TaskViewModel, MediatorLiveData, MutableLiveData

## Knowledge Gaps
- **5 isolated node(s):** `ALL`, `TODAY`, `IMPORTANT`, `COMPLETED`, `SEARCH`
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Task` connect `Task` to `TaskViewModel`, `MenuFragment.java`, `AppDatabase`, `TaskListFragment.java`, `TaskAdapter`, `TaskViewModel`?**
  _High betweenness centrality (0.241) - this node is a cross-community bridge._
- **Why does `TaskViewModel` connect `TaskViewModel` to `TaskViewModel`, `TaskRepository`, `Category`, `Task`, `MenuFragment.java`, `TaskFilter`, `AppDatabase`, `Photo`, `TaskListFragment.java`?**
  _High betweenness centrality (0.237) - this node is a cross-community bridge._
- **Why does `Category` connect `Category` to `TaskViewModel`, `TaskAdapter`, `TaskViewModel`, `Task`?**
  _High betweenness centrality (0.114) - this node is a cross-community bridge._
- **What connects `ALL`, `TODAY`, `IMPORTANT` to the rest of the system?**
  _5 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Task` be split into smaller, more focused modules?**
  _Cohesion score 0.13813813813813813 - nodes in this community are weakly interconnected._
- **Should `TaskFilter` be split into smaller, more focused modules?**
  _Cohesion score 0.12561576354679804 - nodes in this community are weakly interconnected._
- **Should `AppDatabase` be split into smaller, more focused modules?**
  _Cohesion score 0.13538461538461538 - nodes in this community are weakly interconnected._