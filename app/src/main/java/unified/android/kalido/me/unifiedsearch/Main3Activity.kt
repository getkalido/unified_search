package unified.android.kalido.me.unifiedsearch

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_main3.toolbar

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        ViewCompat.setTransitionName(toolbar, TOOLBAR);

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.close)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu).apply {
            menuInflater.inflate(R.menu.reset, menu)
        }
    }

    companion object {
        const val TOOLBAR = "toolbar"
    }
}
