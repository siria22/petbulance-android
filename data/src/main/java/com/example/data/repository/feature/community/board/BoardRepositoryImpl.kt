package com.example.data.repository.feature.community.board

import com.example.data.datasource.remote.network.feature.community.board.BoardApi
import com.example.data.datasource.remote.network.feature.community.board.dto.BoardResDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.mapper.feature.community.toDomain
import com.example.domain.model.feature.community.board.Board
import com.example.domain.repository.feature.community.BoardRepository
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
