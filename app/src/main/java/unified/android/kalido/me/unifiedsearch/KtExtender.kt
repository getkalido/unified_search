package unified.android.kalido.me.unifiedsearch

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(): Boolean {
    val view = currentFocus
    view?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }
    return false
}

fun Activity.showKeyboard(): Boolean {
    val view = currentFocus
    view?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        return inputMethodManager.showSoftInput(it,
                InputMethodManager.SHOW_IMPLICIT)
    }
    return false
}