package ru.xmn.filmfilmfilm.common.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.zhuinden.servicetree.ServiceTree
import ru.xmn.filmfilmfilm.application.App
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseActivity<Component : Any> : AppCompatActivity() {

    private val serviceTree: ServiceTree by lazy { App.component.serviceTree() }

    val componentInit: Component by lazy {
        if (!serviceTree.hasNodeWithKey(getTag())) {
            val node = serviceTree.createChildNode(serviceTree.treeRoot, getTag())
            val component = createComponent()
            node.bindService(App.DAGGER_COMPONENT, component)
            component
        } else {
            serviceTree.getNode(getTag()).getService<Component>(App.DAGGER_COMPONENT)
        }
    }


    private var activeTags: List<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        supportFragmentManager.addOnBackStackChangedListener {
            val newTags = collectActiveTags()
            activeTags
                    .filter { !newTags.contains(it) }
                    .forEach {
                        Log.d(getTag(), "Destroying [$it]")
                        serviceTree.removeNodeAndChildren(serviceTree.getNode(it))
                    }
            activeTags = newTags
        }

        super.onCreate(savedInstanceState)

        if (activeTags.isEmpty() && supportFragmentManager.fragments != null) { // handle process death
            activeTags = collectActiveTags()
        }
    }

    private fun collectActiveTags(): List<String> {
        val fragments: List<Fragment> = supportFragmentManager.fragments ?: emptyList<Fragment>()

        val newTags = LinkedList<String>()
        for (fragment in fragments) {
            if (fragment != null && fragment is HasServices) { // active fragments is a list that can have NULL element
                val serviceFragment = fragment
                val newTag = serviceFragment.getNodeTag()
                newTags.add(newTag)
            }
        }
        return newTags
    }

    fun registerFragmentServices(fragment: Fragment?) {
        if (fragment != null && fragment is HasServices) {
            val serviceFragment = fragment
            val newTag = serviceFragment.getNodeTag()
            if (!serviceTree.hasNodeWithKey(newTag)) {
                serviceFragment.bindServices(serviceTree.createChildNode(serviceTree.getNode(getTag()), newTag))
            }
        }
    }

    fun getComponent(): Component = componentInit

    abstract fun createComponent(): Component

    abstract fun getTag(): String
}

interface HasServices {
    fun getNodeTag(): String

    fun bindServices(node: ServiceTree.Node)
}
