package pl.mlynik

object BoardPreset {
    fun default(): Board {

        var board = Board.empty()

        fun pawns(player: Player) {
            (0..7).forEach { board = board.with(Field(it, if (player == Player.White) 1 else 6), Pawn(player)) }
        }

        fun rooks(player: Player) {
            board =
                board
                    .with(Field(0, if (player == Player.White) 0 else 7), Rook(player))
                    .with(Field(7, if (player == Player.White) 0 else 7), Rook(player))
        }

        fun knights(player: Player) {
            board =
                board
                    .with(Field(1, if (player == Player.White) 0 else 7), Knight(player))
                    .with(Field(6, if (player == Player.White) 0 else 7), Knight(player))
        }

        fun bishops(player: Player) {
            board =
                board
                    .with(Field(2, if (player == Player.White) 0 else 7), Bishop(player))
                    .with(Field(5, if (player == Player.White) 0 else 7), Bishop(player))
        }

        fun royal(player: Player) {
            board =
                board
                    .with(Field(3, if (player == Player.White) 0 else 7), King(player))
                    .with(Field(4, if (player == Player.White) 0 else 7), Queen(player))
        }

        pawns(Player.White)
        pawns(Player.Black)

        rooks(Player.White)
        rooks(Player.Black)

        knights(Player.White)
        knights(Player.Black)

        bishops(Player.White)
        bishops(Player.Black)

        royal(Player.White)
        royal(Player.Black)

        return board
    }
}
