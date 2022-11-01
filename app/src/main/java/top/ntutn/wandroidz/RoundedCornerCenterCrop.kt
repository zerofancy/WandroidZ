package top.ntutn.wandroidz

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

/**
 * Author:jingnan
 * Time:2019-08-28/15
 * Content:Glide同时使用RoundedCorner和CenterCrop，在图片宽高与ImageView不一致对情况下
 */
class RoundedCornerCenterCrop(val radius: Int = 0) : BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        return TransformationUtils.roundedCorners(pool, bitmap, radius)
    }
}
