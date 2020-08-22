package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KnightTest {

    @Test
    fun move_from_start() {
        val board = Board
            .withFields(Field(3, 1))
            .filledWith { Pawn(Player.White) }
            .build()
        val knight = Knight(Player.White)

        assertEquals(
            setOf(
                Field(0, 2),
                Field(2, 2)
            ),
            knight.moves(Field(1, 0), board)
        )
    }

    @Test
    fun move_from_center() {
        val board = Board.empty()
        val knight = Knight(Player.White)

        assertEquals(
            setOf(
                Field(2, 5),
                Field(4, 5),
                Field(2, 1),
                Field(4, 1),
                Field(1, 4),
                Field(1, 2),
                Field(5, 4),
                Field(5, 2)
            ),
            knight.moves(Field(3, 3), board)
        )
    }
}