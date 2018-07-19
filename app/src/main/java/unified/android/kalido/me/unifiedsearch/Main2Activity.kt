package unified.android.kalido.me.unifiedsearch

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_main2.search_background_color
import kotlinx.android.synthetic.main.search_section_two.scroll
import kotlinx.android.synthetic.main.search_section_two.search_holder
import kotlinx.android.synthetic.main.search_section_two.text
import unified.android.kalido.me.unifiedsearch.database.model.AppDatabase
import unified.android.kalido.me.unifiedsearch.database.model.RecentSearch

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        ViewCompat.setTransitionName(search_holder, SEARCH_HOLDER);
        ViewCompat.setTransitionName(scroll, SCROLL);
        ViewCompat.setTransitionName(search_background_color, SEARCH_BACKGROUND_COLOR);

        text.setOnEditorActionListener { textView, actionId, event ->

            when (event.action) {
                KeyEvent.KEYCODE_ENTER -> {
                    if (textView.text.isNullOrEmpty())
                        AppDatabase.getAppDatabase(this).recentSearchDao().insertAll(RecentSearch().apply {
                            id = AppDatabase.getAppDatabase(this@Main2Activity).recentSearchDao().countUsers()
                            searchText = textView.text.toString()
                        })
                }
            }
            return@setOnEditorActionListener true
        }
    }

    override fun onStart() {
        super.onStart()
        text.setOnTouchListener(OnTouchListener { view, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event?.action == MotionEvent.ACTION_UP) {
                when (event.rawX) {
                    in text.left..text.compoundDrawables[DRAWABLE_LEFT].bounds.right -> {
                        this.onBackPressed()
                    }
                }
                return@OnTouchListener true
            }
            false
        })

        if (text.requestFocus())
            text.postDelayed({
                this@Main2Activity.showKeyboard()
            }, 1000)
    }

    companion object {
        const val SEARCH_HOLDER = "search_holder"
        const val SCROLL = "scroll"
        const val SEARCH_BACKGROUND_COLOR = "search_background_color"
    }
}
