package com.zoey.unsplash_app.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

// 문자열이 JSON 형태인지, JSON 배열 형태인지
fun String?.isJsonObject(): Boolean {
    return this?.startsWith("{") == true && this.endsWith("}")
}

fun String?.isJsonArray(): Boolean {
    return this?.startsWith("[") == true && this.endsWith("]")
}

// 에딧 텍스트에 대한 익스텐
fun EditText.onMyTextChanged(completion: (Editable?) -> Unit) {
    this.addTextChangedListener(object: TextWatcher{
        override fun afterTextChanged(editable: Editable?) {
            completion(editable)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    })
}