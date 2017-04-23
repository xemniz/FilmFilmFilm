package ru.xmn.filmfilmfilm.screens.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.xmn.filmfilmfilm.R
import ru.xmn.filmfilmfilm.application.di.ApplicationComponent
import ru.xmn.filmfilmfilm.common.ui.BaseActivity

class MainActivity : BaseActivity() {
    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.plus(MainActivityModule())
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
