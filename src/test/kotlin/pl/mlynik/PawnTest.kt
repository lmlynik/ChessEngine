package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PawnTest {

    @Test
    fun white_can_go_up_and_2_squares_on_start() {
        val pawn = Pawn(Player.White)
        assertEquals(
            setOf(Field(1, 2), Field(1, 3)),
            pawn.moves(Field(1, 1), Board.empty())
        )
    }

    @Test
    fun black_can_go_down() {
        val pawn = Pawn(Player.Black)
        assertEquals(
            setOf(Field(1, 4), Field(1, 5)),
            pawn.moves(Field(1, 6), Board.empty())
        )
    }

    @Test
    fun white_attack_left_right() {
        val board = Board
            .withFields(Field(0, 3), Field(1, 3), Field(2, 3))
            .filledWith { Pawn(Player.Black) }

            .build()

        val pawn = Pawn(Player.White)
        assertEquals(
            setOf(
                Field(0, 3),
                Field(2, 3)
            ),
            pawn.moves(Field(1, 2), board)
        )
    }
}
