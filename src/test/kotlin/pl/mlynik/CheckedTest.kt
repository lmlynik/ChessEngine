package pl.mlynik

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CheckedTest {

    @ParameterizedTest
    @MethodSource("cases")
    fun checked_test(case: Case) {
        val board = case.board
        val moves = case.moves
        var result: Board.MoveResult? = null
        moves.forEach {
            result = board.move(it.from, it.to)
        }

        if (result is Board.MoveResult.ValidMove) {
            assertSame(case.checked, (result as Board.MoveResult.ValidMove).checked)
        } else {
            fail("Expected Checked ${case.checked}, but found $result")
        }
    }

    data class Case(val board: Board, val moves: List<Move>, val checked: Player)

    data class Move(val from: Field, val to: Field)

    companion object {
        @JvmStatic
        fun cases() = listOf(
            Case(
                Board
                    .with(King(Player.White), Field(3, 0))
                    .with(Knight(Player.Black), Field(3, 4))
                    .with(Pawn(Player.White), Field(3, 1))
                    .build(),
                listOf(
                    Move(
                        Field(3, 1), Field(3, 2)
                    ),
                    Move(
                        Field(3, 4), Field(2, 2)
                    )
                ),
                Player.White
            )
        )
    }

}
