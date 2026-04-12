package com.petbulance.data.repository.feature.support.qna

import com.petbulance.data.datasource.local.dao.QnaDao
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.support.qna.QnaApi
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.CreateQnaResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.DeleteQnaResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.PagingQnaListResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.QnaDetailResDto
import com.petbulance.data.datasource.remote.network.feature.support.qna.dto.UpdateQnaResDto
import com.petbulance.data.mapper.feature.support.toCreateQnaDto
import com.petbulance.data.mapper.feature.support.toDomain
import com.petbulance.data.mapper.feature.support.toEntity
import com.petbulance.data.mapper.feature.support.toUpdateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaListResult
import com.petbulance.domain.model.feature.support.qna.QnaParam
import com.petbulance.domain.repository.feature.support.QnaRepository
import javax.inject.Inject

class QnaRepositoryImpl @Inject constructor(
    private val api: QnaApi,
    private val qnaDao: QnaDao
) : QnaRepository {

    companion object {
        private const val CACHE_VALIDITY_MS = 5 * 60 * 1000L // 5분
    }

    override suspend fun getQnaList(lastQnaId: Long?, pageSize: Int): Result<QnaListResult> {
        // 첫 페이지 요청 시에만 캐시 확인
        if (lastQnaId == null) {
            val latestCacheTime = withContext(Dispatchers.IO) {
                qnaDao.getLatestCacheTime()
            }
            val currentTime = System.currentTimeMillis()
            
            // 캐시가 유효한 경우 로컬 데이터 반환
            if (latestCacheTime != null && (currentTime - latestCacheTime) < CACHE_VALIDITY_MS) {
                return withContext(Dispatchers.IO) {
                    val cachedQnaList = qnaDao.getAllQna().map { it.toDomain() }
                    Result.success(QnaListResult(qnaList = cachedQnaList, hasNext = false))
                }
            }
        }

        // API 호출
        return safeApiCall<PagingQnaListResDto>(path = "/qna") {
            api.getQnaList(lastQnaId, pageSize)
        }.map { dto ->
            val result = dto.toDomain()
            
            // 첫 페이지 응답을 캐시에 저장
            if (lastQnaId == null) {
                withContext(Dispatchers.IO) {
                    qnaDao.deleteAll()
                    qnaDao.insertAll(result.qnaList.map { it.toEntity() })
                }
            }
            
            result
        }
    }

    override suspend fun getQnaById(qnaId: Long): Result<Qna> {
        // 로컬 캐시 확인
        val cachedQna = withContext(Dispatchers.IO) {
            qnaDao.getQnaById(qnaId)
        }
        
        if (cachedQna != null) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - cachedQna.cachedAt) < CACHE_VALIDITY_MS) {
                return Result.success(cachedQna.toDomain())
            }
        }

        // API 호출
        return safeApiCall<QnaDetailResDto>(path = "/qna/$qnaId") {
            api.getQnaDetail(qnaId)
        }.map { dto ->
            val qna = dto.toDomain()
            
            // 캐시에 저장
            withContext(Dispatchers.IO) {
                qnaDao.insert(qna.toEntity())
            }
            
            qna
        }
    }

    override suspend fun createQna(param: QnaParam): Result<Qna> {
        return safeApiCall<CreateQnaResDto>(path = "/qna") {
            api.createQna(param.toCreateQnaDto())
        }.map { dto ->
            val qna = dto.toDomain()
            
            // 캐시 무효화 (새 문의 추가 시)
            withContext(Dispatchers.IO) {
                qnaDao.deleteAll()
            }
            
            qna
        }
    }

    override suspend fun updateQna(
        qnaId: Long,
        param: QnaParam
    ): Result<Qna> {
        return safeApiCall<UpdateQnaResDto>(path = "/qna/$qnaId") {
            api.updateQna(qnaId, param.toUpdateDto())
        }.map { dto ->
            val qna = dto.toDomain()
            
            // 캐시 업데이트
            withContext(Dispatchers.IO) {
                qnaDao.insert(qna.toEntity())
            }
            
            qna
        }
    }

    override suspend fun deleteQna(qnaId: Long): Result<DeleteQnaResult> {
        return safeApiCall<DeleteQnaResDto>(path = "/qna/$qnaId") {
            api.deleteQna(qnaId)
        }.map { dto ->
            // 캐시에서 삭제
            withContext(Dispatchers.IO) {
                qnaDao.deleteById(qnaId)
            }
            
            dto.toDomain()
        }
    }
}