package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PawnTest {

    @Test
    fun white_can_go_up() {
        val pawn = Pawn(Player.White)
        assertEquals(
            setOf(Field(1, 2)),
            pawn.moves(Field(1, 1), Board.empty())
        )
    }

    @Test
    fun black_can_go_down() {
        val pawn = Pawn(Player.Black)
        assertEquals(
            setOf(Field(1, 0)),
            pawn.moves(Field(1, 1), Board.empty())
        )
    }

    @Test
    fun white_attack_left_right() {
        val board = Board
            .withFields(Field(2, 2))

            .filledWith { Pawn(Player.Black) }
            .withFields(Field(0, 2))
            .filledWith { Pawn(Player.White) }
            .build()

        val pawn = Pawn(Player.White)
        assertEquals(
            setOf(
//                Field(0, 2),
                Field(1, 2),
                Field(2, 2)
            ),
            pawn.moves(Field(1, 1), board)
        )
    }
}