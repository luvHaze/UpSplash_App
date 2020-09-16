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
import androidx.recyclerview.widget.LinearLayoutManager
import com.zoey.unsplash_app.R
import com.zoey.unsplash_app.model.Photo
import com.zoey.unsplash_app.model.SearchData
import com.zoey.unsplash_app.recyclerview.ISearchHistoryRecyclerView
import com.zoey.unsplash_app.recyclerview.PhotoGridRecyclerViewAdapter
import com.zoey.unsplash_app.recyclerview.SearchHistoryRecyclerViewAdapter
import com.zoey.unsplash_app.retrofit.RetrofitInterface
import com.zoey.unsplash_app.retrofit.RetrofitManager
import com.zoey.unsplash_app.utils.Constants
import com.zoey.unsplash_app.utils.RESPONSE_STATUS
import com.zoey.unsplash_app.utils.SharedPrefManager
import com.zoey.unsplash_app.utils.toSimpleString
import kotlinx.android.synthetic.main.activity_photo_collection.*
import java.util.*
import kotlin.collections.ArrayList

class PhotoCollectionActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener,
    CompoundButton.OnCheckedChangeListener,
    View.OnClickListener,
    ISearchHistoryRecyclerView {

    //데이터
    private var photoList = ArrayList<Photo>()

    // 검색기록 배열
    private var searchHistoryList = ArrayList<SearchData>()

    //어댑터
    private lateinit var photoGridRecyclerViewAdapter: PhotoGridRecyclerViewAdapter
    private lateinit var mySearchHistoryRecyclerViewAdapter: SearchHistoryRecyclerViewAdapter

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
        search_history_mode_switch.isChecked = SharedPrefManager.checkSearchHistoryMode()
        top_app_bar.title = searchTerm

        // 액티비티에서 어떤 액션바를 사용할지 결정한다.
        setSupportActionBar(top_app_bar)

        // 사진 리사이클러뷰 세팅
        myPhotoRecyclerViewSetting(photoList)

        if(searchTerm!!.isNotEmpty()) {
            val term = searchTerm.let {
                it
            } ?: ""
            this.insertSearchTermHistory(term)
        }

        // 저장된 검색기록 가져오기
        this.searchHistoryList = SharedPrefManager.getSearchHistoryList() as ArrayList<SearchData>
        this.searchHistoryList.forEach {
            Log.d(Constants.TAG, "저장된 검색기록 - it : ${it.term} , ${it.timeStamp}")
        }
        handleSearchViewUI()

        // 검색 기록 리사이클러 뷰 준비
        searchHistoryRecyclerViewSetting(searchHistoryList)

    }

    // 검색기록 리사이클러뷰 준비
    private fun searchHistoryRecyclerViewSetting(searchHistoryList: ArrayList<SearchData>) {
        Log.d(Constants.TAG, "PhotoCollectionActivity - searchHistoryRecyclerViewSetting()called")

        mySearchHistoryRecyclerViewAdapter = SearchHistoryRecyclerViewAdapter(this)
        mySearchHistoryRecyclerViewAdapter.submitList(searchHistoryList)

        val myLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        myLinearLayoutManager.stackFromEnd = true // 가장 최근 검색어가 위에 쌓이게 된다. (보통은 아래에 쌓임)

        search_history_recyclerview.apply {
            layoutManager = myLinearLayoutManager
            adapter = mySearchHistoryRecyclerViewAdapter
            scrollToPosition(mySearchHistoryRecyclerViewAdapter.itemCount - 1) //리사이클러의 아이템이 자동적으로 맨 위로 오게끔 한다.
        }

    }

    // 사진 리사이클러뷰 세팅
    private fun myPhotoRecyclerViewSetting(_photolist: ArrayList<Photo>) {
        this.photoGridRecyclerViewAdapter = PhotoGridRecyclerViewAdapter()
        photoGridRecyclerViewAdapter.sumbitList(_photolist)

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
                        handleSearchViewUI()
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
            this.insertSearchTermHistory(query)
            this.searchPhotoApiCall(query)
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
                    SharedPrefManager.setSearchHistoryMode(isActivated = true)
                } else {
                    Log.d(Constants.TAG, "검색어 저장기능 OFF")
                    SharedPrefManager.setSearchHistoryMode(isActivated = false)
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            clear_search_history_button -> {
                Log.d(Constants.TAG, "검색 기록 삭제 버튼이 클릭 되었습니다.")
                this.searchHistoryList.clear()
                handleSearchViewUI()
            }
        }

    }

    // 검색 아이테 삭제 버튼 이벤트
    override fun onSearchItemDeleteClicked(position: Int) {
        Log.d(Constants.TAG, "PhotoCollectionActivity - onSearchItemDeleteClicked() called")

        // 해당 번째의 녀석을 삭제
        this.searchHistoryList.removeAt(position)

        //데이터 덮어쓰기
        SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)

        //데이터 변경 됐다고 알려줌
        this.mySearchHistoryRecyclerViewAdapter.notifyDataSetChanged()
        handleSearchViewUI()
    }

    // 검색 아이템 버튼 이벤트
    override fun onSearchItemClicked(position: Int) {
        Log.d(Constants.TAG, "PhotoCollectionActivity - onSearchItemClicked() called")

        val queryString = this.searchHistoryList[position].term

        // 해당 녀석의 검색어로 API 호출
        searchPhotoApiCall(queryString)
        top_app_bar.title = queryString
        this.insertSearchTermHistory(searchTerm = queryString)
        this.top_app_bar.collapseActionView()
    }

    // 사진검색 API 호출
    private fun searchPhotoApiCall(query: String) {
        RetrofitManager.instance.searchPhotos(searchTerm = query, completion = { status, list ->
            when (status) {
                RESPONSE_STATUS.OKAY -> {
                    Log.d(
                        Constants.TAG,
                        "PhotoCollectionActivity - searchPhotoApiCall( called 응답 성공 / list.size : ${list?.size}"
                    )

                    if (list != null) {
                        this.photoList.clear()
                        this.photoList = list
                        this.photoGridRecyclerViewAdapter.sumbitList(this.photoList)
                        this.photoGridRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }
                RESPONSE_STATUS.NO_CONTENT -> {
                    Toast.makeText(this, "$query 에 대한 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun handleSearchViewUI() {
        if (this.searchHistoryList.size > 0) {
            search_history_recyclerview.visibility = View.VISIBLE
            search_history_recyclerview_label.visibility = View.VISIBLE
            clear_search_history_button.visibility = View.VISIBLE
        } else {
            search_history_recyclerview.visibility = View.INVISIBLE
            search_history_recyclerview_label.visibility = View.INVISIBLE
            clear_search_history_button.visibility = View.INVISIBLE
        }
    }

    // 검색어 저장
    private fun insertSearchTermHistory(searchTerm: String) {
        Log.d(Constants.TAG, "PhotoCollectionActivity - insertSearchTermHistory() called")

        if (SharedPrefManager.checkSearchHistoryMode()) {
            // 중복아이템 삭제
            var indexListToRemove = ArrayList<Int>()
            this.searchHistoryList.forEachIndexed { index, searchDataItem ->
                if (searchDataItem.term == searchTerm) {
                    Log.d(Constants.TAG, "index: $index")
                    indexListToRemove.add(index)
                }
            }
            indexListToRemove.forEach {
                this.searchHistoryList.removeAt(it)
            }
            Log.d(Constants.TAG, "dsksd")
            //새 아이템 넣기
            val newSearchData = SearchData(term = searchTerm, timeStamp = Date().toSimpleString())
            this.searchHistoryList.add(newSearchData)

            // 기존 데이터 덮어쓰기
            SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)
        }
    }


}