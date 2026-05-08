# Gantt Manager – Java Swing Application

A **Java-based desktop application** for loading, filtering, reporting, and visualising Gantt chart project data. Built as a group project (**Dimos Apostolidis 4633, Ilias Georgiadis 4645, Michalis Miaris 4735**) for the academic year 2022‑23.

---

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
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

## Project Structure
