#!/usr/bin/env bash
# Writes androidApp/google-services.json for CI.
# - If the GOOGLE_SERVICES_JSON secret is set, use the real file (release-capable builds).
# - Otherwise copy the committed placeholder: enough for the google-services plugin to configure the
#   build, never valid for talking to Firebase. The real file is gitignored and must never be committed.
set -euo pipefail

target="androidApp/google-services.json"
if [[ -n "${GOOGLE_SERVICES_JSON:-}" ]]; then
  printf '%s' "$GOOGLE_SERVICES_JSON" > "$target"
  echo "Using google-services.json from secrets."
else
  cp androidApp/google-services.json.ci "$target"
  echo "Using placeholder google-services.json (build validation only)."
fi
