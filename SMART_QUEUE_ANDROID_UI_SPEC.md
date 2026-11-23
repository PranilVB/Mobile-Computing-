# Smart Queue — Android App UI & Implementation Brief

> **Project:** Smart Queue and Waiting Time Estimator  
> **Platform:** Native Android mobile app  
> **Purpose of this file:** A practical design and development brief for building the first working version of the app.  
> **Source:** *Smart Queue and Waiting Time Estimator — Mobile Computing Project Documentation* (user-provided PDF, pages 1–5).

## 1. Product concept

Build a mobile application that lets a person join a service queue digitally, see their token, track how many people are ahead, and view an **estimated** wait. Authorized staff can call, finish, skip or cancel tokens from an administration interface.

The app must feel like a real utility used at a **clinic reception, bank, or college office**: direct, calm, dependable, and easy to scan in a hurry. It is **not** a SaaS analytics dashboard, a marketing website, or a futuristic AI assistant.

**Working name:** `Smart Queue` (changeable before final release).  
**Primary actions:** `Join queue`, `View my token`, `Call next`.  
**Core product promise:** *Know your place in line without standing in line.*

### Scope of version 1

- Single service centre; multiple service types.
- User registration and sign-in.
- Select an open service and request a token.
- View token number, current status, people ahead, estimated waiting time and last updated time.
- Cancel own waiting token when permitted.
- Staff-only queue list; call next, complete, skip and cancel tokens.
- Queue updates synchronized to the user app.
- Basic estimator using number ahead and historical average service time, with a fallback configured duration.
- Clear empty, error, offline and loading states.

**Later, not part of version 1:** multiple branches/counters, QR check-in, push notifications, sophisticated prediction/ML, and advanced analytics. These are enhancements in the source documentation, not prerequisites for the first build.

---

## 2. Visual direction — intentionally not “AI-looking”

**Design reference:** an understated appointment/queue utility, closer to a system service app than a promotional fintech product.

### Use

- Native Android screen patterns and sensible system insets.
- Flat surfaces, neat dividers, restrained borders and modest corner radii.
- Clear typographic hierarchy and regular spacing.
- One accent colour used for primary actions and meaningful statuses.
- Plain-language labels such as “3 people ahead” and “About 15 min”.
- Compact information cards only where grouping genuinely helps.
- Simple line icons with one consistent stroke weight.
- Realistic service names: `General Enquiry`, `Fee Payment`, `Document Verification`, `Registration`.

### Avoid

- Gradient backgrounds, glassmorphism, blur-heavy cards and glowing buttons.
- Purple/blue neon themes; huge illustration headers; animated blobs or floating shapes.
- Dashboard-style KPI tiles on every screen, random charts, fake maps, “AI-powered” badges.
- Large generic welcome slogans, emoji-heavy labels, over-rounded pill controls.
- Decorative animations that slow down a queue-related task.
- Unnecessary sidebars, carousels, pop-ups or excessive bottom navigation items.

### Design tokens

These colours are **proposed UI choices**, not requirements from the project PDF.

| Token | Value | Usage |
|---|---|---|
| Background | `#F8F9F7` | Screen background |
| Surface | `#FFFFFF` | Cards and sheets |
| Primary | `#21624F` | Main action, selected state |
| Primary dark | `#174837` | Pressed/contrast state |
| Text primary | `#202623` | Headings, token number |
| Text secondary | `#65716A` | Supporting information |
| Border | `#E0E6E1` | Dividers and outlines |
| Success | `#28724B` | Completed / active confirmation |
| Warning | `#94641D` | Approaching turn / delayed |
| Error | `#B43E3E` | Failure / destructive action |

Use `Roboto` or the Android system font. Typical sizes: 24sp for screen titles, 18sp for section headings, 16sp for primary body, 14sp for supporting text and 12sp for metadata. Make the **token number** the biggest element on its screen (32–40sp), not the estimated time. Respect system font scaling. Use an 8dp spacing grid with 16–20dp screen margins, 12dp card radius and 48dp minimum touch targets.

### Copy rules

- Write **“Estimated wait”**, never “Guaranteed wait”.
- Use `About 20 min`, not `Exactly 20 minutes`.
- Use `Currently serving A-014`, `Your token A-018`, `4 people ahead`.
- Never imply the user is next unless their token position actually confirms it.
- Make confirmation and cancellation language unambiguous.

---

## 3. Navigation and app structure

One Android application with navigation determined by the authenticated role. Do not expose administrator actions merely by hiding buttons: authorize them server-side.

```text
Launch
  ├── Not signed in → Sign in / Create account
  ├── USER → Home
  │     ├── Service details → Join queue → My token
  │     ├── My token
  │     └── Profile
  └── ADMIN → Admin overview
        ├── Service queue → Token action
        ├── Queue history / summary (basic)
        └── Profile / Sign out
```

**User navigation:** bottom bar with `Home`, `My token`, `Profile` (three items maximum). When a user has no active token, `My token` opens its empty state.  
**Admin navigation:** bottom bar with `Queues` and `Profile`; basic history may be an action from `Queues`, not a dedicated dashboard tab.  
**Back behavior:** system back works normally; no screen exits unexpectedly when the user is interacting with a queue.  
**Role handling:** admin role is assigned securely, not selectable on the public registration screen.

---

## 4. Screen-by-screen UI requirements

### A. Launch / authentication

**Sign in**
- App wordmark/text icon at top, without an oversized splash illustration.
- `Email`, `Password` inputs.
- Solid `Sign in` button; text link `Create account`.
- Inline validation and useful authentication errors.
- Password visibility toggle; disabled submit while processing.

**Create account**
- `Full name`, `Email`, `Password`, `Confirm password`.
- Password guidance and input validation.
- After successful sign-up, route to the user home screen.

### B. User home

**Header**
- Small `SMART QUEUE` label or app name.
- `Good morning` is optional; do not prioritize greeting over task.
- Main heading: `Choose a service`.

**Active-token strip, when applicable**
- Label `Your active token`.
- Token number, service name and status.
- Tapping opens `My token`.

**Services list**
Each service row shows:
- Service name.
- Short descriptor, e.g. `Payments and receipts`.
- Current availability: `Open` or `Queue closed`.
- Optional estimated wait preview, only if calculated from actual queue data.
- Chevron/right affordance for accessible navigation.

Use **vertical rows**, not four identical chunky tiles. If a service is closed, indicate why the row cannot be joined.

### C. Service details / join queue

- Top app bar: back arrow + service name.
- Service title and one-sentence description.
- `Queue status: Open`.
- `People waiting: 6` and `Estimated wait: About 30 min` (illustrative UI values only).
- Short disclaimer: `Wait times may change as the queue moves.`
- Main button: `Get token`.
- Disable when the queue is closed or when the user already has an active token for that queue.
- Request must be idempotent against repeated taps; show a progress state.
- On success: show the server-issued token and go to My token.

### D. My token — most important user screen

Arrange the content in this order:

1. Top app bar: `My token`.
2. Service name and service centre name.
3. Large, highly legible token number (for example `A-018`).
4. Status label: `Waiting`, `Called`, `Serving`, `Completed`, `Skipped` or `Cancelled` as relevant.
5. Two simple information rows: `People ahead: 4` and `Estimated wait: About 20 min`.
6. Small current-queue indicator: `Now serving A-014`, only when available.
7. `Updated just now` / timestamp, sourced from the latest successful sync.
8. `Cancel token` secondary destructive-text action, visible only while cancellation is allowed.

**Status treatment**
- Waiting: normal neutral card, green accent on token.
- Called / serving: prominent but restrained status banner, e.g. `It's your turn. Please go to the counter.`; ensure this is driven by real backend state.
- Completed: success text and a clear path back to Home.
- Skipped/cancelled: explain that token is no longer in the active queue.

**Important:** do not add fake progress percentages or animated countdowns. Estimated waiting time is not a timer.

### E. Profile

- Name and email.
- Basic account information.
- `Sign out` action.
- Do not add edit-profile functions until supported by the data model.

### F. Admin — queues overview

- Heading: `Queues`.
- Service list, each item showing its name, status and number waiting.
- A selected service opens its queue-management screen.
- Provide a straightforward empty message when there are no active tokens.
- Keep operational information first; no decorative revenue charts or unrelated statistics.

### G. Admin — service queue

**Top**
- Service name and status.
- `Waiting: 8` and `Now serving: A-014`.

**Primary action**
- Solid `Call next` button, disabled if no eligible waiting token can be called.
- After a call succeeds, display token number clearly.

**Queue list**
- Ordered list of waiting/active tokens showing token number, status and issue time.
- Tap a token to see permitted actions, or place concise actions in a bottom sheet.
- Actions: `Complete`, `Skip`, `Cancel` with confirmation for destructive actions.
- Avoid showing actions that are invalid for the token's current status.

**Operational safeguards**
- Prevent simultaneous `Call next` requests from assigning the same token twice.
- Record staff action and timestamp.
- Refresh other admin and user clients when queue state changes.

### H. Admin — basic summary (optional after core workflow)

- Number of completed tokens today.
- Average completed service duration.
- Count waiting and count skipped.
- Text/list presentation is sufficient. Add charts only if there is a real analytical question to answer.

---

## 5. Low-fidelity layout references

These are structural sketches, not pixel-perfect mockups.

```text
USER HOME                   MY TOKEN
┌──────────────────────┐    ┌──────────────────────┐
│ Smart Queue          │    │ ← My token           │
│                      │    │                      │
│ Choose a service     │    │ Document Verification│
│                      │    │                      │
│ Your active token    │    │        A-018         │
│ A-018  · Waiting   › │    │       Waiting        │
│ ──────────────────── │    │                      │
│ General Enquiry    › │    │ People ahead      4 │
│ Fee Payment        › │    │ Estimated wait  ~20m │
│ Document Verify.   › │    │ ──────────────────── │
│ Registration       › │    │ Now serving   A-014  │
│                      │    │ Updated just now     │
│                      │    │                      │
│ Home  My token Profile│   │ Cancel token         │
└──────────────────────┘    └──────────────────────┘

SERVICE DETAILS             ADMIN QUEUE
┌──────────────────────┐    ┌──────────────────────┐
│ ← Fee Payment        │    │ ← Fee Payment        │
│                      │    │                      │
│ Fee Payment          │    │ Waiting          8   │
│ Pay college fees and │    │ Now serving   A-014  │
│ collect receipts.    │    │                      │
│                      │    │ [     Call next    ] │
│ Queue open           │    │                      │
│ People waiting     6 │    │ Queue                │
│ Est. wait     ~30 min│    │ A-015   Waiting    › │
│                      │    │ A-016   Waiting    › │
│ Times may change.    │    │ A-017   Waiting    › │
│                      │    │ A-018   Waiting    › │
│ [     Get token    ] │    │                      │
└──────────────────────┘    └──────────────────────┘
```

The tokens, counts and estimates above are **sample display data**, not results measured by the project.

---

## 6. Android implementation decisions

For a straightforward Android Studio project, use this **proposed implementation stack** (the PDF lists several alternatives, rather than mandating one):

| Area | Chosen implementation |
|---|---|
| Android app | Kotlin + XML layouts |
| UI components | Material Components for Android, styled conservatively |
| Architecture | MVVM with Repository layer and ViewModel |
| Navigation | Single Activity + Fragments + Navigation Component |
| Authentication | Firebase Authentication |
| Database and realtime sync | Cloud Firestore snapshot listeners |
| Backend privileged queue operations | Firebase Cloud Functions or another trusted backend |
| Async operations | Kotlin coroutines / Flow |
| Version control | Git + GitHub |

**Do not write admin updates directly from an unrestricted mobile client.** Assign tokens, advance queue state and record service history using secure transactional backend logic. Verify role authorization server-side. Use Firestore security rules to limit who can read personal token details and who can access admin data.

If your course requires **Java**, retain the same native Android screen and navigation design and substitute Java implementations. Do not mix Java/Kotlin/Flutter purely for appearance.

### Suggested package layout

```text
com.example.smartqueue/
├── MainActivity.kt
├── auth/
│   ├── LoginFragment.kt
│   └── RegisterFragment.kt
├── user/
│   ├── HomeFragment.kt
│   ├── ServiceDetailFragment.kt
│   ├── MyTokenFragment.kt
│   └── ProfileFragment.kt
├── admin/
│   ├── AdminQueuesFragment.kt
│   └── AdminQueueDetailFragment.kt
├── data/
│   ├── model/
│   ├── repository/
│   └── remote/
├── ui/
│   ├── adapters/
│   └── components/
└── util/
```

### Suggested data entities

Preserve the entities in the documentation:

- `User`: `user_id`, `name`, `email`, `role`.
- `Service`: `service_id`, `service_name`, `average_duration`, `status`.
- `Queue`: `queue_id`, `service_id`, `queue_date`, `queue_status`.
- `Token`: `token_id`, `user_id`, `queue_id`, `token_number`, `issue_time`, `status`.
- `Counter`: `counter_id`, `service_id`, `counter_status` (may be inactive in the single-counter MVP).
- `ServiceHistory`: `history_id`, `token_id`, `start_time`, `end_time`, `actual_duration`.

Add implementation fields only when needed: `updated_at`, `called_at`, `completed_at`, `staff_id`, etc. Use server-generated timestamps and stable document IDs. Use `queue_date` and a transactional sequence allocator so token numbers remain unambiguous per queue/day.

---

## 7. Queue rules and estimation

### Baseline estimate

The source documentation defines:

```text
estimated_wait_minutes = people_ahead × average_service_duration_minutes
```

Example: four people ahead at five minutes each = **about 20 minutes**.

For version 1, compute `people_ahead` from eligible tokens issued earlier in the same queue. Exclude completed, cancelled and skipped tokens. Take service duration from recently completed tokens when enough history exists; otherwise use the service's configured average. If current service is underway and its remaining time cannot be reliably known, keep the estimate approximate.

**Do not claim that the formula is accurate for multiple independent counters.** Multi-counter estimation requires workload allocation and counter availability; that is an enhancement.

### Token lifecycle

```text
WAITING → CALLED → SERVING → COMPLETED
   │         │         │
   ├─────────┴─────────┴──→ CANCELLED (when allowed)
   └─────────┬────────────→ SKIPPED (staff action)
             └─ (optional business rule: recall skipped token)
```

Define the permitted transitions on the backend. If `CALLED` and `SERVING` are combined for the MVP, choose one status consistently in data and UI.

### Live update behavior

- After a token is added or a staff action occurs, update all affected queue views.
- Recompute the estimate after completions, cancellations and changes to serving status.
- Show the last successful synchronization time.
- If network connectivity is unavailable, show previously loaded data as **possibly outdated** and prevent unsupported mutations; never pretend a token was issued or called.

---

## 8. UI states, accessibility and operational details

| Situation | Expected UI |
|---|---|
| Services loading | Small skeleton rows or progress indicator; no unrelated animation |
| No services available | “No services are available right now.” |
| Queue closed | “This queue is currently closed.”; disable `Get token` |
| No active token | “You haven't joined a queue yet.” + `Browse services` |
| Token request in progress | Disable duplicate submit; show progress within action |
| Token creation fails | Plain error message; allow retry without creating duplicate token |
| Token was called | Clear message with token number and where to report, if counter information exists |
| Token completed | “Service completed” and navigation home |
| Offline | Visible `Offline — queue information may be outdated` message |
| Session expired | Request sign-in again; preserve a safe return route |
| Admin queue empty | “No one is waiting.” and disabled `Call next` |

Accessibility requirements: adequate colour contrast, readable labels, accessible button descriptions, 48dp tap areas, support for larger text, and states that are not represented by colour alone.

Use concise confirmations: `Cancel token A-018?` with `Keep token` and `Cancel token`. Never cancel immediately when a user accidentally taps the action.

---

## 9. Development sequence

### Phase 1 — UI skeleton

- Create Android Studio application, theme tokens, navigation graph and common app bars.
- Implement Sign in, Create account, User home, Service detail, My token, Profile, Admin overview and Admin service queue.
- Preview with explicitly labeled sample data (not fake backend behavior).
- Verify screen transitions, keyboard behavior, accessibility and small-screen layout.

### Phase 2 — Real data and authentication

- Add Firebase Authentication and authorized user roles.
- Load services and their open/closed state from the database.
- Implement backend-generated token issuance with transactional duplicate protection.
- Connect `My token` and admin queue lists to realtime updates.

### Phase 3 — Queue operations and estimate

- Implement authorized `Call next`, `Complete`, `Skip`, `Cancel` actions.
- Record service history and update estimates after relevant changes.
- Handle offline access, error conditions and conflicting admin actions.

### Phase 4 — Validation and polish

- Test token uniqueness, queue ordering, state changes, access control and cancellation effects.
- Verify background/resume behavior and data synchronization.
- Check all text, spacing and empty states on an Android emulator and an actual device if available.
- Remove all hard-coded display sample values from production paths.

---

## 10. Acceptance criteria

A version 1 build is acceptable only when all these outcomes are verifiable:

1. A person can sign up, sign in and see available services.
2. Selecting an open service generates **one** valid token, even with repeated taps or concurrent requests.
3. The token screen displays its actual number, status, people ahead and a clearly labelled **estimated** wait.
4. Two signed-in devices observing the same queue receive changes after staff updates, subject to network availability.
5. Authorized staff can call the correct next eligible token and complete, skip or cancel tokens.
6. Cancelled/completed/skipped tokens no longer count as waiting ahead.
7. Unauthorized users cannot call tokens or access another person's private token data.
8. Offline/stale data is identified; errors are not silently treated as success.
9. The user can reach the main action in a few taps without navigating through decorative dashboards.
10. The interface remains visually restrained and legible across common Android screen sizes.

---

## 11. Handoff prompt for a coding assistant

> Build the **Smart Queue** Android application using this Markdown document as the UI and implementation specification. Start with native Kotlin + XML layouts, MVVM and Navigation Component. Prioritize the user home, service details, My token and admin queue screens. Follow the colour, typography, spacing, copy and state guidance closely. Make it look like a practical government/college/clinic queue utility, **not** a gradient-heavy AI-generated dashboard. Use mock repository data only for initial UI previews; clearly separate it from real authenticated backend operations. Do not fabricate working realtime updates, authentication, token issuance or prediction. Add those later using secured transactional backend operations and Firebase services. Keep code modular, conventional and easy for students to explain during a project demonstration.

## 12. Source coverage

The accompanying project PDF establishes the app's objectives, modules, two user roles, client-server structure, database entities, estimation formula, security requirements, testing cases and future enhancements. This brief translates those requirements into concrete UI choices. Its brand colours, exact layout, navigation pattern and chosen Kotlin/Firebase stack are **design and implementation recommendations**, not facts specified in the PDF.
