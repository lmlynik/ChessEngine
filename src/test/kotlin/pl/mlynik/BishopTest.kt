package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BishopTest {

    @Test
    fun move_from_start() {
        val bishop = Bishop(Player.White)

        val board = Board
            .withFields(Field(2, 0))
            .filledWith { bishop }
            .build()

        assertEquals(
            setOf(
                Field(1, 1),
                Field(0, 2),
                Field(3, 1),
                Field(4, 2),
                Field(5, 3),
                Field(6, 4),
                Field(7, 5)
            ),
            bishop.moves(Field(2, 0), board)
        )
    }

    @Test
    fun move_from_start_with_pawn_wall() {
        val bishop = Bishop(Player.White)

        val board = Board
            .withFields(Field(2, 0))
            .filledWith { bishop }
            .withFields(Field(0, 1), Field(1, 1), Field(2, 1), Field(3, 1), Field(4, 1))
            .filledWith { Pawn(Player.White) }
            .build()

        assertEquals(
            setOf<Field>(
            ),
            bishop.moves(Field(2, 0), board)
        )
    }

    @Test
    fun move_from_center() {
        val board = Board.empty()
        val bishop = Bishop(Player.White)

        assertEquals(
            setOf(
                Field(2, 4),
                Field(1, 5),
                Field(0, 6),
                Field(4, 4),
                Field(5, 5),
                Field(6, 6),
                Field(7, 7),
                Field(2, 2),
                Field(1, 1),
                Field(0, 0),
                Field(4, 2),
                Field(5, 1),
                Field(6, 0)
            ),
            bishop.moves(Field(3, 3), board)
        )
    }

    @Test
    fun move_blocked_by_enemy() {
        val bishop = Bishop(Player.White)
        val board = Board
            .withFields(Field(2, 0))
            .filledWith { bishop }
            .withFields(Field(4, 2))
            .filledWith { Pawn(Player.Black) }
            .build()

        assertEquals(
            setOf(
                Field(1, 1),
                Field(0, 2),
                Field(3, 1),
                Field(4, 2),
            ),
            bishop.moves(Field(2, 0), board)
        )
    }
}
