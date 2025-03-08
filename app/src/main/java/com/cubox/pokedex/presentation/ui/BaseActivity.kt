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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> VB
) : AppCompatActivity() {

    protected val binding: VB by lazy {
        bindingFactory(layoutInflater)
    }

    protected val disposables = CompositeDisposable()

    private val _error: PublishSubject<String> = PublishSubject.create()
    val error : Observable<String>
        get() = _error.hide()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        initViews()
        subscribeView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
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

    protected fun processError(throwable: Throwable) {
        _error.onNext(throwable.message ?: "An error occurred")
    }
}