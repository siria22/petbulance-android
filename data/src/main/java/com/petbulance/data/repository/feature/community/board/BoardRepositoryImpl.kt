package com.petbulance.data.repository.feature.community.board

import com.petbulance.data.datasource.remote.network.feature.community.board.BoardApi
import com.petbulance.data.datasource.remote.network.feature.community.board.dto.BoardResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.mapper.feature.community.toDomain
import com.petbulance.domain.model.feature.community.board.Board
import com.petbulance.domain.repository.feature.community.BoardRepository
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val api: BoardApi
) : BoardRepository {

    override suspend fun getBoardList(): Result<List<Board>> {
        return safeApiCall<List<BoardResDto>>(path = "/boards") {
            api.boardList()
        }.map { it -> it.map { it.toDomain() } }
    }

}
