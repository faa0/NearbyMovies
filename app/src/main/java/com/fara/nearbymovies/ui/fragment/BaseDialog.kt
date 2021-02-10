package com.fara.nearbymovies.ui.fragment

import android.app.Dialog
import android.content.Context

abstract class BaseDialog(
    context: Context,
    themeResId: Int = android.R.style.Theme_Translucent_NoTitleBar
) : Dialog(context, themeResId)