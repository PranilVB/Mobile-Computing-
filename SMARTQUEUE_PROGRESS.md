# Smart Queue — Android Project Progress Log

> **Project:** Smart Queue and Waiting Time Estimator  
> **Platform:** Native Android (Kotlin + XML + MVVM)  
> **Package:** `com.example.smartqueue`  
> **Root directory:** `E:\SmartQueue\`  
> **Last updated:** 2026-11-23  
> **Status:** 🟡 Phase 1 — UI skeleton in progress

---

## Purpose of This File

This document is the **single source of truth** for all agents (and human contributors) working on the SmartQueue project. It tracks:

- What has been built and **why** each decision was made
- What is **in progress** and what is **pending**
- Known issues, blockers, and design decisions
- Phase plan with acceptance criteria

Share this file with any new coding agent alongside the spec: `SMART_QUEUE_ANDROID_UI_SPEC.md`

---

## Project Spec Reference

| Item | Detail |
|------|--------|
| Full spec | `c:\Users\ASUS\Downloads\SMART_QUEUE_ANDROID_UI_SPEC.md` |
| Spec sections | Screens A–H, design tokens, data model, queue rules, dev sequence |
| Design mandate | Practical utility app — NO gradients, NO glassmorphism, NO AI-badges |
| Primary accent | `#21624F` (deep green) |
| Font | Roboto / system font |
| Spacing grid | 8dp, 16–20dp margins, 12dp card radius, 48dp min touch targets |

---

## Stack Decisions & Why

| Area | Choice | Reason |
|------|--------|--------|
| Language | **Kotlin** | Modern, concise, coroutine-first |
| Layouts | **XML + ViewBinding** | Familiar for students, easy to review |
| Architecture | **MVVM + Repository** | Separation of concerns; ViewModel survives rotation |
| Navigation | **Navigation Component (Single Activity)** | Handles back stack, safe args, bottom nav |
| Auth | **Firebase Authentication** | Simple email/password; role stored in Firestore |
| Database | **Cloud Firestore** | Real-time snapshot listeners; offline cache |
| Backend ops | **Firebase Cloud Functions** (Phase 2+) | Secure transactional token issuance server-side |
| Async | **Kotlin Coroutines + Flow** | Integrates well with Firestore; lifecycle-safe |
| UI kit | **Material Components for Android** | Native feel; no custom widget dependencies |
| minSdk | **24** | Covers >95% of active devices |
| compileSdk | **34** | Latest stable |

> [!IMPORTANT]
> Admin writes (call next, complete, skip, cancel) MUST go through Cloud Functions — never directly from the client. Firestore security rules enforce this.

---

## File Structure

```
E:\SmartQueue\
├── build.gradle                          ← project-level Gradle
├── settings.gradle
├── gradle.properties
└── app/
    ├── build.gradle                      ← app-level Gradle + all deps
    ├── google-services.json              ← ⚠️ ADD MANUALLY from Firebase Console
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/smartqueue/
        │   ├── MainActivity.kt           ← single activity host
        │   ├── auth/
        │   │   ├── LoginFragment.kt
        │   │   ├── LoginViewModel.kt
        │   │   ├── RegisterFragment.kt
        │   │   └── RegisterViewModel.kt
        │   ├── user/
        │   │   ├── HomeFragment.kt
        │   │   ├── HomeViewModel.kt
        │   │   ├── ServiceDetailFragment.kt
        │   │   ├── ServiceDetailViewModel.kt
        │   │   ├── MyTokenFragment.kt
        │   │   ├── MyTokenViewModel.kt
        │   │   ├── ProfileFragment.kt
        │   │   └── ProfileViewModel.kt
        │   ├── admin/
        │   │   ├── AdminQueuesFragment.kt
        │   │   ├── AdminQueuesViewModel.kt
        │   │   ├── AdminQueueDetailFragment.kt
        │   │   └── AdminQueueDetailViewModel.kt
        │   ├── data/
        │   │   ├── model/
        │   │   │   ├── User.kt
        │   │   │   ├── Service.kt
        │   │   │   ├── Queue.kt
        │   │   │   ├── Token.kt
        │   │   │   ├── ServiceHistory.kt
        │   │   │   └── MockData.kt       ← Phase 1 sample data only
        │   │   ├── repository/
        │   │   │   └── QueueRepository.kt
        │   │   └── remote/
        │   │       └── FirestoreService.kt
        │   ├── ui/
        │   │   └── adapters/
        │   │       ├── ServicesAdapter.kt
        │   │       └── TokenListAdapter.kt
        │   └── util/
        │       ├── Resource.kt
        │       └── Extensions.kt
        └── res/
            ├── values/
            │   ├── colors.xml
            │   ├── strings.xml
            │   ├── themes.xml
            │   └── dimens.xml
            ├── layout/
            │   ├── activity_main.xml
            │   ├── fragment_login.xml
            │   ├── fragment_register.xml
            │   ├── fragment_home.xml
            │   ├── fragment_service_detail.xml
            │   ├── fragment_my_token.xml
            │   ├── fragment_profile.xml
            │   ├── fragment_admin_queues.xml
            │   ├── fragment_admin_queue_detail.xml
            │   ├── item_service.xml
            │   └── item_token.xml
            ├── navigation/
            │   └── nav_graph.xml
            └── menu/
                ├── bottom_nav_user.xml
                └── bottom_nav_admin.xml
```

---

## Phase Plan

### ✅ Phase 0 — Project Setup
- [x] Spec read and analysed
- [x] Project directory created at `E:\SmartQueue\`
- [x] Gradle files written (project + app level)
- [x] AndroidManifest.xml written
- [x] Data models written (`User`, `Service`, `Queue`, `Token`, `ServiceHistory`)
- [x] Mock data object created (`MockData.kt`)
- [x] Repository layer scaffolded (`QueueRepository.kt`)
- [x] Remote service stub written (`FirestoreService.kt`)
- [x] Utilities written (`Resource.kt`, `Extensions.kt`)

### 🟡 Phase 1 — UI Skeleton (In Progress)
- [x] Design tokens → `colors.xml`, `themes.xml`, `dimens.xml`
- [x] String resources → `strings.xml`
- [x] Navigation graph → `nav_graph.xml`
- [x] Bottom nav menus
- [x] `activity_main.xml`
- [x] Auth screens: `fragment_login.xml`, `fragment_register.xml`
- [x] `fragment_home.xml` with services list + active token strip
- [x] `fragment_service_detail.xml`
- [x] `fragment_my_token.xml` (most important user screen)
- [x] `fragment_profile.xml`
- [x] `fragment_admin_queues.xml`
- [x] `fragment_admin_queue_detail.xml`
- [x] RecyclerView adapters
- [x] All Fragment Kotlin files with ViewBinding + mock data wired
- [x] ViewModels for all screens
- [x] `MainActivity.kt` with role-based nav routing
- [ ] Run on emulator — verify layout, back stack, keyboard behavior

### ⬜ Phase 2 — Real Data & Authentication
- [ ] Add `google-services.json` from Firebase Console
- [ ] Wire `FirebaseAuth` login/register
- [ ] Load services from Firestore
- [ ] Implement token request via Cloud Function
- [ ] Connect `MyTokenFragment` to Firestore snapshot
- [ ] Connect admin queue list to Firestore snapshot

### ⬜ Phase 3 — Queue Operations & Estimation
- [ ] Call next / complete / skip / cancel via Cloud Functions
- [ ] Record `ServiceHistory` on completion
- [ ] Compute `estimated_wait = people_ahead × avg_duration`
- [ ] Offline banner + stale data handling

### ⬜ Phase 4 — Validation & Polish
- [ ] Token uniqueness test
- [ ] Access control verification
- [ ] Small screen & font scaling check
- [ ] Remove all mock data from production paths
- [ ] Emulator + physical device test

---

## Design Decisions & Rationale

| Decision | Rationale |
|----------|-----------|
| Vertical service list (not tile grid) | Spec explicitly says "vertical rows, not four identical chunky tiles" |
| Token number is largest element (32–40sp) | Spec: "Make the token number the biggest element on its screen" |
| No animated countdown | Spec: "do not add fake progress percentages or animated countdowns" |
| "About X min" label | Spec: Use `About 20 min`, never `Exactly 20 minutes` |
| Cancel needs confirmation dialog | Spec: "Never cancel immediately when a user accidentally taps" |
| No edit profile | Spec: "Do not add edit-profile functions until supported by the data model" |
| Mock data strictly in `MockData.kt` | Keeps fake data isolated; easy to swap in Phase 2 |
| Cloud Functions for admin ops | Spec: "Do not write admin updates directly from an unrestricted mobile client" |
| `whereNotIn` for active token query | Excludes completed/cancelled/skipped; avoids showing stale tokens |

---

## Known Limitations / TODOs

| Item | Notes |
|------|-------|
| `google-services.json` | Must be downloaded from Firebase Console and placed in `app/` |
| `requestToken` / `callNextToken` | Returns `Phase 2` failure until Cloud Function is deployed |
| Admin role assignment | Must be set in Firestore `users/{uid}.role = "admin"` manually or via admin SDK; not user-selectable |
| Multi-counter estimation | Not in scope for v1; formula assumes single counter |
| Push notifications | Not in scope for v1 |
| `whereNotIn` Firestore limitation | Firestore `whereNotIn` supports max 10 values; fine for token statuses |

---

## How to Open in Android Studio

1. Open Android Studio → **Open an existing project** → select `E:\SmartQueue`
2. Let Gradle sync complete (requires internet for dependencies)
3. Add `google-services.json` to `E:\SmartQueue\app\` (from Firebase Console)
4. Run on emulator or device — Phase 1 shows mock data only

---

## Firestore Security Rules (Add in Phase 2)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read their own profile
    match /users/{userId} {
      allow read: if request.auth.uid == userId;
      allow write: if false; // Only admin SDK
    }
    // Anyone authenticated can read services
    match /services/{serviceId} {
      allow read: if request.auth != null;
      allow write: if false; // Admin SDK only
    }
    // Tokens: user reads own; admin reads all
    match /tokens/{tokenId} {
      allow read: if request.auth.uid == resource.data.userId
        || get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
      allow create: if request.auth != null; // Cloud Function validates
      allow update: if false; // Cloud Functions only
    }
  }
}
```

---

## Agent Handoff Instructions

When continuing this project with a new agent, provide:

1. **This file** (`SMARTQUEUE_PROGRESS.md`) — current state and all decisions
2. **The spec file** (`SMART_QUEUE_ANDROID_UI_SPEC.md`) — full UI/implementation brief
3. **The current phase** — see Phase Plan above
4. **The next task** — described in the pending checklist items

> [!TIP]
> Tell the new agent: *"Continue from Phase X. All previous work is documented in SMARTQUEUE_PROGRESS.md. Follow the spec in SMART_QUEUE_ANDROID_UI_SPEC.md. Do not change architecture decisions already made."*

