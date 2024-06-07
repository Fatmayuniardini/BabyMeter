package com.capstone.babymeter.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.babymeter.R

class EditText : AppCompatEditText {
    private lateinit var backgroundEdtError: Drawable
    private lateinit var backgroundEdt: Drawable
    private var isError = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context,attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isError) backgroundEdtError else backgroundEdt

        hint = "Password"
    }

    private fun init() {

        backgroundEdtError = ContextCompat.getDrawable(context, R.drawable.bg_edt_error) as Drawable
        backgroundEdt = ContextCompat.getDrawable(context, R.drawable.bg_edt) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty() && s.toString().length <= 7) {
                    error = "Password must be at least 8 characters"
                    isError = true
                } else {
                    error = null
                    isError = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }
}