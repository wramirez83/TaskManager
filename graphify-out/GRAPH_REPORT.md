# Graph Report - TaskManager  (2026-09-07)

## Corpus Check
- 19 files · ~3,891 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 195 nodes · 433 edges · 13 communities
- Extraction: 89% EXTRACTED · 11% INFERRED · 0% AMBIGUOUS · INFERRED: 47 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

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

## God Nodes (most connected - your core abstractions)
1. `Task` - 36 edges
2. `TaskViewModel` - 28 edges
3. `Category` - 20 edges
4. `TaskRepository` - 19 edges
5. `ContentFragment` - 18 edges
6. `TaskDao` - 16 edges
7. `TaskFilter` - 15 edges
8. `MenuAdapter` - 14 edges
9. `TaskAdapter` - 13 edges
10. `TaskHolder` - 11 edges

## Surprising Connections (you probably didn't know these)
- `ContentFragment` --references--> `Category`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/ContentFragment.java → app/src/main/java/co/edu/poli/taskmanager/data/Category.java
- `TaskAdapter` --references--> `Category`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/TaskAdapter.java → app/src/main/java/co/edu/poli/taskmanager/data/Category.java
- `TaskViewModel` --references--> `Category`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/TaskViewModel.java → app/src/main/java/co/edu/poli/taskmanager/data/Category.java
- `TaskRepository` --references--> `CategoryDao`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/data/TaskRepository.java → app/src/main/java/co/edu/poli/taskmanager/data/CategoryDao.java
- `TaskAdapter` --references--> `Task`  [EXTRACTED]
  app/src/main/java/co/edu/poli/taskmanager/ui/TaskAdapter.java → app/src/main/java/co/edu/poli/taskmanager/data/Task.java

## Import Cycles
- None detected.

## Communities (13 total, 0 thin omitted)

### Community 0 - "TaskViewModel"
Cohesion: 0.12
Nodes (13): AndroidViewModel, ContentFragment, Bundle, LayoutInflater, Nullable, Override, View, ViewGroup (+5 more)

### Community 1 - "TaskRepository"
Cohesion: 0.17
Nodes (8): Dao, Delete, LiveData, Query, TaskDao, LiveData, TaskRepository, Application

### Community 2 - "Category"
Cohesion: 0.17
Nodes (10): Category, Entity, Ignore, CategoryDao, Dao, Delete, Insert, LiveData (+2 more)

### Community 3 - "MenuAdapter"
Cohesion: 0.18
Nodes (8): NonNull, Override, ViewGroup, MenuAdapter, MenuHolder, OnMenuClick, MenuOption, ItemCategoryBinding

### Community 4 - "Task"
Cohesion: 0.16
Nodes (6): Entity, Ignore, Task, Insert, Update, Listener

### Community 5 - "MenuFragment.java"
Cohesion: 0.19
Nodes (13): Bundle, Override, MainActivity, Bundle, LayoutInflater, Nullable, Override, View (+5 more)

### Community 6 - "TaskAdapter"
Cohesion: 0.20
Nodes (8): Adapter, NonNull, Override, ViewGroup, TaskAdapter, TaskHolder, ItemTaskBinding, ViewHolder

### Community 7 - "TaskFilter"
Cohesion: 0.21
Nodes (6): Kind, ALL, CATEGORY, COMPLETED, PENDING, TaskFilter

### Community 8 - "AppDatabase"
Cohesion: 0.27
Nodes (6): AppDatabase, Application, Callback, Context, Database, RoomDatabase

### Community 9 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **4 isolated node(s):** `ALL`, `PENDING`, `COMPLETED`, `CATEGORY`
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `TaskViewModel` connect `TaskViewModel` to `TaskRepository`, `Category`, `MenuAdapter`, `Task`, `MenuFragment.java`, `TaskFilter`?**
  _High betweenness centrality (0.350) - this node is a cross-community bridge._
- **Why does `Task` connect `Task` to `TaskViewModel`, `TaskRepository`, `TaskAdapter`?**
  _High betweenness centrality (0.265) - this node is a cross-community bridge._
- **Why does `Category` connect `Category` to `TaskViewModel`, `TaskRepository`, `Task`, `TaskAdapter`?**
  _High betweenness centrality (0.163) - this node is a cross-community bridge._
- **What connects `ALL`, `PENDING`, `COMPLETED` to the rest of the system?**
  _4 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TaskViewModel` be split into smaller, more focused modules?**
  _Cohesion score 0.11932773109243698 - nodes in this community are weakly interconnected._