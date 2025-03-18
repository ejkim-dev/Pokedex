package com.pokemon.pokedex.presentation

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import coil.load
import com.pokemon.pokedex.R

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun FragmentActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.setImageUrl(url: String?, placeholderRes: Int = R.drawable.ic_launcher_foreground, errorRes: Int = R.drawable.ic_launcher_background) {
    if (url.isNullOrBlank()) {
        // URL이 비어 있으면 플레이스홀더 표시
        setImageResource(placeholderRes)
    } else {
        // Coil의 load 함수를 통해 URL 로드
        load(url) {
            crossfade(true)             // 페이드 애니메이션
            placeholder(placeholderRes) // 로딩 중 표시할 리소스
            error(errorRes)             // 실패 시 표시할 리소스
        }
    }
}