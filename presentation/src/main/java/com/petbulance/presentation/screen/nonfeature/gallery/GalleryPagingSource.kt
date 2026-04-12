package com.petbulance.presentation.screen.nonfeature.gallery

import android.content.ContentResolver
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState

class GalleryPagingSource(
    private val contentResolver: ContentResolver
) : PagingSource<Int, GalleryImage>() {

    override fun getRefreshKey(state: PagingState<Int, GalleryImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryImage> {
        val page = params.key ?: 0
        val loadSize = params.loadSize
        val offset = page * loadSize

        return try {
            val images = mutableListOf<GalleryImage>()

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
            )

            val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            // 최신순 정렬
            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            contentResolver.query(
                collectionUri,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                // Offset 위치로 이동
                if (cursor.moveToPosition(offset)) {
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                    var count = 0
                    do {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn) ?: ""
                        val dateTaken = cursor.getLong(dateColumn)
                        val contentUri = ContentUris.withAppendedId(collectionUri, id)

                        images.add(GalleryImage(id, contentUri, name, dateTaken))
                        count++
                    } while (cursor.moveToNext() && count < loadSize)
                }
            }

            val nextKey = if (images.size < loadSize) null else page + 1
            val prevKey = if (page == 0) null else page - 1

            LoadResult.Page(
                data = images,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}