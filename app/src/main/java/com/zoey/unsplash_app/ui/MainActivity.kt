package com.zoey.unsplash_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.retrofit.RetrofitManager
import com.zoey.unsplash_app.utils.Constants
import com.zoey.unsplash_app.utils.RESPONSE_STATE
import com.zoey.unsplash_app.utils.SEARCH_TYPE
import com.zoey.unsplash_app.utils.onMyTextChanged
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_button_search.*

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
        // 텍스트가 변경이 되었을 때
        search_term_edit_text.onMyTextChanged {
            // 입력된 글자가 하나라도 있다면
            if(it.toString().count() > 0) {
                // 검색버튼을 보여준다.
                frame_search_btn.visibility = View.VISIBLE
                search_term_text_layout.helperText = " "
                // 스크롤뷰를 올린다.
                mainScrollView.scrollTo(0,200)
            } else {
                frame_search_btn.visibility = View.INVISIBLE
                search_term_text_layout.helperText = "검색어를 입력해주세"
            }

            if(it.toString().count() == 12) {
                Log.d(Constants.TAG, "MainActivity - 에러 띄우기 ")
                Toast.makeText(this, "검색어는 12자 까지만 입력 가능합니다.", Toast.LENGTH_LONG).show()
            }
        }
        // 검색버튼 클릭 시
        btn_search.setOnClickListener {
            Log.d(Constants.TAG, "MainActivity - 검색 버튼이 클릭되었다. / currentSearchType : $currentSearType")

            val userSearchInput = search_term_edit_text.text

            RetrofitManager.instance.searchPhotos(searchTerm = search_term_edit_text.text.toString(), completion = {
                responseState, responseDataArrayList ->

                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d(Constants.TAG, "api 호출 성공 : ${responseDataArrayList?.size} ")
                        val intent = Intent(this, PhotoCollectionActivity::class.java)
                        val bundle = Bundle()
                        // 그냥 넣으면 안돼고 직렬화를 해 준다음에 넣어줘야한다.
                        bundle.putSerializable("photo_array_list", responseDataArrayList)
                        intent.putExtra("array_bundle", bundle)
                        intent.putExtra("search_term", userSearchInput)

                        startActivity(intent)
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(this, "API 호출 에러입니다.", Toast.LENGTH_SHORT).show()
                        Log.d(Constants.TAG, "api 호출 실패 : $responseDataArrayList ")
                    }
                }
            })
            handleSearchButtonUI()
        }
    }

    private fun handleSearchButtonUI() {
        btn_progress.visibility = View.VISIBLE
        btn_search.text =""

        Handler().postDelayed({
            btn_progress.visibility = View.INVISIBLE
            btn_search.text = "검색"
        }, 1500)
    }
}