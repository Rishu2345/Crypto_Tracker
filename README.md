# Crypto Tracker

A native Android app for tracking cryptocurrency prices, built with Kotlin and Jetpack Compose using data from the [CoinGecko API](https://www.coingecko.com/en/api).

---

## Features

- Searchable list of coins with live price and 24h change
- Instant local autocomplete + debounced server-side search
- Infinite scroll pagination with pull-to-refresh
- Coin detail screen — market cap, 24h high/low, % change, last updated, and a price history chart
- Loading, error (with retry), and empty states throughout
- Offline caching via Room — previously loaded pages remain viewable without a connection

---

## Tech Stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM + Repository |
| Async | Kotlin Coroutines, StateFlow |
| Networking | Retrofit, OkHttp, kotlinx.serialization |
| Local cache | Room |
| DI | Koin |
| Image loading | Coil |
| Navigation | Navigation Compose |
| Testing | JUnit, MockK, Turbine, kotlinx-coroutines-test |

---

## Architecture

```
presentation/     Compose UI + ViewModels (CoinListViewModel, CoinDetailViewModel)
domain/           Plain Kotlin models + repository interface
data/
  remote/         Retrofit API, DTOs, DTO -> domain mappers
  local/          Room entities, DAO
  repository/     CoinRepositoryImpl — network-first, Room cache fallback
```

Each screen exposes a sealed `UiState` (`Loading` / `Success` / `Error` / `Empty`) via `StateFlow`, collected by Compose with `collectAsStateWithLifecycle()`.

The repository is **network-first**: successful responses are cached to Room, and failures fall back to whatever's cached for that exact data. Offline pagination works via rank-ordered `LIMIT`/`OFFSET` queries against the cache.

---

## Setup

1. Get a free CoinGecko Demo API key from the [CoinGecko Developer Dashboard](https://www.coingecko.com/en/developers/dashboard).
2. Create a `local.properties` file in the project root (if it doesn't already exist) and add:
   ```
   API_KEY=your_coingecko_demo_api_key_here
   ```
3. Open the project in Android Studio, let Gradle sync, then run on an emulator or device (minSdk 27).

> `local.properties` is gitignored — the key is never committed.

---

## Running Tests

```
./gradlew test
```

Unit tests cover DTO-to-domain mapping, repository network/cache fallback behavior, and ViewModel logic (pagination, debounced search, retry).

---

## Assumptions

- USD is the only supported display currency.
- CoinGecko's free Demo tier rate limits (100 requests/min) are sufficient for normal use; the app doesn't implement custom rate-limit backoff beyond standard error/retry handling.

## Known Limitations

- No user accounts, watchlists, or portfolio tracking.
- Price chart uses CoinGecko's `market_chart` endpoint at a fixed default range.
- No multi-currency support.

---

## Screenshots

| Coin List | Coin Detail |
|---|---|
| <img width="360" alt="crypto_list" src="https://github.com/user-attachments/assets/4228a111-21de-43be-9523-78e38e8ba881" /> | <img width="360" alt="detailsScreen" src="https://github.com/user-attachments/assets/62d049c1-192f-4cef-8ba1-0541c3191194" /> |

| Loading State | Search & Autocomplete |
|---|---|
| <img width="360" alt="loading_list" src="https://github.com/user-attachments/assets/52e2f280-370d-42ba-b7a3-fa84b8a0abd6" /> | <img width="360" alt="search" src="https://github.com/user-attachments/assets/1fbe1a2b-8683-4ea9-9da9-14669c727205" /> |

| Error State | Coin Detail — Price Chart |
|---|---|
| <img width="360" alt="Error_Screen" src="https://github.com/user-attachments/assets/122ba4d5-4963-4ed9-a7c7-cdada425ab28" /> | <img width="360" alt="detailScreen_2" src="https://github.com/user-attachments/assets/3893cd4a-c114-40f1-be19-11cdc2795da2" /> |
