package pl.mlynik

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class BoardTest {

    @Test
    fun field_is_vaLid() {
        assertAll(
            { assertTrue(Field(0, 0).isValid()) },
            { assertFalse(Field(0, -1).isValid()) },
            { assertFalse(Field(8, 8).isValid()) }
        )
    }

    @Test
    fun put_returns_valid_when_put_on_empty() {
        val pawn = Pawn(Player.White)
        val board = Board
            .withFields(Field(1, 1))
            .filledWith { pawn }
            .build()

        val moveResult = board.move(Field(1, 1), Field(1, 2))

        assert(moveResult is Board.MoveResult.ValidMove)
    }

    @Test
    fun put_returns_occupied_friend_when_put_on_friend() {
        val pawn = Pawn(Player.White)
        val board = Board
            .withFields(Field(1, 1), Field(1, 2))
            .filledWith { pawn }
            .build()

        val moveResult = board.move(Field(1, 1), Field(1, 2))

        assert(moveResult is Board.MoveResult.IllegalMove)
    }
}
