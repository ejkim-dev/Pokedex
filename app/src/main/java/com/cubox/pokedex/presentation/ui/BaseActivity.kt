package com.cubox.pokedex.presentation.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> VB
) : AppCompatActivity() {

    protected val binding: VB by lazy {
        bindingFactory(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        initViews()
        subscribeView()
    }

    open fun initViews() {}
    open fun subscribeView() {}

    open fun setLayoutMargin() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val currentInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<MarginLayoutParams> {
                topMargin = currentInsets.top
                bottomMargin = currentInsets.bottom
                leftMargin = currentInsets.left
                rightMargin = currentInsets.right
            }
            insets
        }
    }

    protected fun getDrawableId(resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)
}