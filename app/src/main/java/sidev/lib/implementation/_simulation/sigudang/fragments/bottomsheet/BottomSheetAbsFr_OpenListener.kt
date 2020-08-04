package com.sigudang.android.fragments.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.jetbrains.anko.layoutInflater


abstract class BottomSheetAbsFr_OpenListener<D> : BottomSheetAbsFr<D>() {
    public override var onBsBtnlickListener: ((data: D?) -> Unit)?= null
}