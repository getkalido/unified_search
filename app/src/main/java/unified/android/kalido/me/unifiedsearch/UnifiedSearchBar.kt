package unified.android.kalido.me.unifiedsearch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class UnifiedSearchBar : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.UnifiedSearchBar,
                0, 0)

        try {
            when (a.getBoolean(R.styleable.UnifiedSearchBar_expanded, true)) {
                true -> {
                    (View.inflate(context, R.layout.app_toolbar_search_expanded, this))
                }
                false -> {
                    (View.inflate(context, R.layout.app_toolbar_search_collapsed, this))
                }
            }
        } catch (ex: Throwable) {
            Log.e("TAG", "", ex)
        } finally {
            a.recycle();
        }
    }
}
