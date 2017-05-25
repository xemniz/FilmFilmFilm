package ru.xmn.filmfilmfilm.screens.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.application.App
import ru.xmn.filmfilmfilm.common.ui.BaseActivity
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp.FilmsFragment
import ru.xmn.filmfilmfilm.screens.main.onedayfilms.mvp.OneDayFilmsFragment

class MainActivity : BaseActivity<MainActivityComponent>() {
    override fun createComponent(): MainActivityComponent = App.component.plus(MainActivityModule())

    override fun getTag(): String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        setContentView(R.layout.activity_main)
        changeFragment(FilmsFragment.withDaysOffset(0))
    }

    fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction();
        if (cleanStack) {
            clearBackStack();
        }
        ft.setCustomAnimations(
                R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit);
        ft.replace(R.id.container, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    fun clearBackStack() {
        val manager = supportFragmentManager;
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager;
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }
}
