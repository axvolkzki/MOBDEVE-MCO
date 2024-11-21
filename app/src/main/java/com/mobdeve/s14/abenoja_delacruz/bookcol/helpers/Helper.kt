package com.mobdeve.s14.abenoja_delacruz.bookcol.helpers

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
