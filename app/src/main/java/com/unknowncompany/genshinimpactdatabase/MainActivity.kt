package com.unknowncompany.genshinimpactdatabase

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.unknowncompany.genshinimpactdatabase.character.CharacterFragment
import com.unknowncompany.genshinimpactdatabase.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    var onViewAttachedToWindowCallback: (() -> Unit)? = null
    var onViewDetachedFromWindowCallback: (() -> Unit)? = null

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.title = title
        loadNavHeaderImage()
        binding.navView.itemIconTintList = null
        setupDrawerLayout()
        binding.navView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, CharacterFragment())
                .commit()
            supportActionBar?.title = getString(R.string.characters_list)
        }
    }

    private fun loadNavHeaderImage() {
        val header = binding.navView.getHeaderView(0)
        val backgroundImage = header.findViewById(R.id.iv_background_image) as ImageView

        Glide.with(this)
            .load(R.drawable.side_nav_bar)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_error)
            .into(backgroundImage)
    }

    private fun setupDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_menu) {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchView = item.actionView as SearchView

            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = resources.getString(R.string.search_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!newText.isNullOrEmpty())
                        viewModel.nameQuery.value = newText
                    return true
                }

            })
            searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {
                    onViewAttachedToWindowCallback?.invoke()
                }

                override fun onViewDetachedFromWindow(v: View?) {
                    onViewDetachedFromWindowCallback?.invoke()
                }
            })

        }

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.nav_characters_list -> {
                fragment = CharacterFragment()
            }
            R.id.nav_favorite_characters -> {
                val uri = Uri.parse("genshinimpactdatabase://favoritecharacter")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}