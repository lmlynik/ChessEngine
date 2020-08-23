package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RookTest {

    @Test
    fun move_from_start() {
        val rook = Rook(Player.White)

        val board = Board
            .withFields(Field(0, 0))
            .filledWith { rook }
            .build()

        assertEquals(
            setOf(
                Field(0, 1),
                Field(0, 2),
                Field(0, 3),
                Field(0, 4),
                Field(0, 5),
                Field(0, 6),
                Field(0, 7),

                Field(1, 0),
                Field(2, 0),
                Field(3, 0),
                Field(4, 0),
                Field(5, 0),
                Field(6, 0),
                Field(7, 0)
            ),
            rook.moves(Field(0, 0), board)
        )
    }

    @Test
    fun move_from_center() {
        val board = Board.empty()
        val rook = Rook(Player.White)

        assertEquals(
            setOf(
                Field(3, 4),
                Field(3, 5),
                Field(3, 6),
                Field(3, 7),
                Field(3, 2),
                Field(3, 1),
                Field(3, 0),

                Field(2, 3),
                Field(1, 3),
                Field(0, 3),
                Field(4, 3),
                Field(5, 3),
                Field(6, 3),
                Field(7, 3)
            ),
            rook.moves(Field(3, 3), board)
        )
    }

    @Test
    fun move_blocked_by_friend() {
        val board = Board
            .withFields(Field(0, 0))
            .filledWith { Rook(Player.White) }
            .withFields(Field(0, 2))
            .filledWith { Pawn(Player.White) }
            .withFields(Field(3, 0))
            .filledWith { King(Player.White) }
            .build()

        val rook = Rook(Player.White)

        assertEquals(
            setOf(
                Field(0, 1),
                Field(1, 0),
                Field(2, 0)
            ),
            rook.moves(Field(0, 0), board)
        )
    }

    @Test
    fun move_blocked_by_enemy() {
        val rook = Rook(Player.White)
        val board = Board
            .withFields(Field(0, 0))
            .filledWith { rook }
            .withFields(Field(0, 2))
            .filledWith { Pawn(Player.Black) }
            .withFields(Field(3, 0))
            .filledWith { King(Player.White) }
            .build()

        assertEquals(
            setOf(
                Field(0, 1),
                Field(0, 2),
                Field(1, 0),
                Field(2, 0)
            ),
            rook.moves(Field(0, 0), board)
        )
    }
}
