/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zwq65.unity.ui._base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.zwq65.unity.R

/**
 * ================================================
 * 所有{@link DialogFragment}都必须继承{@link BaseDialogFragment}
 * <p>
 * Created by NIRVANA on 2017/05/24
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class BaseDialogFragment : DialogFragment(), DialogMvpView {

    private var mActivity: BaseDaggerActivity<*, *>? = null

    abstract fun getLayoutId(): Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseDaggerActivity<*, *>) {
            val mActivity = context as BaseDaggerActivity<*, *>?
            this.mActivity = mActivity
            mActivity!!.onFragmentAttached()
        }
    }

    override fun showLoading() {
        if (mActivity != null) {
            mActivity!!.showLoading()
        }
    }

    override fun hideLoading() {
        if (mActivity != null) {
            mActivity!!.hideLoading()
        }
    }

    override fun showMessage(@StringRes resId: Int) {
        if (mActivity != null) {
            mActivity!!.showMessage(resId)
        }
    }

    override fun showMessage(message: String) {
        if (mActivity != null) {
            mActivity!!.showMessage(message)
        }
    }

    override fun showError(@StringRes resId: Int) {
        if (mActivity != null) {
            mActivity!!.showError(resId)
        }
    }

    override fun showError(message: String) {
        if (mActivity != null) {
            mActivity!!.showError(message)
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    protected abstract fun setUp(view: View)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, getLayoutId(), null)

        // creating the fullscreen dialog
        val dialog = Dialog(activity, R.style.DialogFragment)
        dialog.setContentView(view)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(view)
    }

    override fun show(fragmentManager: FragmentManager, tag: String) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    override fun dismissDialog(tag: String) {
        dismiss()
        mActivity!!.onFragmentDetached(tag)
    }

}