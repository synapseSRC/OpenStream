package app.openstream.detail.ui

import app.openstream.core.common.AppError
import app.openstream.detail.R

/** The words for an inline error. `Network` shows the generic text once the offline sheet is closed. */
internal fun AppError.messageRes(): Int = when (this) {
    AppError.RateLimited -> R.string.detail_error_rate_limited
    else -> R.string.detail_error_generic
}
