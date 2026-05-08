# Gantt Manager – Java Swing Application

A **Java-based desktop application** for loading, filtering, reporting, and visualising Gantt chart project data. Built as a group project for the academic year 2022‑23.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture & Design](#architecture--design)
- [Key Components](#key-components)
- [Data Format](#data-format)
- [How to Run](#how-to-run)
- [Testing](#testing)
- [Results & Observations](#results--observations)
- [Future Improvements](#future-improvements)

---

## Overview

Gantt Manager is a **three‑tier Java application** that parses TSV (tab‑separated) files describing project tasks, computes derived properties for composite tasks, and provides:

- **Console‑based** execution via `NaiveClient`
- **Swing GUI** with file choosers, filter dialogs, tabular views, and a Gantt‑like raster visualisation (via `AppStarter`)
- **Report generation** in plain text, Markdown, and HTML

The project follows a clear separation of concerns: **backend** (core logic), **dom2app** (data transfer model), and **app** (orchestration + GUI).

---


---

## Features

| Feature | Description |
|---------|-------------|
| **Load TSV files** | Parse tab‑separated project files with columns: TaskId, TaskText, MamaId, Start, End, Cost |
| **Auto‑compute composite tasks** | Tasks with no explicit dates (`"-"`) derive Start/End/Cost from their sub‑tasks |
| **Topological sorting** | Tasks are sorted so that top‑level tasks appear before their children, ordered by Start date |
| **Filter by prefix** | Search tasks whose description starts with a given text (case‑insensitive) |
| **Filter by ID** | Retrieve a single task by its numeric ID |
| **Top‑level tasks** | Extract only tasks with `MamaId = 0` |
| **Raster (Gantt) view** | Transform table data into a time‑ranged matrix suitable for Gantt‑chart visualisation |
| **Export reports** | Generate **TXT**, **Markdown**, and **HTML** reports of the sorted project |
| **Swing GUI** | Full desktop interface with menus, file choosers, and internal frames |
| **JUnit tests** | 6 unit tests covering load, filters, and edge cases |

---

## Architecture & Design

The application follows a **three‑tier architecture**:
┌──────────────────────────────────────────────┐
│ app (Orchestration + GUI) │
│ AppController, AppStarter, NaiveClient │
├──────────────────────────────────────────────┤
│ backend (Business Logic) │
│ IMainController, MainController, ReportType │
├──────────────────────────────────────────────┤
│ dom2app (Data Transfer) │
│ SimpleTableModel, SimpleRasterModel │
└──────────────────────────────────────────────┘



### Design Patterns

- **Factory Pattern** – `MainControllerFactory` creates `IMainController` implementations
- **Interface Segregation** – `IMainController` defines the contract between front‑end and back‑end
- **MVC‑like separation** – `SimpleTableModel` / `SimpleRasterModel` extend `AbstractTableModel` (Swing's MVC)

---

## Key Components

| Class | Package | Responsibility |
|-------|---------|----------------|
| `IMainController` | `backend` | Interface defining all core operations |
| `MainController` | `backend` | Implements load, filter, sort, and report generation logic |
| `MainControllerFactory` | `backend` | Factory for creating `MainController` instances |
| `ReportType` | `backend` | Enum: `TEXT`, `MD`, `HTML` |
| `SimpleTableModel` | `dom2app` | Extends `AbstractTableModel`; holds column names and row data |
| `SimpleRasterModel` | `app` | Extends `AbstractTableModel`; represents Gantt‑raster data |
| `AppController` | `app` | Orchestrator: delegates to backend and converts between models |
| `AppStarter` | `app.gui` | Entry point for Swing GUI |
| `JFrameLevel00RootFrame` | `app.gui` | Main Swing frame with menus and internal frames |
| `JTableViewer` | `app.gui.jtableview` | Renders `AbstractTableModel` in a `JTable` |
| `NaiveClient` | `app.naive` | Console‑based demo client |

---

## Data Format

Input files are **tab‑separated (TSV)** with exactly 6 columns:

| Column | Meaning | Example |
|--------|---------|---------|
| TaskId | Unique integer ID | `100` |
| TaskText | Human‑readable description | `Prepare Fry` |
| MamaId | Parent task ID (`0` = top‑level) | `0` |
| Start | Start day (integer) | `1` |
| End | End day (integer) | `12` |
| Cost | Numeric cost | `60` |

**Composite tasks** have `"-"` for Start, End, and Cost. Their values are automatically computed as:
- **Start** = minimum Start of all sub‑tasks
- **End** = maximum End of all sub‑tasks
- **Cost** = sum of all sub‑task costs

### Sample Data (`EggsScrambled.tsv`)
100 Prepare Fry 0 1 12 60
101 Turn on burner (low) 100 1 1 10
102 Break eggs and pour into fry 100 2 4 10
103 Steer mixture to avoid sticking 100 5 10 10
104 Throw yellow cheese into fry 100 6 12 10
105 Salt, pepper 100 5 5 10
106 Turn burner off 100 12 12 10
200 Prepare the bread 0 10 12 20
201 Heat bread in toaster 200 10 12 10
202 Little bit of salt, galric spice 200 12 12 10
300 Serve eggs 0 13 20 30
301 Put bread in plate 300 13 13 10
302 Put eggs on bread 300 14 14 10
303 Wash fry 300 15 20 10


---

## How to Run

### Prerequisites
- Java Development Kit (JDK 8 or later)
- Eclipse IDE (project includes `.classpath` and `.project` files) or any IDE supporting Java

### Option 1 – Using Eclipse
1. Clone the repository:
   ```bash
   git clone https://github.com/MikeMiaris/gantt-chart-java.git

2. In Eclipse: File → Import → Existing Projects into Workspace
3. Navigate to the gantt-chart-java/4633_4645_4735_GanttManager folder
4.Select the project and click Finish
5.Right-click src/main/java/app/gui/AppStarter.java → Run As → Java Application

### Option 2 - Command Line (Console Demo)
cd 4633_4645_4735_GanttManager
javac -d bin src/main/java/**/*.java
java -cp bin app.naive.NaiveClient

### Option 3 – Run Tests
java -cp bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.MainControllerTest

## Testing

The project includes **6 JUnit tests** in `MainControllerTest.java`:

| Test Method | What It Verifies |
|-------------|------------------|
| `testLoad()` | TSV loading and expected row ordering after sorting |
| `testGetTopLevelTasks()` | Correct extraction of tasks with `MamaId = 0` |
| `testGetTaskByPrefix()` | Case‑insensitive prefix search (e.g., `"put"` → `"Put eggs on bread"`, `"Put bread in plate"`) |
| `testGetTaskByPrefix2()` | Graceful handling of non‑matching prefix (`"cook"` → error message row) |
| `testGetTaskById()` | Exact ID lookup |
| `testGetTaskById2()` | Graceful handling of non‑existent ID (`120` → error message row) |

All tests pass successfully against the `EggsScrambled.tsv` fixture.

## Results & Observations

- **Composite task computation** works reliably: parent tasks dynamically derive their Start, End, and Cost from children.
- **Sorting algorithm** (manual bubble‑sort) correctly orders tasks hierarchically: top‑level tasks appear before their descendants, both sorted by Start date and then by TaskId for tie‑breaking.
- **Filter functions** include helpful error messages when no match is found, rather than returning empty results.
- **Report generation** automatically appends the correct file extension (`.txt`, `.md`, `.html`) if omitted by the user.
- The **Gantt raster view** successfully maps time‑based data onto a 2D matrix suitable for visual representation in a `JTable`.

## Future Improvements

### Repository & Structure
- [ ] Add `.gitignore` to exclude `bin/`, `.class`, and IDE files
- [ ] Declare a proper Java package (e.g., `com.ganttmanager`) instead of default‑ish packages
- [ ] Add Maven/Gradle build to manage dependencies (JUnit, etc.)

### Code Quality
- [ ] Replace raw `List` types with generics (e.g., `List<String[]>` instead of `List`)
- [ ] Fix spelling: `"galric"` → `"garlic"`, `"Steer"` → `"Stir"`, `"mamaId"` → `"parentId"` or `"mammaId"`
- [ ] Replace manual bubble‑sort with `Comparator` + `Collections.sort()` for clarity
- [ ] Use `try`-with‑resources consistently for file I/O
- [ ] Add Javadoc to all public methods

### Features
- [ ] Drag‑and‑drop task editing in the GUI
- [ ] Export to PNG/PDF of the Gantt chart
- [ ] Undo/redo support
- [ ] Direct editing of task properties within the table view
