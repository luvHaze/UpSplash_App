package com.zoey.unsplash_app.ui

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.model.Photo
import com.zoey.unsplash_app.recyclerview.PhotoGridRecyclerViewAdapter
import com.zoey.unsplash_app.utils.Constants
import kotlinx.android.synthetic.main.activity_photo_collection.*

class PhotoCollectionActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener,
    CompoundButton.OnCheckedChangeListener,
    View.OnClickListener {

    //데이터
    private var photoList = ArrayList<Photo>()

    //어댑터
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter

    //서치뷰
    private lateinit var mySearchView: SearchView

    // 서치뷰 에딧 텍스트
    private lateinit var mySearchViewEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_collection)

        // 이전 액티비티에서 검색결과 데이터와 검색어를 가져와주는 작업
        val bundle = intent.getBundleExtra("array_bundle")
        val searchTerm = intent.getStringExtra("search_term")
        photoList = bundle?.getSerializable("photo_array_list") as ArrayList<Photo>

        Log.d(
            Constants.TAG, "PhotoCollectionActivity - onCreate() called \n" +
                    "searchTerm: ${searchTerm}, photoList.count() : ${photoList.count()}"
        )

        search_history_mode_switch.setOnCheckedChangeListener(this)
        clear_search_history_button.setOnClickListener(this)
        top_app_bar.title = searchTerm

        setSupportActionBar(top_app_bar)

        this.photoGridRecyclerViewAdapter = PhotoGridRecyclerViewAdapter()
        photoGridRecyclerViewAdapter.submitjList(photoList)

        // spanCount = 몇줄로 나눌지
        my_photo_recycler_view.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        my_photo_recycler_view.adapter = photoGridRecyclerViewAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        Log.d(Constants.TAG, "PhotoCollectionActivity - onCreateOptionsMenu() called")

        val inflater = menuInflater
        inflater.inflate(R.menu.top_app_bar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        this.mySearchView = menu?.findItem(R.id.search_menu_item)?.actionView as SearchView

        mySearchView.apply {
            this.queryHint = "검색어를 입력해주세요"
            this.setOnQueryTextListener(this@PhotoCollectionActivity)
            this.setOnQueryTextFocusChangeListener { _, hasExpaned ->
                when (hasExpaned) {
                    true -> {
                        Log.d(Constants.TAG, "서치뷰 열림")
                        linear_search_history_view.visibility = View.VISIBLE
                    }
                    false -> {
                        Log.d(Constants.TAG, "서치뷰 닫힘")
                        linear_search_history_view.visibility = View.INVISIBLE
                    }
                }

            }


            // 서치뷰에서 에딧텍스트를 가져온다.
            mySearchViewEditText = this.findViewById(androidx.appcompat.R.id.search_src_text)
        }
        mySearchViewEditText.apply {
            this.filters = arrayOf(InputFilter.LengthFilter(12))
            this.setTextColor(Color.WHITE)
            this.setHintTextColor(Color.WHITE)

        }

        return true
    }

    //서치뷰 검색 입력 이벤트
    //검색버튼이 클릭되었을때
    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(Constants.TAG, "onQueryTextSubmit() called / query: $query")
        if (!query.isNullOrEmpty()) {
            this.top_app_bar.title = query
            //TODO : API 호출
            //TODO : 검색어 저장
        }

        this.top_app_bar.collapseActionView()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(Constants.TAG, "onQueryTextChange() called / query: $newText")

        //val userInputText = newText ?: ""
        val userInputText = newText.let {
            it
        } ?: ""

        if (userInputText.count() == 12) {
            Toast.makeText(this, "검색어는 12자까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onCheckedChanged(switch: CompoundButton?, isChecked: Boolean) {
        when (switch) {
            search_history_mode_switch -> {
                if (isChecked == true) {
                    Log.d(Constants.TAG, "검색어 저장기능 ON")
                } else {
                    Log.d(Constants.TAG, "검색어 저장기능 OFF")
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            clear_search_history_button -> {
                Log.d(Constants.TAG, "검색 기록 삭제 버튼이 클릭 되었습니다.")
            }
        }

    }

}