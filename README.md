<div align="center">
  <a href="https://github.com/synapseSRC/OpenStream">
    <img src="https://raw.githubusercontent.com/ainulbedjo/owl/refs/heads/master/app/src/main/res/drawable/play_store_512.png" width="100" height="100" alt="OpenStream icon">
  </a>
  <h1>OpenStream</h1>
  <p>An Android app for browsing movies from TMDB, built with Jetpack Compose and Material 3 Expressive.</p>
  <p><sub>Developed by <strong>StudioAs Inc.</strong> · Lead developer <a href="https://github.com/TheRealAshik">@TheRealAshik</a></sub></p>
</div>

<table>
<tr>
<td>
<img src="screenshot/home.png" alt="Home screenshot" width="100%" />
</td>
<td>
<img src="screenshot/detail.png" alt="Detail screenshot" width="100%" />
</td>
<td>
<img src="screenshot/favorites.png" alt="Favorites screenshot" width="100%" />
</td>
</tr>
<tr>
<td>
<img src="screenshot/theme.png" alt="Theme screenshot" width="100%" />
</td>
<td>
<img src="screenshot/offline.png" alt="Offline screenshot" width="100%" />
</td>
<td>
<img src="screenshot/chucker.png" alt="Chucker screenshot" width="100%" />
</td>
</tr>
</table>

### Features:
- **Home**: Popular, Top Rated, and Now Playing. Each list loads, fails, and retries on its own.
- **Detail**: backdrop, facts, overview, and reviews. Save the movie to favorites or share it from a bottom sheet.
- **Favorites**: your saved movies, newest first. Open or remove one, and every screen updates live.
- **Appearance**: light, dark, or system mode and a choice of color palettes, saved between launches.
- **Offline**: only a lost connection opens a "No connection" sheet. Other errors stay in the section that failed.

## Build

```bash
# TMDB credentials go in local.properties (gitignored) — never committed.
echo "access_token=<TMDB read access token>" >> local.properties
echo "api_key=<TMDB v3 api key>" >> local.properties

./gradlew assembleDebug          # app/build/outputs/apk/debug/
./gradlew assembleRelease        # minified + shrunk, unsigned
./gradlew test                   # unit tests
```

Application id and namespace are `app.openstream`; the module list is in [`settings.gradle.kts`](settings.gradle.kts). Versions live in [`Deps.kt`](buildSrc/src/main/kotlin/Deps.kt).

## CI: build the APK and post it to Telegram

[` .github/workflows/android.yml `](.github/workflows/android.yml) builds both debug and release APKs on every push and pull request, uploads them as workflow artifacts, and sends them to a Telegram chat or forum topic with [`scripts/telegram_send.py`](scripts/telegram_send.py).

Repo **Settings → Secrets and variables → Actions** needs:

| Secret | Used for |
|---|---|
| `TMDB_ACCESS_TOKEN` | written into `local.properties` so the build has a working API token |
| `TMDB_API_KEY` | same, for the v3 key |
| `TELEGRAM_API_ID` | Telegram api_id from [my.telegram.org](https://my.telegram.org/) |
| `TELEGRAM_API_HASH` | Telegram api_hash |
| `TELEGRAM_BOT_TOKEN` | bot token from @BotFather |
| `TELEGRAM_CHAT_ID` | target chat, group, or channel id |
| `TELEGRAM_TOPIC_ID` | forum topic id; leave unset to post in General |

Uploads need the bot to be a member/admin of the chat, and topics require forum mode to be enabled.

## Libraries

| Area | Library |
|---|---|
| UI | Jetpack Compose, Material 3 Expressive |
| Navigation | Navigation Compose (type-safe routes) |
| Architecture | Lifecycle ViewModel, StateFlow, Kotlin coroutines and Flow |
| DI | Hilt, with KSP |
| Network | Retrofit, OkHttp, kotlinx.serialization |
| Images | Coil 3 |
| Local data | Room (favorites and theme) |
| Debugging | Chucker (debug builds only) |
| Tests | JUnit 4, kotlinx-coroutines-test |

Why each one was chosen is in [docs/libraries.md](docs/libraries.md). Versions are in [`Deps.kt`](buildSrc/src/main/kotlin/Deps.kt).

## Architecture

MVVM with one-way data flow: the UI draws a `UiState`, a ViewModel owns it as a `StateFlow`, and repositories return `AppResult` values instead of throwing.

## Studio

OpenStream is maintained by **StudioAs Inc.**, with **@TheRealAshik** as lead developer. Planning notes and per-area decisions are in [docs/](docs/README.md).

## Credits and license

OpenStream is a rebranded build of the original **MovieDB** app by Ainul ([ainulbedjo/owl](https://github.com/ainulbedjo/owl)). That upstream project is MIT licensed and its copyright and permission notice are retained in [LICENSE](LICENSE), as the license requires; StudioAs Inc. holds the copyright on the OpenStream modifications.

## Resources
- [Material 3 Expressive](https://m3.material.io/blog/building-with-m3-expressive) | M3 Expressive design system
- [Moviebase.app](https://moviebase.app/) | Direct inspiration for research purposes
- [ip-as-logo-skill](https://github.com/s1dashu/ip-as-logo-skill) | Skill to create the logo used.
- [Icon kitchen](https://icon.kitchen/) | To create a ```icon/ic_launcher``` resources used in the codebase.
- [themoviedb.org](https://www.themoviedb.org/) | API and data seeding.
