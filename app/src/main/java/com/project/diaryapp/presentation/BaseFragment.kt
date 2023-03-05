package com.project.diaryapp.presentation

import androidx.fragment.app.Fragment
import com.project.diaryapp.presentation.diary.MainActivity

open class BaseFragment : Fragment() {
    fun showLoadingDialog(){
        (activity as MainActivity).showProgressDialog()
    }

    fun hideLoadingDialog(){
        (activity as MainActivity).hideProgressDialog()
    }


}