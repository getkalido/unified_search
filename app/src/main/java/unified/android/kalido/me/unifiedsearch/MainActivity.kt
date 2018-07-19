package unified.android.kalido.me.unifiedsearch

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import kotlinx.android.synthetic.main.search_section_one.toolbar
import kotlinx.android.synthetic.main.search_section_one.scroll
import kotlinx.android.synthetic.main.search_section_one.search_holder
import kotlinx.android.synthetic.main.search_section_one.text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        text.setOnClickListener {
            hideKeyboard()
            openScreen(it)
        }
    }

    fun openScreen(v: View) {
        val intent = Intent(this, Main2Activity::class.java)
        //intent.putExtra(Main2Activity.EXTRA_PARAM_ID, text.id)

        val statusBar = findViewById<View>(android.R.id.statusBarBackground)
        val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)

        val transitionViews = arrayListOf(
                Pair<View, String>(toolbar,
                        Main2Activity.SEARCH_BACKGROUND_COLOR),
                Pair<View, String>(search_holder,
                        Main2Activity.SEARCH_HOLDER),
                Pair<View, String>(scroll,
                        Main2Activity.SCROLL))


        if (statusBar != null) {
            transitionViews.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME))
        }
        if (navigationBar != null) {
            transitionViews.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME))
        }
        //pairs.add(Pair.create(mSharedElement, mSharedElement.getTransitionName()))

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                *transitionViews.toTypedArray())

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
        // END_INCLUDE(start_activity)
    }

    fun action1(v: View) {
        val intent = Intent(this, Main3Activity::class.java)

        val transitionViews = arrayListOf(
                Pair<View, String>(v,
                        Main3Activity.TOOLBAR))

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                *transitionViews.toTypedArray())

        ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
    }

    fun action2(v: View) {
        val intent = Intent(this, Main4Activity::class.java)

        val transitionViews = arrayListOf(
                Pair<View, String>(v,
                        Main4Activity.TOOLBAR))

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                *transitionViews.toTypedArray())

        ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
    }
}
