package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.repository.feature.user.UserRepository
import com.petbulance.domain.usecase.nonfeature.app.UploadImageUseCase
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val uploadImage: UploadImageUseCase
) {
    suspend operator fun invoke(
        currentNickname: String,
        newNickname: String,
        imageBytes: ByteArray?,
        imageFilename: String?,
        imageMimeType: String?
    ): Result<Unit> {
        val nicknameChanged = newNickname != currentNickname
        val imageChanged = imageBytes != null && imageFilename != null && imageMimeType != null

        if (!nicknameChanged && !imageChanged) return Result.success(Unit)

        if (nicknameChanged) {
            userRepository.updateNickname(newNickname).getOrElse {
                return Result.failure(it)
            }
        }

        if (imageChanged) {
            val uploadInfo = userRepository.requestProfileImageUpload(
                filename = imageFilename!!,
                mimeType = imageMimeType!!
            ).getOrElse {
                return Result.failure(it)
            }

            uploadImage(
                url = uploadInfo.uploadUrl,
                imageBytes = imageBytes!!,
                mimeType = imageMimeType!!
            ).getOrElse {
                return Result.failure(it)
            }

            userRepository.checkProfileImageUpdate(
                saveId = uploadInfo.fileId,
                filename = imageFilename!!
            ).getOrElse {
                return Result.failure(it)
            }
        }

        return Result.success(Unit)
    }
}
