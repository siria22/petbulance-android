package com.example.data.mapper.feature.community

import com.example.data.datasource.remote.network.feature.community.board.dto.BoardResDto

fun BoardResDto.toDomain() =
    _root_ide_package_.com.example.domain.model.feature.community.board.Board(
        boardId = boardId,
        nameKr = nameKr,
        nameEn = nameEn,
        description = description
    )