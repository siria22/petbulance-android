package com.petbulance.data.mapper.feature.community

import com.petbulance.data.datasource.remote.network.feature.community.board.dto.BoardResDto
import com.petbulance.domain.model.feature.community.board.Board

fun BoardResDto.toDomain() =
    Board(
        boardId = boardId,
        nameKr = nameKr,
        nameEn = nameEn,
        description = description
    )