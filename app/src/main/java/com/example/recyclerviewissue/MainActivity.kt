package com.example.recyclerviewissue

import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Property
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.right, Page1())
                .setReorderingAllowed(true)
                .commit()
        }
    }

    fun navigate() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.right, Page2())
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
        val parent = findViewById<ViewGroup>(R.id.root)
        val transition = ChangeBounds().apply {
            // only apply to top-level views instead of recursively
            parent.forEach { addTarget(it) }
        }
        TransitionManager.beginDelayedTransition(parent, transition)
        findViewById<View>(R.id.left).apply {
            layoutParams.width = 0
            requestLayout()
        }
        for (i in 0 until 200) {
            val overlayDrawable = ColorDrawable(0)
            ObjectAnimator.ofInt(overlayDrawable, DRAWABLE_ALPHA, 255, 0)
                .setDuration(1000).start()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        findViewById<View>(R.id.left).apply {
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.left_width)
            requestLayout()
        }
    }
}
private val DRAWABLE_ALPHA = object : Property<Drawable, Int>(Int::class.java, "alpha") {
    override fun get(target: Drawable): Int = target.alpha

    override fun set(target: Drawable, value: Int) {
        target.alpha = value
    }
}

class Page1 : Fragment(R.layout.page_1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.button).setOnClickListener {
            (requireActivity() as MainActivity).navigate()
        }
    }
}

class Page2 : Fragment(R.layout.page_2) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = view.findViewById<RecyclerView>(R.id.list)
        val adapter = MainAdapter()
        adapter.submitList(listOf("A", "B", "C"))
        list.adapter = adapter
        Handler.createAsync(Looper.getMainLooper()).postDelayed({
            adapter.submitList(listOf("A", "B", "D", "C"))
        }, 100)
    }
}

class MainAdapter : ListAdapter<String, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        ) {
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as TextView).text = "[Item ${getItem(position)}]"
    }
}

