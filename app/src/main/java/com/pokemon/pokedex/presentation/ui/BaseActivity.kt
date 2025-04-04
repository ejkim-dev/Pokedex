package com.pokemon.pokedex.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.pokemon.pokedex.presentation.showToast
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> VB,
    private val needApplyWindowInsets: Boolean = true
) : AppCompatActivity() {

    protected val binding: VB by lazy {
        bindingFactory(layoutInflater)
    }

    protected val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        if (needApplyWindowInsets) {
            setLayoutMargin()
        }
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    abstract fun initView()

    protected fun processError(message: String) {
        lifecycleScope.launch {
            showToast(message)
        }
    }

    protected fun processError(throwable: Throwable) {
        processError(throwable.message ?: "An error occurred")
    }

    private fun setLayoutMargin() {
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
}