package com.example.dovenews.utils

import android.content.Context
import com.bumptech.glide.load.resource.bitmap.CenterCrop

import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import android.util.DisplayMetrics

import com.bumptech.glide.annotation.GlideModule

import com.bumptech.glide.request.RequestOptions

import com.bumptech.glide.annotation.GlideOption

import com.bumptech.glide.module.AppGlideModule

@GlideModule
object NewsGlideModule : AppGlideModule() {
    @GlideOption
    fun roundedCornerImage(options: RequestOptions, context: Context, radius: Int): RequestOptions {
        if (radius > 0) {
            val px = Math.round(
                radius * (context.resources
                    .displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
            )
                .toInt()
            return options.transform(CenterCrop(), RoundedCorners(px))
        }
        return options.transform(CenterCrop())
    }
}