# Graph Report - TaskManager  (2026-09-18)

## Corpus Check
- 27 files · ~6,391 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 329 nodes · 756 edges · 16 communities
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 56 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `cb302d5e`
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
- gradlew
- Photo
- DonutChartView
- TaskListFragment.java
- TaskViewModel

## God Nodes (most connected - your core abstractions)
1. `Task` - 39 edges
2. `TaskViewModel` - 32 edges
3. `MainActivity` - 22 edges
4. `TaskRepository` - 20 edges
5. `Category` - 19 edges
6. `Photo` - 16 edges
7. `CreateTaskFragment` - 14 edges
8. `TaskFilter` - 14 edges
9. `TaskDao` - 12 edges
10. `CategoryDao` - 11 edges

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

## Communities (16 total, 0 thin omitted)

### Community 0 - "TaskViewModel"
Cohesion: 0.20
Nodes (8): CreateTaskFragment, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, FragmentCreateTaskBinding

### Community 1 - "TaskRepository"
Cohesion: 0.09
Nodes (26): ActivityResultLauncher, Adapter, GalleryFragment, Holder, Bundle, LayoutInflater, NonNull, Nullable (+18 more)

### Community 2 - "Category"
Cohesion: 0.18
Nodes (10): Category, Entity, Ignore, CategoryDao, Dao, Delete, Insert, LiveData (+2 more)

### Community 3 - "MenuAdapter"
Cohesion: 0.16
Nodes (14): BrowserFragment, Holder, Bundle, LayoutInflater, NonNull, Nullable, Override, View (+6 more)

### Community 4 - "Task"
Cohesion: 0.08
Nodes (19): Entity, Ignore, Task, Dao, Delete, Insert, LiveData, Query (+11 more)

### Community 5 - "MenuFragment.java"
Cohesion: 0.17
Nodes (11): ActionBarDrawerToggle, ActivityMainBinding, Bundle, Fragment, Override, MainActivity, AppCompatActivity, ImageView (+3 more)

### Community 6 - "TaskAdapter"
Cohesion: 0.27
Nodes (8): Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, VideoFragment, FragmentVideoBinding

### Community 7 - "TaskFilter"
Cohesion: 0.12
Nodes (16): HomeFragment, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, Kind (+8 more)

### Community 9 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 13 - "Photo"
Cohesion: 0.18
Nodes (9): Entity, Ignore, Photo, Dao, Delete, Insert, LiveData, Query (+1 more)

### Community 15 - "DonutChartView"
Cohesion: 0.24
Nodes (8): DonutChartView, Context, Override, AttributeSet, Canvas, Paint, RectF, View

### Community 16 - "TaskListFragment.java"
Cohesion: 0.12
Nodes (15): AndroidViewModel, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup, TaskListFragment (+7 more)

### Community 18 - "TaskViewModel"
Cohesion: 0.17
Nodes (6): AppDatabase, Context, Application, DateUtils, Database, RoomDatabase

## Knowledge Gaps
- **5 isolated node(s):** `ALL`, `TODAY`, `IMPORTANT`, `COMPLETED`, `SEARCH`
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `TaskViewModel` connect `TaskListFragment.java` to `TaskViewModel`, `TaskRepository`, `Category`, `Task`, `MenuFragment.java`, `TaskFilter`, `Photo`, `TaskViewModel`?**
  _High betweenness centrality (0.248) - this node is a cross-community bridge._
- **Why does `Task` connect `Task` to `TaskViewModel`, `TaskListFragment.java`, `TaskViewModel`, `MenuFragment.java`?**
  _High betweenness centrality (0.240) - this node is a cross-community bridge._
- **Why does `Category` connect `Category` to `TaskViewModel`, `TaskListFragment.java`, `Task`?**
  _High betweenness centrality (0.110) - this node is a cross-community bridge._
- **What connects `ALL`, `TODAY`, `IMPORTANT` to the rest of the system?**
  _5 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TaskRepository` be split into smaller, more focused modules?**
  _Cohesion score 0.09268292682926829 - nodes in this community are weakly interconnected._
- **Should `Task` be split into smaller, more focused modules?**
  _Cohesion score 0.08166969147005444 - nodes in this community are weakly interconnected._
- **Should `TaskFilter` be split into smaller, more focused modules?**
  _Cohesion score 0.12183908045977011 - nodes in this community are weakly interconnected._