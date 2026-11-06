#!/usr/bin/env python3
"""Upload built APKs to a Telegram chat or forum topic.

Designed for CI: credentials come from the environment, never from argv, so they
do not leak into process listings or logs.

    env: API_ID, API_HASH, BOT_TOKEN, CHAT_ID, TOPIC_ID (optional)
    python scripts/telegram_send.py --dir out --build-sha abc1234 --branch main

Telethon uploads through the MTProto API with a bot token, which is what
API_ID/API_HASH are for: they identify the Telegram application the bot logs in
through. Bots are limited to ~50 MB per file, so a note is printed when an APK
is bigger than that.
"""

from __future__ import annotations

import argparse
import asyncio
import os
import sys
from pathlib import Path

# Above this size a bot upload is rejected by Telegram.
BOT_UPLOAD_LIMIT = 50 * 1024 * 1024

REQUIRED_ENV = ("API_ID", "API_HASH", "BOT_TOKEN", "CHAT_ID")


def read_required_env() -> dict[str, str]:
    values = {key: os.environ.get(key, "").strip() for key in REQUIRED_ENV}
    missing = [key for key, value in values.items() if not value]
    if missing:
        # Never print values, only the names that are absent.
        raise SystemExit(
            "Missing required environment variables: " + ", ".join(missing)
        )
    return values


def human_size(num_bytes: int) -> str:
    size = float(num_bytes)
    for unit in ("B", "KB", "MB", "GB"):
        if size < 1024 or unit == "GB":
            return f"{size:.0f} {unit}" if unit == "B" else f"{size:.1f} {unit}"
        size /= 1024
    return f"{size:.1f} GB"


def find_apks(directory: Path, explicit: list[str]) -> list[Path]:
    if explicit:
        files = [Path(name) for name in explicit]
    elif directory.is_dir():
        files = sorted(directory.glob("*.apk"), key=lambda p: p.name)
    else:
        files = []
    found = [f for f in files if f.is_file()]
    if not found:
        raise SystemExit(f"No APK files found in {directory}")
    return found


def describe(path: Path, sha: str, branch: str, run_url: str, extra: str) -> str:
    """Plain-text caption: parse_mode is off, so no escaping is needed."""
    flavor = "Debug"
    lower = path.name.lower()
    if "release" in lower:
        flavor = "Release (unsigned)"
    elif "debug" not in lower:
        flavor = path.stem

    lines = [
        f"📱 {extra or 'OpenStream'} — {flavor}",
        f"🔖 {sha} · 🌿 {branch}" if sha or branch else None,
        f"📦 {human_size(path.stat().st_size)}",
        f"🔗 {run_url}" if run_url else None,
    ]
    return "\n".join(line for line in lines if line)


async def upload(
    files: list[Path],
    captions: list[str],
    creds: dict[str, str],
    topic_id: str | None,
) -> None:
    from telethon import TelegramClient
    from telethon.sessions import StringSession

    client = TelegramClient(StringSession(), int(creds["API_ID"]), creds["API_HASH"])
    await client.start(bot_token=creds["BOT_TOKEN"])
    try:
        entity = await client.get_entity(creds["CHAT_ID"])
        for path, caption in zip(files, captions):
            kwargs = {"caption": caption, "force_document": True, "parse_mode": None}
            if topic_id:
                try:
                    await client.send_file(entity, str(path), reply_to=int(topic_id), **kwargs)
                    continue
                except (ValueError, TypeError):
                    # Older/newer Telethon naming for forum topics; fall through.
                    pass
                except Exception as exc:  # noqa: BLE001 - topic routing is best effort
                    print(
                        f"⚠️  TOPIC_ID={topic_id} rejected ({exc.__class__.__name__}); "
                        "sending to the chat instead.",
                        file=sys.stderr,
                    )
            await client.send_file(entity, str(path), **kwargs)
            print(f"✅ Sent {path.name}")
    finally:
        await client.disconnect()


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--dir", default="out", help="directory holding the APKs")
    parser.add_argument("--files", nargs="*", help="explicit APK paths; overrides --dir")
    parser.add_argument("--build-sha", default="", help="short commit sha for the caption")
    parser.add_argument("--branch", default="", help="branch name for the caption")
    parser.add_argument("--run-url", default="", help="link to the workflow run")
    parser.add_argument("--app-name", default="OpenStream", help="product name in captions")
    parser.add_argument(
        "--dry-run", action="store_true", help="resolve and describe what would be sent"
    )
    args = parser.parse_args(argv)

    files = find_apks(Path(args.dir), args.files)
    captions = [
        describe(p, args.build_sha, args.branch, args.run_url, args.app_name) for p in files
    ]

    for path, caption in zip(files, captions):
        print(f"→ {path} ({human_size(path.stat().st_size)})")
        print(caption)
        if path.stat().st_size > BOT_UPLOAD_LIMIT:
            print(
                "⚠️  Over the 50 MB bot upload limit — this send will fail. "
                "Use a user session or split the build.",
                file=sys.stderr,
            )

    if args.dry_run:
        print("Dry run: nothing uploaded.")
        return 0

    creds = read_required_env()
    topic_id = os.environ.get("TOPIC_ID", "").strip() or None
    asyncio.run(upload(files, captions, creds, topic_id))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
