package com.zoey.unsplash_app

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.zoey.unsplash_app.utils.Constants
import com.zoey.unsplash_app.utils.SEARCH_TYPE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentSearType: SEARCH_TYPE = SEARCH_TYPE.PHOTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(Constants.TAG, "MainActivity - onCreate() Called")

        //라디오 그룹 가져오기
        search_term_radio_group.setOnCheckedChangeListener { _, checkedID ->
            when (checkedID) {
                R.id.photo_search_radio_btn -> {
                    Log.d(Constants.TAG, "사진검색 버튼클릭")
                    search_term_text_layout.hint = "사진검색"
                    search_term_text_layout.startIconDrawable =
                        resources.getDrawable(R.drawable.ic_photo, resources.newTheme())
                    this.currentSearType = SEARCH_TYPE.PHOTO
                }
                R.id.user_search_radio_btn -> {
                    Log.d(Constants.TAG, "사용자검색 버튼클릭")
                    search_term_text_layout.hint = "사용자검색"
                    search_term_text_layout.startIconDrawable =
                        resources.getDrawable(R.drawable.ic_user, resources.newTheme())
                    this.currentSearType = SEARCH_TYPE.USER
                }
            }
            Log.d(
                Constants.TAG,
                "MainAcitivity - OnCheckedChanged() called / currentSearchType :  $currentSearType"
            )
        }

        search_term_edit_text.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

        })
    }
}