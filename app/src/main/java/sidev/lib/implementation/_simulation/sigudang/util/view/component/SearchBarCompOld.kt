package com.sigudang.android.utilities.view.component
/*
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.sigudang.android.R

class SearchBarCompOld(c: Context, view: View?): ViewCompWithViewPagerOld(c, view) {
    override val layoutId= R.layout.component_bar_search

    override var view: View?= null
    private var et: EditText?= null
    private var textWatcher: TextWatcher?= null


    override fun initViewCompOnce() {
        super.initViewCompOnce()
        initOnTextChangeListener()
        searchEditTextInView()
    }
    override fun initViewComp() {
        searchEditTextInView()
    }

    private fun initOnTextChangeListener(){
        textWatcher= object: TextWatcher{
            var before= ""
            var after= ""
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                before= s?.toString() ?: ""
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                after= s?.toString() ?: ""
                onSearchTextListener?.onSearchText(activePageView, activePageInd, this.before, after)
            }
        }
    }

    private fun searchEditTextInView(){
        et?.removeTextChangedListener(textWatcher)
        et= view?.findViewById(R.id.et)
        et?.addTextChangedListener(textWatcher)
    }

    var onSearchTextListener: OnSearchTextListener?=  null
    interface OnSearchTextListener{
        fun onSearchText(searchedView: View?, pos: Int, before: String, after: String)
    }
    fun setOnSearchTextListener(l: (searchedView: View?, pos: Int, before: String, after: String) -> Unit){
        onSearchTextListener= object : OnSearchTextListener{
            override fun onSearchText(
                searchedView: View?,
                pos: Int,
                before: String,
                after: String
            ) {
                l(searchedView, pos, before, after)
            }
        }
    }
}

 */