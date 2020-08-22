package pl.mlynik

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class CheckedTest {

    @Test
    fun checked_test() {
        val board = Board
            .with(King(Player.White), Field(3, 0))
            .with(Knight(Player.Black), Field(3, 4))
            .with(Pawn(Player.White), Field(3, 1))
            .build()

        assertSame(Player.White, board.player)

        board.move(Field(3, 1), Field(3, 2))

        assertSame(Player.Black, board.player)

        val result = board.move(Field(3, 4), Field(2, 2))

        if (result is Board.MoveResult.ValidMove) {
            assertSame(Player.White, (result as Board.MoveResult.ValidMove).checked)
        } else {
            fail("Expected Checked ${Player.White}, but found $result")
        }

        assertSame(Player.White, board.checked)
        assertSame(board.checked, board.player)
    }

    @Test
    fun disallow_move_which_keeps_check() {
        val board = Board
            .with(King(Player.White), Field(3, 0))
            .with(Knight(Player.Black), Field(3, 4))
            .with(Pawn(Player.White), Field(3, 1))
            .build()

        assertSame(Player.White, board.player)

        board.move(Field(3, 1), Field(3, 2))

        assertSame(Player.Black, board.player)

        board.move(Field(3, 4), Field(2, 2))

        assertSame(Player.White, board.checked)
        assertSame(board.checked, board.player)

        val boardCopy = board.copy()
        // move Pawn which will keep King Checked
        val result = board.move(Field(3, 2), Field(3, 3))

        assert(result is Board.MoveResult.StillInCheck)

        assertEquals(boardCopy, board)
    }
}
