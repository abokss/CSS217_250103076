# CSS217 — Applied Design Patterns Lab

Java SE 8+ solutions for the five tasks in `DesignPatternsAppliedLabAssignmentv2.pdf`. Uses only the JDK standard library. No Maven, Gradle, or external dependencies.

## Run

On macOS/Linux with a JDK installed:

```sh
./verify.sh
```

This compiles every source, runs 24 behavioral checks, then runs all five original verification programs and compares their output with `expected/`. Only the measured cache age is normalized.

After compilation, run an individual example:

```sh
java -Duser.language=en -Duser.country=US -cp build task1.Task1Runner
```

Replace `task1.Task1Runner` with `task2.Task2Runner` through `task5.Task5Runner`. The language options preserve the decimal points used in the assignment regardless of the computer's regional settings.

## Structure and patterns

| Package | System | Patterns |
| --- | --- | --- |
| `task1` | JSON/CSV report export with metadata and Base64 wrapping | Strategy + Decorator |
| `task2` | Legacy stock feed with per-symbol TTL price cache | Adapter + Proxy |
| `task3` | Email/SMS alerts with business-hours policies | Bridge + Strategy |
| `task4` | GBP-to-USD order pricing and stacked surcharges | Adapter + Decorator |
| `task5` | S3/NFS media access with a role-checking gateway | Bridge + Proxy |

Each public type has its own source file. Runner bodies are retained from the handout, with package declarations added. The implementation builds on the supplied skeleton. Simulated exports, notifications, and cloud access only print to the console.

## Assignment inconsistencies and decisions

- **Task 1:** The supplied runner constructs `Base64(HeaderFooter(BaseFileExporter))`. With normal forwarding decorators, Base64 transforms the report first, then HeaderFooter adds plaintext metadata. The handout's expected output instead encodes the metadata too, which requires the reverse composition. The runner and standard decorator behavior are preserved; `expected/task1.txt` records the consistent result (326 UTF-8 bytes). The handout's 236-byte count does not match its displayed payload either. Reverse composition is covered by a separate test.
- **Task 1:** Metadata strings follow the supplied skeleton; the prose specifies different strings and a row count that the supplied constructor does not accept. Base64 is encoding, not encryption. The `SHA-256 OK` text is the handout's simulated label, not a computed or verified hash.
- **Task 2:** Cache age is measured, never hard-coded to the example's 2 ms. TTL begins after a successful upstream response, uses a monotonic clock, and expires exactly at the boundary. A clock can be injected for deterministic tests. Symbols are normalized and cache access is synchronized. As in the skeleton, `getStatus` delegates directly; repeated price requests are limited by the TTL cache, with no separate request quota specified.
- **Task 3:** Method names and console messages follow the supplied runner/skeleton (`processAlert`), which differ from the prose (`send`). Business hours include both 09:00 and 17:00; severity 5 or higher bypasses the policy.
- **Task 4:** Customs duties use decimal HALF_UP rounding so 145 × 1.085 becomes 157.33. The legacy converter retains the handout's fixed GBP/USD rate and pass-through fallback for other pairs; it is not a live exchange-rate service.
- **Task 5:** `getStorageType` follows the runner/skeleton rather than the prose's `getStorageId`. ADMIN and MEMBER (prose), plus ENGINEER (skeleton), are allowed. GUEST, unknown/null roles, and blank identities are blocked before the backend is called. UserSession is a simulated trusted context; no token verification mechanism is provided in the handout.

## Verification coverage

Tests cover JSON/CSV escaping, UTF-8 Base64 and decorator order, cold/hot/expired cache behavior, exact TTL boundaries and symbol separation, legacy packet parsing, business-hour boundaries and emergency bypass, suppression before transmission, currency conversion, surcharge ordering and rounding, and denial before storage access.

Verified with JDK 24 using `javac --release 8` to check Java 8 API and bytecode compatibility. All 24 behavioral checks and all five runner output comparisons pass. A Java 8 runtime itself was not used.
