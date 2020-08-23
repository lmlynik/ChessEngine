package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KingTest {

    @Test
    fun move_from_start() {
        val board = Board
            .withFields(Field(3, 1))
            .filledWith { Pawn(Player.White) }
            .build()
        val king = King(Player.White)

        assertEquals(
            setOf(
                Field(2, 0),
                Field(4, 0),
                Field(2, 1),
                Field(4, 1)
            ),
            king.moves(Field(3, 0), board)
        )
    }

    @Test
    fun move_from_center() {
        val board = Board.empty()
        val king = King(Player.White)

        assertEquals(
            setOf(
                Field(3, 4),
                Field(3, 2),
                Field(2, 3),
                Field(4, 3),
                Field(2, 4),
                Field(4, 4),
                Field(2, 2),
                Field(4, 2)
            ),
            king.moves(Field(3, 3), board)
        )
    }
}
