package com.example.domain.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * 여러 비동기 작업을 묶어서 처리
 * @param block1 처리할 비동기 작업으로, 각 작업은 람다로 감싼 형태로 제공되어야 함
 * @return blocks의 결과들을 순서대로 반환
 */
suspend fun <T1, T2> zip(
    block1: suspend () -> T1,
    block2: suspend () -> T2
): Pair<T1, T2> = coroutineScope {
    val deferred1 = async { block1() }
    val deferred2 = async { block2() }
    Pair(deferred1.await(), deferred2.await())
}

suspend fun <T1, T2, T3> zip(
    block1: suspend () -> T1,
    block2: suspend () -> T2,
    block3: suspend () -> T3
): Triple<T1, T2, T3> = coroutineScope {
    val deferred1 = async { block1() }
    val deferred2 = async { block2() }
    val deferred3 = async { block3() }
    Triple(deferred1.await(), deferred2.await(), deferred3.await())
}

data class Quadruple<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
)

suspend fun <T1, T2, T3, T4> zip(
    block1: suspend () -> T1,
    block2: suspend () -> T2,
    block3: suspend () -> T3,
    block4: suspend () -> T4
): Quadruple<T1, T2, T3, T4> = coroutineScope {
    val deferred1 = async { block1() }
    val deferred2 = async { block2() }
    val deferred3 = async { block3() }
    val deferred4 = async { block4() }
    Quadruple(deferred1.await(), deferred2.await(), deferred3.await(), deferred4.await())
}
