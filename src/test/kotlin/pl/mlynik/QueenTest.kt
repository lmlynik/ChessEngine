package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QueenTest {

    @Test
    fun move_from_start() {
        val queen = Queen(Player.White)

        val board = Board
            .withFields(Field(4, 0))
            .filledWith { queen }
            .build()

        assertEquals(
            setOf(
                Field(4, 1),
                Field(4, 2),
                Field(4, 3),
                Field(4, 4),
                Field(4, 5),
                Field(4, 6),
                Field(4, 7),
                Field(3, 0),
                Field(2, 0),
                Field(1, 0),
                Field(0, 0),
                Field(5, 0),
                Field(6, 0),
                Field(7, 0),
                Field(3, 1),
                Field(2, 2),
                Field(1, 3),
                Field(0, 4),
                Field(5, 1),
                Field(6, 2),
                Field(7, 3)
            ),
            queen.moves(Field(4, 0), board)
        )

    }

    @Test
    fun move_from_start_with_pawn_wall() {
        val queen = Queen(Player.White)

        val board = Board
            .withFields(Field(4, 0))
            .filledWith { queen }
            .withFields(
                Field(0, 1),
                Field(1, 1),
                Field(2, 1),
                Field(3, 1),
                Field(4, 1),
                Field(5, 1),
                Field(6, 1),
                Field(7, 1),
            )
            .filledWith { Pawn(Player.White) }
            .build()

        assertEquals(
            setOf(
                Field(0, 0),
                Field(1, 0),
                Field(2, 0),
                Field(3, 0),
                Field(5, 0),
                Field(6, 0),
                Field(7, 0)
            ),
            queen.moves(Field(4, 0), board)
        )
    }

    @Test
    fun move_from_center() {
        val board = Board.empty()
        val queen = Queen(Player.White)

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
                Field(7, 3),
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
            queen.moves(Field(3, 3), board)
        )
    }

    @Test
    fun move_blocked_by_enemy_surrounded() {
        val queen = Queen(Player.White)
        val queenField = Field(3, 3)

        val fieldsWithOpponent = setOf(
            queenField + Rank.Up,
            queenField + Rank.Down,
            queenField + Rank.Up + Direction.Right,
            queenField + Rank.Up + Direction.Left,
            queenField + Rank.Down + Direction.Right,
            queenField + Rank.Down + Direction.Left,
            queenField + Direction.Right,
            queenField + Direction.Left,
        )

        val board = Board
            .withFields(queenField)
            .filledWith { queen }
            .withFields(fieldsWithOpponent)
            .filledWith { Pawn(Player.Black) }
            .build()

        assertEquals(
            fieldsWithOpponent,
            queen.moves(queenField, board)
        )
    }
}
